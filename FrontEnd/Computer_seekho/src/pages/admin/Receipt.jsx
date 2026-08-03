import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { getReceiptById, getPaymentSummary, getPaymentsByStudent } from '../../api/students';
import { Loading } from '../../components/ui/ui';

function money(v) {
  if (v === null || v === undefined || v === '') return '—';
  return `₹${Number(v).toLocaleString('en-IN')}`;
}
function formatDate(d) {
  if (!d) return '—';
  return new Date(d).toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
}

export default function Receipt() {
  const { receiptId } = useParams();
  const [receipt, setReceipt] = useState(null);
  const [summary, setSummary] = useState(null);
  const [history, setHistory] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    let cancelled = false;
    setReceipt(null);
    setSummary(null);
    setHistory(null);
    setError('');

    getReceiptById(receiptId)
      .then(async (r) => {
        if (cancelled) return;
        setReceipt(r);
        const studentId = r?.student?.studentId;
        if (!studentId) return;
        const [s, h] = await Promise.all([
          getPaymentSummary(studentId).catch(() => null),
          getPaymentsByStudent(studentId).catch(() => []),
        ]);
        if (cancelled) return;
        setSummary(s);
        setHistory(h);
      })
      .catch((e) => !cancelled && setError(e.message));

    return () => { cancelled = true; };
  }, [receiptId]);

  if (error) {
    return (
      <div>
        <p className="alert alert-danger">{error}</p>
        <Link to="/admin/admissions" className="btn btn-outline btn-sm">Back to Admissions</Link>
      </div>
    );
  }

  if (!receipt) return <Loading label="Loading receipt…" />;

  const student = receipt.student || {};
  const payment = receipt.payment || {};
  const course = receipt.course || payment.course || student.course || {};
  const batch = payment.batch || student.batch || {};
  const paymentMode = payment.paymentType?.paymentTypeDesc || (payment.razorpayPaymentId ? 'Online (Razorpay)' : '—');

  return (
    <div>
      <style>{`
        @media print {
          .admin-sidebar, .admin-topbar, .no-print { display: none !important; }
          .admin-main, .admin-content { margin: 0 !important; padding: 0 !important; }
          .receipt-page-card { box-shadow: none !important; border: none !important; }
        }
      `}</style>

      <div className="admin-title-row no-print">
        <div>
          <h1>Fee Receipt</h1>
          <p className="muted">Receipt RCPT-{receipt.receiptId} — printable record for this payment.</p>
        </div>
        <div style={{ display: 'flex', gap: 10 }}>
          <Link to="/admin/admissions" className="btn btn-outline btn-sm">Back to Admissions</Link>
          <button className="btn btn-primary btn-sm" type="button" onClick={() => window.print()}>Print Receipt</button>
        </div>
      </div>

      <div className="card card-pad receipt-page-card" style={{ maxWidth: 760, margin: '0 auto' }}>
        <div style={{ textAlign: 'center', borderBottom: '2px solid var(--border-strong, #ccc)', paddingBottom: 16, marginBottom: 20 }}>
          <h2 style={{ margin: 0 }}>Computer Seekho</h2>
          <p className="muted" style={{ margin: '4px 0 0' }}>USM's Vidyanidhi Info Tech Academy</p>
          <p className="muted" style={{ margin: '2px 0 0', fontSize: 'var(--text-sm)' }}>
            5th Floor, Vidyanidhi Education Complex, JVPD Scheme, Juhu, Mumbai 400049
          </p>
          <p className="muted" style={{ margin: '2px 0 0', fontSize: 'var(--text-sm)' }}>
            022-2625 5629 / 2670 5498 · training.vita@gmail.com
          </p>
          <h3 style={{ marginTop: 14, letterSpacing: 1 }}>FEE RECEIPT</h3>
        </div>

        <div className="two-col" style={{ marginBottom: 20 }}>
          <div><span className="muted">Receipt No.</span><br /><b>RCPT-{receipt.receiptId}</b></div>
          <div><span className="muted">Receipt Date</span><br /><b>{formatDate(receipt.receiptDate)}</b></div>
        </div>

        <h4 style={{ borderBottom: '1px solid var(--border, #e5e7eb)', paddingBottom: 6 }}>Student Details</h4>
        <div className="two-col" style={{ marginBottom: 20 }}>
          <div><span className="muted">Name</span><br /><b>{student.studentName || '—'}</b></div>
          <div><span className="muted">Student ID</span><br /><b>{student.studentId ? `STU-${student.studentId}` : '—'}</b></div>
          <div><span className="muted">Mobile</span><br /><b>{student.studentMobile || '—'}</b></div>
          <div><span className="muted">Email</span><br /><b>{student.studentEmail || '—'}</b></div>
          <div><span className="muted">Date of birth</span><br /><b>{formatDate(student.studentDob)}</b></div>
          <div><span className="muted">Gender</span><br /><b>{student.studentGender || '—'}</b></div>
          <div className="field full"><span className="muted">Address</span><br /><b>{student.studentAddress || '—'}</b></div>
        </div>

        <h4 style={{ borderBottom: '1px solid var(--border, #e5e7eb)', paddingBottom: 6 }}>Course Details</h4>
        <div className="two-col" style={{ marginBottom: 20 }}>
          <div><span className="muted">Course</span><br /><b>{course.courseName || '—'}</b></div>
          <div><span className="muted">Batch</span><br /><b>{batch.batchName || '—'}</b></div>
        </div>

        <h4 style={{ borderBottom: '1px solid var(--border, #e5e7eb)', paddingBottom: 6 }}>This Payment</h4>
        <div className="two-col" style={{ marginBottom: 20 }}>
          <div><span className="muted">Amount Paid</span><br /><b>{money(receipt.receiptAmount)}</b></div>
          <div><span className="muted">Payment Mode</span><br /><b>{paymentMode}</b></div>
          <div><span className="muted">Payment Status</span><br /><b>{payment.status || 'PAID'}</b></div>
          {payment.razorpayPaymentId && (
            <div><span className="muted">Razorpay Payment ID</span><br /><b className="mono">{payment.razorpayPaymentId}</b></div>
          )}
        </div>

        {summary && (
          <>
            <h4 style={{ borderBottom: '1px solid var(--border, #e5e7eb)', paddingBottom: 6 }}>Balance Summary</h4>
            <div className="stat-row" style={{ marginBottom: 20 }}>
              <div className="stat-tile"><b>{money(summary.courseFee)}</b><span>Total course fee</span></div>
              <div className="stat-tile"><b style={{ color: 'var(--ok-600)' }}>{money(summary.totalPaid)}</b><span>Total paid to date</span></div>
              <div className="stat-tile">
                <b style={{ color: summary.fullyPaid ? 'var(--ok-600)' : 'var(--danger-600)' }}>{money(summary.pendingAmount)}</b>
                <span>{summary.fullyPaid ? 'Fully paid' : 'Balance pending'}</span>
              </div>
            </div>
          </>
        )}

        {history && history.length > 0 && (
          <>
            <h4 style={{ borderBottom: '1px solid var(--border, #e5e7eb)', paddingBottom: 6 }}>Payment History</h4>
            <div className="table-wrap" style={{ marginBottom: 20 }}>
              <table className="data-table">
                <thead>
                  <tr><th>Date</th><th>Mode</th><th>Reference</th><th>Amount</th><th>Status</th></tr>
                </thead>
                <tbody>
                  {history.map((p) => (
                    <tr key={p.paymentId} style={p.paymentId === payment.paymentId ? { fontWeight: 600 } : undefined}>
                      <td>{formatDate(p.paymentDate)}</td>
                      <td>{p.paymentMode || '—'}</td>
                      <td className="mono" style={{ fontSize: 'var(--text-xs)' }}>{p.transactionId || '—'}</td>
                      <td>{money(p.amountPaid)}</td>
                      <td>{p.status}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </>
        )}

        <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 40, paddingTop: 20, borderTop: '1px solid var(--border, #e5e7eb)' }}>
          <div>
            <p className="muted" style={{ fontSize: 'var(--text-sm)' }}>This is a system-generated receipt.</p>
          </div>
          <div style={{ textAlign: 'center' }}>
            <div style={{ height: 40 }} />
            <p style={{ borderTop: '1px solid var(--border-strong, #999)', paddingTop: 4, fontSize: 'var(--text-sm)' }}>Authorized Signature</p>
          </div>
        </div>
      </div>
    </div>
  );
}