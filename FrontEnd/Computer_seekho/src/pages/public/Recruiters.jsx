import { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { getAllRecruiters } from '../../api/content';
import { EmptyState, Loading } from '../../components/ui/ui';

export default function Recruiters() {
  const [recruiters, setRecruiters] = useState(null);
  const [search, setSearch] = useState('');

  useEffect(() => {
    getAllRecruiters().then(setRecruiters).catch(() => setRecruiters([]));
  }, []);

  const filtered = useMemo(
    () => (recruiters || []).filter((r) => r.recruiterName?.toLowerCase().includes(search.toLowerCase())),
    [recruiters, search]
  );

  return (
    <div className="container section">
      <div className="page-head">
        <div>
          <p className="eyebrow">Outcomes</p>
          <h1 className="h2">Our recruiters</h1>
          <p className="body-text">Only active recruiter records are published here.</p>
        </div>
        <div className="filter-bar" style={{ marginBottom: 0 }}>
          <input className="input" placeholder="Search recruiters" value={search} onChange={(e) => setSearch(e.target.value)} />
          <Link to="/placements" className="btn btn-outline btn-sm">Back to batchwise placements</Link>
        </div>
      </div>

      {recruiters === null && <Loading label="Loading recruiters…" />}
      {recruiters && recruiters.length === 0 && <EmptyState title="No recruiters published yet" />}

      {recruiters && recruiters.length > 0 && (
        <div className="logo-grid">
          {filtered.map((r) => (
            <div className="logo-card" key={r.recruiterId} title={r.description || r.recruiterName}>
              {r.photoUrl ? <img src={r.photoUrl} alt={r.recruiterName} /> : <span>{r.recruiterName}</span>}
            </div>
          ))}
        </div>
      )}

      <div className="card card-pad cta-banner">
        <div>
          <b>Want to prepare for these opportunities?</b>
          <p className="muted" style={{ marginTop: 4 }}>Explore active programs and request a callback.</p>
        </div>
        <Link to="/enquiry" className="btn btn-primary">Enquire Now</Link>
      </div>
    </div>
  );
}
