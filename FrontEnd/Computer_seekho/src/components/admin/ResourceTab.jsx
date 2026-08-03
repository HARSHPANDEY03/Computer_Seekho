import { useEffect, useState } from 'react';
import { EmptyState, Loading, StatusBadge } from '../ui/ui';

/*
  Config-driven CRUD table for one master resource (Courses, Batches,
  Staff, Recruiters, Albums, Images, Announcements, Closure Reasons).
  Payment Types renders as a BackendGapNotice instead — see Content.jsx.

  config shape:
  {
    idKey: 'courseId',
    columns: [{ key, label, render?(item) }],
    fields: [{ key, label, type: 'text'|'number'|'textarea'|'date'|'select'|'checkbox', options?, required? }],
    api: { list, create, update, remove },
    searchKeys: ['courseName', ...],
    toForm(item) -> formState,      // item -> editable form shape
    toPayload(form) -> apiPayload,  // form -> request body
    emptyForm,
    canUpdate: boolean (default true),
    canDelete: boolean (default true),
  }
*/
export default function ResourceTab({ config }) {
  const [items, setItems] = useState(null);
  const [error, setError] = useState('');
  const [search, setSearch] = useState('');
  const [editing, setEditing] = useState(null); // form state or null
  const [editingId, setEditingId] = useState(null); // null = creating new
  const [saving, setSaving] = useState(false);

  function load() {
    config.api.list().then(setItems).catch((e) => setError(e.message));
  }
  useEffect(() => { load(); }, []); // eslint-disable-line react-hooks/exhaustive-deps

  function startCreate() {
    setEditingId('new');
    setEditing(config.emptyForm);
    setError('');
  }
  function startEdit(item) {
    setEditingId(item[config.idKey]);
    setEditing(config.toForm(item));
    setError('');
  }
  function cancelEdit() {
    setEditingId(null);
    setEditing(null);
  }

  // Checks every field marked required against the current form state.
  // Returns the list of field labels that are still empty.
  function findMissingFields() {
    return config.fields
      .filter((f) => f.required)
      .filter((f) => {
        const v = editing[f.key];
        if (f.type === 'checkbox') return false; // a checkbox is never "empty"
        return v === undefined || v === null || String(v).trim() === '';
      })
      .map((f) => f.label);
  }

  async function onSave(e) {
    e.preventDefault();
    setError('');

    const missing = findMissingFields();
    if (missing.length > 0) {
      setError(`Please fill in: ${missing.join(', ')}.`);
      return;
    }

    setSaving(true);
    try {
      const payload = config.toPayload(editing);
      if (editingId === 'new') await config.api.create(payload);
      else await config.api.update(editingId, payload);
      cancelEdit();
      load();
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  }

  async function onDelete(item) {
    if (!window.confirm(`Delete this record? This can't be undone.`)) return;
    try {
      await config.api.remove(item[config.idKey]);
      load();
    } catch (err) {
      setError(err.message);
    }
  }

  const filtered = (items || []).filter((item) => {
    if (!search) return true;
    return config.searchKeys.some((k) => String(item[k] ?? '').toLowerCase().includes(search.toLowerCase()));
  });

  return (
    <div>
      <div className="filterbar">
        <input className="filter-input search" placeholder={`Search ${config.title.toLowerCase()}`} value={search} onChange={(e) => setSearch(e.target.value)} />
        <button className="btn btn-primary btn-sm" onClick={startCreate}>+ Add {config.singular}</button>
      </div>

      {error && <p className="alert alert-danger">{error}</p>}
      {items === null && <Loading label={`Loading ${config.title.toLowerCase()}…`} />}
      {items && items.length === 0 && <EmptyState title={`No ${config.title.toLowerCase()} yet`} />}

      {items && items.length > 0 && (
        <div className="card table-wrap">
          <table className="data-table">
            <thead>
              <tr>
                {config.columns.map((c) => <th key={c.key}>{c.label}</th>)}
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((item) => (
                <tr key={item[config.idKey]}>
                  {config.columns.map((c) => (
                    <td key={c.key}>{c.render ? c.render(item) : formatCell(item[c.key])}</td>
                  ))}
                  <td>
                    <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
                      {config.canUpdate !== false && <button className="tag-btn" onClick={() => startEdit(item)}>Edit</button>}
                      {config.statusToggle && (
                        <button
                          className="tag-btn"
                          onClick={async () => {
                            try { await config.statusToggle.run(item, !item[config.statusToggle.key]); load(); }
                            catch (err) { setError(err.message); }
                          }}
                        >
                          {item[config.statusToggle.key] ? 'Deactivate' : 'Activate'}
                        </button>
                      )}
                      {config.canDelete !== false && <button className="tag-btn tag-btn-danger" onClick={() => onDelete(item)}>Delete</button>}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {editingId && (
        <form className="card form-section" onSubmit={onSave} noValidate>
          <b style={{ fontSize: 'var(--text-sm)' }}>{editingId === 'new' ? `New ${config.singular}` : `Editing record`}</b>
          <div className="form-grid-3" style={{ marginTop: 12 }}>
            {config.fields.map((f) => (
              <FieldInput key={f.key} field={f} value={editing[f.key]} onChange={(v) => setEditing({ ...editing, [f.key]: v })} />
            ))}
          </div>
          <div className="form-actions">
            <button type="button" className="btn btn-outline" onClick={cancelEdit}>Cancel</button>
            <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? <span className="spinner" /> : 'Save Record'}</button>
          </div>
        </form>
      )}
    </div>
  );
}

function FieldInput({ field, value, onChange }) {
  const wide = field.type === 'textarea' ? 'full' : '';
  if (field.type === 'checkbox') {
    return (
      <label className="checkbox-row" style={{ alignSelf: 'end', paddingBottom: 10 }}>
        <input type="checkbox" checked={Boolean(value)} onChange={(e) => onChange(e.target.checked)} /> {field.label}
      </label>
    );
  }
  if (field.type === 'select') {
    return (
      <div className={`field ${wide}`}>
        <label>{field.label}{field.required ? ' *' : ''}</label>
        <select className="select" value={value ?? ''} onChange={(e) => onChange(e.target.value)}>
          <option value="">Select…</option>
          {field.options.map((o) => <option key={o.value} value={o.value}>{o.label}</option>)}
        </select>
      </div>
    );
  }
  if (field.type === 'textarea') {
    return (
      <div className={`field ${wide}`}>
        <label>{field.label}{field.required ? ' *' : ''}</label>
        <textarea className="textarea" rows={3} value={value ?? ''} onChange={(e) => onChange(e.target.value)} />
      </div>
    );
  }
  return (
    <div className={`field ${wide}`}>
      <label>{field.label}{field.required ? ' *' : ''}</label>
      <input
        className="input"
        type={field.type || 'text'}
        value={value ?? ''}
        onChange={(e) => onChange(e.target.value)}
      />
    </div>
  );
}

function formatCell(v) {
  if (v === true) return <StatusBadge status="active" />;
  if (v === false) return <StatusBadge status="inactive" />;
  if (v === null || v === undefined || v === '') return <span className="muted">—</span>;
  return String(v);
}