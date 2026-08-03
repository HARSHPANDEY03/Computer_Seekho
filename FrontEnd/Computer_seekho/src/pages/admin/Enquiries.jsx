import { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { getAllEnquiries } from '../../api/enquiries';
import { useAuth } from '../../context/AuthContext';
import { Loading, StatusBadge } from '../../components/ui/ui';
import { deriveEnquiryStatus, extractProgramInterest, STATUS_COPY } from '../../utils/enquiry';

const COLUMNS = [
  { key: 'new', label: 'New' },
  { key: 'contacted', label: 'Contacted' },
  { key: 'followup-due', label: 'Follow-up due' },
  { key: 'closed', label: 'Closed (not joining)' },
];

export default function Enquiries() {
  const { staff } = useAuth();
  const [enquiries, setEnquiries] = useState(null);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [staffScope, setStaffScope] = useState('all');

  useEffect(() => {
    getAllEnquiries().then(setEnquiries).catch((e) => setError(e.message));
  }, []);

  const filtered = useMemo(() => {
    if (!enquiries) return [];
    return enquiries
      // Once registered as a student, an enquiry is no longer an active
      // lead — it belongs in Admissions/Students, not the enquiry funnel.
      // It still exists in the database (for conversion history/reporting),
      // it's just not shown here anymore.
      .filter((e) => deriveEnquiryStatus(e) !== 'registered')
      .filter((e) => {
        if (staffScope === 'mine' && e.staff?.staffId !== staff?.staffId) return false;
        if (!search) return true;
        const haystack = `${e.enquirerName} ${e.enquirerMobile} ${e.enquirerEmailId || ''} ${extractProgramInterest(e.enquirerQuery)}`.toLowerCase();
        return haystack.includes(search.toLowerCase());
      });
  }, [enquiries, search, staffScope, staff]);

  const columns = useMemo(() => {
    const map = { new: [], contacted: [], 'followup-due': [], closed: [] };
    filtered.forEach((e) => {
      const status = deriveEnquiryStatus(e);
      map[status].push(e);
    });
    return map;
  }, [filtered]);

  return (
    <div>
      <div className="admin-title-row">
        <div>
          <h1>Enquiries and Follow-ups</h1>
          <p className="muted">Track every enquiry from initial contact through registration or closure.</p>
        </div>
      </div>

      <div className="filterbar">
        <input className="filter-input search" placeholder="Search by name, phone, email or program" value={search} onChange={(e) => setSearch(e.target.value)} />
        <select className="filter-input" value={staffScope} onChange={(e) => setStaffScope(e.target.value)}>
          <option value="all">Staff: All</option>
          <option value="mine">Staff: Me</option>
        </select>
        <Link to="/admin/enquiries/new" className="btn btn-primary btn-sm">+ Add Enquiry</Link>
      </div>

      {error && <p className="alert alert-danger">{error}</p>}
      {!enquiries && !error && <Loading label="Loading enquiries…" />}

      {enquiries && (
        <div className="kanban">
          {COLUMNS.map((col) => (
            <div className="kan-col" key={col.key}>
              <div className="kan-title"><span>{col.label}</span><span>{columns[col.key].length}</span></div>
              {columns[col.key].map((e) => (
                <EnquiryCard key={e.enquiryId} enquiry={e} />
              ))}
              {columns[col.key].length === 0 && <p className="muted kan-empty">Nothing here.</p>}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

function EnquiryCard({ enquiry }) {
  const status = deriveEnquiryStatus(enquiry);
  const program = extractProgramInterest(enquiry.enquirerQuery);
  const isDone = status === 'closed';
  return (
    <div className="lead-card">
      <b>{enquiry.enquirerName}</b>
      <p>{program || enquiry.enquirySource || 'General enquiry'}{enquiry.enquirySource ? ` · ${enquiry.enquirySource}` : ''}</p>
      <div className="meta-row">
        <span className="mono">{enquiry.enquirerMobile}</span>
        <StatusBadge status={status}>{STATUS_COPY[status]}</StatusBadge>
      </div>
      {!isDone && enquiry.followupDate && (
        <p className="muted" style={{ marginTop: 4, fontSize: 12 }}>
          Next follow-up: <b>{enquiry.followupDate}</b> · Call {enquiry.inquiryCounter || 0} of 3
        </p>
      )}
      <div className="lead-actions">
        <Link to={`/admin/enquiries/${enquiry.enquiryId}`}>{isDone ? 'View' : 'Open'}</Link>
        {!isDone && <a href={`tel:${enquiry.enquirerMobile}`}>Call</a>}
      </div>
    </div>
  );
}