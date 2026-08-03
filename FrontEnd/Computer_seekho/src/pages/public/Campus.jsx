import { useEffect, useMemo, useState } from 'react';
import { getAllAlbums, getAllImages } from '../../api/content';
import { getAllStaff } from '../../api/staff';
import { Avatar, EmptyState, Loading } from '../../components/ui/ui';

export default function Campus() {
  const [tab, setTab] = useState('Campus Life');
  const [albums, setAlbums] = useState(null);
  const [images, setImages] = useState(null);
  const [staff, setStaff] = useState(null);
  const [selectedAlbum, setSelectedAlbum] = useState(null);
  const [selectedStaff, setSelectedStaff] = useState(null);

  useEffect(() => {
    getAllAlbums().then(setAlbums).catch(() => setAlbums([]));
    getAllImages().then(setImages).catch(() => setImages([]));
    getAllStaff().then(setStaff).catch(() => setStaff([]));
  }, []);

  useEffect(() => {
    if (!selectedStaff) return;
    const onKey = (e) => { if (e.key === 'Escape') setSelectedStaff(null); };
    window.addEventListener('keydown', onKey);
    return () => window.removeEventListener('keydown', onKey);
  }, [selectedStaff]);

  const albumGroups = useMemo(() => {
    if (!albums || !images) return null;
    return albums
      .filter((a) => a.albumIsActive)
      .map((album) => ({
        album,
        photos: images
          .filter((im) => im.imageIsActive && im.albumId === album.albumId)
          .sort((a, b) => (b.isAlbumCover ? 1 : 0) - (a.isAlbumCover ? 1 : 0)),
      }))
      .filter((g) => g.photos.length > 0)
      .sort((a, b) => (b.album.startDate || '').localeCompare(a.album.startDate || ''));
  }, [albums, images]);

  useEffect(() => {
    if (albumGroups && albumGroups.length > 0 && selectedAlbum === null) {
      setSelectedAlbum(String(albumGroups[0].album.albumId));
    }
  }, [albumGroups, selectedAlbum]);

  const visibleGroups = useMemo(() => {
    if (!albumGroups) return null;
    if (selectedAlbum === null || selectedAlbum === 'all') return albumGroups;
    return albumGroups.filter((g) => String(g.album.albumId) === selectedAlbum);
  }, [albumGroups, selectedAlbum]);

  return (
    <div className="container section">
      <div className="page-head">
        <div>
          <p className="eyebrow">Campus</p>
          <h1 className="h2">Campus life</h1>
          <p className="body-text">Photo albums, facilities and the faculty who run them.</p>
        </div>
        <div className="segmented">
          <button className={tab === 'Campus Life' ? 'active' : ''} onClick={() => setTab('Campus Life')}>Campus Life</button>
          <button className={tab === 'Faculty' ? 'active' : ''} onClick={() => setTab('Faculty')}>Faculty</button>
        </div>
      </div>

      {tab === 'Campus Life' && (
        <>
          {albumGroups === null && <Loading label="Loading campus photos…" />}
          {albumGroups && albumGroups.length === 0 && (
            <EmptyState title="No campus photos yet" description="Albums published from the admin panel will appear here." />
          )}
          {albumGroups && albumGroups.length > 0 && (
            <>
              <div className="album-picker-row">
                <label htmlFor="album-select" className="muted" style={{ fontSize: 'var(--text-sm)' }}>Album</label>
                <select
                  id="album-select"
                  className="select album-picker"
                  value={selectedAlbum ?? 'all'}
                  onChange={(e) => setSelectedAlbum(e.target.value)}
                >
                  <option value="all">All albums</option>
                  {albumGroups.map(({ album }) => (
                    <option key={album.albumId} value={String(album.albumId)}>{album.albumName}</option>
                  ))}
                </select>
              </div>

              <div className="campus-albums">
                {visibleGroups.map(({ album, photos }) => (
                  <section className="campus-album-group" key={album.albumId}>
                    {selectedAlbum === 'all' && <h3 className="h3 campus-album-title">{album.albumName}</h3>}
                    <div className="campus-grid">
                      {photos.map((im) => (
                        <div className="campus-photo" key={im.imageId} style={{ backgroundImage: `url(${im.imagePath})` }} />
                      ))}
                    </div>
                  </section>
                ))}
              </div>
            </>
          )}
        </>
      )}

      {tab === 'Faculty' && (
        <>
          {staff === null && <Loading label="Loading faculty…" />}
          {staff && staff.length === 0 && <EmptyState title="No staff published yet" />}
          {staff && staff.length > 0 && (
            <div className="faculty-grid">
              {staff.map((s) => (
                <div
                  className="card card-pad faculty-card faculty-card-clickable"
                  key={s.staffId}
                  onClick={() => setSelectedStaff(s)}
                  role="button"
                  tabIndex={0}
                  onKeyDown={(e) => {
                    if (e.key === 'Enter' || e.key === ' ') {
                      e.preventDefault();
                      setSelectedStaff(s);
                    }
                  }}
                >
                  <Avatar name={s.staffName} src={s.photoUrl} size={64} />
                  <b>{s.staffName}</b>
                  <p className="muted">{s.staffRole || 'Staff'}</p>
                </div>
              ))}
            </div>
          )}
        </>
      )}

      {selectedStaff && (
        <div className="modal-backdrop" onClick={() => setSelectedStaff(null)}>
          <div
            className="modal-card"
            role="dialog"
            aria-modal="true"
            aria-label={`${selectedStaff.staffName} details`}
            onClick={(e) => e.stopPropagation()}
          >
            <button className="modal-close" onClick={() => setSelectedStaff(null)} aria-label="Close">×</button>

            <div className="modal-hero">
              <div className="modal-avatar-frame">
                {selectedStaff.photoUrl ? (
                  <img src={selectedStaff.photoUrl} alt={selectedStaff.staffName} />
                ) : (
                  <span className="modal-avatar-initials">
                    {selectedStaff.staffName
                      .split(' ')
                      .filter(Boolean)
                      .slice(0, 2)
                      .map((p) => p[0]?.toUpperCase())
                      .join('')}
                  </span>
                )}
              </div>
            </div>

            <div className="modal-body">
              <h3 className="h3">{selectedStaff.staffName}</h3>
              {selectedStaff.description && (
                <p className="body-text modal-description">{selectedStaff.description}</p>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}