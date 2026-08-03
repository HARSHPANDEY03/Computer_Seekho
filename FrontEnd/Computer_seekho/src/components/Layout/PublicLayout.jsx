import { useEffect, useState, useRef } from 'react';
import { Link, NavLink, Outlet, useLocation } from 'react-router-dom';
import { getAllAnnouncements } from '../../api/content';
import { getActiveCourses } from '../../api/courses';

const NAV_LINKS = [
  { to: '/', label: 'Home', end: true },
  { to: '/about', label: 'About' },
  { to: '/programs', label: 'Programs' },
  { to: '/campus', label: 'Campus' },
  { to: '/placements', label: 'Placements' },
  { to: '/contact', label: 'Contact' },
];

function isAnnouncementLive(a) {
  const today = new Date().toISOString().slice(0, 10);
  const publish = a.publishDate ? a.publishDate.slice(0, 10) : null;
  const expiry = a.expiryDate ? a.expiryDate.slice(0, 10) : null;
  if (publish && publish > today) return false;
  if (expiry && expiry < today) return false;
  return true;
}

function Ticker() {
  const [items, setItems] = useState(['Admissions open for upcoming batches — limited seats available.']);

  useEffect(() => {
    getAllAnnouncements()
      .then((all) => {
        const live = (all || []).filter(isAnnouncementLive).map((a) => a.title || a.description).filter(Boolean);
        if (live.length) setItems(live);
      })
      .catch(() => {
        /* keep default copy — ticker should never look broken */
      });
  }, []);

  const track = [...items, ...items];
  return (
    <div className="ticker" role="marquee" aria-label="Announcements">
      <div className="ticker-track">
        {track.map((t, i) => (
          <span className="ticker-item" key={i}>
            {t}
          </span>
        ))}
      </div>
    </div>
  );
}

function ProgramsMegaMenu({ open, onClose }) {
  const [groups, setGroups] = useState(null);
  const ref = useRef(null);

  useEffect(() => {
    if (open && !groups) {
      getActiveCourses()
        .then((courses) => {
          const byType = {};
          (courses || []).forEach((c) => {
            const key = c.ageGrpType || 'Programs';
            byType[key] = byType[key] || [];
            byType[key].push(c);
          });
          setGroups(byType);
        })
        .catch(() => setGroups({}));
    }
  }, [open, groups]);

  useEffect(() => {
    function onDocClick(e) {
      if (ref.current && !ref.current.contains(e.target)) onClose();
    }
    if (open) document.addEventListener('mousedown', onDocClick);
    return () => document.removeEventListener('mousedown', onDocClick);
  }, [open, onClose]);

  if (!open) return null;
  const entries = groups ? Object.entries(groups) : [];

  return (
    <div className="mega-menu" ref={ref}>
      <div className="mega-menu-grid">
        {entries.length === 0 && (
          <div className="mega-menu-empty muted">
            {groups === null ? 'Loading programs…' : 'Programs will appear here once published from the admin panel.'}
          </div>
        )}
        {entries.map(([group, courses]) => (
          <div className="mega-col" key={group}>
            <h4>{group}</h4>
            {courses.slice(0, 5).map((c) => (
              <Link to={`/programs/${c.courseId}`} className="mega-item" key={c.courseId} onClick={onClose}>
                {c.courseName} <span>›</span>
              </Link>
            ))}
          </div>
        ))}
        <div className="mega-col mega-col-feature">
          <div className="mega-feature-label">Not sure where to start?</div>
          <p>Tell us your goals and we'll recommend a program.</p>
          <Link to="/enquiry" className="btn btn-primary btn-sm" onClick={onClose}>
            Request guidance
          </Link>
        </div>
      </div>
      <Link to="/programs" className="mega-view-all" onClick={onClose}>
        View all programs →
      </Link>
    </div>
  );
}

export default function PublicLayout() {
  const [menuOpen, setMenuOpen] = useState(false);
  const [mobileNavOpen, setMobileNavOpen] = useState(false);
  const location = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0);
    setMobileNavOpen(false);
  }, [location.pathname]);

  return (
    <div className="public-shell">
      <Ticker />
      <header className="public-header">
        <div className="container public-nav">
          <Link to="/" className="brand">
            <span className="brand-mark">CS</span>
            <span className="brand-word">
              Computer <br /> Seekho
            </span>
          </Link>

          <nav className="nav-links" aria-label="Primary">
            {NAV_LINKS.map((l) =>
              l.label === 'Programs' ? (
                <div className="nav-item-menu" key={l.to}>
                  <button
                    className={`nav-link-btn ${location.pathname.startsWith('/programs') ? 'active' : ''}`}
                    onClick={() => setMenuOpen((v) => !v)}
                    aria-expanded={menuOpen}
                  >
                    Programs
                  </button>
                  <ProgramsMegaMenu open={menuOpen} onClose={() => setMenuOpen(false)} />
                </div>
              ) : (
                <NavLink to={l.to} end={l.end} className={({ isActive }) => (isActive ? 'active' : '')} key={l.to}>
                  {l.label}
                </NavLink>
              )
            )}
          </nav>

          <Link to="/enquiry" className="btn btn-primary btn-sm nav-cta">
            Enquire Now
          </Link>

          <button className="mobile-nav-toggle" onClick={() => setMobileNavOpen((v) => !v)} aria-label="Toggle menu">
            <span />
            <span />
            <span />
          </button>
        </div>

        {mobileNavOpen && (
          <div className="mobile-nav">
            {NAV_LINKS.map((l) => (
              <NavLink to={l.to} end={l.end} key={l.to} className={({ isActive }) => (isActive ? 'active' : '')}>
                {l.label}
              </NavLink>
            ))}
            <Link to="/enquiry" className="btn btn-primary btn-block">
              Enquire Now
            </Link>
          </div>
        )}
      </header>

      <main>
        <Outlet />
      </main>

      <Footer />
    </div>
  );
}

function Footer() {
  return (
    <footer className="public-footer">
      <div className="container public-footer-grid">
        <div>
          <div className="brand brand-on-dark">
            <span className="brand-mark">CS</span>
            <span className="brand-word">
              Computer <br /> Seekho
            </span>
          </div>
          <p className="footer-desc">
            The public face of Vidyanidhi Info Tech Academy (VITA) — career-focused, placement-driven computer
            education in Juhu, Mumbai since 1989.
          </p>
        </div>
        <div>
          <h4 className="footer-heading">Explore</h4>
          <ul className="footer-links">
            <li><Link to="/about">About Computer Seekho</Link></li>
            <li><Link to="/programs">Programs</Link></li>
            <li><Link to="/campus">Campus life &amp; faculty</Link></li>
            <li><Link to="/placements">Batchwise placements</Link></li>
            <li><Link to="/recruiters">Our recruiters</Link></li>
          </ul>
        </div>
        <div>
          <h4 className="footer-heading">Institute</h4>
          <ul className="footer-links">
            <li>5th Floor, Vidyanidhi Education Complex</li>
            <li>Vidyanidhi Marg, JVPD Scheme, Juhu</li>
            <li>Mumbai – 400 049</li>
            <li>training.vita@gmail.com</li>
          </ul>
        </div>
        <div>
          <h4 className="footer-heading">Get in touch</h4>
          <p className="footer-desc">Have a question about a program or batch timing?</p>
          <Link to="/contact" className="btn btn-outline btn-on-dark btn-sm">
            Contact us
          </Link>
        </div>
      </div>
      <div className="container public-footer-bottom">
        <span>&copy; {new Date().getFullYear()} Shriram Mantri Vidyanidhi InfoTech Academy. All rights reserved.</span>
        <Link to="/admin/login">Staff Login</Link>
      </div>
    </footer>
  );
}
