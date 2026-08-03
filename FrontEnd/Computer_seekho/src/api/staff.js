import { client } from './client';

// ---- Staff ----
export const getAllStaff = () => client.get('/api/staff').then((r) => r.data);
export const getStaffById = (id) => client.get(`/api/staff/${id}`).then((r) => r.data);
export const searchStaff = (keyword) => client.get('/api/staff/search', { params: { keyword } }).then((r) => r.data);
export const createStaff = (payload) => client.post('/api/staff', payload).then((r) => r.data);
export const updateStaff = (id, payload) => client.put(`/api/staff/${id}`, payload).then((r) => r.data);
export const deleteStaff = (id) => client.delete(`/api/staff/${id}`).then((r) => r.data);

// ---- User roles ----
export const getAllRoles = () => client.get('/api/roles').then((r) => r.data);
export const createRole = (payload) => client.post('/api/roles', payload).then((r) => r.data);
export const updateRole = (id, payload) => client.put(`/api/roles/${id}`, payload).then((r) => r.data);
export const activateRole = (id) => client.patch(`/api/roles/${id}/activate`).then((r) => r.data);
export const deactivateRole = (id) => client.patch(`/api/roles/${id}/deactivate`).then((r) => r.data);
export const deleteRole = (id) => client.delete(`/api/roles/${id}`).then((r) => r.data);
