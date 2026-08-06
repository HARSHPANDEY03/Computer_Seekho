import { useEffect, useState } from 'react';
import ResourceTab from '../../components/admin/ResourceTab';
import { Loading } from '../../components/ui/ui';
import * as coursesApi from '../../api/courses';
import * as staffApi from '../../api/staff';
import * as contentApi from '../../api/content';
import * as enquiriesApi from '../../api/enquiries';
import * as studentsApi from '../../api/students';
import * as miscApi from '../../api/misc';

const TABS = ['Courses', 'Batches', 'Staff', 'Students', 'Recruiters', 'Placements', 'Albums', 'Images', 'Announcements', 'Contact Messages', 'Closure Reasons'];

export default function Content() {
  const [tab, setTab] = useState('Courses');
  const [courses, setCourses] = useState(null);
  const [batches, setBatches] = useState(null);
  const [recruiters, setRecruiters] = useState(null);
  const [albums, setAlbums] = useState(null);
  const [roles, setRoles] = useState(null);

  useEffect(() => {
    coursesApi.getAllCourses().then(setCourses).catch(() => setCourses([]));
    coursesApi.getAllBatches().then(setBatches).catch(() => setBatches([]));
    contentApi.getAllRecruiters().then(setRecruiters).catch(() => setRecruiters([]));
    contentApi.getAllAlbums().then(setAlbums).catch(() => setAlbums([]));
    staffApi.getAllRoles().then(setRoles).catch(() => setRoles([]));
  }, []);

  if (courses === null || batches === null || recruiters === null || albums === null || roles === null) return <Loading label="Loading content manager…" />;

  return (
    <div>
      <div className="admin-title-row">
        <div>
          <h1>Content Manager</h1>
          <p className="muted">Every master table behind the public site — changes here appear on the public pages immediately.</p>
        </div>
      </div>

      <div className="content-tabs">
        {TABS.map((t) => (
          <button key={t} className={`content-tab ${tab === t ? 'active' : ''}`} onClick={() => setTab(t)}>{t}</button>
        ))}
      </div>

      {tab === 'Courses' && <ResourceTab config={coursesConfig()} />}
      {tab === 'Batches' && <ResourceTab config={batchesConfig(courses)} />}
      {tab === 'Staff' && <ResourceTab config={staffConfig(roles)} />}
      {tab === 'Students' && <ResourceTab config={studentsConfig(courses, batches)} />}
      {tab === 'Recruiters' && <ResourceTab config={recruitersConfig()} />}
      {tab === 'Placements' && <ResourceTab config={placementsConfig(batches, recruiters)} />}
      {tab === 'Albums' && <ResourceTab config={albumsConfig()} />}
      {tab === 'Images' && <ResourceTab config={imagesConfig(albums)} />}
      {tab === 'Announcements' && <ResourceTab config={announcementsConfig()} />}
      {tab === 'Contact Messages' && <ResourceTab config={contactMessagesConfig()} />}
      {tab === 'Closure Reasons' && <ResourceTab config={closureReasonsConfig()} />}
    </div>
  );
}

function money(v) { return v ? `₹${Number(v).toLocaleString('en-IN')}` : '—'; }

function coursesConfig() {
  return {
    idKey: 'courseId',
    title: 'Courses',
    singular: 'Course',
    searchKeys: ['courseName', 'courseCategory'],
    columns: [
      { key: 'courseName', label: 'Course' },
      { key: 'courseCategory', label: 'Category' },
      { key: 'ageGrpType', label: 'Age Group' },
      { key: 'courseDuration', label: 'Duration', render: (i) => (i.courseDuration ? `${i.courseDuration} mo` : '—') },
      { key: 'courseFees', label: 'Fees', render: (i) => money(i.courseFees) },
      { key: 'courseIsActive', label: 'Status' },
    ],
    fields: [
      { key: 'courseName', label: 'Course name', type: 'text', required: true },
      { key: 'courseCategory', label: 'Category', type: 'text', required: true },
      { key: 'ageGrpType', label: 'Age Group', type: 'select', options: [{ value: 'School students', label: 'School students' }, { value: 'College students', label: 'College students' }, { value: 'Professionals', label: 'Professionals' }] },
      { key: 'courseDuration', label: 'Duration (months)', type: 'number' },
      { key: 'courseFees', label: 'Fees (INR)', type: 'number' },
      { key: 'courseFeesFrom', label: 'Fee valid from', type: 'date' },
      { key: 'courseFeesTo', label: 'Fee valid to', type: 'date' },
      { key: 'coverPhoto', label: 'Cover Photo', type: 'photo' },
      { key: 'courseDescription', label: 'Description', type: 'textarea' },
      { key: 'courseSyllabus', label: 'Syllabus (one point per line)', type: 'textarea' },
      { key: 'isFeatured', label: 'Featured on homepage', type: 'checkbox' },
    ],
    api: { list: coursesApi.getAllCourses, create: coursesApi.createCourse, update: coursesApi.updateCourse },
    statusToggle: { key: 'courseIsActive', run: (item, next) => coursesApi.updateCourseStatus(item.courseId, next) },
    canDelete: false, // backend has no delete endpoint for courses, only the status toggle
    toForm: (c) => ({ ...c }),
    toPayload: (f) => ({ ...f, courseDuration: f.courseDuration ? Number(f.courseDuration) : null, courseFees: f.courseFees ? Number(f.courseFees) : null }),
    emptyForm: { courseName: '', courseCategory: '', ageGrpType: '', courseDuration: '', courseFees: '', courseFeesFrom: '', courseFeesTo: '', coverPhoto: '', courseDescription: '', courseSyllabus: '', isFeatured: false },
  };
}

function batchesConfig(courses) {
  const courseOptions = courses.map((c) => ({ value: c.courseId, label: c.courseName }));
  return {
    idKey: 'batchId',
    title: 'Batches',
    singular: 'Batch',
    searchKeys: ['batchName', 'courseName'],
    columns: [
      { key: 'batchName', label: 'Batch' },
      { key: 'courseName', label: 'Course' },
      { key: 'batchStartTime', label: 'Start' },
      { key: 'batchEndTime', label: 'End' },
      { key: 'batchIsActive', label: 'Status' },
    ],
    fields: [
      { key: 'batchName', label: 'Batch name', type: 'text', required: true },
      { key: 'courseId', label: 'Course', type: 'select', required: true, options: courseOptions },
      { key: 'batchStartTime', label: 'Start time', type: 'time' },
      { key: 'batchEndTime', label: 'End time', type: 'time' },
    ],
    api: { list: coursesApi.getAllBatches, create: coursesApi.createBatch, update: coursesApi.updateBatch },
    statusToggle: { key: 'batchIsActive', run: (item, next) => coursesApi.updateBatchStatus(item.batchId, next) },
    canDelete: false,
    toForm: (b) => ({ ...b, courseId: b.courseId }),
    toPayload: (f) => ({ ...f, courseId: Number(f.courseId) }),
    emptyForm: { batchName: '', courseId: '', batchStartTime: '', batchEndTime: '' },
  };
}

function staffConfig(roles) {
  const roleOptions = roles.map((r) => ({ value: r.userId, label: r.roleName }));
  return {
    idKey: 'staffId',
    title: 'Staff',
    singular: 'Staff member',
    searchKeys: ['staffName', 'staffRole', 'staffEmail'],
    columns: [
      { key: 'staffName', label: 'Name' },
      { key: 'staffRole', label: 'Role' },
      { key: 'staffMobile', label: 'Mobile' },
      { key: 'staffEmail', label: 'Email' },
    ],
    fields: [
      { key: 'staffName', label: 'Full name', type: 'text', required: true },
      // The backend derives the displayed role name from userRoleId - it
      // does not accept a free-text role at all. A plain text field here
      // was silently ignored, and omitting this dropdown caused
      // "The given id must not be null" on save, since userRoleId arrived
      // as null and StaffServiceImpl looks it up via findById(userRoleId).
      { key: 'userRoleId', label: 'Role', type: 'select', required: true, options: roleOptions },
      { key: 'staffMobile', label: 'Mobile', type: 'text' },
      { key: 'staffEmail', label: 'Email', type: 'text' },
      { key: 'photoUrl', label: 'Photo', type: 'photo' },
      { key: 'description', label: 'Bio / description', type: 'textarea' },
      { key: 'staffUsername', label: 'Login username', type: 'text', required: true },
      { key: 'staffPassword', label: 'Password (leave blank to keep unchanged)', type: 'password' },
    ],
    api: { list: staffApi.getAllStaff, create: staffApi.createStaff, update: staffApi.updateStaff, remove: staffApi.deleteStaff },
    toForm: (s) => ({ ...s, staffPassword: '' }),
    toPayload: (f) => {
      const { staffPassword, ...rest } = f;
      const payload = { ...rest, userRoleId: Number(f.userRoleId) };
      return staffPassword ? { ...payload, staffPassword } : payload;
    },
    emptyForm: { staffName: '', userRoleId: '', staffMobile: '', staffEmail: '', photoUrl: '', description: '', staffUsername: '', staffPassword: '' },
  };
}

// Students are only ever created through the Admissions flow (they require
// an enquiry, a course, a batch and a first payment together) - this tab is
// for viewing and editing already-admitted students, not creating new ones.
// canCreate is false, so api.create is never called and isn't provided.
//
// The backend's PUT /api/students/{id} reuses the same StudentRequest DTO
// as registration, which has @NotNull on enquiryId - so toPayload below
// must carry the student's existing enquiryId through unchanged, even
// though it isn't user-editable, or every save would fail validation.
function studentsConfig(courses, batches) {
  const courseOptions = courses.map((c) => ({ value: c.courseId, label: c.courseName }));
  const courseName = (id) => courses.find((c) => c.courseId === id)?.courseName;
  // Batch options aren't filtered live by the course field in this simple
  // form framework, so the course name is included in each label to keep
  // them identifiable regardless of which course is currently selected.
  const batchOptions = batches.map((b) => ({ value: b.batchId, label: `${b.batchName} — ${courseName(b.courseId) || 'Unknown course'}` }));

  return {
    idKey: 'studentId',
    title: 'Students',
    singular: 'Student',
    searchKeys: ['studentName', 'studentMobile', 'studentEmail', 'courseName', 'batchName'],
    filters: [{ key: 'courseId', label: 'Courses', options: courseOptions }],
    columns: [
      { key: 'studentName', label: 'Name' },
      { key: 'studentMobile', label: 'Mobile' },
      { key: 'studentEmail', label: 'Email' },
      { key: 'courseName', label: 'Course' },
      { key: 'batchName', label: 'Batch' },
      { key: 'courseFee', label: 'Course Fee', render: (i) => money(i.courseFee) },
    ],
    fields: [
      { key: 'studentName', label: 'Student name', type: 'text', required: true },
      { key: 'studentMobile', label: 'Mobile', type: 'text', required: true },
      { key: 'studentEmail', label: 'Email', type: 'text' },
      { key: 'studentDob', label: 'Date of birth', type: 'date' },
      { key: 'studentGender', label: 'Gender', type: 'select', options: [{ value: 'Male', label: 'Male' }, { value: 'Female', label: 'Female' }, { value: 'Other', label: 'Other' }] },
      { key: 'studentQualification', label: 'Qualification', type: 'text' },
      { key: 'studentAddress', label: 'Address', type: 'textarea' },
      { key: 'courseId', label: 'Course', type: 'select', required: true, options: courseOptions },
      { key: 'batchId', label: 'Batch', type: 'select', required: true, options: batchOptions },
      { key: 'courseFee', label: 'Course fee (INR)', type: 'number' },
      { key: 'photoUrl', label: 'Photo', type: 'photo' },
    ],
    api: { list: studentsApi.getAllStudents, update: studentsApi.updateStudent },
    canCreate: false, // students are admitted via the Admissions page, not created here
    canDelete: false, // no DELETE endpoint exists for students on the backend
    toForm: (s) => ({ ...s }),
    toPayload: (f) => ({
      ...f,
      enquiryId: f.enquiryId, // required by the backend's shared StudentRequest DTO, not user-editable here
      courseId: Number(f.courseId),
      batchId: Number(f.batchId),
      courseFee: f.courseFee ? Number(f.courseFee) : null,
      studentMobile: f.studentMobile ? Number(f.studentMobile) : null,
    }),
    emptyForm: {},
  };
}

function recruitersConfig() {
  return {
    idKey: 'recruiterId',
    title: 'Recruiters',
    singular: 'Recruiter',
    searchKeys: ['recruiterName'],
    columns: [
      { key: 'recruiterName', label: 'Recruiter' },
      { key: 'description', label: 'Notes' },
    ],
    fields: [
      { key: 'recruiterName', label: 'Recruiter name', type: 'text', required: true },
      { key: 'photoUrl', label: 'Logo', type: 'photo' },
      { key: 'description', label: 'Notes', type: 'textarea' },
    ],
    api: { list: contentApi.getAllRecruiters, create: contentApi.createRecruiter, update: contentApi.updateRecruiter, remove: contentApi.deleteRecruiter },
    toForm: (r) => ({ ...r }),
    toPayload: (f) => f,
    emptyForm: { recruiterName: '', photoUrl: '', description: '' },
  };
}

// This was previously entirely missing from the admin panel, even though
// the backend's PlacementController already supports full CRUD - nothing
// here was ever wired up, which is why the public Placements page always
// showed "No placements published yet" with nothing to add one.
function placementsConfig(batches, recruiters) {
  const batchOptions = batches.map((b) => ({ value: b.batchId, label: `${b.batchName} — ${b.courseName || 'Unknown course'}` }));
  const recruiterOptions = recruiters.map((r) => ({ value: r.recruiterId, label: r.recruiterName }));

  return {
    idKey: 'placedStudentId',
    title: 'Placements',
    singular: 'Placement',
    searchKeys: ['placedStudentName', 'recruiterName', 'batchName'],
    filters: [{ key: 'batchId', label: 'Batches', options: batchOptions }],
    columns: [
      { key: 'placedStudentName', label: 'Student' },
      { key: 'batchName', label: 'Batch' },
      { key: 'recruiterName', label: 'Recruiter' },
      { key: 'placementPackage', label: 'Package', render: (i) => money(i.placementPackage) },
    ],
    fields: [
      { key: 'placedStudentName', label: 'Placed student name', type: 'text', required: true },
      { key: 'batchId', label: 'Batch', type: 'select', required: true, options: batchOptions },
      { key: 'recruiterId', label: 'Recruiter', type: 'select', required: true, options: recruiterOptions },
      { key: 'placementPackage', label: 'Package (INR per annum)', type: 'number' },
    ],
    api: { list: miscApi.getAllPlacements, create: miscApi.createPlacement, update: miscApi.updatePlacement, remove: miscApi.deletePlacement },
    toForm: (p) => ({ ...p }),
    toPayload: (f) => ({
      placedStudentName: f.placedStudentName,
      batchId: Number(f.batchId),
      recruiterId: Number(f.recruiterId),
      placementPackage: f.placementPackage ? Number(f.placementPackage) : null,
    }),
    emptyForm: { placedStudentName: '', batchId: '', recruiterId: '', placementPackage: '' },
  };
}

function albumsConfig() {
  return {
    idKey: 'albumId',
    title: 'Albums',
    singular: 'Album',
    searchKeys: ['albumName'],
    columns: [
      { key: 'albumName', label: 'Album' },
      { key: 'startDate', label: 'Start' },
      { key: 'endDate', label: 'End' },
      { key: 'albumIsActive', label: 'Status' },
    ],
    fields: [
      { key: 'albumName', label: 'Album name', type: 'text', required: true },
      { key: 'albumDescription', label: 'Description', type: 'textarea' },
      { key: 'startDate', label: 'Start date', type: 'date', required: true },
      { key: 'endDate', label: 'End date', type: 'date', required: true },
      { key: 'albumIsActive', label: 'Active (shown on public site)', type: 'checkbox' },
    ],
    api: { list: contentApi.getAllAlbums, create: contentApi.createAlbum, update: contentApi.updateAlbum, remove: contentApi.deleteAlbum },
    toForm: (a) => ({
      ...a,
      startDate: a.startDate ? a.startDate.slice(0, 10) : '',
      endDate: a.endDate ? a.endDate.slice(0, 10) : '',
    }),
    toPayload: (f) => ({
      ...f,
      startDate: f.startDate ? `${f.startDate.slice(0, 10)}T00:00:00` : null,
      endDate: f.endDate ? `${f.endDate.slice(0, 10)}T00:00:00` : null,
    }),
    emptyForm: { albumName: '', albumDescription: '', startDate: '', endDate: '', albumIsActive: true },
  };
}

function imagesConfig(albums) {
  const albumOptions = albums.map((a) => ({ value: a.albumId, label: a.albumName }));
  const albumName = (id) => albums.find((a) => a.albumId === id)?.albumName || '—';
  return {
    idKey: 'imageId',
    title: 'Images',
    singular: 'Image',
    searchKeys: ['imagePath'],
    columns: [
      { key: 'imagePath', label: 'Image URL', render: (i) => <span className="mono" style={{ fontSize: 'var(--text-xs)' }}>{i.imagePath?.slice(0, 40)}</span> },
      { key: 'albumId', label: 'Album', render: (i) => albumName(i.albumId) },
      { key: 'isAlbumCover', label: 'Cover' },
      { key: 'imageIsActive', label: 'Status' },
    ],
    fields: [
      { key: 'imagePath', label: 'Image', type: 'photo', required: true },
      { key: 'albumId', label: 'Album', type: 'select', required: true, options: albumOptions },
      { key: 'isAlbumCover', label: 'Use as album cover', type: 'checkbox' },
      { key: 'imageIsActive', label: 'Active (shown on public site)', type: 'checkbox' },
    ],
    api: { list: contentApi.getAllImages, create: contentApi.createImage, update: contentApi.updateImage, remove: contentApi.deleteImage },
    toForm: (i) => ({ ...i }),
    toPayload: (f) => ({ ...f, albumId: Number(f.albumId) }),
    emptyForm: { imagePath: '', albumId: '', isAlbumCover: false, imageIsActive: true },
  };
}

function announcementsConfig() {
  return {
    idKey: 'announcementId',
    title: 'Announcements',
    singular: 'Announcement',
    searchKeys: ['title'],
    columns: [
      { key: 'title', label: 'Title' },
      { key: 'publishDate', label: 'Publish' },
      { key: 'expiryDate', label: 'Expires' },
    ],
    fields: [
      { key: 'title', label: 'Title', type: 'text', required: true },
      { key: 'description', label: 'Description', type: 'textarea' },
      { key: 'publishDate', label: 'Publish date', type: 'date' },
      { key: 'expiryDate', label: 'Expiry date', type: 'date' },
    ],
    api: { list: contentApi.getAllAnnouncements, create: contentApi.createAnnouncement, update: contentApi.updateAnnouncement, remove: contentApi.deleteAnnouncement },
    toForm: (a) => ({ ...a }),
    toPayload: (f) => f,
    emptyForm: { title: '', description: '', publishDate: '', expiryDate: '' },
  };
}

// Read-only inbox: submitted messages are never edited by staff, only
// read and deleted once handled - so canCreate/canUpdate are both off.
// GET /contacts (and DELETE) already require a valid staff JWT under the
// current SecurityConfig - only POST /contacts (the public submission
// itself) is in the permitAll allowlist, so this tab is already properly
// admin-only without any security change needed.
function contactMessagesConfig() {
  return {
    idKey: 'contactId',
    title: 'Contact Messages',
    singular: 'Message',
    searchKeys: ['name', 'email', 'message'],
    columns: [
      { key: 'name', label: 'Name' },
      { key: 'email', label: 'Email' },
      { key: 'message', label: 'Message', render: (i) => (i.message?.length > 80 ? `${i.message.slice(0, 80)}…` : i.message) },
    ],
    fields: [],
    api: { list: contentApi.getAllContacts, remove: contentApi.deleteContact },
    canCreate: false,
    canUpdate: false,
    toForm: (c) => ({ ...c }),
    toPayload: (f) => f,
    emptyForm: {},
  };
}

function closureReasonsConfig() {
  return {
    idKey: 'closureReasonId',
    title: 'Closure Reasons',
    singular: 'Closure reason',
    searchKeys: ['closureReasonDesc'],
    columns: [{ key: 'closureReasonDesc', label: 'Reason' }],
    fields: [{ key: 'closureReasonDesc', label: 'Reason', type: 'text', required: true }],
    api: { list: enquiriesApi.getAllClosureReasons, create: enquiriesApi.createClosureReason, remove: enquiriesApi.deleteClosureReason },
    canUpdate: false, // no PUT endpoint on the backend for this resource
    toForm: (r) => ({ ...r }),
    toPayload: (f) => f,
    emptyForm: { closureReasonDesc: '' },
  };
}