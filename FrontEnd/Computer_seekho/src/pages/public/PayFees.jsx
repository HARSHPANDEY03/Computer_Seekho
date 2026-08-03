import { useEffect, useState } from 'react';
import { getActiveCourses, getActiveBatchesByCourse } from '../../api/courses';
import { createRazorpayOrder, verifyRazorpayAdmission } from '../../api/students';

const emptyForm = {
  studentName: '', studentMobile: '', studentEmail: '', studentAddress: '',
  studentDob: '', studentGender: '', studentQualification: '',
  courseId: '', batchId: '',
};

// Loads Razorpay's Checkout script once and reuses the same promise on
// every subsequent call, instead of injecting the <script> tag again.
// (Same helper as the admin Admissions page, kept local here since this
// page can be reached without ever visiting /admin.)
function loadRazorpayScript() {
  if (window.Razorpay) return Promise.resolve();
  if (loadRazorpayScript._promise) return loadRazorpayScript._promise;
  loadRazorpayScript._promise = new Promise((resolve, reject) => {
    const script = document.createElement('script');
    script.src = 'https://checkout.razorpay.com/v1/checkout.js';
    script.onload = () => resolve();
    script.onerror = () => reject(new Error('Could not load the payment gateway. Check your connection and try again.'));
    document.body.appendChild(script);
  });
  return loadRazorpayScript._promise;
}

export default function PayFees() {
  const [courses, setCourses] = useState([]);
  const [batches, setBatches] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [errors, setErrors] = useState({});
  const [paying, setPaying] = useState(false);
  const [error, setError] = useState('');
  const [receipt, setReceipt] = useState(null);

  useEffect(() => {
    getActiveCourses().then(setCourses).catch(() => setCourses([]));
  }, []);

  useEffect(() => {
    if (form.courseId) getActiveBatchesByCourse(form.courseId).then(setBatches).catch(() => setBatches([]));
    else setBatches([]);
  }, [form.courseId]);

  function update(key, value) {
    setForm((f) => ({ ...f, [key]: value }));
  }

  const selectedCourse = courses.find((c) => c.courseId === Number(form.courseId));
  const selectedBatch = batches.find((b) => b.batchId === Number(form.batchId));
  const feeToShow = selectedBatch?.courseFees ?? selectedCourse?.courseFees ?? null;

  function validate() {
    const e = {};
    if (!form.studentName.trim()) e.studentName = 'Enter your full name.';
    if (!/^\d{10}$/.test(form.studentMobile.trim())) e.studentMobile = 'Enter a valid 10-digit mobile number.';
    if (form.studentEmail && !/^\S+@\S+\.\S+$/.test(form.studentEmail)) e.studentEmail = 'Enter a valid email address.';
    if (!form.courseId) e.courseId = 'Select a course.';
    if (!form.batchId) e.batchId = 'Select a batch.';
    setErrors(e);
    return Object.keys(e).length === 0;
  }

  // Payment is verified server-side FIRST; only a genuine success creates
  // the student record. A failed or cancelled payment leaves nothing behind.
  async function onPay() {
    setError('');
    if (!validate()) return;

    setPaying(true);
    try {
      await loadRazorpayScript();

      const order = await createRazorpayOrder({
        courseId: Number(form.courseId),
        batchId: Number(form.batchId),
      });

      const rzp = new window.Razorpay({
        key: order.keyId,
        amount: order.amountInPaise,
        currency: order.currency,
        name: 'Computer Seekho',
        description: `${order.courseName || selectedCourse?.courseName || 'Course'} admission fee`,
        order_id: order.razorpayOrderId,
        prefill: {
          name: form.studentName,
          contact: form.studentMobile,
          email: form.studentEmail || undefined,
        },
        theme: { color: '#0D9488' },
        handler: async (response) => {
          try {
            const result = await verifyRazorpayAdmission({
              razorpayOrderId: response.razorpay_order_id,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpaySignature: response.razorpay_signature,
              enquiryId: null,
              studentName: form.studentName,
              studentAddress: form.studentAddress || null,
              studentGender: form.studentGender || null,
              studentDob: form.studentDob || null,
              studentQualification: form.studentQualification || null,
              studentMobile: Number(form.studentMobile),
              studentEmail: form.studentEmail || null,
              photoUrl: null,
              courseId: Number(form.courseId),
              batchId: Number(form.batchId),
            });
            setReceipt(result);
          } catch (err) {
            // Razorpay itself succeeded, but our verification/admission step
            // failed — the person is not admitted. Keep the payment ID
            // visible so the institute can reconcile it manually if needed.
            setError(`Payment was received, but admission could not be completed: ${err.message}. Reference payment ID: ${response.razorpay_payment_id}. Please contact the institute with this reference.`);
          } finally {
            setPaying(false);
          }
        },
        modal: {
          ondismiss: () => setPaying(false),
        },
      });

      rzp.on('payment.failed', () => {
        setError('Payment failed or was declined. Nothing has been charged and no admission was created — you can try again.');
        setPaying(false);
      });

      rzp.open();
    } catch (err) {
      setError(err.message);
      setPaying(false);
    }
  }

  if (receipt) {
    return (
      <div className="container section">
        <div className="card card-pad form-card" style={{ maxWidth: 560, margin: '0 auto' }}>
          <div className="alert alert-ok">
            <b>Payment successful — admission confirmed!</b>
            <p style={{ marginTop: 6 }}>
              {receipt.emailSent
                ? `A confirmation email with your receipt and admission slip has been sent to ${receipt.studentEmail}.`
                : 'Keep the details below for your records. Contact the institute if you need a copy of your receipt resent.'}
            </p>
          </div>
          <div className="receipt-paper" style={{ marginTop: 16 }}>
            <h4>COMPUTER SEEKHO — FEE RECEIPT</h4>
            <div className="receipt-row"><span>Receipt No.</span><b>RCPT-{receipt.receiptId}</b></div>
            <div className="receipt-row"><span>Student</span><b>{receipt.studentName}</b></div>
            <div className="receipt-row"><span>Course / Batch</span><b>{receipt.courseName} {receipt.batchName ? `/ ${receipt.batchName}` : ''}</b></div>
            <div className="receipt-row"><span>Amount paid</span><b>₹{Number(receipt.receiptAmount).toLocaleString('en-IN')}</b></div>
            <div className="receipt-row"><span>Payment ID</span><b>{receipt.razorpayPaymentId}</b></div>
          </div>
          <button className="btn btn-outline btn-sm" style={{ marginTop: 16 }} onClick={() => window.print()}>Print receipt</button>
        </div>
      </div>
    );
  }

  return (
    <div className="container section">
      <div className="form-layout">
        <div className="info-panel">
          <p className="pill">Pay fees online</p>
          <h1 className="h2">Pay your admission fee securely.</h1>
          <p className="body-text">
            Select your course and batch, enter your details, and pay via UPI, net banking, cards or wallets through
            Razorpay. You're admitted the moment payment is verified, and a receipt is emailed to you automatically.
          </p>
          <div className="info-list">
            <div className="info-item">
              <div className="info-icon" aria-hidden="true" />
              <div><b>Secure checkout</b><span>Payments are processed and verified by Razorpay — we never see your card or bank details.</span></div>
            </div>
            <div className="info-item">
              <div className="info-icon" aria-hidden="true" />
              <div><b>Instant confirmation</b><span>Your admission is created the moment payment succeeds, with an emailed receipt and admission slip.</span></div>
            </div>
          </div>
        </div>

        <div className="card card-pad form-card">
          <h2 className="h3">Your details</h2>
          {error && <p className="alert alert-danger" style={{ marginTop: 12 }}>{error}</p>}
          <div className="form-grid" style={{ marginTop: 16 }}>
            <div className="field">
              <label>Full name *</label>
              <input className={`input ${errors.studentName ? 'has-error' : ''}`} value={form.studentName} onChange={(e) => update('studentName', e.target.value)} placeholder="Full name" />
              {errors.studentName && <span className="error">{errors.studentName}</span>}
            </div>
            <div className="field">
              <label>Mobile number *</label>
              <input className={`input ${errors.studentMobile ? 'has-error' : ''}`} value={form.studentMobile} onChange={(e) => update('studentMobile', e.target.value.replace(/\D/g, ''))} placeholder="10-digit mobile number" maxLength={10} />
              {errors.studentMobile && <span className="error">{errors.studentMobile}</span>}
            </div>
            <div className="field">
              <label>Email address</label>
              <input className={`input ${errors.studentEmail ? 'has-error' : ''}`} value={form.studentEmail} onChange={(e) => update('studentEmail', e.target.value)} placeholder="name@example.com — for your receipt" />
              {errors.studentEmail && <span className="error">{errors.studentEmail}</span>}
            </div>
            <div className="field">
              <label>Date of birth</label>
              <input type="date" className="input" value={form.studentDob} onChange={(e) => update('studentDob', e.target.value)} />
            </div>
            <div className="field full">
              <label>Address</label>
              <input className="input" value={form.studentAddress} onChange={(e) => update('studentAddress', e.target.value)} placeholder="Residential address" />
            </div>
            <div className="field">
              <label>Course *</label>
              <select className={`select ${errors.courseId ? 'has-error' : ''}`} value={form.courseId} onChange={(e) => update('courseId', e.target.value)}>
                <option value="">Select course</option>
                {courses.map((c) => <option key={c.courseId} value={c.courseId}>{c.courseName}</option>)}
              </select>
              {errors.courseId && <span className="error">{errors.courseId}</span>}
            </div>
            <div className="field">
              <label>Batch *</label>
              <select className={`select ${errors.batchId ? 'has-error' : ''}`} value={form.batchId} onChange={(e) => update('batchId', e.target.value)} disabled={!form.courseId}>
                <option value="">Select batch</option>
                {batches.map((b) => <option key={b.batchId} value={b.batchId}>{b.batchName}</option>)}
              </select>
              {errors.batchId && <span className="error">{errors.batchId}</span>}
            </div>
          </div>

          {feeToShow != null && (
            <p className="body-text" style={{ marginTop: 14 }}>
              Amount to pay: <b>₹{Number(feeToShow).toLocaleString('en-IN')}</b>
            </p>
          )}

          <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: 16 }}>
            <button className="btn btn-primary" type="button" onClick={onPay} disabled={paying}>
              {paying ? <span className="spinner" /> : 'Pay & Confirm Admission'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
