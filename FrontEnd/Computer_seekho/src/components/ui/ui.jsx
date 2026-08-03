/*
  Shared primitives. The Arch* components are the one signature element of
  this design (see tokens.css header comment) — used deliberately in only
  three places across the whole app (Home hero, About portrait, Admin
  login). Everything else stays quiet by design; don't reach for <Arch*>
  anywhere else.
*/

// Rendered once near the app root. Multiple elements can share this single
// clip-path definition via clip-path: url(#archClip).
export function ArchDefs() {
  return (
    <svg width="0" height="0" style={{ position: 'absolute' }} aria-hidden="true">
      <defs>
        <clipPath id="archClip" clipPathUnits="objectBoundingBox">
          <path d="M0,1 L0,0.26 C0,0.1 0.16,0 0.5,0 C0.84,0 1,0.1 1,0.26 L1,1 Z" />
        </clipPath>
      </defs>
    </svg>
  );
}

export function ArchFrame({ src, alt = '', className = '', children }) {
  return (
    <div className={`arch-frame ${className}`}>
      {src ? <img src={src} alt={alt} /> : children}
    </div>
  );
}

// Decorative fanlight line-art shown low-opacity behind hero copy / on the
// admin login's dark panel. Purely atmospheric, aria-hidden.
export function ArchMotif({ className = '' }) {
  return (
    <svg viewBox="0 0 300 300" className={`arch-motif ${className}`} aria-hidden="true">
      <path d="M20 280 L20 90 C20 40 70 10 150 10 C230 10 280 40 280 90 L280 280" fill="none" stroke="currentColor" strokeWidth="1.5" />
      <path d="M50 280 L50 100 C50 62 90 38 150 38 C210 38 250 62 250 100 L250 280" fill="none" stroke="currentColor" strokeWidth="1.5" />
      <path d="M80 280 L80 108 C80 84 108 66 150 66 C192 66 220 84 220 108 L220 280" fill="none" stroke="currentColor" strokeWidth="1.5" />
      <line x1="150" y1="10" x2="150" y2="280" stroke="currentColor" strokeWidth="1" opacity="0.5" />
      <line x1="20" y1="150" x2="280" y2="150" stroke="currentColor" strokeWidth="1" opacity="0.35" />
    </svg>
  );
}

export function Stat({ value, label, tone = 'default' }) {
  return (
    <div className={`stat-tile tone-${tone}`}>
      <b>{value}</b>
      <span>{label}</span>
    </div>
  );
}

const STATUS_MAP = {
  new: { cls: 'badge-blue', label: 'New' },
  contacted: { cls: 'badge-blue', label: 'Contacted' },
  'follow-up': { cls: 'badge-warn', label: 'Follow-up' },
  overdue: { cls: 'badge-danger', label: 'Overdue' },
  registered: { cls: 'badge-ok', label: 'Registered' },
  closed: { cls: 'badge-plain', label: 'Closed' },
  active: { cls: 'badge-ok', label: 'Active' },
  inactive: { cls: 'badge-danger', label: 'Inactive' },
};

export function StatusBadge({ status, children }) {
  const key = String(status || '').toLowerCase();
  const meta = STATUS_MAP[key] || { cls: 'badge-plain', label: children || status };
  return <span className={`badge ${meta.cls}`}>{children || meta.label}</span>;
}

export function EmptyState({ title, description, action }) {
  return (
    <div className="empty-state">
      <h4>{title}</h4>
      {description && <p className="body-text">{description}</p>}
      {action}
    </div>
  );
}

export function Loading({ label = 'Loading…' }) {
  return (
    <div className="loading-row">
      <span className="spinner dark" />
      <span className="muted">{label}</span>
    </div>
  );
}

export function ErrorAlert({ message }) {
  if (!message) return null;
  return <div className="alert alert-danger">{message}</div>;
}

export function Avatar({ name = '', src, size = 40 }) {
  const initials = name
    .split(' ')
    .filter(Boolean)
    .slice(0, 2)
    .map((p) => p[0]?.toUpperCase())
    .join('');
  const style = { width: size, height: size, fontSize: size * 0.38 };
  return (
    <div className="avatar" style={style}>
      {src ? <img src={src} alt={name} width={size} height={size} style={{ objectFit: 'cover' }} /> : initials || '?'}
    </div>
  );
}

// A "backend not wired yet" notice — used only for the two genuine gaps
// found in this specific backend (Payment Types has no controller at all;
// Payments has @RestController commented out). Not a generic error state.
export function BackendGapNotice({ children }) {
  return (
    <div className="alert alert-info">
      <strong>Not live yet — </strong>{children}
    </div>
  );
}
