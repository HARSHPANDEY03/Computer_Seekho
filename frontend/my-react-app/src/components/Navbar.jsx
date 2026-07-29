import React, { useState, useRef, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getActiveCourses } from '../services/courseService';
import './Navbar.css';

const Navbar = () => {
  const [courses, setCourses] = useState([]);
  const [isMegaMenuOpen, setIsMegaMenuOpen] = useState(false);
  const navigate = useNavigate();
  const timeoutRef = useRef(null);

  useEffect(() => {
    getActiveCourses()
      .then((data) => setCourses(data))
      .catch((err) => console.error('Error fetching active courses:', err));
  }, []);

  const handleMouseEnter = () => {
    if (timeoutRef.current) clearTimeout(timeoutRef.current);
    setIsMegaMenuOpen(true);
  };

  const handleMouseLeave = () => {
    timeoutRef.current = setTimeout(() => {
      setIsMegaMenuOpen(false);
    }, 250);
  };

  const categoryOrder = ['PG DIPLOMAS', 'CERTIFICATIONS', 'SHORT-TERM'];

  const groupedCourses = {
    'PG DIPLOMAS': [],
    'CERTIFICATIONS': [],
    'SHORT-TERM': []
  };

  const seenCourseNames = new Set();

  courses.forEach((course) => {
    const rawCategory = (course.courseCategory || '').toUpperCase();
    const rawName = (course.courseName || '').trim();
    const upperName = rawName.toUpperCase();

    // ❌ Explicitly EXCLUDE "PG-DAC Advanced" or any variations with "ADVANCED" in the name
    if (upperName.includes('ADVANCED')) return;

    // Skip duplicate names
    if (seenCourseNames.has(upperName)) return;
    seenCourseNames.add(upperName);

    // 1. Group PG Diplomas strictly (PG-DAC, PG-DBDA, PG-DITSS)
    if (
      upperName.includes('PG') ||
      upperName.includes('DAC') ||
      upperName.includes('DBDA') ||
      upperName.includes('DITSS') ||
      rawCategory.includes('PG') ||
      rawCategory.includes('DIPLOMA')
    ) {
      groupedCourses['PG DIPLOMAS'].push(course);
    } 
    // 2. Short-Term Courses
    else if (
      upperName.includes('MS-CIT') ||
      upperName.includes('TALLY') ||
      rawCategory.includes('SHORT')
    ) {
      groupedCourses['SHORT-TERM'].push(course);
    } 
    // 3. Certifications
    else {
      groupedCourses['CERTIFICATIONS'].push(course);
    }
  });

  const featuredCourse = courses.find((c) => c.isFeatured) || courses[0];

  return (
    <header className="site-header">
      <div className="announcement-bar">
        Admissions open for upcoming batches - limited seats available
      </div>

      <div className="main-nav">
        <div className="logo" onClick={() => navigate('/')}>
          <span className="cs-box">CS</span>
          <span className="brand-name">COMPUTER SEEKHO</span>
        </div>

        <nav className="nav-links">
          <Link to="/">Home</Link>
          <Link to="/about">About</Link>

          <div
            className="dropdown-container"
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
          >
            <button
              className="nav-btn active"
              onClick={() => setIsMegaMenuOpen(!isMegaMenuOpen)}
            >
              Programs ▾
            </button>

            {isMegaMenuOpen && (
              <div className="mega-menu">
                <div className="mega-menu-grid">
                  {categoryOrder.map((category) => (
                    <div key={category} className="category-column">
                      <h4>{category}</h4>
                      <ul>
                        {groupedCourses[category].length > 0 ? (
                          groupedCourses[category].map((course) => (
                            <li key={course.courseId}>
                              <Link
                                to={`/program/${course.courseId}`}
                                onClick={() => setIsMegaMenuOpen(false)}
                              >
                                {course.courseName} <span className="arrow">&gt;</span>
                              </Link>
                            </li>
                          ))
                        ) : (
                          <li style={{ color: '#94a3b8', fontSize: '13px' }}>
                            No courses available
                          </li>
                        )}
                      </ul>
                    </div>
                  ))}

                  {featuredCourse && (
                    <div className="featured-card">
                      <div className="featured-img-placeholder">
                        {featuredCourse.coverPhoto ? (
                          <img src={featuredCourse.coverPhoto} alt={featuredCourse.courseName} />
                        ) : (
                          <span>Featured program image</span>
                        )}
                      </div>
                      <h5>Upcoming batch</h5>
                      <p><strong>{featuredCourse.courseName}</strong></p>
                    </div>
                  )}
                </div>

                <div className="mega-menu-footer">
                  <small>Only active courses are shown. Course grouping uses database category/age group fields.</small>
                </div>
              </div>
            )}
          </div>

          <Link to="/campus">Campus</Link>
          <Link to="/placements">Placements</Link>
          <Link to="/contact">Contact</Link>
        </nav>

        <button className="btn-enquire" onClick={() => navigate('/contact')}>
          Enquire Now
        </button>
      </div>
    </header>
  );
};

export default Navbar;