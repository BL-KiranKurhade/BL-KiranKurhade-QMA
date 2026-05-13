import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box, Avatar } from '@mui/material';
import AutoFixHighIcon from '@mui/icons-material/AutoFixHigh';
import LogoutIcon      from '@mui/icons-material/Logout';
import { useNavigate } from 'react-router-dom';
import { useAuth }     from '../context/AuthContext';

export default function Navbar() {
  const { user, logout, isLoggedIn } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => { logout(); navigate('/login'); };

  return (
    <AppBar
      position="sticky"
      elevation={0}
      sx={{
        background: 'rgba(10,10,26,0.8)',
        backdropFilter: 'blur(12px)',
        borderBottom: '1px solid rgba(168,85,247,0.15)',
      }}
    >
      <Toolbar>
        {/* Logo */}
        <Box
          sx={{ display: 'flex', alignItems: 'center', gap: 1, cursor: 'pointer', flexGrow: 1 }}
          onClick={() => navigate(isLoggedIn ? '/dashboard' : '/')}
        >
          <Box sx={{
            width: 32, height: 32, borderRadius: 2,
            background: 'linear-gradient(135deg, #a855f7, #ec4899)',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
          }}>
            <AutoFixHighIcon sx={{ color: '#fff', fontSize: 18 }} />
          </Box>
          <Typography
            variant="h6" fontWeight={800}
            sx={{
              background: 'linear-gradient(90deg, #a855f7, #ec4899)',
              WebkitBackgroundClip: 'text',
              WebkitTextFillColor: 'transparent',
            }}
          >
            QMA
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ ml: 0.5, display: { xs: 'none', sm: 'block' } }}>
            Quantity Measurement App
          </Typography>
        </Box>

        {isLoggedIn ? (
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
              <Avatar sx={{
                width: 32, height: 32, fontSize: '0.8rem', fontWeight: 700,
                background: 'linear-gradient(135deg, #a855f7, #ec4899)',
              }}>
                {(user?.username || 'U')[0].toUpperCase()}
              </Avatar>
              <Typography variant="body2" color="text.secondary"
                sx={{ display: { xs: 'none', sm: 'block' } }}>
                {user?.username}
              </Typography>
            </Box>
            <Button
              startIcon={<LogoutIcon />}
              onClick={handleLogout}
              size="small"
              sx={{
                color: '#9ca3af', border: '1px solid rgba(168,85,247,0.3)',
                borderRadius: 2, px: 2,
                '&:hover': { color: '#ec4899', borderColor: '#ec4899',
                             background: 'rgba(236,72,153,0.08)' },
              }}
            >
              Logout
            </Button>
          </Box>
        ) : (
          <Box sx={{ display: 'flex', gap: 1 }}>
            <Button
              onClick={() => navigate('/login')}
              sx={{ color: '#9ca3af', '&:hover': { color: '#a855f7' } }}
            >
              Login
            </Button>
            <Button
              onClick={() => navigate('/register')}
              sx={{
                background: 'linear-gradient(135deg, #a855f7, #ec4899)',
                color: '#fff', borderRadius: 2, px: 2,
                '&:hover': { background: 'linear-gradient(135deg, #9333ea, #db2777)' },
              }}
            >
              Register
            </Button>
          </Box>
        )}
      </Toolbar>
    </AppBar>
  );
}
