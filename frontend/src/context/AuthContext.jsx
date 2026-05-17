import React, { createContext, useContext, useState, useCallback } from 'react';
import { authApi } from '../services/api';

const AuthContext = createContext(null);

/**
 * AuthProvider — JWT-based authentication context.
 *
 * Provides:
 *   user        — current user object (null when logged out)
 *   loading     — true while an auth request is in-flight
 *   error       — last error message (empty string when none)
 *   isLoggedIn  — boolean shorthand for !!user
 *   register()  — register with username/email/password → stores JWT
 *   login()     — login with email/password → stores JWT
 *   logout()    — clears token and user from storage
 *
 * Token persistence: JWT is stored in localStorage under 'qma_token'.
 * On page reload the token is re-read from storage; the user object is
 * re-read from 'qma_user' so the UI can render immediately without an
 * extra /me round-trip.
 */
export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try { return JSON.parse(localStorage.getItem('qma_user')); }
    catch { return null; }
  });
  const [loading, setLoading] = useState(false);
  const [error,   setError]   = useState('');

  // ── Friendly error helper ──────────────────────────────────────────────────
  const friendlyError = (e, fallback) => {
    const status = e.response?.status;
    // 503 = API Gateway can't find the service in Eureka (service not started yet)
    if (status === 503) {
      return 'Auth service is starting up. Please wait a few seconds and try again.';
    }
    // 0 / network error = backend is completely unreachable
    if (!e.response) {
      return 'Cannot reach the server. Make sure all services are running.';
    }
    // Extract message from either { message } or { error } in the response body
    const body = e.response?.data;
    return body?.message || body?.error || fallback;
  };

  // ── Register ───────────────────────────────────────────────────────────────

  const register = useCallback(async (username, email, password) => {
    setLoading(true); setError('');
    try {
      const { data } = await authApi.register({ username, email, password });
      if (data.success) {
        localStorage.setItem('qma_token', data.token);
        localStorage.setItem('qma_user',  JSON.stringify(data.user));
        setUser(data.user);
        return { success: true };
      }
      setError(data.message);
      return { success: false, message: data.message };
    } catch (e) {
      const msg = friendlyError(e, 'Registration failed. Please try again.');
      setError(msg);
      return { success: false, message: msg };
    } finally {
      setLoading(false);
    }
  }, []);

  // ── Login ─────────────────────────────────────────────────────────────────

  const login = useCallback(async (email, password) => {
    setLoading(true); setError('');
    try {
      const { data } = await authApi.login({ email, password });
      if (data.success) {
        localStorage.setItem('qma_token', data.token);
        localStorage.setItem('qma_user',  JSON.stringify(data.user));
        setUser(data.user);
        return { success: true };
      }
      setError(data.message);
      return { success: false, message: data.message };
    } catch (e) {
      const msg = friendlyError(e, 'Login failed. Check your credentials.');
      setError(msg);
      return { success: false, message: msg };
    } finally {
      setLoading(false);
    }
  }, []);

  // ── Logout ────────────────────────────────────────────────────────────────

  const logout = useCallback(() => {
    localStorage.removeItem('qma_token');
    localStorage.removeItem('qma_user');
    setUser(null);
    setError('');
  }, []);

  return (
    <AuthContext.Provider value={{
      user, loading, error, isLoggedIn: !!user,
      register, login, logout,
    }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
export default AuthContext;
