import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme, CssBaseline, Box } from '@mui/material';
import { AuthProvider }  from './context/AuthContext';
import Navbar            from './components/Navbar';
import ProtectedRoute    from './components/ProtectedRoute';
import LoginPage         from './pages/LoginPage';
import RegisterPage      from './pages/RegisterPage';
import DashboardPage     from './pages/DashboardPage';

const theme = createTheme({
  palette: {
    primary:    { main: '#1976d2' },
    secondary:  { main: '#9c27b0' },
    background: { default: '#f5f7fa' },
  },
  typography: { fontFamily: 'Roboto, sans-serif' },
  shape: { borderRadius: 8 },
});

export default function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <BrowserRouter>
          <Box sx={{ minHeight: '100vh', bgcolor: 'background.default' }}>
            <Navbar />
            <Routes>
              <Route path="/"          element={<Navigate to="/dashboard" replace />} />
              <Route path="/login"     element={<LoginPage />} />
              <Route path="/register"  element={<RegisterPage />} />
              <Route path="/dashboard" element={
                <ProtectedRoute><DashboardPage /></ProtectedRoute>
              } />
              {/* Catch-all — redirect to login */}
              <Route path="*" element={<Navigate to="/login" replace />} />
            </Routes>
          </Box>
        </BrowserRouter>
      </AuthProvider>
    </ThemeProvider>
  );
}
