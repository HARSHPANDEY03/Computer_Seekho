import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ArchDefs } from './components/ui/ui';
import PayFees from './pages/public/PayFees';
import PublicLayout from './components/layout/PublicLayout';
import AdminLayout, { RequireAuth } from './components/layout/AdminLayout';

import Home from './pages/public/Home';
import Programs from './pages/public/Programs';
import ProgramDetail from './pages/public/ProgramDetail';
import About from './pages/public/About';
import Campus from './pages/public/Campus';
import Placements from './pages/public/Placements';
import Recruiters from './pages/public/Recruiters';
import Enquiry from './pages/public/Enquiry';
import Contact from './pages/public/Contact';

import Login from './pages/admin/Login';
import Dashboard from './pages/admin/Dashboard';
import Enquiries from './pages/admin/Enquiries';
import EnquiryForm from './pages/admin/EnquiryForm';
import Admissions from './pages/admin/Admissions';
import Receipt from './pages/admin/Receipt';
import Content from './pages/admin/Content';
import ExcelUpload from './pages/admin/ExcelUpload';

function NotFound() {
  return (
    <div className="container section" style={{ textAlign: 'center' }}>
      <p className="eyebrow">404</p>
      <h2 style={{ marginTop: 8 }}>This page doesn't exist.</h2>
      <p className="body-text" style={{ marginTop: 8 }}>Check the address, or use the navigation to find your way back.</p>
    </div>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <ArchDefs />
      <AuthProvider>
        <Routes>
          <Route element={<PublicLayout />}>
            <Route path="/" element={<Home />} />
            <Route path="/about" element={<About />} />
            <Route path="/programs" element={<Programs />} />
            <Route path="/programs/:id" element={<ProgramDetail />} />
            <Route path="/campus" element={<Campus />} />
            <Route path="/placements" element={<Placements />} />
            <Route path="/recruiters" element={<Recruiters />} />
            <Route path="/enquiry" element={<Enquiry />} />
            <Route path="/contact" element={<Contact />} />
            <Route path="/pay-fees" element={<PayFees />} />
          </Route>

          <Route path="/admin/login" element={<Login />} />

          <Route
            path="/admin"
            element={
              <RequireAuth>
                <AdminLayout />
              </RequireAuth>
            }
          >
            <Route path="dashboard" element={<Dashboard />} />
            <Route path="enquiries" element={<Enquiries />} />
            <Route path="enquiries/new" element={<EnquiryForm />} />
            <Route path="enquiries/:id" element={<EnquiryForm />} />
            <Route path="admissions" element={<Admissions />} />
            <Route path="admissions/:enquiryId" element={<Admissions />} />
            <Route path="receipt/:receiptId" element={<Receipt />} />
            <Route path="content" element={<Content />} />
            <Route path="excel-upload" element={<ExcelUpload />} />
          </Route>

          <Route path="*" element={<NotFound />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
