import React, { useState, useEffect } from 'react';
import {
  Container, Box, Typography, Button, MenuItem, Select,
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
  Chip, CircularProgress, Paper, Tooltip, Snackbar, Alert,
} from '@mui/material';
import StraightenIcon        from '@mui/icons-material/Straighten';
import ScaleIcon             from '@mui/icons-material/Scale';
import DeviceThermostatIcon  from '@mui/icons-material/DeviceThermostat';
import OpacityIcon           from '@mui/icons-material/Opacity';
import SwapHorizIcon         from '@mui/icons-material/SwapHoriz';
import ContentCopyIcon       from '@mui/icons-material/ContentCopy';
import HistoryIcon           from '@mui/icons-material/History';
import RefreshIcon           from '@mui/icons-material/Refresh';
import { conversionApi }     from '../services/api';
import { useAuth }           from '../context/AuthContext';

// ── Unit configuration ─────────────────────────────────────────────────────────
const CATEGORIES = [
  { id: 'LENGTH',      label: 'Length',  icon: <StraightenIcon /> },
  { id: 'WEIGHT',      label: 'Weight',  icon: <ScaleIcon /> },
  { id: 'TEMPERATURE', label: 'Temp',    icon: <DeviceThermostatIcon /> },
  { id: 'VOLUME',      label: 'Volume',  icon: <OpacityIcon /> },
];

const UNIT_CONFIG = {
  LENGTH: [
    { value: 'mm',  label: 'Millimetres' },
    { value: 'cm',  label: 'Centimetres' },
    { value: 'in',  label: 'Inches' },
    { value: 'ft',  label: 'Feet' },
    { value: 'yd',  label: 'Yards' },
    { value: 'm',   label: 'Metres' },
    { value: 'km',  label: 'Kilometres' },
    { value: 'mi',  label: 'Miles' },
  ],
  WEIGHT: [
    { value: 'mg',  label: 'Milligrams' },
    { value: 'g',   label: 'Grams' },
    { value: 'oz',  label: 'Ounces' },
    { value: 'lb',  label: 'Pounds' },
    { value: 'kg',  label: 'Kilograms' },
    { value: 't',   label: 'Metric Tons' },
  ],
  VOLUME: [
    { value: 'ml',    label: 'Millilitres' },
    { value: 'l',     label: 'Litres' },
    { value: 'cup',   label: 'Cups' },
    { value: 'gal',   label: 'Gallons' },
    { value: 'fl_oz', label: 'Fluid Ounces' },
    { value: 'tsp',   label: 'Teaspoons' },
    { value: 'tbsp',  label: 'Tablespoons' },
  ],
  TEMPERATURE: [
    { value: 'C', label: 'Celsius' },
    { value: 'F', label: 'Fahrenheit' },
    { value: 'K', label: 'Kelvin' },
  ],
};

const MODE_TABS = ['Conversion', 'Comparison', 'Arithmetic'];

// ── Styles ─────────────────────────────────────────────────────────────────────
const card = {
  background: 'rgba(255,255,255,0.04)',
  border: '1px solid rgba(168,85,247,0.15)',
  borderRadius: 4,
  backdropFilter: 'blur(12px)',
};

const inputPanel = {
  background: 'rgba(0,0,0,0.35)',
  border: '1px solid rgba(168,85,247,0.2)',
  borderRadius: 3,
  p: 3,
  flex: 1,
};

const styledSelect = {
  background: 'transparent',
  color: '#9ca3af',
  fontSize: '0.85rem',
  '& .MuiOutlinedInput-notchedOutline': { border: 'none' },
  '& .MuiSvgIcon-root': { color: '#9ca3af' },
};

// ── Dashboard ──────────────────────────────────────────────────────────────────
export default function DashboardPage() {
  const { user } = useAuth();

  const [category, setCategory] = useState('LENGTH');
  const [modeTab,  setModeTab]  = useState('Conversion');
  const [fromUnit, setFromUnit] = useState('km');
  const [toUnit,   setToUnit]   = useState('mi');
  const [inputVal, setInputVal] = useState('');
  const [result,   setResult]   = useState(null);
  const [convError, setConvError] = useState('');
  const [copied,   setCopied]   = useState(false);

  const [history,  setHistory]  = useState([]);
  const [loadingH, setLoadingH] = useState(false);

  useEffect(() => { fetchHistory(); }, []);

  // Reset units when category changes
  useEffect(() => {
    const units = UNIT_CONFIG[category] || [];
    setFromUnit(units[0]?.value ?? '');
    setToUnit(units[1]?.value ?? units[0]?.value ?? '');
    setResult(null);
    setConvError('');
  }, [category]);

  const fetchHistory = async () => {
    setLoadingH(true);
    try {
      const { data } = await conversionApi.getHistory();
      setHistory(Array.isArray(data) ? data : []);
    } catch { /* silent */ }
    finally { setLoadingH(false); }
  };

  const handleConvert = async (e) => {
    e?.preventDefault();
    setConvError('');
    if (!inputVal || isNaN(+inputVal)) { setConvError('Enter a valid number'); return; }
    try {
      const { data } = await conversionApi.convert(inputVal, fromUnit, toUnit, category);
      setResult(data);
      fetchHistory();
    } catch (err) {
      setConvError(err.response?.data?.message || 'Conversion failed');
    }
  };

  const handleSwap = () => {
    setFromUnit(toUnit);
    setToUnit(fromUnit);
    setResult(null);
  };

  const handleCopy = () => {
    if (!result) return;
    const text = `${result.input} ${result.from} = ${parseFloat(result.result?.toFixed(8))} ${result.to}`;
    navigator.clipboard.writeText(text).then(() => setCopied(true));
  };

  const units = UNIT_CONFIG[category] || [];
  const displayResult = result
    ? parseFloat(result.result?.toFixed(6))
    : null;

  return (
    <Box sx={{ minHeight: '100vh', py: 6 }}>
      <Container maxWidth="md">

        {/* ── Hero Header ──────────────────────────────────────────────────── */}
        <Box sx={{ textAlign: 'center', mb: 6 }}>
          <Typography
            variant="h2"
            fontWeight={800}
            sx={{ letterSpacing: '-1px', lineHeight: 1.1, mb: 1 }}
          >
            Convert anything,
          </Typography>
          <Typography
            variant="h2"
            fontWeight={800}
            sx={{
              background: 'linear-gradient(90deg, #a855f7, #ec4899)',
              WebkitBackgroundClip: 'text',
              WebkitTextFillColor: 'transparent',
              letterSpacing: '-1px',
              lineHeight: 1.1,
              mb: 2,
            }}
          >
            beautifully.
          </Typography>
          <Typography variant="body1" color="text.secondary" sx={{ maxWidth: 480, mx: 'auto' }}>
            The world's most vibrant measurement tool for designers, engineers,
            and modern creators.
          </Typography>
        </Box>

        {/* ── Converter Card ───────────────────────────────────────────────── */}
        <Box sx={{ ...card, p: 3, mb: 4 }}>

          {/* Step label */}
          <Typography variant="caption" color="text.secondary" sx={{ letterSpacing: 2, fontWeight: 600 }}>
            01 / CHOOSE DIMENSION
          </Typography>

          {/* Category tabs */}
          <Box sx={{ display: 'flex', gap: 1.5, mt: 1.5, mb: 3, flexWrap: 'wrap' }}>
            {CATEGORIES.map(cat => (
              <Box
                key={cat.id}
                onClick={() => setCategory(cat.id)}
                sx={{
                  display: 'flex', flexDirection: 'column', alignItems: 'center',
                  gap: 0.5, px: 3, py: 1.5, borderRadius: 3, cursor: 'pointer',
                  border: category === cat.id
                    ? '2px solid #a855f7'
                    : '2px solid rgba(255,255,255,0.08)',
                  background: category === cat.id
                    ? 'rgba(168,85,247,0.12)'
                    : 'transparent',
                  color: category === cat.id ? '#a855f7' : '#9ca3af',
                  transition: 'all 0.2s',
                  minWidth: 90,
                  '&:hover': {
                    border: '2px solid rgba(168,85,247,0.5)',
                    color: '#a855f7',
                  },
                }}
              >
                {cat.icon}
                <Typography variant="caption" fontWeight={600}>{cat.label}</Typography>
              </Box>
            ))}
          </Box>

          {/* Mode sub-tabs */}
          <Box sx={{ display: 'flex', gap: 0.5, mb: 3,
            background: 'rgba(0,0,0,0.3)', borderRadius: 2, p: 0.5, width: 'fit-content' }}>
            {MODE_TABS.map(tab => (
              <Box
                key={tab}
                onClick={() => setModeTab(tab)}
                sx={{
                  px: 2.5, py: 0.8, borderRadius: 1.5, cursor: 'pointer',
                  background: modeTab === tab ? '#ffffff' : 'transparent',
                  color: modeTab === tab ? '#000000' : '#9ca3af',
                  fontWeight: 600, fontSize: '0.85rem',
                  transition: 'all 0.2s',
                }}
              >
                {tab}
              </Box>
            ))}
          </Box>

          {/* FROM / SWAP / TO row */}
          <Box sx={{ display: 'flex', gap: 2, alignItems: 'stretch' }}>

            {/* FROM */}
            <Box sx={inputPanel}>
              <Typography variant="caption" color="text.secondary" sx={{ letterSpacing: 1 }}>
                FROM
              </Typography>
              <input
                type="number"
                value={inputVal}
                onChange={e => { setInputVal(e.target.value); setResult(null); }}
                onKeyDown={e => e.key === 'Enter' && handleConvert()}
                placeholder="0"
                style={{
                  background: 'transparent', border: 'none', outline: 'none',
                  color: '#ffffff', fontSize: '2.8rem', fontWeight: 700,
                  width: '100%', display: 'block', marginTop: 8, marginBottom: 12,
                }}
              />
              <Select
                value={fromUnit}
                onChange={e => { setFromUnit(e.target.value); setResult(null); }}
                size="small" fullWidth sx={styledSelect}
              >
                {units.map(u => (
                  <MenuItem key={u.value} value={u.value}>{u.label}</MenuItem>
                ))}
              </Select>
            </Box>

            {/* Swap button */}
            <Box sx={{ display: 'flex', alignItems: 'center', flexShrink: 0 }}>
              <Tooltip title="Swap units">
                <Box
                  onClick={handleSwap}
                  sx={{
                    width: 48, height: 48, borderRadius: '50%', cursor: 'pointer',
                    background: 'linear-gradient(135deg, #a855f7, #ec4899)',
                    display: 'flex', alignItems: 'center', justifyContent: 'center',
                    boxShadow: '0 0 20px rgba(168,85,247,0.4)',
                    transition: 'transform 0.2s',
                    '&:hover': { transform: 'scale(1.1) rotate(180deg)' },
                  }}
                >
                  <SwapHorizIcon sx={{ color: '#fff' }} />
                </Box>
              </Tooltip>
            </Box>

            {/* TO RESULT */}
            <Box sx={inputPanel}>
              <Typography variant="caption" color="text.secondary" sx={{ letterSpacing: 1 }}>
                TO RESULT
              </Typography>
              <Typography
                sx={{
                  fontSize: '2.8rem', fontWeight: 700, mt: 1, mb: 1.5,
                  background: displayResult !== null
                    ? 'linear-gradient(90deg, #a855f7, #ec4899)'
                    : 'none',
                  WebkitBackgroundClip: displayResult !== null ? 'text' : 'unset',
                  WebkitTextFillColor: displayResult !== null ? 'transparent' : '#4b5563',
                  color: displayResult !== null ? 'transparent' : '#4b5563',
                }}
              >
                {displayResult !== null ? displayResult : '—'}
              </Typography>
              <Select
                value={toUnit}
                onChange={e => { setToUnit(e.target.value); setResult(null); }}
                size="small" fullWidth sx={styledSelect}
              >
                {units.map(u => (
                  <MenuItem key={u.value} value={u.value}>{u.label}</MenuItem>
                ))}
              </Select>
            </Box>
          </Box>

          {/* Error */}
          {convError && (
            <Alert severity="error" sx={{ mt: 2, borderRadius: 2 }}>{convError}</Alert>
          )}

          {/* Footer row */}
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mt: 3 }}>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
              {['#a855f7', '#ec4899', '#6366f1'].map((c, i) => (
                <Box key={i} sx={{
                  width: 28, height: 28, borderRadius: '50%',
                  background: c, border: '2px solid #0a0a1a',
                  ml: i > 0 ? -1 : 0,
                }} />
              ))}
              <Typography variant="caption" color="text.secondary" sx={{ ml: 1 }}>
                Supporting 450+ unit pairings instantly.
              </Typography>
            </Box>

            <Box sx={{ display: 'flex', gap: 1 }}>
              <Button
                variant="contained"
                onClick={handleConvert}
                sx={{
                  background: 'linear-gradient(135deg, #a855f7, #ec4899)',
                  px: 3, borderRadius: 3,
                  '&:hover': { background: 'linear-gradient(135deg, #9333ea, #db2777)' },
                }}
              >
                Convert
              </Button>
              <Button
                variant="outlined"
                startIcon={<ContentCopyIcon />}
                onClick={handleCopy}
                disabled={!result}
                sx={{
                  borderColor: 'rgba(168,85,247,0.4)', color: '#a855f7',
                  borderRadius: 3, px: 2,
                  '&:hover': { borderColor: '#a855f7', background: 'rgba(168,85,247,0.08)' },
                }}
              >
                Copy to Clipboard
              </Button>
            </Box>
          </Box>
        </Box>

        {/* ── History Table ─────────────────────────────────────────────────── */}
        <Box sx={{ ...card, p: 3 }}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
              <HistoryIcon sx={{ color: '#a855f7' }} />
              <Typography fontWeight={700}>Conversion History</Typography>
            </Box>
            <Button
              size="small" startIcon={<RefreshIcon />}
              onClick={fetchHistory} disabled={loadingH}
              sx={{ color: '#a855f7', '&:hover': { background: 'rgba(168,85,247,0.08)' } }}
            >
              Refresh
            </Button>
          </Box>

          {loadingH ? (
            <Box sx={{ textAlign: 'center', py: 4 }}><CircularProgress sx={{ color: '#a855f7' }} /></Box>
          ) : (
            <TableContainer sx={{ maxHeight: 380 }}>
              <Table size="small" stickyHeader>
                <TableHead>
                  <TableRow>
                    {['Input', 'From', 'To', 'Result', 'Category', 'Time'].map(col => (
                      <TableCell
                        key={col}
                        sx={{ background: 'rgba(0,0,0,0.4)', color: '#9ca3af',
                             borderBottom: '1px solid rgba(168,85,247,0.15)', fontWeight: 600 }}
                      >
                        {col}
                      </TableCell>
                    ))}
                  </TableRow>
                </TableHead>
                <TableBody>
                  {history.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={6} align="center"
                        sx={{ py: 5, color: '#6b7280', border: 'none' }}>
                        No conversions yet — try the converter above!
                      </TableCell>
                    </TableRow>
                  ) : history.map(h => (
                    <TableRow key={h.id} sx={{
                      '&:hover': { background: 'rgba(168,85,247,0.05)' },
                      '& td': { borderBottom: '1px solid rgba(255,255,255,0.04)' },
                    }}>
                      <TableCell sx={{ color: '#e5e7eb' }}>{h.fromValue}</TableCell>
                      <TableCell>
                        <Chip label={h.fromUnit} size="small"
                          sx={{ background: 'rgba(168,85,247,0.15)', color: '#a855f7',
                               border: '1px solid rgba(168,85,247,0.3)' }} />
                      </TableCell>
                      <TableCell>
                        <Chip label={h.toUnit} size="small"
                          sx={{ background: 'rgba(236,72,153,0.15)', color: '#ec4899',
                               border: '1px solid rgba(236,72,153,0.3)' }} />
                      </TableCell>
                      <TableCell sx={{ color: '#fff', fontWeight: 700 }}>
                        {parseFloat(h.result?.toFixed(6))}
                      </TableCell>
                      <TableCell>
                        <Chip label={h.category} size="small"
                          sx={{ background: 'rgba(99,102,241,0.15)', color: '#818cf8',
                               border: '1px solid rgba(99,102,241,0.3)', fontSize: '0.7rem' }} />
                      </TableCell>
                      <TableCell sx={{ color: '#6b7280', fontSize: '0.72rem', whiteSpace: 'nowrap' }}>
                        {h.createdAt ? new Date(h.createdAt).toLocaleString() : '—'}
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          )}
        </Box>

      </Container>

      {/* Copy toast */}
      <Snackbar open={copied} autoHideDuration={2500} onClose={() => setCopied(false)}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}>
        <Alert severity="success" sx={{ borderRadius: 3 }}>
          Result copied to clipboard!
        </Alert>
      </Snackbar>
    </Box>
  );
}
