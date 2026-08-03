import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { getDashboardSummary } from '../../api/misc';
import { getTodayForStaff, getOverdueForStaff, createEnquiry } from '../../api/enquiries';
import { Loading, EmptyState } from '../../components/ui/ui';
import { extractProgramInterest } from '../../utils/enquiry';
import { registerStudent } from '../../api/students';
import { getActiveCourses, getActiveBatchesByCourse } from '../../api/courses';


export default function Dashboard() {
  const { staff } = useAuth();
  const [summary, setSummary] = useState(null);
  const [today, setToday] = useState(null);
  const [overdueCount, setOverdueCount] = useState(null);

  useEffect(() => {
    getDashboardSummary().then(setSummary).catch(() => setSummary({}));
    if (staff?.staffId) {
      getTodayForStaff(staff.staffId).then(setToday).catch(() => setToday([]));
      getOverdueForStaff(staff.staffId).then((l) => setOverdueCount(l.length)).catch(() => setOverdueCount(0));
    }
  }, [staff]);

  return (
    <div>
      <div className="admin-title-row">
        <div>
          <h1>Follow-up Dashboard</h1>
          <p className="muted">Default landing page after sign-in. Follow-ups due today and pending follow-ups are prioritized.</p>
        </div>
      </div>

      <div className="kpi-grid">
        <div className="card kpi"><span>Due today (you)</span><b>{today ? today.length : '—'}</b></div>
        <div className="card kpi"><span>Overdue (you)</span><b className={overdueCount ? 'kpi-danger' : ''}>{overdueCount ?? '—'}</b></div>
        <div className="card kpi"><span>Open enquiries</span><b>{summary?.totalEnquiries ?? '—'}</b></div>
        <div className="card kpi"><span>Total admissions</span><b>{summary?.totalAdmissions ?? '—'}</b></div>
      </div>

      <div className="dashboard-grid">
        <div className="card panel">
          <div className="panel-head">
            <h3>My follow-ups today</h3>
            <div style={{ display: 'flex', gap: 8 }}>
              <Link to="/admin/enquiries" className="btn btn-ghost btn-sm">View All</Link>
              <Link to="/admin/enquiries/new" className="btn btn-primary btn-sm">Add Enquiry</Link>
            </div>
          </div>
          {today === null && <Loading label="Loading follow-ups…" />}
          {today && today.length === 0 && <EmptyState title="Nothing due today" description="Enjoy the clear queue — check View All for anything upcoming." />}
          {today && today.length > 0 && (
            <div className="table-wrap">
              <table className="data-table">
                <thead>
                  <tr><th>Enq. ID</th><th>Enquirer</th><th>Phone</th><th>Program</th><th>Follow-up date</th><th>Action</th></tr>
                </thead>
                <tbody>
                  {today.map((e) => (
                    <tr key={e.enquiryId}>
                      <td className="mono">ENQ-{e.enquiryId}</td>
                      <td>{e.enquirerName}</td>
                      <td className="mono">{e.enquirerMobile}</td>
                      <td>{extractProgramInterest(e.enquirerQuery) || '—'}</td>
                      <td>{e.followupDate}</td>
                      <td><Link to={`/admin/enquiries/${e.enquiryId}`} className="btn btn-sm btn-outline">Call</Link></td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        <div className="card panel">
          <div className="panel-head"><h3>Quick actions</h3></div>
          <div className="quick-actions">
            <Link to="/admin/enquiries/new" className="quick-action">
              <b>Add Enquiry</b>
              <span>Log a new walk-in, phone or email enquiry.</span>
            </Link>
            <Link to="/admin/admissions" className="quick-action">
              <b>Register Student</b>
              <span>Search an enquiry and complete admission.</span>
            </Link>
            <Link to="/admin/excel-upload" className="quick-action">
              <b>Excel Upload</b>
              <span>Bulk-import courses, batches or recruiters.</span>
            </Link>
            <Link to="/admin/content" className="quick-action">
              <b>Content Manager</b>
              <span>Update programs, staff, albums and more.</span>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
