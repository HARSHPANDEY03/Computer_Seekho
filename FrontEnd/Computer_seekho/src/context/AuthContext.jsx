import { createContext, useContext, useState, useCallback } from 'react';
import * as authApi from '../api/auth';
import { getToken, getStoredStaff, setSession, clearSession } from '../api/client';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [staff, setStaff] = useState(getStoredStaff());
  const [token, setToken] = useState(getToken());

  const signIn = useCallback(async (username, password) => {
    const res = await authApi.login(username, password);
    const staffInfo = {
      staffId: res.staffId,
      staffName: res.staffName,
      staffUsername: res.staffUsername,
      staffEmail: res.staffEmail,
      staffRole: res.staffRole,
    };
    setSession(res.accessToken, staffInfo);
    setToken(res.accessToken);
    setStaff(staffInfo);
    return staffInfo;
  }, []);

  const signOut = useCallback(() => {
    clearSession();
    setToken(null);
    setStaff(null);
  }, []);

  const value = { staff, token, isAuthenticated: Boolean(token && staff), signIn, signOut };
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
