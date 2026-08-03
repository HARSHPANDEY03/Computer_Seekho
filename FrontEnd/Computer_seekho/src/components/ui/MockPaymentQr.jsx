import { QRCodeSVG } from 'qrcode.react';

export default function MockPaymentQr({ amount, courseName, onSuccess, onClose, busy }) {
  return (
    <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.5)', zIndex: 999, display: 'flex', alignItems: 'center', justifyContent: 'center' }} onClick={onClose}>
      <div className="card card-pad" style={{ maxWidth: 340, textAlign: 'center' }} onClick={(e) => e.stopPropagation()}>
        <h3 style={{ margin: 0 }}>Scan & Pay</h3>
        <p className="muted" style={{ margin: '4px 0 16px' }}>{courseName}</p>
        <div style={{ display: 'inline-block', padding: 12, background: '#fff', borderRadius: 8 }}>
          <QRCodeSVG value={`upi://pay?pa=computerseekho@ybl&pn=ComputerSeekho&am=${amount}&cu=INR`} size={200} />
        </div>
        <p style={{ marginTop: 12, fontWeight: 600 }}>₹{Number(amount).toLocaleString('en-IN')}</p>
        <p className="muted" style={{ fontSize: 12 }}>Scan with any UPI app to pay</p>
        <button className="btn btn-primary btn-block" style={{ marginTop: 16 }} onClick={onSuccess} disabled={busy}>
          {busy ? 'Confirming…' : 'Simulate Successful Payment'}
        </button>
        <button className="btn btn-ghost btn-block" style={{ marginTop: 8 }} onClick={onClose}>Cancel</button>
      </div>
    </div>
  );
}