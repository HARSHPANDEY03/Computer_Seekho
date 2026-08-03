import { client } from './client';

// ---- Enquiries (backend returns/accepts the raw Enquiry entity, not a DTO) ----
export const getAllEnquiries = () => client.get('/api/enquiries').then((r) => r.data);
export const getEnquiryById = (id) => client.get(`/api/enquiries/${id}`).then((r) => r.data);
export const createEnquiry = (payload) => client.post('/api/enquiries', payload).then((r) => r.data);
export const updateEnquiry = (id, payload) => client.put(`/api/enquiries/${id}`, payload).then((r) => r.data);
export const assignStaffToEnquiry = (id, payload) => client.put(`/api/enquiries/${id}/assign-staff`, payload).then((r) => r.data);
export const deleteEnquiry = (id) => client.delete(`/api/enquiries/${id}`).then((r) => r.data);

// ---- Follow-ups ----
export const getFollowupHistory = (enquiryId) => client.get(`/api/followups/enquiry/${enquiryId}`).then((r) => r.data);
export const getAllFollowups = () => client.get('/api/followups').then((r) => r.data);
export const logFollowup = (payload, staffId) =>
  client.post('/api/followups', payload, { params: { staffId } }).then((r) => r.data);
export const getTodayForStaff = (staffId) => client.get(`/api/followups/staff/${staffId}/today`).then((r) => r.data);
export const getOverdueForStaff = (staffId) => client.get(`/api/followups/staff/${staffId}/overdue`).then((r) => r.data);

// ---- Closure reasons (master table: create + list + delete only — the
// backend has no update endpoint, so the UI mustn't offer an "edit") ----
export const getAllClosureReasons = () => client.get('/api/closure-reasons').then((r) => r.data);
export const createClosureReason = (payload) => client.post('/api/closure-reasons', payload).then((r) => r.data);
export const deleteClosureReason = (id) => client.delete(`/api/closure-reasons/${id}`).then((r) => r.data);
