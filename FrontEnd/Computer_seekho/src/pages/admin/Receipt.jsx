import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import jsPDF from 'jspdf';
import { getReceiptById, getPaymentSummary } from '../../api/students';
import { Loading } from '../../components/ui/ui';

function money(v) {
  if (v === null || v === undefined || v === '') return '—';
  return `Rs. ${Number(v).toLocaleString('en-IN')}`;
}
function moneyUi(v) {
  if (v === null || v === undefined || v === '') return '—';
  return `₹${Number(v).toLocaleString('en-IN')}`;
}
function formatDate(d) {
  if (!d) return '—';
  return new Date(d).toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
}

// The actual receipt markup, shared by the on-screen view and the
// print-only portal below so it's only written once.
function ReceiptBody({ receipt, summary, student, payment, course, batch, paymentMode }) {
  return (
    <>
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
        <div><span className="muted">Amount Paid</span><br /><b>{moneyUi(receipt.receiptAmount)}</b></div>
        <div><span className="muted">Payment Mode</span><br /><b>{paymentMode}</b></div>
        <div><span className="muted">Payment Status</span><br /><b>{payment.status || 'PAID'}</b></div>
        {payment.razorpayPaymentId && (
          <div><span className="muted">Razorpay Payment ID</span><br /><b className="mono">{payment.razorpayPaymentId}</b></div>
        )}
      </div>

      {summary && (
        <>
          <h4 style={{ borderBottom: '1px solid var(--border, #e5e7eb)', paddingBottom: 6 }}>Balance Summary</h4>
          <div className="stat-row" style={{ marginBottom: 20, display: 'grid', gridTemplateColumns: 'repeat(3, minmax(0, 1fr))', gap: 12 }}>
            <div className="stat-tile" style={{ minWidth: 0 }}><b style={{ fontSize: 20, whiteSpace: 'nowrap' }}>{moneyUi(summary.courseFee)}</b><span>Total course fee</span></div>
            <div className="stat-tile" style={{ minWidth: 0 }}><b style={{ color: 'var(--ok-600)', fontSize: 20, whiteSpace: 'nowrap' }}>{moneyUi(summary.totalPaid)}</b><span>Total paid to date</span></div>
            <div className="stat-tile" style={{ minWidth: 0 }}>
              <b style={{ color: summary.fullyPaid ? 'var(--ok-600)' : 'var(--danger-600)', fontSize: 20, whiteSpace: 'nowrap' }}>{moneyUi(summary.pendingAmount)}</b>
              <span>{summary.fullyPaid ? 'Fully paid' : 'Balance pending'}</span>
            </div>
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
    </>
  );
}

export default function Receipt() {
  const { receiptId } = useParams();
  const [receipt, setReceipt] = useState(null);
  const [summary, setSummary] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    let cancelled = false;
    setReceipt(null);
    setSummary(null);
    setError('');

    getReceiptById(receiptId)
      .then(async (r) => {
        if (cancelled) return;
        setReceipt(r);
        const studentId = r?.student?.studentId;
        if (!studentId) return;
        const s = await getPaymentSummary(studentId).catch(() => null);
        if (cancelled) return;
        setSummary(s);
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

  function downloadPdf() {
    const doc = new jsPDF({ unit: 'pt', format: 'a4' });
    const pageWidth = doc.internal.pageSize.getWidth();
    const margin = 36;
    let y = 36;

    doc.setFont('helvetica', 'bold');
    doc.setFontSize(16);
    doc.text('Computer Seekho', pageWidth / 2, y, { align: 'center' });
    y += 14;
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(9);
    doc.text("USM's Vidyanidhi Info Tech Academy", pageWidth / 2, y, { align: 'center' });
    y += 11;
    doc.text('5th Floor, Vidyanidhi Education Complex, JVPD Scheme, Juhu, Mumbai 400049', pageWidth / 2, y, { align: 'center' });
    y += 11;
    doc.text('022-2625 5629 / 2670 5498  |  training.vita@gmail.com', pageWidth / 2, y, { align: 'center' });
    y += 8;
    doc.setLineWidth(1);
    doc.line(margin, y, pageWidth - margin, y);
    y += 18;

    doc.setFont('helvetica', 'bold');
    doc.setFontSize(12);
    doc.text('FEE RECEIPT', pageWidth / 2, y, { align: 'center' });
    y += 18;

    doc.setFontSize(9);
    doc.setFont('helvetica', 'normal');
    doc.text(`Receipt No.: RCPT-${receipt.receiptId}`, margin, y);
    doc.text(`Date: ${formatDate(receipt.receiptDate)}`, pageWidth - margin, y, { align: 'right' });
    y += 16;

    const ROW_H = 19;

    function section(title, rows) {
      doc.setFont('helvetica', 'bold');
      doc.setFontSize(9.5);
      doc.setTextColor(13, 148, 136);
      doc.text(title.toUpperCase(), margin, y);
      doc.setTextColor(20, 20, 20);
      y += 4;
      doc.setLineWidth(0.5);
      doc.setDrawColor(220, 220, 220);
      doc.line(margin, y, pageWidth - margin, y);
      doc.setDrawColor(0, 0, 0);
      y += 12;
      doc.setFont('helvetica', 'normal');
      doc.setFontSize(9);
      const colWidth = (pageWidth - margin * 2) / 2;
      rows.forEach((pair, i) => {
        const row = Math.floor(i / 2);
        const col = i % 2;
        const x = margin + col * colWidth;
        const rowY = y + row * ROW_H;
        doc.setTextColor(120, 120, 120);
        doc.text(pair[0], x, rowY);
        doc.setTextColor(17, 24, 39);
        doc.setFont('helvetica', 'bold');
        doc.text(String(pair[1] ?? '—'), x, rowY + 11);
        doc.setFont('helvetica', 'normal');
      });
      const rowCount = Math.ceil(rows.length / 2);
      y += rowCount * ROW_H + 8;
    }

    section('Student Details', [
      ['Name', student.studentName],
      ['Student ID', student.studentId ? `STU-${student.studentId}` : '—'],
      ['Mobile', student.studentMobile],
      ['Email', student.studentEmail],
      ['Date of Birth', formatDate(student.studentDob)],
      ['Gender', student.studentGender],
      ['Address', student.studentAddress],
    ]);

    section('Course Details', [
      ['Course', course.courseName],
      ['Batch', batch.batchName],
    ]);

    section('This Payment', [
      ['Amount Paid', money(receipt.receiptAmount)],
      ['Payment Mode', paymentMode],
      ['Payment Status', payment.status || 'PAID'],
      ['Transaction ID', payment.razorpayPaymentId || '—'],
    ]);

    if (summary) {
      section('Balance Summary', [
        ['Total Course Fee', money(summary.courseFee)],
        ['Total Paid to Date', money(summary.totalPaid)],
        ['Balance Pending', summary.fullyPaid ? 'Fully paid' : money(summary.pendingAmount)],
      ]);
    }

    // Force everything onto one page: if the natural layout overflowed,
    // shrink-to-fit is not possible after the fact with jsPDF's text API,
    // so instead we simply never let the accumulated y exceed the page -
    // ROW_H and section spacing above were chosen so the standard set of
    // fields fits comfortably within a single A4 page's height.
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(8);
    doc.setTextColor(140, 140, 140);
    doc.text('This is a system-generated receipt.', margin, y + 22);
    doc.setTextColor(20, 20, 20);
    doc.line(pageWidth - margin - 130, y + 18, pageWidth - margin, y + 18);
    doc.setFontSize(8);
    doc.text('Authorized Signature', pageWidth - margin - 65, y + 29, { align: 'center' });

    doc.save(`Receipt-RCPT-${receipt.receiptId}.pdf`);
  }

  function printReceipt() {
    const infoRow = (label, value) => `
      <tr><td class="lbl">${label}</td><td class="val">${value ?? '—'}</td></tr>
    `;

    const summaryHtml = summary ? `
      <div class="section-title">Balance Summary</div>
      <table class="stats">
        <tr>
          <td>
            <div class="stat-label">Total Course Fee</div>
            <div class="stat-value">${moneyUi(summary.courseFee)}</div>
          </td>
          <td>
            <div class="stat-label">Total Paid to Date</div>
            <div class="stat-value ok">${moneyUi(summary.totalPaid)}</div>
          </td>
          <td>
            <div class="stat-label">${summary.fullyPaid ? 'Status' : 'Balance Pending'}</div>
            <div class="stat-value ${summary.fullyPaid ? 'ok' : 'danger'}">${summary.fullyPaid ? 'Fully Paid' : moneyUi(summary.pendingAmount)}</div>
          </td>
        </tr>
      </table>
    ` : '';

    const html = `<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Receipt RCPT-${receipt.receiptId}</title>
<style>
  * { box-sizing: border-box; -webkit-print-color-adjust: exact; print-color-adjust: exact; }
  body {
    font-family: 'Segoe UI', Arial, Helvetica, sans-serif;
    color: #1f2937;
    margin: 0;
    padding: 14px;
    background: #f3f4f6;
    font-size: 12px;
    line-height: 1.35;
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
    padding: 14px 28px;
    text-align: center;
  }
  .band h1 { margin: 0; font-size: 18px; font-weight: 700; letter-spacing: 0.3px; }
  .band p { margin: 2px 0 0; font-size: 11px; color: #e0f2f1; }
  .band .addr { max-width: 460px; margin: 2px auto 0; }

  .receipt-title-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 28px;
    border-bottom: 2px solid #0d9488;
    background: #f0fdfa;
  }
  .receipt-title-row .tag {
    font-size: 10px;
    font-weight: 700;
    letter-spacing: 1.5px;
    color: #0d9488;
    text-transform: uppercase;
  }
  .receipt-title-row .num { font-size: 15px; font-weight: 700; margin-top: 1px; }
  .receipt-title-row .date-block { text-align: right; }
  .receipt-title-row .date-block .lbl-sm { font-size: 10px; color: #6b7280; }
  .receipt-title-row .date-block .val-sm { font-size: 13px; font-weight: 600; }

  .content { padding: 4px 28px 16px; }

  .section-title {
    font-size: 10px;
    font-weight: 700;
    letter-spacing: 0.6px;
    text-transform: uppercase;
    color: #0d9488;
    margin: 12px 0 4px;
    padding-bottom: 3px;
    border-bottom: 1px solid #e5e7eb;
  }
  .info-table { width: 100%; border-collapse: collapse; }
  .info-table tr { border-bottom: 1px solid #f1f2f4; }
  .info-table tr:last-child { border-bottom: none; }
  .info-table td { padding: 3.5px 0; vertical-align: top; }
  .info-table td.lbl { width: 42%; color: #6b7280; }
  .info-table td.val { font-weight: 600; color: #111827; }

  .amount-box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #f0fdfa;
    border: 1px solid #99f6e4;
    border-radius: 6px;
    padding: 9px 16px;
    margin-top: 8px;
  }
  .amount-box .amt-label { font-size: 11px; color: #0f766e; font-weight: 600; }
  .amount-box .amt-value { font-size: 18px; font-weight: 800; color: #0d9488; }

  .stats { width: 100%; border-collapse: separate; border-spacing: 8px 0; margin-top: 2px; }
  .stats td { border: 1px solid #e5e7eb; border-radius: 6px; padding: 7px; text-align: center; width: 33.33%; }
  .stat-label { font-size: 10px; color: #6b7280; margin-bottom: 2px; }
  .stat-value { font-size: 14px; font-weight: 700; }
  .ok { color: #0d9488; }
  .danger { color: #dc2626; }

  .foot {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    margin-top: 16px;
    padding-top: 8px;
    border-top: 1px solid #e5e7eb;
  }
  .foot .note { font-size: 10px; color: #9ca3af; }
  .sig { text-align: center; }
  .sig .line { border-top: 1px solid #9ca3af; padding-top: 3px; margin-top: 22px; font-size: 10px; color: #6b7280; min-width: 150px; }

  @media print {
    body { background: #fff; padding: 0; }
    .paper { border: none; border-radius: 0; max-width: 100%; }
    .info-table tr, .amount-box, .stats { break-inside: avoid; }
  }
  @page { size: A4; margin: 10mm; }
</style>
</head>
<body>
  <div class="paper">
    <div class="band">
      <h1>Computer Seekho</h1>
      <p>USM's Vidyanidhi Info Tech Academy</p>
      <p class="addr">5th Floor, Vidyanidhi Education Complex, JVPD Scheme, Juhu, Mumbai 400049</p>
      <p>022-2625 5629 / 2670 5498 &middot; training.vita@gmail.com</p>
    </div>

    <div class="receipt-title-row">
      <div>
        <div class="tag">Fee Receipt</div>
        <div class="num">RCPT-${receipt.receiptId}</div>
      </div>
      <div class="date-block">
        <div class="lbl-sm">Date</div>
        <div class="val-sm">${formatDate(receipt.receiptDate)}</div>
      </div>
    </div>

    <div class="content">
      <div class="section-title">Student Details</div>
      <table class="info-table">
        ${infoRow('Name', student.studentName)}
        ${infoRow('Student ID', student.studentId ? `STU-${student.studentId}` : '—')}
        ${infoRow('Mobile', student.studentMobile)}
        ${infoRow('Email', student.studentEmail)}
        ${infoRow('Date of Birth', formatDate(student.studentDob))}
        ${infoRow('Gender', student.studentGender)}
        ${infoRow('Address', student.studentAddress)}
      </table>

      <div class="section-title">Course Details</div>
      <table class="info-table">
        ${infoRow('Course', course.courseName)}
        ${infoRow('Batch', batch.batchName)}
      </table>

      <div class="section-title">This Payment</div>
      <table class="info-table">
        ${infoRow('Payment Mode', paymentMode)}
        ${infoRow('Payment Status', payment.status || 'PAID')}
        ${payment.razorpayPaymentId ? infoRow('Transaction ID', payment.razorpayPaymentId) : ''}
      </table>
      <div class="amount-box">
        <span class="amt-label">AMOUNT PAID</span>
        <span class="amt-value">${moneyUi(receipt.receiptAmount)}</span>
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

  return (
    <div>
      <div className="admin-title-row no-print">
        <div>
          <h1>Fee Receipt</h1>
          <p className="muted">Receipt RCPT-{receipt.receiptId} — printable record for this payment.</p>
        </div>
        <div style={{ display: 'flex', gap: 10 }}>
          <Link to="/admin/admissions" className="btn btn-outline btn-sm">Back to Admissions</Link>
          <button className="btn btn-outline btn-sm" type="button" onClick={downloadPdf}>Download PDF</button>
          <button className="btn btn-primary btn-sm" type="button" onClick={printReceipt}>Print Receipt</button>
        </div>
      </div>

      <div className="card card-pad receipt-page-card" style={{ maxWidth: 760, margin: '0 auto', border: '1px solid var(--border, #e5e7eb)' }}>
        <ReceiptBody receipt={receipt} summary={summary} student={student} payment={payment} course={course} batch={batch} paymentMode={paymentMode} />
      </div>
    </div>
  );
}