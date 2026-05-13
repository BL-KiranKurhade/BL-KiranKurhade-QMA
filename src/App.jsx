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
    mode: 'dark',
    primary:    { main: '#a855f7' },
    secondary:  { main: '#ec4899' },
    background: {
      default: '#0a0a1a',
      paper:   '#13132b',
    },
    text: {
      primary:   '#ffffff',
      secondary: '#9ca3af',
    },
  },
  typography: {
    fontFamily: "'Inter', 'Roboto', sans-serif",
  },
  shape: { borderRadius: 16 },
  components: {
    MuiButton: {
      styleOverrides: {
        root: { textTransform: 'none', fontWeight: 600 },
      },
    },
    MuiSelect: {
      styleOverrides: {
        root: { borderRadius: 12 },
      },
    },
    MuiOutlinedInput: {
      styleOverrides: {
        root: {
          borderRadius: 12,
          '& fieldset': { borderColor: 'rgba(168,85,247,0.3)' },
          '&:hover fieldset': { borderColor: 'rgba(168,85,247,0.6)' },
          '&.Mui-focused fieldset': { borderColor: '#a855f7' },
        },
      },
    },
  },
});

export default function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <BrowserRouter>
          <Box sx={{
            minHeight: '100vh',
            background: 'linear-gradient(135deg, #0a0a1a 0%, #130d2e 50%, #0a0a1a 100%)',
          }}>
            <Navbar />
            <Routes>
              <Route path="/"          element={<Navigate to="/dashboard" replace />} />
              <Route path="/login"     element={<LoginPage />} />
              <Route path="/register"  element={<RegisterPage />} />
              <Route path="/dashboard" element={
                <ProtectedRoute><DashboardPage /></ProtectedRoute>
              } />
              <Route path="*" element={<Navigate to="/login" replace />} />
            </Routes>
          </Box>
        </BrowserRouter>
      </AuthProvider>
    </ThemeProvider>
  );
}
