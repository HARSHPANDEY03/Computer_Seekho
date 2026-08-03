import { useState } from 'react';
import { submitContact } from '../../api/content';

const MAP_QUERY = encodeURIComponent('Vidyanidhi Education Complex, JVPD Scheme, Juhu, Mumbai 400049');

function ContactIcon({ kind }) {
  const paths = {
    pin: <><path d="M12 21s7-6.5 7-12a7 7 0 0 0-14 0c0 5.5 7 12 7 12Z" /><circle cx="12" cy="9" r="2.5" /></>,
    phone: <path d="M6.6 10.8c1.4 2.7 3.9 5.2 6.6 6.6l2.2-2.2c.3-.3.7-.4 1-.2 1.1.4 2.3.6 3.6.6.6 0 1 .4 1 1V20c0 .6-.4 1-1 1C11.4 21 3 12.6 3 3c0-.6.4-1 1-1h3.4c.6 0 1 .4 1 1 0 1.3.2 2.5.6 3.6.1.4 0 .8-.2 1L6.6 10.8Z" />,
    mail: <><rect x="3" y="5" width="18" height="14" rx="2" /><path d="m3.5 6.5 8.5 6 8.5-6" /></>,
  };
  return (
    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="var(--teal-700, #0d9488)" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" style={{ display: 'block' }}>
      {paths[kind]}
    </svg>
  );
}

export default function Contact() {
  const [form, setForm] = useState({ name: '', email: '', message: '' });
  const [errors, setErrors] = useState({});
  const [submitting, setSubmitting] = useState(false);
  const [done, setDone] = useState(false);
  const [error, setError] = useState('');

  function validate() {
    const e = {};
    if (!form.name.trim()) e.name = 'Enter your name.';
    if (!/^\S+@\S+\.\S+$/.test(form.email)) e.email = 'Enter a valid email address.';
    if (!form.message.trim()) e.message = 'Enter a message.';
    if (form.message.length > 500) e.message = 'Message must be 500 characters or fewer.';
    setErrors(e);
    return Object.keys(e).length === 0;
  }

  async function onSubmit(e) {
    e.preventDefault();
    if (!validate()) return;
    setSubmitting(true);
    setError('');
    try {
      await submitContact(form);
      setDone(true);
      setForm({ name: '', email: '', message: '' });
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="container section">
      <div className="page-head">
        <div>
          <p className="eyebrow">Reach us</p>
          <h1 className="h2">Get in touch</h1>
          <p className="body-text">Institute information, location and a direct message form.</p>
        </div>
      </div>

      <div className="contact-top">
        <div className="card card-pad contact-method">
          <div className="circle" aria-hidden="true" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}><ContactIcon kind="pin" /></div>
          <b>Visit us</b>
          <span>5th Floor, Vidyanidhi Education Complex, JVPD Scheme, Juhu, Mumbai 400049</span>
        </div>
        <div className="card card-pad contact-method">
          <div className="circle" aria-hidden="true" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}><ContactIcon kind="phone" /></div>
          <b>Call us</b>
          <span>022-2625 5629 / 2670 5498</span>
        </div>
        <div className="card card-pad contact-method">
          <div className="circle" aria-hidden="true" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}><ContactIcon kind="mail" /></div>
          <b>Email us</b>
          <span>training.vita@gmail.com</span>
        </div>
      </div>

      <div className="contact-grid">
        <div>
          <div className="card card-pad" style={{ marginBottom: 16 }}>
            <h3>Our Origin</h3>
            <p className="body-text" style={{ marginTop: 10 }}>
              USM's Vidyanidhi Info Tech Academy has trained students from the same Juhu campus since 1989 — from
              first-time computer users to graduates preparing for C-DAC diplomas, a short walk from Juhu Beach.
            </p>
          </div>
          <div className="map-frame">
            <iframe
              title="VITA campus location"
              src={`https://www.google.com/maps?q=${MAP_QUERY}&output=embed`}
              loading="lazy"
              referrerPolicy="no-referrer-when-downgrade"
            />
          </div>
        </div>

        <div className="card card-pad form-card">
          {done ? (
            <div className="alert alert-ok">
              <b>Message sent — thank you.</b>
              <p style={{ marginTop: 6 }}>We'll get back to you at the email address you provided.</p>
              <button className="btn btn-outline btn-sm" style={{ marginTop: 14 }} onClick={() => setDone(false)}>Send another message</button>
            </div>
          ) : (
            <form onSubmit={onSubmit} noValidate>
              <h3>Send us a message</h3>
              {error && <p className="alert alert-danger" style={{ marginTop: 12 }}>{error}</p>}
              <div className="field" style={{ marginTop: 16, marginBottom: 14 }}>
                <label>Name *</label>
                <input className={`input ${errors.name ? 'has-error' : ''}`} value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} placeholder="Your name" />
                {errors.name && <span className="error">{errors.name}</span>}
              </div>
              <div className="field" style={{ marginBottom: 14 }}>
                <label>Email *</label>
                <input className={`input ${errors.email ? 'has-error' : ''}`} value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} placeholder="Your email address" />
                {errors.email && <span className="error">{errors.email}</span>}
              </div>
              <div className="field">
                <label style={{ display: 'flex', justifyContent: 'space-between' }}>
                  <span>Message *</span>
                  <span className="muted" style={{ fontWeight: 400 }}>{form.message.length} / 500</span>
                </label>
                <textarea
                  className={`textarea ${errors.message ? 'has-error' : ''}`}
                  rows={6}
                  maxLength={500}
                  value={form.message}
                  onChange={(e) => setForm({ ...form, message: e.target.value })}
                  placeholder="Type your message"
                />
                {errors.message && <span className="error">{errors.message}</span>}
              </div>
              <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: 16 }}>
                <button className="btn btn-primary" type="submit" disabled={submitting}>
                  {submitting ? <span className="spinner" /> : 'Send message'}
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}