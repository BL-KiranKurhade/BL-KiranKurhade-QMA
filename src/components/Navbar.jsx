import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box, Chip } from '@mui/material';
import ScaleIcon from '@mui/icons-material/Scale';
import LogoutIcon from '@mui/icons-material/Logout';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { user, logout, isLoggedIn } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => { logout(); navigate('/login'); };

  return (
    <AppBar position="sticky" elevation={2}>
      <Toolbar>
        <ScaleIcon sx={{ mr: 1 }} />
        <Typography variant="h6" fontWeight={700} sx={{ flexGrow: 1, cursor: 'pointer' }}
          onClick={() => navigate(isLoggedIn ? '/dashboard' : '/')}>
          QMA — Quantity Measurement App
        </Typography>
        {isLoggedIn ? (
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
            <Chip label={user?.username} color="default" sx={{ bgcolor: 'rgba(255,255,255,0.2)', color: 'white' }} />
            <Button color="inherit" startIcon={<LogoutIcon />} onClick={handleLogout}>Logout</Button>
          </Box>
        ) : (
          <Box sx={{ display: 'flex', gap: 1 }}>
            <Button color="inherit" onClick={() => navigate('/login')}>Login</Button>
            <Button color="inherit" variant="outlined" onClick={() => navigate('/register')}>Register</Button>
          </Box>
        )}
      </Toolbar>
    </AppBar>
  );
}
