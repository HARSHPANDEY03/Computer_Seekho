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
  const [printMode, setPrintMode] = useState(null);
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
        getPaymentsByStudent(studentId).catch(() => []),
      ]);
      setSummary(s);
      setHistory(h);
      setPayment((p) => ({ ...p, amount: s.fullyPaid ? '' : String(s.pendingAmount) }));
    } catch {
      // Non-fatal — the payment itself already succeeded (if this was
      // called after one); the summary panel just won't show.
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

  function doPrint(mode) {
    setPrintMode(mode);
    setTimeout(() => { window.print(); setPrintMode(null); }, 60);
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
                return (
                  <button className="search-result-row" key={e.enquiryId} onClick={() => { selectEnquiry(e); setSearch(''); }}>
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
                <label>Photo URL</label>
                <div className="photo-upload">
                  {form.photoUrl ? <img src={form.photoUrl} alt="" /> : <span className="muted">No photo URL set</span>}
                </div>
                <input className="input" style={{ marginTop: 8 }} placeholder="https://…" value={form.photoUrl} onChange={(e) => setForm({ ...form, photoUrl: e.target.value })} disabled={Boolean(admittedStudentId)} />
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
              <div className="field"><label>Full course fee (INR)</label><input className="input" value={form.courseFee} onChange={(e) => setForm({ ...form, courseFee: e.target.value })} disabled={Boolean(admittedStudentId)} /></div>
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
              <div className="field">
                <label>Amount to pay now (INR)</label>
                <input className="input" value={payment.amount} onChange={(e) => setPayment({ ...payment, amount: e.target.value.replace(/\D/g, '') })} disabled={fullyPaid} />
              </div>
              {!isOnline && (
                <div className="field"><label>Transaction / Cheque / DD reference</label><input className="input" value={payment.reference} onChange={(e) => setPayment({ ...payment, reference: e.target.value })} disabled={fullyPaid} /></div>
              )}
              <div className="field"><label>Enquiry reference</label><input className="input" disabled value={form.enquiryId ? `ENQ-${form.enquiryId}` : '—'} /></div>
              <div className="field"><label>Remarks (optional)</label><input className="input" placeholder="e.g. partial payment, balance due next week" value={payment.remarks} onChange={(e) => setPayment({ ...payment, remarks: e.target.value })} disabled={fullyPaid} /></div>
            </div>
            {isOnline && !fullyPaid && (
              <p className="muted" style={{ marginTop: 10, fontSize: 'var(--text-sm)' }}>
                {admittedStudentId ? 'The student is only credited once payment succeeds.' : 'The student is only admitted once payment succeeds.'} A failed or cancelled payment leaves things exactly as they are now.
                {form.studentEmail ? ` A confirmation email with the receipt will be sent to ${form.studentEmail}.` : ' Add a student email above to also receive receipts by email.'}
              </p>
            )}
            <div className="form-actions">
              <button className="btn btn-outline" type="button" onClick={() => doPrint('form')}>Print Form</button>
              <button className="btn btn-outline" type="button" onClick={() => doPrint('receipt')} disabled={!result}>Print Receipt</button>
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

        <div className="card receipt-preview">
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
            <div className="stat-row">
              <div className="stat-tile"><b>₹{Number(summary.courseFee).toLocaleString('en-IN')}</b><span>Total course fee</span></div>
              <div className="stat-tile"><b style={{ color: 'var(--ok-600)' }}>₹{Number(summary.totalPaid).toLocaleString('en-IN')}</b><span>Total paid</span></div>
              <div className="stat-tile">
                <b style={{ color: summary.fullyPaid ? 'var(--ok-600)' : 'var(--danger-600)' }}>₹{Number(summary.pendingAmount).toLocaleString('en-IN')}</b>
                <span>{summary.fullyPaid ? 'Fully paid' : 'Remaining fee'}</span>
              </div>
            </div>
          )}

          {history && history.length > 0 && (
            <div className="table-wrap" style={{ marginTop: 16 }}>
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Receipt No</th>
                    <th>Date</th>
                    <th>Paid Fee</th>
                    <th>Remaining Fee</th>
                    <th>Mode</th>
                    <th>Transaction ID</th>
                    <th>Status</th>
                    <th>Collected By</th>
                    <th>Remarks</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {history.map((p) => (
                    <tr key={p.paymentId}>
                      <td>{p.receiptId ? `RCPT-${p.receiptId}` : '—'}</td>
                      <td>{p.paymentDate ? new Date(p.paymentDate).toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' }) : '—'}</td>
                      <td>₹{Number(p.amountPaid || 0).toLocaleString('en-IN')}</td>
                      <td>₹{Number(p.remainingFeeAfter || 0).toLocaleString('en-IN')}</td>
                      <td>{p.paymentMode || '—'}</td>
                      <td className="mono" style={{ fontSize: 'var(--text-xs)' }}>{p.transactionId || '—'}</td>
                      <td>
                        <span className={`badge ${p.status === 'Success' ? 'badge-ok' : p.status === 'Failed' ? 'badge-danger' : 'badge-plain'}`}>
                          {p.status}
                        </span>
                      </td>
                      <td>{p.collectedBy || '—'}</td>
                      <td style={{ maxWidth: 160, whiteSpace: 'normal' }}>{p.remarks || '—'}</td>
                      <td>
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

      {printMode && (
        <div className="print-area">
          {printMode === 'form' ? (
            <div>
              <h2>USM's Vidyanidhi Info Tech Academy — Enrollment Form</h2>
              <p>Name: {form.studentName}</p>
              <p>Mobile: {form.studentMobile}</p>
              <p>Email: {form.studentEmail || '—'}</p>
              <p>Date of birth: {form.studentDob || '—'}</p>
              <p>Gender: {form.studentGender || '—'}</p>
              <p>Address: {form.studentAddress || '—'}</p>
              <p>Qualification: {form.studentQualification || '—'}</p>
              <p>Course: {selectedCourse?.courseName || '—'}</p>
              <p>Batch: {selectedBatch?.batchName || '—'}</p>
              <p>Full course fee: {form.courseFee ? `₹${form.courseFee}` : '—'}</p>
            </div>
          ) : (
            <div>
              <h2>Computer Seekho — Fee Receipt</h2>
              {result?.receiptId && <p>Receipt No.: RCPT-{result.receiptId}</p>}
              <p>Student: {form.studentName}</p>
              <p>Course / Batch: {selectedCourse?.courseName} / {selectedBatch?.batchName}</p>
              <p>Payment type: {payment.type}</p>
              <p>This payment: ₹{payment.amount}</p>
              <p>Date: {payment.date}</p>
              {summary && <p>Remaining fee: ₹{summary.pendingAmount}</p>}
            </div>
          )}
        </div>
      )}
    </div>
  );
}

function blankForm() {
  return {
    enquiryId: '', studentName: '', studentMobile: '', studentEmail: '', studentAddress: '', studentDob: '', studentGender: '',
    studentQualification: '', photoUrl: '', courseId: '', batchId: '', courseFee: '',
  };
}