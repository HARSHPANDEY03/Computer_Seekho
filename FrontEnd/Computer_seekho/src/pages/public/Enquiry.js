import { useEffect, useState } from 'react';
import { getActiveCourses } from '../../api/courses';
import { createEnquiry } from '../../api/enquiries';

const INFO_ITEMS = [
  { title: 'Course guidance', desc: 'Choose the most suitable active program.' },
  { title: 'Batch information', desc: 'Get timing, duration and current fee details.' },
  { title: 'Follow-up support', desc: 'Your enquiry appears in the staff follow-up dashboard.' },
];

const emptyForm = { name: '', mobile: '', altMobile: '', email: '', address: '', course: '', time: '', query: '' };

export default function Enquiry() {
  const [courses, setCourses] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [errors, setErrors] = useState({});
  const [submitting, setSubmitting] = useState(false);
  const [done, setDone] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    getActiveCourses().then(setCourses).catch(() => setCourses([]));
  }, []);

  function update(key, value) {
    setForm((f) => ({ ...f, [key]: value }));
  }

  function validate() {
    const e = {};
    if (!form.name.trim()) e.name = 'Enter your full name.';
    if (!/^\d{10}$/.test(form.mobile.trim())) e.mobile = 'Enter a valid 10-digit mobile number.';
    if (!form.course) e.course = 'Select the program you are interested in.';
    if (form.email && !/^\S+@\S+\.\S+$/.test(form.email)) e.email = 'Enter a valid email address.';
    setErrors(e);
    return Object.keys(e).length === 0;
  }

  async function onSubmit(e) {
    e.preventDefault();
    if (!validate()) return;
    setSubmitting(true);
    setError('');
    try {
      const today = new Date();
      const followup = new Date(today);
      followup.setDate(followup.getDate() + 3);

      // The backend's Enquiry entity has no course/preferred-time columns,
      // so that context is folded into the query text rather than lost.
      const notesHeader = [
        form.course && `Program interested in: ${form.course}`,
        form.time && `Preferred time to call: ${form.time}`,
      ].filter(Boolean).join('\n');
      const fullQuery = [notesHeader, form.query].filter(Boolean).join('\n\n');

      await createEnquiry({
        enquirerName: form.name,
        enquirerAddress: form.address || null,
        enquirerMobile: Number(form.mobile),
        enquirerAlternateMobile: form.altMobile ? Number(form.altMobile) : null,
        enquirerEmailId: form.email || null,
        enquiryDate: today.toISOString().slice(0, 10),
        enquirerQuery: fullQuery || null,
        enquiryProcessedFlag: false,
        inquiryCounter: 0,
        followupDate: followup.toISOString().slice(0, 10),
        enquirySource: 'Online',
      });
      setDone(true);
      setForm(emptyForm);
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="container section">
      <div className="form-layout">
        <div className="info-panel">
          <p className="pill">Admission enquiry</p>
          <h1 className="h2">Request a call back.</h1>
          <p className="body-text">
            Submit your details to create an online enquiry. The admissions team will follow up using the program
            and time you select below.
          </p>
          <div className="info-list">
            {INFO_ITEMS.map((it) => (
              <div className="info-item" key={it.title}>
                <div className="info-icon" aria-hidden="true" />
                <div><b>{it.title}</b><span>{it.desc}</span></div>
              </div>
            ))}
          </div>
        </div>

        <div className="card card-pad form-card">
          {done ? (
            <div className="alert alert-ok">
              <b>Thank you — your enquiry has been submitted.</b>
              <p style={{ marginTop: 6 }}>Our admissions team will call you at your preferred time. You can also reach us directly from the Contact page.</p>
              <button className="btn btn-outline btn-sm" style={{ marginTop: 14 }} onClick={() => setDone(false)}>Submit another enquiry</button>
            </div>
          ) : (
            <form onSubmit={onSubmit} noValidate>
              <h2 className="h3">Enquiry details</h2>
              {error && <p className="alert alert-danger" style={{ marginTop: 12 }}>{error}</p>}
              <div className="form-grid" style={{ marginTop: 16 }}>
                <div className="field">
                  <label>Enquirer name *</label>
                  <input className={`input ${errors.name ? 'has-error' : ''}`} value={form.name} onChange={(e) => update('name', e.target.value)} placeholder="Full name" />
                  {errors.name && <span className="error">{errors.name}</span>}
                </div>
                <div className="field">
                  <label>Mobile number *</label>
                  <input className={`input ${errors.mobile ? 'has-error' : ''}`} value={form.mobile} onChange={(e) => update('mobile', e.target.value.replace(/\D/g, ''))} placeholder="10-digit mobile number" maxLength={10} />
                  {errors.mobile && <span className="error">{errors.mobile}</span>}
                </div>
                <div className="field">
                  <label>Alternate mobile</label>
                  <input className="input" value={form.altMobile} onChange={(e) => update('altMobile', e.target.value.replace(/\D/g, ''))} placeholder="Optional" maxLength={10} />
                </div>
                <div className="field">
                  <label>Email address</label>
                  <input className={`input ${errors.email ? 'has-error' : ''}`} value={form.email} onChange={(e) => update('email', e.target.value)} placeholder="name@example.com" />
                  {errors.email && <span className="error">{errors.email}</span>}
                </div>
                <div className="field full">
                  <label>Address</label>
                  <input className="input" value={form.address} onChange={(e) => update('address', e.target.value)} placeholder="Residential address" />
                </div>
                <div className="field">
                  <label>Program interested in *</label>
                  <select className={`select ${errors.course ? 'has-error' : ''}`} value={form.course} onChange={(e) => update('course', e.target.value)}>
                    <option value="">Select active course</option>
                    {courses.map((c) => <option key={c.courseId} value={c.courseName}>{c.courseName}</option>)}
                  </select>
                  {errors.course && <span className="error">{errors.course}</span>}
                </div>
                <div className="field">
                  <label>Preferred time to call</label>
                  <select className="select" value={form.time} onChange={(e) => update('time', e.target.value)}>
                    <option value="">Any time</option>
                    <option>Morning</option>
                    <option>Afternoon</option>
                    <option>Evening</option>
                  </select>
                </div>
                <div className="field full">
                  <label>Query</label>
                  <textarea className="textarea" rows={4} value={form.query} onChange={(e) => update('query', e.target.value)} placeholder="Tell us what you would like to know" />
                </div>
              </div>
              <p className="check">By submitting, you agree to be contacted regarding this enquiry.</p>
              <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                <button className="btn btn-primary" type="submit" disabled={submitting}>
                  {submitting ? <span className="spinner" /> : 'Submit enquiry'}
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}
