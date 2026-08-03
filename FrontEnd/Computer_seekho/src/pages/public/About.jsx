import { Link } from 'react-router-dom';
import { ArchFrame } from '../../components/ui/ui';

const VALUES = [
  { title: 'Practical learning', desc: 'Classroom sessions paired with hands-on lab time, assignments, presentations and real project work.', icon: 'learning' },
  { title: 'Experienced staff', desc: 'Teaching and non-teaching staff who know every student by name, from enquiry through to placement.', icon: 'staff' },
  { title: 'Career support', desc: 'Batchwise placement records and recruiter visibility, so outcomes are never just a promise.', icon: 'career' },
];

const JOURNEY = ['Enquiry', 'Follow-up', 'Admission', 'Placement'];

function ValueIcon({ kind }) {
  const paths = {
    learning: <><path d="M3 5.5 12 2l9 3.5-9 3.5-9-3.5Z" /><path d="M6 8.5V15c0 1.4 2.7 2.5 6 2.5s6-1.1 6-2.5V8.5" /><path d="M21 5.5V13" /></>,
    staff: <><circle cx="8.5" cy="8" r="3" /><path d="M2.5 20c.6-3.6 3-5.7 6-5.7s5.4 2.1 6 5.7" /><path d="M15.5 8a3 3 0 1 1 3.9 2.86" /><path d="M15 14.4c2.6.4 4.4 2.3 4.9 5.6" /></>,
    career: <><circle cx="12" cy="12" r="9" /><circle cx="12" cy="12" r="5" /><circle cx="12" cy="12" r="1.4" fill="var(--teal-700, #0d9488)" stroke="none" /></>,
  };
  return (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="var(--teal-700, #0d9488)" strokeWidth="1.7" strokeLinecap="round" strokeLinejoin="round" style={{ display: 'block' }}>
      {paths[kind]}
    </svg>
  );
}

export default function About() {
  return (
    <div className="container section">
      <div className="about-layout">
        <div className="card card-pad about-copy">
          <p className="pill">About Computer Seekho</p>
          <h1 className="h2">Practical learning with a strong placement focus.</h1>
          <p className="body-text">
            Computer Seekho is the public face of USM's Vidyanidhi Info Tech Academy (VITA) — an ISO 9001 certified
            institute that has run computer training out of the Vidyanidhi Education Complex in JVPD Scheme, Juhu,
            since 1989. What began as one Upnagar Shikshan Mandal initiative has grown into a full C-DAC affiliated
            campus, a short walk from Juhu Beach.
          </p>
          <p className="body-text">
            Students learn through classroom sessions, lab practice, assignments, presentations and projects —
            supported by staff who track every enquiry and follow-up personally, not just at admission time.
          </p>
          <div className="about-actions">
            <Link to="/programs" className="btn btn-primary">Explore programs</Link>
            <Link to="/contact" className="btn btn-outline">Contact us</Link>
          </div>
        </div>
        <ArchFrame className="about-img-frame" src="" alt="Vidyanidhi Education Complex">
          <div className="about-img-fallback">
            <span className="eyebrow on-dark">JVPD Scheme, Juhu</span>
            <p>Vidyanidhi Education Complex</p>
          </div>
        </ArchFrame>
      </div>

      <div className="values-grid">
        {VALUES.map((v) => (
          <div className="card card-pad value-card" key={v.title}>
            <div className="value-icon" aria-hidden="true" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <ValueIcon kind={v.icon} />
            </div>
            <h4>{v.title}</h4>
            <p className="muted" style={{ marginTop: 8 }}>{v.desc}</p>
          </div>
        ))}
      </div>

      <div className="card card-pad journey-card">
        <p className="eyebrow">Student journey</p>
        <div className="journey-timeline">
          {JOURNEY.map((step, i) => (
            <div className="journey-timeline-step" key={step}>
              <span className="journey-timeline-dot">{i + 1}</span>
              <b>{step}</b>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}