import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getActiveCourses } from '../../api/courses';
import { getAllRecruiters } from '../../api/content';
import { getAllPlacements } from '../../api/misc';
import { getAllStudents } from '../../api/students';
import { getAllAnnouncements } from '../../api/content';
import { ArchMotif, Stat } from '../../components/ui/ui';

export default function Home() {
  const navigate = useNavigate();
  const [query, setQuery] = useState('');
  const [courses, setCourses] = useState([]);
  const [stats, setStats] = useState({ programs: null, recruiters: null, placementRate: null, trained: null });
  const [announcements, setAnnouncements] = useState([]);

  useEffect(() => {
    getActiveCourses().then(setCourses).catch(() => setCourses([]));
    getAllAnnouncements().then((a) => setAnnouncements(a || [])).catch(() => {});

    Promise.allSettled([getActiveCourses(), getAllRecruiters(), getAllPlacements(), getAllStudents()]).then(
      ([coursesR, recruitersR, placementsR, studentsR]) => {
        const programs = coursesR.status === 'fulfilled' ? coursesR.value.length : null;
        const recruiters = recruitersR.status === 'fulfilled' ? recruitersR.value.length : null;
        const placed = placementsR.status === 'fulfilled' ? placementsR.value.length : null;
        const trained = studentsR.status === 'fulfilled' ? studentsR.value.length : null;
        const placementRate = placed !== null && trained ? Math.min(100, Math.round((placed / trained) * 100)) : null;
        setStats({ programs, recruiters, placementRate, trained });
      }
    );
  }, []);

  function onSearch(e) {
    e.preventDefault();
    navigate(`/programs${query ? `?q=${encodeURIComponent(query)}` : ''}`);
  }

  return (
    <>
      <section className="hero">
        <div className="container hero-grid">
          <div className="hero-copy">
            <p className="pill">Career-focused computer education</p>
            <h1>Build practical skills for your next career step.</h1>
            <p className="lede">
              Explore active courses, upcoming batches, campus facilities and placement outcomes — published
              straight from the institute's own records.
            </p>
            <form className="hero-search" onSubmit={onSearch}>
              <svg width="18" height="18" viewBox="0 0 18 18" fill="none" stroke="currentColor" strokeWidth="1.6">
                <circle cx="8" cy="8" r="6" />
                <path d="M12.5 12.5 16 16" strokeLinecap="round" />
              </svg>
              <input
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Search programs — e.g. PG-DAC, Java, Web Development"
                aria-label="Search programs"
              />
              <button type="submit" className="btn btn-primary btn-sm">Search</button>
            </form>
          </div>
          <div className="hero-art">
            <div className="hero-art-frame">
              <ArchMotif />
              <div className="hero-art-caption">
                <span className="eyebrow on-dark">Est. 1989</span>
                <p>USM's Vidyanidhi Info Tech Academy — JVPD Scheme, Juhu</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className="container">
        <div className="stat-row stat-row-overlap">
          <Stat value={stats.programs ?? '—'} label="Active programs" />
          <Stat value={stats.recruiters ?? '—'} label="Recruiters" tone="brass" />
          <Stat value={stats.placementRate !== null ? `${stats.placementRate}%` : '—'} label="Placement rate" />
          <Stat value={stats.trained ?? '—'} label="Students trained" tone="brass" />
        </div>
      </section>

      <section className="section container home-lower">
        <div>
          <div className="section-head">
            <p className="eyebrow">Programs</p>
            <h2>Featured programs</h2>
          </div>
          <div className="program-card-grid">
            {courses.slice(0, 3).map((c) => (
              <Link to={`/programs/${c.courseId}`} className="card card-hover program-card" key={c.courseId}>
                <div className="program-card-img" style={c.coverPhoto ? { backgroundImage: `url(${c.coverPhoto})` } : {}}>
                  {!c.coverPhoto && <span>{c.courseName?.[0] || 'C'}</span>}
                </div>
                <div className="card-pad-sm">
                  <h4>{c.courseName}</h4>
                  <p className="muted" style={{ fontSize: 'var(--text-sm)', marginTop: 4 }}>
                    {c.courseDescription?.slice(0, 90) || `${c.courseDuration ? c.courseDuration + '-month' : ''} program`}
                  </p>
                </div>
              </Link>
            ))}
            {courses.length === 0 && (
              <p className="muted">Programs will appear here as soon as they're published from the admin panel.</p>
            )}
          </div>
        </div>
        <div>
          <div className="section-head">
            <p className="eyebrow">Notice board</p>
            <h2>Latest announcements</h2>
          </div>
          <div className="card announcement-list">
            {announcements.length === 0 && <p className="muted card-pad-sm">No announcements published right now.</p>}
            {announcements.map((a) => (
              <div className="announcement-item" key={a.announcementId}>
                <b>{a.title}</b>
                <span>{a.description}</span>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="section-tight journey-band">
        <div className="container journey-inner">
          <div>
            <p className="eyebrow on-dark">How admissions work</p>
            <h2 className="on-dark">From first enquiry to placement day.</h2>
          </div>
          <div className="journey-steps">
            {['Enquiry', 'Follow-up', 'Admission', 'Placement'].map((step, i) => (
              <div className="journey-step" key={step}>
                <span className="journey-dot" />
                <b>{step}</b>
                <span className="muted">Step {i + 1}</span>
              </div>
            ))}
          </div>
          <Link to="/enquiry" className="btn btn-brass">Start your enquiry</Link>
        </div>
      </section>
    </>
  );
}