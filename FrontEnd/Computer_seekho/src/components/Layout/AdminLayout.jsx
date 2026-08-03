import { useEffect, useState } from 'react';
import { Navigate, NavLink, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Avatar } from '../ui/ui';

const NAV = [
  { to: '/admin/dashboard', label: 'Dashboard' },
  { to: '/admin/enquiries', label: 'Enquiries' },
  { to: '/admin/admissions', label: 'Admissions' },
  { to: '/admin/content', label: 'Content Manager' },
  { to: '/admin/excel-upload', label: 'Excel Upload' },
];

const CRUMB_LABEL = {
  '/admin/dashboard': 'Follow-up Dashboard',
  '/admin/enquiries': 'Enquiries and Follow-ups',
  '/admin/admissions': 'Student Admission',
  '/admin/content': 'Content Manager',
  '/admin/excel-upload': 'Excel Data Upload',
};

export function RequireAuth({ children }) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();
  if (!isAuthenticated) {
    return <Navigate to="/admin/login" replace state={{ from: location.pathname }} />;
  }
  return children;
}

export default function AdminLayout() {
  const { staff, signOut } = useAuth();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const location = useLocation();

  useEffect(() => setSidebarOpen(false), [location.pathname]);

  const crumb = CRUMB_LABEL[location.pathname] || 'Admin';

  return (
    <div className="admin-shell">
      <aside className={`admin-sidebar ${sidebarOpen ? 'open' : ''}`}>
        <div className="admin-brand">
          COMPUTER SEEKHO
          <span>Administration Panel</span>
        </div>
        <nav className="admin-side-nav">
          {NAV.map((n) => (
            <NavLink to={n.to} key={n.to} className={({ isActive }) => `side-link ${isActive ? 'active' : ''}`}>
              <NavIcon label={n.label} />
              {n.label}
            </NavLink>
          ))}
        </nav>
        <div className="admin-sidebar-footer">
          <div>Logged in as {staff?.staffName || 'Staff User'}</div>
          <button className="link-btn" onClick={signOut}>
            Logout
          </button>
        </div>
      </aside>

      {sidebarOpen && <div className="admin-sidebar-scrim" onClick={() => setSidebarOpen(false)} />}

      <div className="admin-main">
        <header className="admin-topbar">
          <button className="admin-topbar-menu-btn" onClick={() => setSidebarOpen(true)} aria-label="Open menu">
            <span />
            <span />
            <span />
          </button>
          <div className="crumb muted mono">Admin / {crumb}</div>
          <div className="admin-user">
            <div className="admin-user-info">
              <div className="name">{staff?.staffName}</div>
              <div className="role muted">{staff?.staffRole || 'Authorized user'}</div>
            </div>
            <Avatar name={staff?.staffName || '?'} size={36} />
          </div>
        </header>
        <div className="admin-content">
          <Outlet />
        </div>
      </div>
    </div>
  );
}

// Small geometric glyphs instead of an icon-font dependency — kept quiet
// and functional, deliberately not part of the arch signature system.
function NavIcon({ label }) {
  const shapes = {
    Dashboard: <rect x="3" y="3" width="7" height="7" rx="1.5" />,
    Enquiries: <path d="M3 5h10M3 8.5h10M3 12h6" strokeLinecap="round" />,
    Admissions: <circle cx="8" cy="6" r="2.6" />,
    'Content Manager': <rect x="3" y="3" width="10" height="10" rx="2" />,
    'Excel Upload': <path d="M8 3v7m0 0-2.6-2.6M8 10l2.6-2.6M3 13h10" strokeLinecap="round" strokeLinejoin="round" />,
  };
  return (
    <svg width="16" height="16" viewBox="0 0 16 16" fill="none" stroke="currentColor" strokeWidth="1.4">
      {shapes[label]}
    </svg>
  );
}
