// The Spring Boot backend has no context-path prefix; endpoints are
// whatever each @RequestMapping declares. Base URL only, no /api suffix,
// because four controllers (Album, Announcement, Contact, Image) were
// written WITHOUT the /api prefix that every other controller uses.
export const BASE_URL = 'http://localhost:8080';

const TOKEN_KEY = 'cs_admin_token';
const STAFF_KEY = 'cs_admin_staff';

export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}
export function getStoredStaff() {
  const raw = localStorage.getItem(STAFF_KEY);
  return raw ? JSON.parse(raw) : null;
}
export function setSession(token, staff) {
  localStorage.setItem(TOKEN_KEY, token);
  localStorage.setItem(STAFF_KEY, JSON.stringify(staff));
}
export function clearSession() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(STAFF_KEY);
}

// Reads the "exp" claim out of a JWT without verifying the signature (the
// backend does that) — just enough to know locally whether it's expired,
// so we can bounce to login before ever sending a doomed request.
function isTokenExpired(token) {
  try {
    const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
    if (!payload.exp) return false;
    return Date.now() >= payload.exp * 1000;
  } catch {
    // Can't read it — treat as expired/invalid rather than risk a crash.
    return true;
  }
}

// Session has just been invalidated (expired token, or the backend
// rejected it outright). Clear it and send the user back to login instead
// of leaving them staring at a broken page. A full navigation (not
// react-router) guarantees this fires even from code paths outside the
// router, and forces AuthContext to re-read localStorage on reload.
function forceReauth() {
  clearSession();
  if (!window.location.pathname.startsWith('/admin/login')) {
    window.location.href = '/admin/login';
  }
}

// Builds "?a=1&b=2" from a plain params object, skipping null/undefined.
function buildQuery(params) {
  if (!params) return '';
  const usable = Object.entries(params).filter(([, v]) => v !== undefined && v !== null);
  if (usable.length === 0) return '';
  const search = new URLSearchParams();
  usable.forEach(([k, v]) => search.append(k, v));
  return `?${search.toString()}`;
}

// Normalizes whatever shape the backend throws (Spring's default error
// body, a plain string, or a network failure) into a single readable
// message, so every page can do `catch (e) { setError(e.message) }`.
async function toFriendlyError(res) {
  let data = null;
  const raw = await res.text().catch(() => '');
  if (raw) {
    try {
      data = JSON.parse(raw);
    } catch {
      data = raw;
    }
  }

  let message = 'Something went wrong. Please try again.';
  if (typeof data === 'string' && data.trim()) message = data;
  else if (data?.message) message = data.message;
  else if (res.status === 404) message = 'Not found.';
  else if (res.status === 401 || res.status === 403) message = 'You are not authorized. Please sign in again.';
  else if (res.status >= 500) message = 'The server had a problem handling that request.';

  const error = new Error(message);
  error.status = res.status;
  error.data = data;
  return error;
}

// Core request function backing every client.* method below.
// Mirrors the axios client's shape: resolves to { data }, rejects with
// a plain Error carrying a readable .message.
async function request(method, url, { data, params, headers } = {}) {
  const isFormData = typeof FormData !== 'undefined' && data instanceof FormData;

  const finalHeaders = { ...headers };
  if (!isFormData && data !== undefined && !finalHeaders['Content-Type']) {
    finalHeaders['Content-Type'] = 'application/json';
  }

  const token = getToken();
  if (token) {
    // Caught locally before it ever reaches the network — same experience
    // as a 401, without waiting on a round trip to find out.
    if (isTokenExpired(token)) {
      forceReauth();
      throw new Error('Your session has expired. Please sign in again.');
    }
    finalHeaders.Authorization = `Bearer ${token}`;
  }

  let res;
  try {
    res = await fetch(`${BASE_URL}${url}${buildQuery(params)}`, {
      method,
      headers: finalHeaders,
      body: data === undefined ? undefined : isFormData ? data : JSON.stringify(data),
    });
  } catch {
    const error = new Error('Could not reach the server. Is the backend running on localhost:8080?');
    throw error;
  }

  // Backstop for anything isTokenExpired() didn't catch (clock skew, a
  // token rejected for a reason other than expiry, etc.) — the backend
  // flags this with a response header instead of a 500 crash.
  if (res.headers.get('X-Token-Expired') === 'true' && token) {
    forceReauth();
    throw new Error('Your session has expired. Please sign in again.');
  }

  if (!res.ok) {
    const error = await toFriendlyError(res);
    if (res.status === 401 || res.status === 403) forceReauth();
    throw error;
  }

  const raw = await res.text();
  if (!raw) return { data: null };
  try {
    return { data: JSON.parse(raw) };
  } catch {
    // Every endpoint in this app returns real JSON on success, so a parse
    // failure here means the response body was cut short mid-stream —
    // almost always a server-side crash while writing the response (e.g.
    // an entity with a circular reference recursing forever). Silently
    // handing the broken text back as "data" used to corrupt whatever
    // page state consumed it (e.g. an array turning into a string) and
    // crash the whole app with no error message. Surface it clearly instead.
    const error = new Error('The server sent back an incomplete response. It may have hit an internal error — please try again.');
    error.status = res.status;
    error.data = raw;
    throw error;
  }
}

export const client = {
  get: (url, config) => request('GET', url, config),
  post: (url, data, config) => request('POST', url, { ...config, data }),
  put: (url, data, config) => request('PUT', url, { ...config, data }),
  patch: (url, data, config) => request('PATCH', url, { ...config, data }),
  delete: (url, config) => request('DELETE', url, config),
};
