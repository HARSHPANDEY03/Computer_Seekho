@import "./tokens.css";

/* ============ Hero ============ */
.hero { padding: var(--space-8) 0 var(--space-7); background: linear-gradient(180deg, var(--blue-50), var(--white) 70%); }
.hero-grid { display: grid; grid-template-columns: 1.25fr 0.9fr; gap: var(--space-8); align-items: center; }
.hero-copy .pill { margin-bottom: var(--space-4); }
.hero-copy .lede { margin-top: var(--space-4); max-width: 560px; }
.hero-search {
  margin-top: var(--space-6); display: flex; align-items: center; gap: var(--space-3);
  background: var(--white); border: 1.5px solid var(--border-default); border-radius: var(--radius-md);
  padding: 6px 6px 6px 16px; max-width: 560px; box-shadow: var(--shadow-sm);
}
.hero-search svg { color: var(--text-tertiary); flex-shrink: 0; }
.hero-search input { flex: 1; border: none; height: 40px; font-size: var(--text-md); }
.hero-search input:focus { outline: none; }
.hero-art-frame {
  position: relative; aspect-ratio: 4/5; border-radius: var(--radius-lg) var(--radius-lg) var(--radius-lg) 60px;
  background: var(--ink-900); overflow: hidden; display: flex; align-items: flex-end;
}
.hero-art-caption { position: relative; z-index: 2; padding: var(--space-6); color: var(--white); }
.hero-art-caption p { font-family: var(--font-serif); font-size: var(--text-lg); margin-top: 8px; line-height: 1.4; }
.hero-art-frame .arch-motif { position: absolute; inset: 0; }
@media (max-width: 860px) { .hero-grid { grid-template-columns: 1fr; } .hero-art-frame { aspect-ratio: 16/9; } }

.stat-row-overlap { margin-top: calc(var(--space-8) * -1); position: relative; z-index: 3; box-shadow: var(--shadow-lg); border-radius: var(--radius-lg); }
.stat-row-overlap.stat-row { background: var(--white); padding: var(--space-2); border-radius: var(--radius-lg); border: 1px solid var(--border-subtle); }
.stat-row-overlap .stat-tile { border: none; box-shadow: none; }
@media (max-width: 720px) { .stat-row-overlap { margin-top: var(--space-6); } }

/* ============ Home lower ============ */
.home-lower { display: grid; grid-template-columns: 1.3fr 0.9fr; gap: var(--space-8); }
.section-head { margin-bottom: var(--space-5); }
.section-head h2 { margin-top: 6px; }
@media (max-width: 860px) { .home-lower { grid-template-columns: 1fr; } }

.program-card-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: var(--space-5); }
.program-card { display: flex; flex-direction: column; overflow: hidden; }
.program-card-img {
  height: 140px; background: var(--blue-100); background-size: cover; background-position: center;
  display: flex; align-items: center; justify-content: center;
  font-family: var(--font-display); font-weight: 700; font-size: var(--text-3xl); color: var(--blue-600);
}
.program-card-row { display: flex; justify-content: space-between; align-items: flex-start; gap: 8px; }
.program-card-meta { display: flex; justify-content: space-between; font-size: var(--text-sm); font-weight: 600; color: var(--text-secondary); padding-top: 10px; border-top: 1px solid var(--border-subtle); }
@media (max-width: 860px) { .program-card-grid { grid-template-columns: 1fr 1fr; } }
@media (max-width: 560px) { .program-card-grid { grid-template-columns: 1fr; } }

.announcement-list { padding: 6px var(--space-5); }
.announcement-item { padding: var(--space-4) 0; border-bottom: 1px solid var(--border-subtle); }
.announcement-item:last-child { border-bottom: none; }
.announcement-item b { display: block; font-size: var(--text-md); }
.announcement-item span { display: block; font-size: var(--text-sm); color: var(--text-tertiary); margin-top: 4px; }

/* ============ Journey band (home) ============ */
.journey-band { background: var(--ink-900); color: var(--text-on-dark-muted); margin-top: var(--space-8); }
.journey-inner { display: grid; grid-template-columns: 1fr auto auto; gap: var(--space-7); align-items: center; }
.journey-inner h2.on-dark { color: var(--white); margin-top: 8px; max-width: 420px; }
.journey-steps { display: flex; gap: var(--space-6); }
.journey-step { display: flex; flex-direction: column; align-items: flex-start; gap: 6px; }
.journey-dot { width: 10px; height: 10px; border-radius: 50%; background: var(--brass-500); }
.journey-step b { color: var(--white); font-family: var(--font-display); }
@media (max-width: 960px) { .journey-inner { grid-template-columns: 1fr; text-align: left; } .journey-steps { flex-wrap: wrap; } }

/* ============ Generic page head ============ */
.page-head { display: flex; justify-content: space-between; align-items: flex-end; gap: var(--space-5); margin-bottom: var(--space-6); flex-wrap: wrap; }
.page-head h1 { margin-top: 6px; }
.page-head .body-text { margin-top: 8px; max-width: 640px; }

.inline-search { display: flex; gap: 8px; }
.inline-search .input { width: 260px; }

.segmented { display: inline-flex; background: var(--paper-100); border-radius: var(--radius-pill); padding: 4px; gap: 2px; }
.segmented button, .segmented a {
  padding: 8px 18px; border-radius: var(--radius-pill); font-size: var(--text-sm); font-weight: 600; color: var(--text-secondary);
}
.segmented button.active, .segmented a.active { background: var(--blue-600); color: var(--white); }

.filter-bar { display: flex; gap: var(--space-3); margin-bottom: var(--space-5); flex-wrap: wrap; }
.filter-bar .input { flex: 1; min-width: 200px; }
.filter-bar .select { width: auto; min-width: 160px; }

/* ============ Programs listing ============ */
.program-group { margin-top: var(--space-7); }
.program-group-title { font-family: var(--font-mono); text-transform: uppercase; letter-spacing: 0.06em; font-size: var(--text-sm); color: var(--text-tertiary); margin-bottom: var(--space-4); }

/* ============ Program detail ============ */
.breadcrumb { font-size: var(--text-sm); margin-bottom: var(--space-5); }
.breadcrumb a:hover { color: var(--blue-600); }
.detail-head { display: grid; grid-template-columns: 1.5fr 0.7fr; gap: var(--space-6); }
.detail-meta { display: flex; gap: var(--space-6); margin-top: var(--space-5); flex-wrap: wrap; }
.detail-meta div b { display: block; font-family: var(--font-display); font-size: var(--text-lg); }
.detail-meta div span { font-size: var(--text-xs); color: var(--text-tertiary); }
.detail-img {
  border-radius: var(--radius-lg); background: var(--blue-100) center/cover; min-height: 200px;
  display: flex; align-items: center; justify-content: center; font-family: var(--font-display); font-weight: 700; font-size: var(--text-4xl); color: var(--blue-600);
}
@media (max-width: 860px) { .detail-head { grid-template-columns: 1fr; } .detail-img { order: -1; height: 180px; } }

.tabs { display: flex; gap: var(--space-2); border-bottom: 1.5px solid var(--border-subtle); margin: var(--space-7) 0 var(--space-6); overflow-x: auto; }
.tab { padding: 12px 18px; font-weight: 600; font-size: var(--text-md); color: var(--text-secondary); border-bottom: 2px solid transparent; margin-bottom: -1.5px; white-space: nowrap; }
.tab.active { color: var(--blue-600); border-color: var(--blue-600); }
.detail-body { display: grid; grid-template-columns: 1.6fr 0.8fr; gap: var(--space-6); align-items: start; }
@media (max-width: 860px) { .detail-body { grid-template-columns: 1fr; } }
.bullet-list li { position: relative; padding-left: 22px; margin-bottom: 10px; color: var(--text-secondary); line-height: 1.6; }
.bullet-list li::before { content: ""; position: absolute; left: 0; top: 9px; width: 7px; height: 7px; border-radius: 2px; background: var(--brass-500); }
.fee-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: var(--space-5); margin-top: var(--space-4); }
.fee-grid b { display: block; font-size: var(--text-lg); font-family: var(--font-display); margin-top: 4px; }
.faq-list > div { padding: var(--space-4) 0; border-bottom: 1px solid var(--border-subtle); }
.faq-list > div:last-child { border-bottom: none; }
.faq-list p { margin-top: 6px; color: var(--text-secondary); line-height: 1.6; }
.side-card h4 { margin-bottom: 4px; }
.batch-row, .batch-row-full { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; border-bottom: 1px solid var(--border-subtle); font-size: var(--text-sm); }
.batch-row-full { padding: 12px var(--space-4); background: var(--paper-50); border-radius: var(--radius-sm); border-bottom: none; }

/* ============ About ============ */
.about-layout { display: grid; grid-template-columns: 1.2fr 0.8fr; gap: var(--space-6); align-items: stretch; }
.about-actions { display: flex; gap: var(--space-3); margin-top: var(--space-5); flex-wrap: wrap; }
.about-img-frame, .about-img-frame.arch-frame { background: var(--ink-900); }
.about-img-fallback { position: relative; width: 100%; height: 100%; display: flex; flex-direction: column; justify-content: flex-end; padding: var(--space-6); color: var(--white); }
.about-img-fallback p { font-family: var(--font-serif); font-size: var(--text-xl); margin-top: 8px; }
@media (max-width: 860px) { .about-layout { grid-template-columns: 1fr; } .about-img-frame { aspect-ratio: 16/9; } }

.values-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: var(--space-5); margin-top: var(--space-6); }
.value-card { text-align: left; }
.value-icon { width: 40px; height: 40px; border-radius: var(--radius-sm); background: var(--blue-100); margin-bottom: var(--space-3); }
@media (max-width: 720px) { .values-grid { grid-template-columns: 1fr; } }

.journey-card { margin-top: var(--space-6); }
.journey-timeline { display: flex; justify-content: space-between; margin-top: var(--space-5); position: relative; }
.journey-timeline::before { content: ""; position: absolute; top: 15px; left: 15px; right: 15px; height: 2px; background: var(--border-default); }
.journey-timeline-step { position: relative; z-index: 1; display: flex; flex-direction: column; align-items: center; gap: 10px; flex: 1; }
.journey-timeline-dot {
  width: 32px; height: 32px; border-radius: 50%; background: var(--white); border: 2px solid var(--blue-600); color: var(--blue-600);
  display: flex; align-items: center; justify-content: center; font-family: var(--font-mono); font-weight: 600; font-size: var(--text-sm);
}
@media (max-width: 600px) { .journey-timeline { flex-direction: column; gap: var(--space-4); align-items: flex-start; } .journey-timeline::before { display: none; } .journey-timeline-step { flex-direction: row; } }

/* ============ Campus ============ */
.campus-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: var(--space-4); }
.campus-photo {
  aspect-ratio: 4/3; border-radius: var(--radius-md); background: var(--blue-100) center/cover; position: relative;
}
.campus-photo span {
  position: absolute; bottom: 10px; left: 10px; background: rgba(255,255,255,0.92); padding: 5px 10px; border-radius: var(--radius-sm);
  font-size: var(--text-xs); font-weight: 600;
}
.faculty-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: var(--space-4); margin-top: var(--space-6); }
.faculty-card { text-align: center; display: flex; flex-direction: column; align-items: center; gap: 10px; }
@media (max-width: 860px) { .campus-grid, .faculty-grid { grid-template-columns: repeat(2, 1fr); } }

/* ============ Placements ============ */
.student-cell { display: flex; align-items: center; gap: 10px; }
.placement-bottom { display: grid; grid-template-columns: 1.4fr 0.8fr; gap: var(--space-5); margin-top: var(--space-5); }
.chart-box { }
.bar-chart { display: flex; align-items: flex-end; gap: var(--space-4); height: 160px; margin-top: var(--space-4); padding: 0 var(--space-2); border-bottom: 1px solid var(--border-default); }
.bar-col { flex: 1; display: flex; flex-direction: column; align-items: center; justify-content: flex-end; height: 100%; gap: 8px; }
.bar { width: 100%; max-width: 34px; background: var(--blue-600); border-radius: 4px 4px 0 0; min-height: 3px; transition: height var(--med) var(--ease); }
.bar-col span { font-size: var(--text-xs); color: var(--text-tertiary); }
.legend-item { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid var(--border-subtle); font-size: var(--text-sm); }
.legend-item:last-child { border-bottom: none; }
@media (max-width: 860px) { .placement-bottom { grid-template-columns: 1fr; } }

/* ============ Recruiters ============ */
.logo-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: var(--space-4); }
.logo-card {
  height: 92px; display: flex; align-items: center; justify-content: center; background: var(--white);
  border: 1px solid var(--border-subtle); border-radius: var(--radius-md); font-weight: 600; color: var(--text-secondary);
  font-size: var(--text-sm); text-align: center; padding: var(--space-2); transition: box-shadow var(--fast) var(--ease), transform var(--fast) var(--ease);
}
.logo-card:hover { box-shadow: var(--shadow-sm); transform: translateY(-2px); }
.logo-card img { max-height: 48px; max-width: 100%; object-fit: contain; }
@media (max-width: 860px) { .logo-grid { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 560px) { .logo-grid { grid-template-columns: repeat(2, 1fr); } }

.cta-banner { margin-top: var(--space-6); display: flex; justify-content: space-between; align-items: center; gap: var(--space-4); background: var(--blue-50); flex-wrap: wrap; }

/* ============ Enquiry / Contact forms ============ */
.form-layout { display: grid; grid-template-columns: 0.85fr 1.15fr; gap: var(--space-6); align-items: start; }
.info-panel { background: var(--blue-50); border: 1px solid var(--blue-100); border-radius: var(--radius-lg); padding: var(--space-6); position: sticky; top: 100px; }
.info-list { margin-top: var(--space-6); display: flex; flex-direction: column; gap: var(--space-4); }
.info-item { display: flex; gap: var(--space-3); }
.info-icon { width: 34px; height: 34px; border-radius: 50%; background: var(--white); border: 1px solid var(--blue-100); flex-shrink: 0; }
.info-item b { display: block; font-size: var(--text-sm); }
.info-item span { display: block; font-size: var(--text-xs); color: var(--text-secondary); margin-top: 2px; }
.check { font-size: var(--text-xs); color: var(--text-tertiary); margin: var(--space-4) 0; }
@media (max-width: 860px) { .form-layout { grid-template-columns: 1fr; } .info-panel { position: static; } }

.contact-top { display: grid; grid-template-columns: repeat(3, 1fr); gap: var(--space-4); margin-bottom: var(--space-6); }
.contact-method { text-align: center; }
.contact-method .circle { width: 40px; height: 40px; border-radius: 50%; background: var(--blue-100); margin: 0 auto var(--space-3); }
.contact-method b { display: block; }
.contact-method span { font-size: var(--text-sm); color: var(--text-secondary); display: block; margin-top: 4px; }
.contact-grid { display: grid; grid-template-columns: 1fr 1fr; gap: var(--space-6); align-items: start; }
.map-frame { border-radius: var(--radius-lg); overflow: hidden; aspect-ratio: 4/3; border: 1px solid var(--border-subtle); }
.map-frame iframe { width: 100%; height: 100%; border: 0; }
@media (max-width: 860px) { .contact-top, .contact-grid { grid-template-columns: 1fr; } }
.album-picker-row { display: inline-flex; align-items: center; gap: var(--space-3); margin: var(--space-6) 0; }
.album-picker-row .select { width: auto; min-width: 200px; }
.faculty-card-clickable { cursor: pointer; transition: box-shadow var(--med) var(--ease), transform var(--med) var(--ease); }
.faculty-card-clickable:hover { box-shadow: var(--shadow-md); transform: translateY(-2px); }

.modal-backdrop {
  position: fixed; inset: 0; background: rgba(15, 23, 42, 0.55);
  display: flex; align-items: center; justify-content: center;
  padding: var(--space-4); z-index: 100;
}
.modal-card {
  background: var(--white); border-radius: var(--radius-lg);
  max-width: min(420px, 92vw); width: 100%; max-height: 88vh;
  overflow-y: auto; overflow-x: hidden;
  position: relative; box-shadow: var(--shadow-lg);
}
.modal-close {
  position: absolute; top: var(--space-3); right: var(--space-3); z-index: 1;
  width: 32px; height: 32px; border-radius: 50%; border: none;
  background: rgba(255,255,255,0.85); color: var(--text-secondary); font-size: 18px; cursor: pointer;
}
.modal-hero {
  background: linear-gradient(135deg, var(--blue-600), var(--blue-700));
  padding: var(--space-6) var(--space-5) var(--space-8);
  display: flex; justify-content: center;
}
.modal-avatar-frame {
  width: 120px; height: 120px; border-radius: var(--radius-arch);
  overflow: hidden; border: 4px solid var(--white); box-shadow: var(--shadow-md);
  background: var(--paper-100); display: flex; align-items: center; justify-content: center;
}
.modal-avatar-frame img { width: 100%; height: 100%; object-fit: cover; display: block; }
.modal-avatar-initials { font-family: var(--font-display); font-weight: 700; font-size: var(--text-3xl); color: var(--blue-700); }
.modal-body { padding: var(--space-6); text-align: center; }
.modal-description { margin-top: var(--space-4); text-align: left; line-height: 1.65; color: var(--text-secondary); }