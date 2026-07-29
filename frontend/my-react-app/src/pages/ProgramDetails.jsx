import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getCourseById } from '../services/courseService';
import { getActiveBatchesByCourse } from '../services/batchService';
import './ProgramDetails.css';

const ProgramDetails = () => {
  const { courseId } = useParams();
  const navigate = useNavigate();

  const [course, setCourse] = useState(null);
  const [batches, setBatches] = useState([]);
  const [activeTab, setActiveTab] = useState('overview');
  const [loading, setLoading] = useState(true);

  // Payment Modal States
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const [selectedPaymentMethod, setSelectedPaymentMethod] = useState('upi');
  const [paymentSuccess, setPaymentSuccess] = useState(false);

  useEffect(() => {
    setLoading(true);

    Promise.all([getCourseById(courseId), getActiveBatchesByCourse(courseId)])
      .then(([courseData, batchesData]) => {
        setCourse(courseData);
        setBatches(batchesData);
        setLoading(false);
      })
      .catch((err) => {
        console.error('Error fetching database records:', err);
        setLoading(false);
      });
  }, [courseId]);

  const formatTime = (timeString) => {
    if (!timeString) return '';
    const [hours, minutes] = timeString.split(':');
    const date = new Date();
    date.setHours(parseInt(hours, 10));
    date.setMinutes(parseInt(minutes, 10));
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: true });
  };

  const handlePayNow = (e) => {
    e.preventDefault();
    setPaymentSuccess(true);
    setTimeout(() => {
      setPaymentSuccess(false);
      setShowPaymentModal(false);
      alert(`Payment Successful for ${course.courseName}! Receipt sent to your email.`);
    }, 1500);
  };

  if (loading) return <div className="status-msg">Loading program details from database...</div>;
  if (!course) return <div className="status-msg">Program not found.</div>;

  const syllabusHighlights = course.courseSyllabus
    ? course.courseSyllabus.split(',').map((item) => item.trim())
    : ['Programming', 'Database', 'Project'];

  return (
    <div className="program-detail-page">
      <div className="breadcrumb">
        Home / Programs / {course.courseName}
      </div>

      <div className="program-header">
        <div className="header-left">
          <span className="badge-active">
            {course.courseIsActive ? 'Active program' : 'Inactive'}
          </span>
          <h1>{course.courseName}</h1>
          <p className="description">{course.courseDescription}</p>

          <div className="stats-row">
            <div className="stat-item">
              <h3>{course.courseDuration ? `${course.courseDuration} days` : '6 months'}</h3>
              <span>Duration</span>
            </div>
            <div className="stat-item">
              <h3>INR {course.courseFees ? course.courseFees.toLocaleString('en-IN') : 'N/A'}</h3>
              <span>Current fee</span>
            </div>
            <div className="stat-item">
              <h3>{course.ageGrpType || 'Graduate'}</h3>
              <span>Age / eligibility group</span>
            </div>
            <div className="stat-item">
              <h3>Certificate</h3>
              <span>Completion</span>
            </div>
          </div>
        </div>

        <div className="header-right">
          <div className="cover-photo-box">
            {course.coverPhoto ? (
              <img src={course.coverPhoto} alt={course.courseName} />
            ) : (
              <span>Program cover photo</span>
            )}
          </div>
        </div>
      </div>

      <div className="program-body-grid">
        <div className="content-left">
          <div className="tabs-header">
            {['overview', 'syllabus', 'fees', 'batches', 'faqs'].map((tab) => (
              <button
                key={tab}
                className={activeTab === tab ? 'tab-btn active' : 'tab-btn'}
                onClick={() => setActiveTab(tab)}
              >
                {tab === 'faqs' ? 'FAQS' : tab === 'batches' ? 'Upcoming Batches' : tab.charAt(0).toUpperCase() + tab.slice(1)}
              </button>
            ))}
          </div>

          <div className="tab-content">
            {activeTab === 'overview' && (
              <div className="overview-section">
                <h3>Program overview</h3>
                <p>{course.courseDescription}</p>

                <h4>Syllabus highlights</h4>
                <div className="highlights-grid">
                  {syllabusHighlights.map((topic, index) => (
                    <div key={index} className="highlight-card">
                      <h5>{topic}</h5>
                      <p>Core practical modules and hands-on coverage for {topic}.</p>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {activeTab === 'syllabus' && (
              <div>
                <h3>Full Syllabus</h3>
                <p>{course.courseSyllabus}</p>
              </div>
            )}

            {activeTab === 'fees' && (
              <div>
                <h3>Fee Structure</h3>
                <p><strong>Fee:</strong> INR {course.courseFees ? course.courseFees.toLocaleString('en-IN') : 'N/A'}</p>
                <p><strong>Valid From:</strong> {course.courseFeesFrom || 'N/A'}</p>
                <p><strong>Valid To:</strong> {course.courseFeesTo || 'N/A'}</p>
              </div>
            )}

            {activeTab === 'batches' && (
              <div>
                <h3>Upcoming Batches & Timings</h3>
                {batches.length > 0 ? (
                  <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '16px' }}>
                    <thead>
                      <tr style={{ backgroundColor: '#f1f5f9', textAlign: 'left' }}>
                        <th style={{ padding: '12px' }}>Batch Name</th>
                        <th style={{ padding: '12px' }}>Start Time</th>
                        <th style={{ padding: '12px' }}>End Time</th>
                        <th style={{ padding: '12px' }}>Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      {batches.map((b) => (
                        <tr key={b.batchId} style={{ borderBottom: '1px solid #e2e8f0' }}>
                          <td style={{ padding: '12px', fontWeight: '600' }}>{b.batchName}</td>
                          <td style={{ padding: '12px' }}>{formatTime(b.batchStartTime)}</td>
                          <td style={{ padding: '12px' }}>{formatTime(b.batchEndTime)}</td>
                          <td style={{ padding: '12px', color: '#16a34a', fontWeight: 'bold' }}>
                            {b.batchIsActive ? 'Active' : 'Inactive'}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                ) : (
                  <p>No active batches available currently for this course.</p>
                )}
              </div>
            )}

            {activeTab === 'faqs' && (
              <div>
                <h3>Frequently Asked Questions</h3>
                <p><strong>Q: Who is eligible for {course.courseName}?</strong></p>
                <p>A: Candidates meeting the {course.ageGrpType || 'Graduate'} requirement are eligible to apply.</p>
              </div>
            )}
          </div>
        </div>

        {/* Sidebar with Pay Now & Enquiry Buttons */}
        <div className="content-right">
          <div className="batches-card">
            <h3>Upcoming batches</h3>
            <ul className="batch-list">
              {batches.length > 0 ? (
                batches.map((batch) => (
                  <li key={batch.batchId} className="batch-item">
                    <div className="batch-info">
                      <span className="batch-name">{batch.batchName}</span>
                      <small style={{ color: '#64748b', fontSize: '12px' }}>
                        {formatTime(batch.batchStartTime)} - {formatTime(batch.batchEndTime)}
                      </small>
                    </div>
                    <span className="batch-status-tag">Active</span>
                  </li>
                ))
              ) : (
                <li className="no-batches" style={{ padding: '12px 0', color: '#64748b' }}>
                  No active batches available.
                </li>
              )}
            </ul>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
              <button
                className="btn-pay-now"
                onClick={() => setShowPaymentModal(true)}
              >
                💳 Pay Now (INR {course.courseFees ? course.courseFees.toLocaleString('en-IN') : ''})
              </button>

              <button
                className="btn-enquire-primary"
                onClick={() => navigate(`/contact?courseId=${course.courseId}`)}
              >
                Enquire for this program
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Payment Gateway Modal */}
      {showPaymentModal && (
        <div className="modal-overlay">
          <div className="modal-card">
            <div className="modal-header">
              <h3>Secure Checkout - Computer Seekho</h3>
              <button className="close-btn" onClick={() => setShowPaymentModal(false)}>✕</button>
            </div>

            <div className="modal-body">
              <div className="summary-box">
                <p><strong>Program:</strong> {course.courseName}</p>
                <p><strong>Total Amount:</strong> INR {course.courseFees ? course.courseFees.toLocaleString('en-IN') : '0'}</p>
              </div>

              <h4>Select Payment Method</h4>
              <div className="payment-options">
                <label className={selectedPaymentMethod === 'upi' ? 'active-opt' : ''}>
                  <input
                    type="radio"
                    name="payment"
                    value="upi"
                    checked={selectedPaymentMethod === 'upi'}
                    onChange={() => setSelectedPaymentMethod('upi')}
                  />
                  UPI (GPay, PhonePe, Paytm)
                </label>
                <label className={selectedPaymentMethod === 'card' ? 'active-opt' : ''}>
                  <input
                    type="radio"
                    name="payment"
                    value="card"
                    checked={selectedPaymentMethod === 'card'}
                    onChange={() => setSelectedPaymentMethod('card')}
                  />
                  Credit / Debit Card
                </label>
                <label className={selectedPaymentMethod === 'netbanking' ? 'active-opt' : ''}>
                  <input
                    type="radio"
                    name="payment"
                    value="netbanking"
                    checked={selectedPaymentMethod === 'netbanking'}
                    onChange={() => setSelectedPaymentMethod('netbanking')}
                  />
                  Net Banking
                </label>
              </div>

              <form onSubmit={handlePayNow} style={{ marginTop: '20px' }}>
                {selectedPaymentMethod === 'upi' && (
                  <input type="text" placeholder="Enter UPI ID (e.g. name@upi)" required className="pay-input" />
                )}
                {selectedPaymentMethod === 'card' && (
                  <div>
                    <input type="text" placeholder="Card Number" required className="pay-input" />
                    <div style={{ display: 'flex', gap: '10px' }}>
                      <input type="text" placeholder="MM/YY" required className="pay-input" />
                      <input type="password" placeholder="CVV" required className="pay-input" />
                    </div>
                  </div>
                )}
                {selectedPaymentMethod === 'netbanking' && (
                  <select required className="pay-input">
                    <option value="">Choose Bank</option>
                    <option value="sbi">State Bank of India</option>
                    <option value="hdfc">HDFC Bank</option>
                    <option value="icici">ICICI Bank</option>
                  </select>
                )}

                <button type="submit" className="btn-confirm-payment">
                  {paymentSuccess ? 'Processing Payment...' : `Pay INR ${course.courseFees ? course.courseFees.toLocaleString('en-IN') : ''}`}
                </button>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProgramDetails;