import { useEffect, useMemo, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { getAllEnquiries } from '../../api/enquiries';
import { getActiveCourses, getActiveBatchesByCourse } from '../../api/courses';
import { registerStudent, recordOfflinePayment, createRazorpayOrder, verifyRazorpayAdmission, getPaymentSummary, getPaymentsByStudent, searchStudents } from '../../api/students';
import { Loading, StatusBadge } from '../../components/ui/ui';
import { deriveEnquiryStatus, extractProgramInterest, STATUS_COPY } from '../../utils/enquiry';

const PAYMENT_TYPES = ['Cash', 'Cheque', 'Demand Draft', 'Bank Transfer', 'UPI', 'Net Banking'];

// Picking Bank Transfer, UPI or Net Banking opens the Razorpay gateway
// directly — there's no separate "Online Payment" wrapper type anymore.
// Razorpay Checkout doesn't offer a distinct "bank transfer" checkout
// method (that's only available via Payment Links/Smart Collect), so both
// Bank Transfer and Net Banking are opened through Razorpay's netbanking
// flow; UPI opens the UPI flow. The label you pick is what gets recorded
// against the payment either way.
const ONLINE_PAYMENT_TYPES = ['Bank Transfer', 'UPI', 'Net Banking'];
const RAZORPAY_METHOD_BY_TYPE = {
  'Bank Transfer': 'netbanking',
  'Net Banking': 'netbanking',
  UPI: 'upi',
};

// Loads Razorpay's Checkout script once and reuses the same promise on
// every subsequent call, instead of injecting the <script> tag again.
function loadRazorpayScript() {
  if (window.Razorpay) return Promise.resolve();
  if (loadRazorpayScript._promise) return loadRazorpayScript._promise;
  loadRazorpayScript._promise = new Promise((resolve, reject) => {
    const script = document.createElement('script');
    script.src = 'https://checkout.razorpay.com/v1/checkout.js';
    script.onload = () => resolve();
    script.onerror = () => reject(new Error('Could not load the Razorpay payment gateway. Check your connection and try again.'));
    document.body.appendChild(script);
  });
  return loadRazorpayScript._promise;
}

export default function Admissions() {
  const { enquiryId } = useParams();
  const [allEnquiries, setAllEnquiries] = useState(null);
  const [search, setSearch] = useState('');
  const [selected, setSelected] = useState(null);
  const [courses, setCourses] = useState([]);
  const [batches, setBatches] = useState([]);

  const [form, setForm] = useState(blankForm());
  const [payment, setPayment] = useState({ type: 'Cash', date: new Date().toISOString().slice(0, 10), amount: '', reference: '', remarks: '' });
  const [result, setResult] = useState(null);
  // Live course-fee / paid / pending balance for the admitted student, plus
  // the full itemized payment history. null until either the first payment
  // succeeds, or an already-admitted student is looked up directly below.
  const [summary, setSummary] = useState(null);
  const [history, setHistory] = useState(null);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  // Looking up an already-admitted student directly, separate from the
  // "search an enquiry" box above it. mode picks which field studentSearch
  // is matched against.
  const [studentSearchMode, setStudentSearchMode] = useState('name');
  const [studentSearch, setStudentSearch] = useState('');
  const [studentResults, setStudentResults] = useState([]);
  const [studentSearching, setStudentSearching] = useState(false);
  const [selectedStudent, setSelectedStudent] = useState(null);

  // Either a payment made just now (result) or a student looked up directly
  // from history (selectedStudent) puts the page into "already admitted"
  // mode — same locked form, same installment controls, same balance panel.
  const admittedStudentId = result?.studentId || selectedStudent?.studentId || null;

  useEffect(() => {
    getAllEnquiries().then(setAllEnquiries).catch(() => setAllEnquiries([]));
    getActiveCourses().then(setCourses).catch(() => setCourses([]));
  }, []);

  useEffect(() => {
    if (enquiryId && allEnquiries) {
      const found = allEnquiries.find((e) => String(e.enquiryId) === String(enquiryId));
      if (found) selectEnquiry(found);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [enquiryId, allEnquiries]);

  useEffect(() => {
    if (form.courseId) getActiveBatchesByCourse(form.courseId).then(setBatches).catch(() => setBatches([]));
    else setBatches([]);
  }, [form.courseId]);

  // Debounced lookup of already-admitted students, matched against
  // whichever field studentSearchMode currently selects. Name is a partial
  // match; mobile, student ID and admission ID are exact matches.
  useEffect(() => {
    const q = studentSearch.trim();
    const minLen = studentSearchMode === 'name' ? 2 : 1;
    if (q.length < minLen) {
      setStudentResults([]);
      return;
    }
    setStudentSearching(true);
    const timer = setTimeout(() => {
      let params;
      if (studentSearchMode === 'mobile') params = { mobile: q };
      else if (studentSearchMode === 'studentId') params = { studentId: q };
      else if (studentSearchMode === 'admissionId') params = { admissionId: q };
      else params = { name: q };

      searchStudents(params)
        .then(setStudentResults)
        .catch(() => setStudentResults([]))
        .finally(() => setStudentSearching(false));
    }, 350);
    return () => clearTimeout(timer);
  }, [studentSearch, studentSearchMode]);

  // Prefill the amount field with the full course/batch fee once one is
  // picked — before the first payment. Admin can lower it to pay less as
  // the first installment. Left alone once a student is already admitted;
  // refreshSummary() takes over prefilling from the pending balance instead.
  useEffect(() => {
    if (admittedStudentId) return;
    const c = courses.find((c) => c.courseId === Number(form.courseId));
    const b = batches.find((b) => b.batchId === Number(form.batchId));
    const fee = b?.courseFees ?? c?.courseFees ?? form.courseFee;
    if (fee && !payment.amount) setPayment((p) => ({ ...p, amount: String(fee) }));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [form.courseId, form.batchId, form.courseFee, courses, batches, admittedStudentId]);

  const results = useMemo(() => {
    if (!allEnquiries || !search) return [];
    const q = search.toLowerCase();
    return allEnquiries.filter((e) => `${e.enquirerName} ${e.enquirerMobile}`.toLowerCase().includes(q)).slice(0, 8);
  }, [allEnquiries, search]);

  function selectEnquiry(e) {
    setSelected(e);
    const programName = extractProgramInterest(e.enquirerQuery);
    const matchedCourse = courses.find((c) => c.courseName === programName);
    setForm((f) => ({
      ...f,
      enquiryId: e.enquiryId,
      studentName: e.enquirerName || '',
      studentMobile: e.enquirerMobile || '',
      studentEmail: e.enquirerEmailId || '',
      studentAddress: e.enquirerAddress || '',
      courseId: matchedCourse?.courseId || f.courseId,
      courseFee: matchedCourse?.courseFees || f.courseFee,
    }));
  }

  // Loads an already-admitted student directly (no new payment yet this
  // session) - fills the form read-only from their record, and pulls up
  // their live balance + full payment history right away.
  async function selectExistingStudent(s) {
    setError('');
    setStudentSearch('');
    setStudentResults([]);
    setSelected(null);
    setResult(null);
    setSelectedStudent(s);
    setForm({
      enquiryId: s.enquiryId || '',
      studentName: s.studentName || '',
      studentMobile: s.studentMobile || '',
      studentEmail: s.studentEmail || '',
      studentAddress: s.studentAddress || '',
      studentDob: s.studentDob || '',
      studentGender: s.studentGender || '',
      studentQualification: s.studentQualification || '',
      photoUrl: s.photoUrl || '',
      courseId: s.courseId ? String(s.courseId) : '',
      batchId: s.batchId ? String(s.batchId) : '',
      courseFee: s.courseFee || '',
    });
    await refreshSummary(s.studentId);
  }

  function validateBeforeCharging() {
    if (!form.studentName || !form.studentMobile || !form.courseId || !form.batchId) {
      setError('Student name, mobile, course and batch are required.');
      return false;
    }
    return true;
  }

  // Re-reads the live balance and full payment history after every payment
  // (and whenever an existing student is looked up directly). If there's
  // still something owed, pre-fills the amount field with exactly that so
  // the admin can just click Pay again for the remaining balance (or edit
  // it down for yet another partial installment).
  async function refreshSummary(studentId) {
    try {
      const [s, h] = await Promise.all([
        getPaymentSummary(studentId),
        getPaymentsByStudent(studentId),
      ]);
      setSummary(s);
      setHistory(h);
      setPayment((p) => ({ ...p, amount: s.fullyPaid ? '' : String(s.pendingAmount) }));
    } catch (err) {
      // Previously swallowed silently, which made failures here
      // indistinguishable from "no history yet". Surface it instead.
      setError(`Could not load payment history: ${err.message}`);
    }
  }

  // Cash / Cheque / Demand Draft / Bank: no gateway step — the admin has
  // already collected payment in person. Works both for the first payment
  // (admits the student) and any later installment (studentId set).
  async function onConfirm() {
    setError('');
    if (!admittedStudentId && !validateBeforeCharging()) return;
    if (!payment.amount || Number(payment.amount) <= 0) {
      setError('Enter the amount to pay.');
      return;
    }
    setSaving(true);
    try {
      const receipt = await recordOfflinePayment({
        studentId: admittedStudentId,
        enquiryId: form.enquiryId || null,
        studentName: form.studentName,
        studentAddress: form.studentAddress || null,
        studentGender: form.studentGender || null,
        studentDob: form.studentDob || null,
        studentQualification: form.studentQualification || null,
        studentMobile: Number(form.studentMobile),
        studentEmail: form.studentEmail || null,
        photoUrl: form.photoUrl || null,
        courseId: Number(form.courseId),
        batchId: Number(form.batchId),
        paymentType: payment.type,
        amount: Number(payment.amount),
        remarks: payment.remarks || null,
      });
      setResult(receipt);
      await refreshSummary(receipt.studentId);
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  }

  // Online-payment path: verify the payment FIRST, and only on a genuine
  // success does anything get persisted. A failed, cancelled, or forged
  // payment leaves things exactly as they were. Works for the first
  // payment (admits the student) and any later installment (studentId set)
  // — either way the amount charged is whatever's in the amount field,
  // capped server-side against the full fee or the remaining balance.
  async function onPayOnline() {
    setError('');
    if (!admittedStudentId && !validateBeforeCharging()) return;
    if (!payment.amount || Number(payment.amount) <= 0) {
      setError('Enter the amount to pay.');
      return;
    }

    const razorpayMethod = RAZORPAY_METHOD_BY_TYPE[payment.type];
    if (!razorpayMethod) {
      setError('Select Bank Transfer, UPI or Net Banking to pay through Razorpay.');
      return;
    }

    setSaving(true);
    try {
      await loadRazorpayScript();

      const order = await createRazorpayOrder({
        studentId: admittedStudentId,
        courseId: Number(form.courseId),
        batchId: Number(form.batchId),
        amount: Number(payment.amount),
        enquiryId: admittedStudentId ? null : (form.enquiryId || null),
      });

      const rzp = new window.Razorpay({
        key: order.keyId,
        amount: order.amountInPaise,
        currency: order.currency,
        name: 'Computer Seekho',
        description: `${order.courseName || selectedCourse?.courseName || 'Course'} - ${admittedStudentId ? 'installment' : 'admission fee'}`,
        order_id: order.razorpayOrderId,
        method: { [razorpayMethod]: true },
        prefill: {
          name: form.studentName,
          contact: form.studentMobile,
          email: form.studentEmail || undefined,
        },
        theme: { color: '#0D9488' },
        handler: async (response) => {
          try {
            const receipt = await verifyRazorpayAdmission({
              studentId: admittedStudentId,
              razorpayOrderId: response.razorpay_order_id,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpaySignature: response.razorpay_signature,
              enquiryId: form.enquiryId || null,
              studentName: form.studentName,
              studentAddress: form.studentAddress || null,
              studentGender: form.studentGender || null,
              studentDob: form.studentDob || null,
              studentQualification: form.studentQualification || null,
              studentMobile: Number(form.studentMobile),
              studentEmail: form.studentEmail || null,
              photoUrl: form.photoUrl || null,
              courseId: Number(form.courseId),
              batchId: Number(form.batchId),
              remarks: payment.remarks || null,
            });
            setResult(receipt);
            await refreshSummary(receipt.studentId);
          } catch (err) {
            setError(`Payment was received, but could not be recorded: ${err.message}. Reference payment ID: ${response.razorpay_payment_id}.`);
          } finally {
            setSaving(false);
          }
        },
        modal: {
          ondismiss: () => setSaving(false),
        },
      });

      rzp.on('payment.failed', () => {
        setError('Payment failed. Nothing has been charged for this installment — you can try again.');
        setSaving(false);
      });

      rzp.open();
    } catch (err) {
      setError(err.message);
      setSaving(false);
    }
  }

  function printFeeReceipt() {
    const row = (label, value) => `
      <tr><td class="lbl">${label}</td><td class="val">${value ?? '—'}</td></tr>
    `;

    const paymentMode = payment.type;

    const summaryHtml = summary ? `
      <div class="section-title">Balance Summary</div>
      <table class="stats">
        <tr>
          <td><div class="stat-label">Total Course Fee</div><div class="stat-value">₹${Number(summary.courseFee).toLocaleString('en-IN')}</div></td>
          <td><div class="stat-label">Total Paid to Date</div><div class="stat-value ok">₹${Number(summary.totalPaid).toLocaleString('en-IN')}</div></td>
          <td><div class="stat-label">${summary.fullyPaid ? 'Status' : 'Balance Pending'}</div><div class="stat-value ${summary.fullyPaid ? 'ok' : 'danger'}">${summary.fullyPaid ? 'Fully Paid' : `₹${Number(summary.pendingAmount).toLocaleString('en-IN')}`}</div></td>
        </tr>
      </table>
    ` : '';

    const html = `<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Receipt ${result?.receiptId ? `RCPT-${result.receiptId}` : ''}</title>
<style>
  * { box-sizing: border-box; -webkit-print-color-adjust: exact; print-color-adjust: exact; }
  body { font-family: 'Segoe UI', Arial, Helvetica, sans-serif; color: #1f2937; margin: 0; padding: 14px; background: #f3f4f6; font-size: 12px; line-height: 1.35; }
  .paper { max-width: 720px; margin: 0 auto; background: #fff; border: 1px solid #d1d5db; border-radius: 6px; overflow: hidden; }
  .band { background: #0d9488; color: #fff; padding: 14px 28px; text-align: center; }
  .band h1 { margin: 0; font-size: 18px; font-weight: 700; }
  .band p { margin: 2px 0 0; font-size: 11px; color: #e0f2f1; }
  .title-row { display: flex; justify-content: space-between; align-items: center; padding: 10px 28px; border-bottom: 2px solid #0d9488; background: #f0fdfa; }
  .title-row .tag { font-size: 10px; font-weight: 700; letter-spacing: 1.5px; color: #0d9488; text-transform: uppercase; }
  .title-row .num { font-size: 15px; font-weight: 700; margin-top: 1px; }
  .title-row .date-block { text-align: right; }
  .title-row .lbl-sm { font-size: 10px; color: #6b7280; }
  .title-row .val-sm { font-size: 13px; font-weight: 600; }
  .content { padding: 4px 28px 16px; }
  .section-title { font-size: 10px; font-weight: 700; letter-spacing: 0.6px; text-transform: uppercase; color: #0d9488; margin: 12px 0 4px; padding-bottom: 3px; border-bottom: 1px solid #e5e7eb; }
  .info-table { width: 100%; border-collapse: collapse; }
  .info-table tr { border-bottom: 1px solid #f1f2f4; }
  .info-table tr:last-child { border-bottom: none; }
  .info-table td { padding: 3.5px 0; vertical-align: top; }
  .info-table td.lbl { width: 42%; color: #6b7280; }
  .info-table td.val { font-weight: 600; color: #111827; }
  .amount-box { display: flex; justify-content: space-between; align-items: center; background: #f0fdfa; border: 1px solid #99f6e4; border-radius: 6px; padding: 9px 16px; margin-top: 8px; }
  .amount-box .amt-label { font-size: 11px; color: #0f766e; font-weight: 600; }
  .amount-box .amt-value { font-size: 18px; font-weight: 800; color: #0d9488; }
  .stats { width: 100%; border-collapse: separate; border-spacing: 8px 0; margin-top: 2px; }
  .stats td { border: 1px solid #e5e7eb; border-radius: 6px; padding: 7px; text-align: center; width: 33.33%; }
  .stat-label { font-size: 10px; color: #6b7280; margin-bottom: 2px; }
  .stat-value { font-size: 14px; font-weight: 700; }
  .ok { color: #0d9488; }
  .danger { color: #dc2626; }
  .foot { display: flex; justify-content: space-between; align-items: flex-end; margin-top: 16px; padding-top: 8px; border-top: 1px solid #e5e7eb; }
  .foot .note { font-size: 10px; color: #9ca3af; }
  .sig { text-align: center; }
  .sig .line { border-top: 1px solid #9ca3af; padding-top: 3px; margin-top: 22px; font-size: 10px; color: #6b7280; min-width: 150px; }
  @media print { body { background: #fff; padding: 0; } .paper { border: none; border-radius: 0; max-width: 100%; } }
  @page { size: A4; margin: 10mm; }
</style>
</head>
<body>
  <div class="paper">
    <div class="band">
      <h1>Computer Seekho</h1>
      <p>USM's Vidyanidhi Info Tech Academy</p>
      <p>5th Floor, Vidyanidhi Education Complex, JVPD Scheme, Juhu, Mumbai 400049</p>
      <p>8368772333 / 87422262553 &middot; computerseekho10@gmail.com</p>
    </div>

    <div class="title-row">
      <div>
        <div class="tag">Fee Receipt</div>
        <div class="num">${result?.receiptId ? `RCPT-${result.receiptId}` : 'Draft'}</div>
      </div>
      <div class="date-block">
        <div class="lbl-sm">Date</div>
        <div class="val-sm">${new Date(payment.date).toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' })}</div>
      </div>
    </div>

    <div class="content">
      <div class="section-title">Student Details</div>
      <table class="info-table">
        ${row('Name', form.studentName)}
        ${row('Mobile', form.studentMobile)}
        ${row('Email', form.studentEmail)}
      </table>

      <div class="section-title">Course Details</div>
      <table class="info-table">
        ${row('Course', selectedCourse?.courseName)}
        ${row('Batch', selectedBatch?.batchName)}
      </table>

      <div class="section-title">This Payment</div>
      <table class="info-table">
        ${row('Payment Mode', paymentMode)}
        ${result?.razorpayPaymentId ? row('Transaction ID', result.razorpayPaymentId) : ''}
      </table>
      <div class="amount-box">
        <span class="amt-label">AMOUNT PAID</span>
        <span class="amt-value">₹${Number(result?.receiptAmount ?? payment.amount ?? 0).toLocaleString('en-IN')}</span>
      </div>

      ${summaryHtml}

      <div class="foot">
        <p class="note">This is a system-generated receipt.</p>
        <div class="sig"><div class="line">Authorized Signature</div></div>
      </div>
    </div>
  </div>
</body>
</html>`;

    const printWindow = window.open('', '_blank', 'width=850,height=1000');
    if (!printWindow) {
      setError('Your browser blocked the print window. Please allow pop-ups for this site and try again.');
      return;
    }
    printWindow.document.open();
    printWindow.document.write(html);
    printWindow.document.close();
    printWindow.focus();
    printWindow.onload = () => {
      printWindow.print();
    };
  }

  function printEnrollmentForm() {
    const row = (label, value) => `
      <div class="cell"><span class="lbl">${label}</span><b>${value ?? '—'}</b></div>
    `;

    const html = `<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Enrollment Form - ${form.studentName || 'Student'}</title>
<style>
  * { box-sizing: border-box; -webkit-print-color-adjust: exact; print-color-adjust: exact; }
  body {
    font-family: 'Segoe UI', Arial, Helvetica, sans-serif;
    color: #1f2937;
    margin: 0;
    padding: 14px;
    background: #f3f4f6;
    font-size: 12px;
    line-height: 1.4;
  }
  .paper {
    max-width: 720px;
    margin: 0 auto;
    background: #fff;
    border: 1px solid #d1d5db;
    border-radius: 6px;
    overflow: hidden;
  }
  .band {
    background: #0d9488;
    color: #fff;
    padding: 18px 28px;
    text-align: center;
  }
  .band h1 { margin: 0; font-size: 19px; font-weight: 700; }
  .band p { margin: 3px 0 0; font-size: 11px; color: #e0f2f1; }
  .title-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 28px;
    border-bottom: 2px solid #0d9488;
    background: #f0fdfa;
  }
  .title-row .tag { font-size: 10px; font-weight: 700; letter-spacing: 1.5px; color: #0d9488; text-transform: uppercase; }
  .title-row .num { font-size: 15px; font-weight: 700; margin-top: 1px; }
  .content { padding: 20px 28px 24px; display: flex; gap: 24px; }
  .photo-box {
    width: 110px;
    height: 130px;
    border: 1px solid #d1d5db;
    border-radius: 6px;
    overflow: hidden;
    flex-shrink: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f9fafb;
    color: #9ca3af;
    font-size: 10px;
    text-align: center;
  }
  .photo-box img { width: 100%; height: 100%; object-fit: cover; }
  .fields { flex: 1; }
  .section-title {
    font-size: 10px;
    font-weight: 700;
    letter-spacing: 0.6px;
    text-transform: uppercase;
    color: #0d9488;
    margin: 16px 28px 6px;
    padding-bottom: 4px;
    border-bottom: 1px solid #e5e7eb;
  }
  .grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px 20px; padding: 0 28px; }
  .cell .lbl { display: block; color: #6b7280; font-size: 10px; margin-bottom: 1px; }
  .cell b { font-size: 12.5px; color: #111827; }
  .full { grid-column: span 2; }
  .foot {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    margin: 28px 28px 20px;
    padding-top: 14px;
    border-top: 1px solid #e5e7eb;
  }
  .foot .note { font-size: 10px; color: #9ca3af; }
  .sig { text-align: center; }
  .sig .line { border-top: 1px solid #9ca3af; padding-top: 4px; margin-top: 34px; font-size: 10px; color: #6b7280; min-width: 150px; }
  @media print {
    body { background: #fff; padding: 0; }
    .paper { border: none; border-radius: 0; max-width: 100%; }
  }
  @page { size: A4; margin: 12mm; }
</style>
</head>
<body>
  <div class="paper">
    <div class="band">
      <h1>Computer Seekho</h1>
      <p>USM's Vidyanidhi Info Tech Academy</p>
      <p>5th Floor, Vidyanidhi Education Complex, JVPD Scheme, Juhu, Mumbai 400049</p>
      <p>8368772333 / 87422262553 &middot; computerseekho10@gmail.com</p>
    </div>

    <div class="title-row">
      <div>
        <div class="tag">Enrollment Form</div>
        <div class="num">${form.studentName || 'New Applicant'}</div>
      </div>
      <div class="tag" style="text-align:right">${new Date().toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' })}</div>
    </div>

    <div class="content">
      <div class="photo-box">
        ${form.photoUrl ? `<img src="${form.photoUrl}" alt="" />` : 'No photo'}
      </div>
      <div class="fields" style="display:grid; grid-template-columns: 1fr 1fr; gap: 10px 20px;">
        ${row('Mobile', form.studentMobile)}
        ${row('Email', form.studentEmail)}
        ${row('Date of Birth', form.studentDob)}
        ${row('Gender', form.studentGender)}
      </div>
    </div>

    <div class="section-title">Background</div>
    <div class="grid">
      ${row('Qualification', form.studentQualification)}
      <div class="cell full">${row('Address', form.studentAddress)}</div>
    </div>

    <div class="section-title">Course Details</div>
    <div class="grid">
      ${row('Course', selectedCourse?.courseName)}
      ${row('Batch', selectedBatch?.batchName)}
      ${row('Full Course Fee', form.courseFee ? `₹${Number(form.courseFee).toLocaleString('en-IN')}` : null)}
    </div>

    <div class="foot">
      <p class="note">This is a system-generated enrollment form.</p>
      <div class="sig"><div class="line">Authorized Signature</div></div>
    </div>
  </div>
</body>
</html>`;

    const printWindow = window.open('', '_blank', 'width=850,height=1000');
    if (!printWindow) {
      setError('Your browser blocked the print window. Please allow pop-ups for this site and try again.');
      return;
    }
    printWindow.document.open();
    printWindow.document.write(html);
    printWindow.document.close();
    printWindow.focus();
    printWindow.onload = () => {
      printWindow.print();
    };
  }

  function startOver() {
    setResult(null);
    setSelectedStudent(null);
    setSummary(null);
    setHistory(null);
    setSelected(null);
    setForm(blankForm());
    setPayment({ type: 'Cash', date: new Date().toISOString().slice(0, 10), amount: '', reference: '', remarks: '' });
    setError('');
  }

  const selectedCourse = courses.find((c) => c.courseId === Number(form.courseId));
  const selectedBatch = batches.find((b) => b.batchId === Number(form.batchId));
  const isOnline = ONLINE_PAYMENT_TYPES.includes(payment.type);
  const fullyPaid = Boolean(summary?.fullyPaid);

  return (
    <div>
      <div className="admin-title-row">
        <div>
          <h1>Student Admission</h1>
          <p className="muted">Search an enquiry, prefill student information, and collect payment in as many installments as needed.</p>
        </div>
        {admittedStudentId && (
          <button className="btn btn-outline btn-sm" type="button" onClick={startOver}>← Look up a different student</button>
        )}
      </div>

      {!admittedStudentId && (
        <div className="card card-pad-sm" style={{ marginBottom: 20, position: 'relative' }}>
          <div className="filterbar" style={{ marginBottom: 0 }}>
            <input className="filter-input search" placeholder="Search enquiry by phone number or name" value={search} onChange={(e) => setSearch(e.target.value)} />
            <button className="btn btn-primary btn-sm" type="button">Search</button>
          </div>
          {results.length > 0 && (
            <div className="search-results">
              {results.map((e) => {
                const status = deriveEnquiryStatus(e);
                const alreadyAdmitted = status === 'registered';
                return (
                  <button
                    className="search-result-row"
                    key={e.enquiryId}
                    style={alreadyAdmitted ? { opacity: 0.55, cursor: 'not-allowed' } : undefined}
                    onClick={() => {
                      if (alreadyAdmitted) {
                        setError('This enquiry is already admitted. Use "Manage Student" to look up their record and collect further payments.');
                        return;
                      }
                      selectEnquiry(e);
                      setSearch('');
                    }}
                  >
                    <div><b>{e.enquirerName}</b><span className="muted mono" style={{ marginLeft: 8 }}>{e.enquirerMobile}</span></div>
                    <StatusBadge status={status}>{STATUS_COPY[status]}</StatusBadge>
                  </button>
                );
              })}
            </div>
          )}
          {selected && (
            <p className="muted" style={{ marginTop: 10 }}>
              Prefilled from <b>ENQ-{selected.enquiryId}</b> — {selected.enquirerName}
            </p>
          )}

          <div style={{ display: 'flex', gap: 6, marginTop: 16 }}>
            {[
              ['name', 'Name'],
              ['mobile', 'Mobile'],
              ['studentId', 'Student ID'],
              ['admissionId', 'Admission ID'],
            ].map(([mode, label]) => (
              <button
                key={mode}
                type="button"
                className={`btn btn-sm ${studentSearchMode === mode ? 'btn-primary' : 'btn-outline'}`}
                onClick={() => { setStudentSearchMode(mode); setStudentSearch(''); setStudentResults([]); }}
              >
                {label}
              </button>
            ))}
          </div>
          <div className="filterbar" style={{ marginTop: 8, marginBottom: 0 }}>
            <input
              className="filter-input search"
              placeholder={
                studentSearchMode === 'name' ? 'Look up an already-admitted student by name'
                : studentSearchMode === 'mobile' ? 'Look up by exact mobile number'
                : studentSearchMode === 'studentId' ? 'Look up by exact Student ID (e.g. 42)'
                : 'Look up by exact Admission ID / Enquiry ID (e.g. 17)'
              }
              value={studentSearch}
              onChange={(e) => {
                const v = e.target.value;
                setStudentSearch(studentSearchMode === 'name' ? v : v.replace(/\D/g, ''));
              }}
            />
          </div>
          {studentSearching && <p className="muted" style={{ marginTop: 8, fontSize: 'var(--text-sm)' }}>Searching…</p>}
          {studentResults.length > 0 && (
            <div className="search-results">
              {studentResults.map((s) => (
                <button className="search-result-row" key={s.studentId} onClick={() => selectExistingStudent(s)}>
                  <div><b>{s.studentName}</b><span className="muted mono" style={{ marginLeft: 8 }}>STU-{s.studentId} · {s.studentMobile}</span></div>
                  <span className="muted">{s.courseName || '—'}</span>
                </button>
              ))}
            </div>
          )}
          {studentSearch.trim().length >= 2 && !studentSearching && studentResults.length === 0 && (
            <p className="muted" style={{ marginTop: 8, fontSize: 'var(--text-sm)' }}>No admitted students match "{studentSearch.trim()}".</p>
          )}
        </div>
      )}

      {error && <p className="alert alert-danger">{error}</p>}
      {allEnquiries === null && <Loading label="Loading…" />}

      <div className="admission-layout">
        <div>
          <div className="card form-section">
            <h3>Student registration</h3>
            <div className="admission-grid">
              <div className="field">
                <label>Photo</label>
                <div className="photo-upload">
                  {form.photoUrl ? <img src={form.photoUrl} alt="" /> : <span className="muted">No photo set</span>}
                </div>
                <div style={{ display: 'flex', gap: 8, marginTop: 8 }}>
                  <button
                    type="button"
                    className="btn btn-outline btn-sm"
                    disabled={Boolean(admittedStudentId)}
                    onClick={() => document.getElementById('photo-file-input').click()}
                  >
                    Browse…
                  </button>
                  {form.photoUrl && (
                    <button
                      type="button"
                      className="btn btn-ghost btn-sm"
                      disabled={Boolean(admittedStudentId)}
                      onClick={() => setForm({ ...form, photoUrl: '' })}
                    >
                      Remove
                    </button>
                  )}
                </div>
                <input
                  id="photo-file-input"
                  type="file"
                  accept="image/*"
                  style={{ display: 'none' }}
                  disabled={Boolean(admittedStudentId)}
                  onChange={(e) => {
                    const file = e.target.files?.[0];
                    e.target.value = ''; // allow picking the same file again later
                    if (!file) return;
                    if (!file.type.startsWith('image/')) {
                      setError('Please choose an image file.');
                      return;
                    }
                    if (file.size > 2 * 1024 * 1024) {
                      setError('Image is too large — please choose one under 2 MB.');
                      return;
                    }
                    setError('');
                    const reader = new FileReader();
                    reader.onload = () => setForm((f) => ({ ...f, photoUrl: reader.result }));
                    reader.onerror = () => setError('Could not read that image file. Please try another.');
                    reader.readAsDataURL(file);
                  }}
                />
                <input
                  className="input"
                  style={{ marginTop: 8 }}
                  placeholder="…or paste an image URL"
                  value={form.photoUrl?.startsWith('data:') ? '' : form.photoUrl}
                  onChange={(e) => setForm({ ...form, photoUrl: e.target.value })}
                  disabled={Boolean(admittedStudentId)}
                />
                {form.photoUrl?.startsWith('data:') && (
                  <span className="muted" style={{ fontSize: 'var(--text-xs)', display: 'block', marginTop: 4 }}>
                    Using browsed image — clear it above to paste a URL instead.
                  </span>
                )}
              </div>
              <div className="two-col" style={{ gridColumn: 'span 2' }}>
                <div className="field"><label>Student name *</label><input className="input" value={form.studentName} onChange={(e) => setForm({ ...form, studentName: e.target.value })} disabled={Boolean(admittedStudentId)} /></div>
                <div className="field"><label>Mobile *</label><input className="input" value={form.studentMobile} onChange={(e) => setForm({ ...form, studentMobile: e.target.value.replace(/\D/g, '') })} maxLength={10} disabled={Boolean(admittedStudentId)} /></div>
                <div className="field"><label>Email</label><input type="email" className="input" placeholder="student@example.com" value={form.studentEmail} onChange={(e) => setForm({ ...form, studentEmail: e.target.value })} disabled={Boolean(admittedStudentId)} /></div>
                <div className="field"><label>Date of birth</label><input type="date" className="input" value={form.studentDob} onChange={(e) => setForm({ ...form, studentDob: e.target.value })} disabled={Boolean(admittedStudentId)} /></div>
                <div className="field">
                  <label>Gender</label>
                  <select className="select" value={form.studentGender} onChange={(e) => setForm({ ...form, studentGender: e.target.value })} disabled={Boolean(admittedStudentId)}>
                    <option value="">Select</option><option>Male</option><option>Female</option><option>Other</option>
                  </select>
                </div>
                <div className="field"><label>Qualification</label><input className="input" value={form.studentQualification} onChange={(e) => setForm({ ...form, studentQualification: e.target.value })} disabled={Boolean(admittedStudentId)} /></div>
                <div className="field"><label>Address</label><input className="input" value={form.studentAddress} onChange={(e) => setForm({ ...form, studentAddress: e.target.value })} disabled={Boolean(admittedStudentId)} /></div>
              </div>
            </div>
            <div className="form-grid-3" style={{ marginTop: 14 }}>
              <div className="field">
                <label>Course *</label>
                <select className="select" value={form.courseId} onChange={(e) => { const c = courses.find((c) => String(c.courseId) === e.target.value); setForm({ ...form, courseId: e.target.value, batchId: '', courseFee: c?.courseFees || '' }); }} disabled={Boolean(admittedStudentId)}>
                  <option value="">Selected course</option>
                  {courses.map((c) => <option key={c.courseId} value={c.courseId}>{c.courseName}</option>)}
                </select>
              </div>
              <div className="field">
                <label>Batch *</label>
                <select className="select" value={form.batchId} onChange={(e) => setForm({ ...form, batchId: e.target.value })} disabled={!form.courseId || Boolean(admittedStudentId)}>
                  <option value="">Selected batch</option>
                  {batches.map((b) => <option key={b.batchId} value={b.batchId}>{b.batchName}</option>)}
                </select>
              </div>
              <div className="field"><label>Full course fee (INR)</label><input className="input" value={form.courseFee} disabled title="Set automatically from the selected course/batch" /></div>
            </div>
          </div>

          <div className="card form-section">
            <h3>{admittedStudentId ? 'Collect an installment' : 'Payment details'}</h3>
            {!admittedStudentId && (
              <p className="muted" style={{ marginTop: 4, fontSize: 'var(--text-sm)' }}>
                Pay the full fee now, or enter a smaller amount to collect the rest in more installments later.
              </p>
            )}
            <div className="form-grid-3" style={{ marginTop: 14 }}>
              <div className="field">
                <label>Payment type</label>
                <select className="select" value={payment.type} onChange={(e) => setPayment({ ...payment, type: e.target.value })} disabled={fullyPaid}>
                  {PAYMENT_TYPES.map((t) => <option key={t}>{t}</option>)}
                </select>
              </div>
              <div className="field"><label>Payment date</label><input type="date" className="input" value={payment.date} onChange={(e) => setPayment({ ...payment, date: e.target.value })} disabled={fullyPaid} /></div>
              {!isOnline && (
                <div className="field"><label>Transaction / Cheque / DD reference</label><input className="input" value={payment.reference} onChange={(e) => setPayment({ ...payment, reference: e.target.value })} disabled={fullyPaid} /></div>
              )}
              <div className="field"><label>Enquiry reference</label><input className="input" disabled value={form.enquiryId ? `ENQ-${form.enquiryId}` : '—'} /></div>
              <div className="field" style={{ maxWidth: 260 }}>
                <label>Amount to pay now (INR)</label>
                <input className="input" style={{ fontSize: 'var(--text-lg)', fontWeight: 600 }} value={payment.amount} onChange={(e) => setPayment({ ...payment, amount: e.target.value.replace(/\D/g, '') })} disabled={fullyPaid} />
              </div>
              <div className="field"><label>Remarks (optional)</label><input className="input" placeholder="e.g. partial payment, balance due next week" value={payment.remarks} onChange={(e) => setPayment({ ...payment, remarks: e.target.value })} disabled={fullyPaid} /></div>
            </div>
            {isOnline && !fullyPaid && (
              <p className="muted" style={{ marginTop: 10, fontSize: 'var(--text-sm)' }}>
                {admittedStudentId ? 'The student is only credited once payment succeeds.' : 'The student is only admitted once payment succeeds.'} A failed or cancelled payment leaves things exactly as they are now.
                {form.studentEmail ? ` A confirmation email with the receipt will be sent to ${form.studentEmail}.` : ' Add a student email above to also receive receipts by email.'}
              </p>
            )}
            <div className="form-actions">
              <button className="btn btn-outline" type="button" onClick={printEnrollmentForm}>Print Form</button>
              <button className="btn btn-outline" type="button" onClick={printFeeReceipt} disabled={!result}>Print Receipt</button>
              {isOnline ? (
                <button className="btn btn-primary" type="button" onClick={onPayOnline} disabled={saving || fullyPaid}>
                  {saving ? <span className="spinner" /> : fullyPaid ? 'Fully paid' : admittedStudentId ? 'Pay next installment' : 'Pay & Admit via Razorpay'}
                </button>
              ) : (
                <button className="btn btn-primary" type="button" onClick={onConfirm} disabled={saving || fullyPaid}>
                  {saving ? <span className="spinner" /> : fullyPaid ? 'Fully paid' : admittedStudentId ? 'Record installment' : 'Confirm Admission'}
                </button>
              )}
            </div>
          </div>
        </div>

        <div className="card receipt-preview" style={{ minWidth: 0 }}>
          <div className="panel-head">
            <h3>History</h3>
            <span className="badge badge-plain">{fullyPaid ? 'Fully paid' : admittedStudentId ? 'Active' : 'No student selected'}</span>
          </div>

          {!admittedStudentId && (
            <p className="muted" style={{ fontSize: 'var(--text-sm)' }}>
              Admit a student, or look up an existing one above, to see their payment history here.
            </p>
          )}

          {summary && (
            <div className="stat-row" style={{ display: 'grid', gridTemplateColumns: 'repeat(3, minmax(0, 1fr))', gap: 12 }}>
              <div className="stat-tile" style={{ minWidth: 0 }}><b style={{ fontSize: 20, whiteSpace: 'nowrap' }}>₹{Number(summary.courseFee).toLocaleString('en-IN')}</b><span>Total course fee</span></div>
              <div className="stat-tile" style={{ minWidth: 0 }}><b style={{ color: 'var(--ok-600)', fontSize: 20, whiteSpace: 'nowrap' }}>₹{Number(summary.totalPaid).toLocaleString('en-IN')}</b><span>Total paid</span></div>
              <div className="stat-tile" style={{ minWidth: 0 }}>
                <b style={{ color: summary.fullyPaid ? 'var(--ok-600)' : 'var(--danger-600)', fontSize: 20, whiteSpace: 'nowrap' }}>₹{Number(summary.pendingAmount).toLocaleString('en-IN')}</b>
                <span>{summary.fullyPaid ? 'Fully paid' : 'Remaining fee'}</span>
              </div>
            </div>
          )}

          {history && history.length > 0 && (
            <div className="table-wrap" style={{ marginTop: 16, overflowX: 'auto' }}>
              <table className="data-table">
                <thead>
                  <tr>
                    <th style={{ whiteSpace: 'nowrap' }}>Receipt No</th>
                    <th style={{ whiteSpace: 'nowrap' }}>Date</th>
                    <th style={{ whiteSpace: 'nowrap' }}>Paid Fee</th>
                    <th style={{ whiteSpace: 'nowrap' }}>Remaining Fee</th>
                    <th style={{ whiteSpace: 'nowrap' }}>Mode</th>
                    <th style={{ whiteSpace: 'nowrap' }}>Transaction ID</th>
                    <th style={{ whiteSpace: 'nowrap' }}>Status</th>
                    <th style={{ whiteSpace: 'nowrap' }}>Collected By</th>
                    <th>Remarks</th>
                    <th style={{ whiteSpace: 'nowrap' }}>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {history.map((p) => (
                    <tr key={p.paymentId}>
                      <td style={{ whiteSpace: 'nowrap' }}>{p.receiptId ? `RCPT-${p.receiptId}` : '—'}</td>
                      <td style={{ whiteSpace: 'nowrap' }}>{p.paymentDate ? new Date(p.paymentDate).toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' }) : '—'}</td>
                      <td style={{ whiteSpace: 'nowrap' }}>₹{Number(p.amountPaid || 0).toLocaleString('en-IN')}</td>
                      <td style={{ whiteSpace: 'nowrap' }}>₹{Number(p.remainingFeeAfter || 0).toLocaleString('en-IN')}</td>
                      <td style={{ whiteSpace: 'nowrap' }}>{p.paymentMode || '—'}</td>
                      <td className="mono" style={{ fontSize: 'var(--text-xs)', whiteSpace: 'nowrap' }}>{p.transactionId || '—'}</td>
                      <td style={{ whiteSpace: 'nowrap' }}>
                        <span className={`badge ${p.status === 'Success' ? 'badge-ok' : p.status === 'Failed' ? 'badge-danger' : 'badge-plain'}`}>
                          {p.status}
                        </span>
                      </td>
                      <td style={{ whiteSpace: 'nowrap' }}>{p.collectedBy || '—'}</td>
                      <td style={{ maxWidth: 160, whiteSpace: 'normal' }}>{p.remarks || '—'}</td>
                      <td style={{ whiteSpace: 'nowrap' }}>
                        {p.receiptId ? (
                          <Link to={`/admin/receipt/${p.receiptId}`} target="_blank" rel="noopener noreferrer" className="btn btn-outline btn-sm">
                            View / Print
                          </Link>
                        ) : '—'}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          {admittedStudentId && (!history || history.length === 0) && (
            <p className="muted" style={{ marginTop: 12, fontSize: 'var(--text-sm)' }}>No payments recorded yet for this student.</p>
          )}

          {result && (
            <p className="muted" style={{ marginTop: 12, fontSize: 'var(--text-sm)' }}>
              {result.emailSent
                ? `Confirmation email with receipt sent to ${result.studentEmail}.`
                : form.studentEmail
                  ? 'Payment recorded, but the confirmation email could not be sent — check the mail server settings on the backend.'
                  : 'Payment recorded. No student email was provided, so no confirmation email was sent.'}
            </p>
          )}
        </div>
      </div>
    </div>
  );
}

function blankForm() {
  return {
    enquiryId: '', studentName: '', studentMobile: '', studentEmail: '', studentAddress: '', studentDob: '', studentGender: '',
    studentQualification: '', photoUrl: '', courseId: '', batchId: '', courseFee: '',
  };
}