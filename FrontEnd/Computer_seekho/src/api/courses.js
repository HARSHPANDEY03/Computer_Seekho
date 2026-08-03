import { client } from './client';

// ---- Courses ----
export const getAllCourses = () => client.get('/api/courses').then((r) => r.data);
export const getActiveCourses = () => client.get('/api/courses/active').then((r) => r.data);
export const getCourseById = (id) => client.get(`/api/courses/${id}`).then((r) => r.data);
export const searchCourses = (keyword) => client.get('/api/courses/search', { params: { keyword } }).then((r) => r.data);
export const createCourse = (payload) => client.post('/api/courses', payload).then((r) => r.data);
export const updateCourse = (id, payload) => client.put(`/api/courses/${id}`, payload).then((r) => r.data);
export const updateCourseStatus = (id, active) =>
  client.patch(`/api/courses/${id}/status`, null, { params: { active } }).then((r) => r.data);

// ---- Batches ----
export const getAllBatches = () => client.get('/api/batches').then((r) => r.data);
export const getBatchById = (id) => client.get(`/api/batches/${id}`).then((r) => r.data);
export const getBatchesByCourse = (courseId) => client.get(`/api/batches/course/${courseId}`).then((r) => r.data);
export const getActiveBatchesByCourse = (courseId) => client.get(`/api/batches/course/${courseId}/active`).then((r) => r.data);
export const createBatch = (payload) => client.post('/api/batches', payload).then((r) => r.data);
export const updateBatch = (id, payload) => client.put(`/api/batches/${id}`, payload).then((r) => r.data);
export const updateBatchStatus = (id, active) =>
  client.patch(`/api/batches/${id}/status`, null, { params: { active } }).then((r) => r.data);
