import { client } from './client';

// ---- Students ----
export const registerStudent = (payload) => client.post('/api/students/register', payload).then((r) => r.data);
export const getAllStudents = () => client.get('/api/students').then((r) => r.data);
export const getStudentById = (id) => client.get(`/api/students/${id}`).then((r) => r.data);
export const getStudentByEnquiryId = (enquiryId) => client.get(`/api/students/enquiry/${enquiryId}`).then((r) => r.data);
export const searchStudents = (params) => client.get('/api/students/search', { params }).then((r) => r.data);
export const updateStudent = (id, payload) => client.put(`/api/students/${id}`, payload).then((r) => r.data);

// ---- Payments ----
// Matches com.example.controllers.PaymentController on the backend.
// Every one of these accepts an optional `studentId` in its payload:
// omitted → this is the first payment and admits the student; present →
// this is a follow-up installment against an already-admitted student.

// Cash / Bank payment - no gateway involved. Saves Payment (+ Student and
// Receipt on the first payment) in one call.
export const recordOfflinePayment = (payload) =>
  client.post('/api/payments/offline', payload).then((r) => r.data);

// ---- Razorpay online payment (Bank Transfer / UPI / Net Banking) ----

// Step 1: open a Razorpay order. `amount` is optional on a first payment
// (defaults to the full course/batch fee) but required for an installment
// (studentId set) - either way the backend validates it against what's
// actually still owed, never trusting the browser's number outright.
export const createRazorpayOrder = ({ studentId, courseId, batchId, amount }) =>
  client.post('/api/payments/create-order', { studentId, courseId, batchId, amount }).then((r) => r.data);

// Step 2: called from Razorpay Checkout's `handler` callback once payment
// succeeds. Only after the backend verifies the signature does it persist
// anything. Returns a PaymentReceiptResponse.
export const verifyRazorpayAdmission = (payload) =>
  client.post('/api/payments/verify', payload).then((r) => r.data);

// ---- Installment balance ----
// Live course fee / paid-so-far / pending-balance for one student.
export const getPaymentSummary = (studentId) =>
  client.get(`/api/payments/summary/${studentId}`).then((r) => r.data);

// Full itemized payment/installment history for one student, oldest first.
export const getPaymentsByStudent = (studentId) =>
  client.get(`/api/payments/student/${studentId}`).then((r) => r.data);

// ---- Receipts ----
export const getReceiptById = (id) => client.get(`/api/receipts/${id}`).then((r) => r.data);
export const searchReceiptsByStudentName = (studentName) =>
  client.get('/api/receipts/search', { params: { studentName } }).then((r) => r.data);
