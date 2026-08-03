import { client } from './client';

// NOTE: these four resources were written WITHOUT the /api prefix that
// every other controller uses (AlbumController, AnnouncementController,
// ContactController, ImageController). Not a typo on this side — matching
// the backend exactly.

// ---- Albums ----
export const getAllAlbums = () => client.get('/albums').then((r) => r.data);
export const getAlbumById = (id) => client.get(`/albums/${id}`).then((r) => r.data);
export const createAlbum = (payload) => client.post('/albums', payload).then((r) => r.data);
export const updateAlbum = (id, payload) => client.put(`/albums/${id}`, payload).then((r) => r.data);
export const deleteAlbum = (id) => client.delete(`/albums/${id}`).then((r) => r.data);

// ---- Images ----
export const getAllImages = () => client.get('/images').then((r) => r.data);
export const createImage = (payload) => client.post('/images', payload).then((r) => r.data);
export const updateImage = (id, payload) => client.put(`/images/${id}`, payload).then((r) => r.data);
export const deleteImage = (id) => client.delete(`/images/${id}`).then((r) => r.data);

// ---- Announcements ----
export const getAllAnnouncements = () => client.get('/announcements').then((r) => r.data);
export const createAnnouncement = (payload) => client.post('/announcements', payload).then((r) => r.data);
export const updateAnnouncement = (id, payload) => client.put(`/announcements/${id}`, payload).then((r) => r.data);
export const deleteAnnouncement = (id) => client.delete(`/announcements/${id}`).then((r) => r.data);

// ---- Recruiters (this one DOES use /api) ----
export const getAllRecruiters = () => client.get('/api/recruiters').then((r) => r.data);
export const createRecruiter = (payload) => client.post('/api/recruiters', payload).then((r) => r.data);
export const updateRecruiter = (id, payload) => client.put(`/api/recruiters/${id}`, payload).then((r) => r.data);
export const deleteRecruiter = (id) => client.delete(`/api/recruiters/${id}`).then((r) => r.data);

// ---- Contact messages ----
export const submitContact = (payload) => client.post('/contacts', payload).then((r) => r.data);
export const getAllContacts = () => client.get('/contacts').then((r) => r.data);
export const deleteContact = (id) => client.delete(`/contacts/${id}`).then((r) => r.data);
