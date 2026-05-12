import React, { useState, useEffect } from 'react';
import {
  Container, Box, Grid, Paper, Typography, Button, TextField,
  MenuItem, Table, TableBody, TableCell, TableContainer,
  TableHead, TableRow, Alert, Chip, CircularProgress, Divider,
} from '@mui/material';
import SwapHorizIcon from '@mui/icons-material/SwapHoriz';
import HistoryIcon   from '@mui/icons-material/History';
import { conversionApi } from '../services/api';
import { useAuth } from '../context/AuthContext';

// ── Unit configuration — values MUST match the conversion-service backend ─────
const CATEGORIES = ['LENGTH', 'WEIGHT', 'VOLUME', 'TEMPERATURE'];

const UNIT_CONFIG = {
  LENGTH: [
    { value: 'mm',  label: 'mm  (Millimetre)' },
    { value: 'cm',  label: 'cm  (Centimetre)' },
    { value: 'in',  label: 'in  (Inch)' },
    { value: 'ft',  label: 'ft  (Foot)' },
    { value: 'yd',  label: 'yd  (Yard)' },
    { value: 'm',   label: 'm   (Metre)' },
    { value: 'km',  label: 'km  (Kilometre)' },
    { value: 'mi',  label: 'mi  (Mile)' },
  ],
  WEIGHT: [
    { value: 'mg',  label: 'mg  (Milligram)' },
    { value: 'g',   label: 'g   (Gram)' },
    { value: 'oz',  label: 'oz  (Ounce)' },
    { value: 'lb',  label: 'lb  (Pound)' },
    { value: 'kg',  label: 'kg  (Kilogram)' },
    { value: 't',   label: 't   (Metric Ton)' },
  ],
  VOLUME: [
    { value: 'ml',    label: 'ml   (Millilitre)' },
    { value: 'l',     label: 'L    (Litre)' },
    { value: 'cup',   label: 'cup' },
    { value: 'gal',   label: 'gal  (Gallon)' },
    { value: 'fl_oz', label: 'fl oz' },
    { value: 'tsp',   label: 'tsp  (Teaspoon)' },
    { value: 'tbsp',  label: 'tbsp (Tablespoon)' },
  ],
  TEMPERATURE: [
    { value: 'C', label: '°C  (Celsius)' },
    { value: 'F', label: '°F  (Fahrenheit)' },
    { value: 'K', label: 'K   (Kelvin)' },
  ],
};

const defaultUnit = (cat) => UNIT_CONFIG[cat]?.[0]?.value ?? '';

// ── Dashboard ──────────────────────────────────────────────────────────────────
export default function DashboardPage() {
  const { user } = useAuth();

  // converter state
  const [conv, setConv] = useState({
    value: '',
    from:     defaultUnit('LENGTH'),
    to:       UNIT_CONFIG['LENGTH'][1]?.value ?? 'cm',
    category: 'LENGTH',
  });
  const [convResult, setConvResult] = useState(null);
  const [convError,  setConvError]  = useState('');

  // conversion history state
  const [history,  setHistory]  = useState([]);
  const [loadingH, setLoadingH] = useState(false);

  // ── Fetch history on mount ───────────────────────────────────────────────────
  useEffect(() => { fetchHistory(); }, []);

  const fetchHistory = async () => {
    setLoadingH(true);
    try {
      const { data } = await conversionApi.getHistory();
      setHistory(Array.isArray(data) ? data : []);
    } catch {
      // silently ignore — user may not have converted anything yet
    } finally {
      setLoadingH(false);
    }
  };

  // ── Handle category change — reset units ─────────────────────────────────────
  const handleCategoryChange = (newCat) => {
    const units = UNIT_CONFIG[newCat] || [];
    setConv({
      category: newCat,
      value:    conv.value,
      from:     units[0]?.value ?? '',
      to:       units[1]?.value ?? units[0]?.value ?? '',
    });
    setConvResult(null);
    setConvError('');
  };

  // ── Handle conversion ────────────────────────────────────────────────────────
  const handleConvert = async (e) => {
    e.preventDefault();
    setConvResult(null);
    setConvError('');
    try {
      const { data } = await conversionApi.convert(
        conv.value, conv.from, conv.to, conv.category
      );
      setConvResult(data);
      fetchHistory();
    } catch (err) {
      setConvError(
        err.response?.data?.message || 'Conversion failed — check unit/category combination'
      );
    }
  };

  // ── Render ───────────────────────────────────────────────────────────────────
  return (
    <Container maxWidth="lg" sx={{ mt: 3, mb: 6 }}>

      {/* Header */}
      <Box sx={{ mb: 3 }}>
        <Typography variant="h4" fontWeight={700}>Dashboard</Typography>
        <Typography color="text.secondary">
          Welcome, <b>{user?.username}</b> — {user?.email}
        </Typography>
      </Box>

      <Grid container spacing={3}>

        {/* ── Converter ─────────────────────────────────────────────────────── */}
        <Grid item xs={12} md={4}>
          <Paper elevation={3} sx={{ p: 3, borderRadius: 3 }}>
            <Typography variant="h6" fontWeight={600} gutterBottom>
              <SwapHorizIcon sx={{ mr: 1, verticalAlign: 'middle' }} />
              Unit Converter
            </Typography>
            <Divider sx={{ mb: 2 }} />

            <Box component="form" onSubmit={handleConvert}>
              {/* Category */}
              <TextField
                fullWidth select label="Category" margin="normal"
                value={conv.category}
                onChange={e => handleCategoryChange(e.target.value)}
              >
                {CATEGORIES.map(c => <MenuItem key={c} value={c}>{c}</MenuItem>)}
              </TextField>

              {/* Value */}
              <TextField
                fullWidth label="Value" type="number" margin="normal" required
                value={conv.value}
                onChange={e => setConv(c => ({ ...c, value: e.target.value }))}
                inputProps={{ step: 'any' }}
              />

              {/* From / To */}
              <Grid container spacing={1}>
                <Grid item xs={6}>
                  <TextField
                    fullWidth select label="From" margin="normal"
                    value={conv.from}
                    onChange={e => setConv(c => ({ ...c, from: e.target.value }))}
                  >
                    {(UNIT_CONFIG[conv.category] || []).map(u => (
                      <MenuItem key={u.value} value={u.value}>{u.label}</MenuItem>
                    ))}
                  </TextField>
                </Grid>
                <Grid item xs={6}>
                  <TextField
                    fullWidth select label="To" margin="normal"
                    value={conv.to}
                    onChange={e => setConv(c => ({ ...c, to: e.target.value }))}
                  >
                    {(UNIT_CONFIG[conv.category] || []).map(u => (
                      <MenuItem key={u.value} value={u.value}>{u.label}</MenuItem>
                    ))}
                  </TextField>
                </Grid>
              </Grid>

              <Button type="submit" variant="contained" fullWidth sx={{ mt: 2, py: 1.2 }}>
                Convert
              </Button>

              {convError && (
                <Alert severity="error" sx={{ mt: 2 }}>{convError}</Alert>
              )}

              {convResult && (
                <Alert severity="success" sx={{ mt: 2, fontSize: '1rem' }}>
                  <b>{convResult.input} {convResult.from}</b>
                  {' = '}
                  <b>
                    {typeof convResult.result === 'number'
                      ? parseFloat(convResult.result.toFixed(8))
                      : convResult.result}
                    {' '}{convResult.to}
                  </b>
                </Alert>
              )}
            </Box>
          </Paper>
        </Grid>

        {/* ── Conversion History ─────────────────────────────────────────────── */}
        <Grid item xs={12} md={8}>
          <Paper elevation={3} sx={{ p: 3, borderRadius: 3 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
              <Typography variant="h6" fontWeight={600}>
                <HistoryIcon sx={{ mr: 1, verticalAlign: 'middle' }} />
                My Conversion History
              </Typography>
              <Button size="small" onClick={fetchHistory} disabled={loadingH}>
                Refresh
              </Button>
            </Box>
            <Divider sx={{ mb: 2 }} />

            {loadingH ? (
              <Box sx={{ textAlign: 'center', py: 4 }}><CircularProgress /></Box>
            ) : (
              <TableContainer sx={{ maxHeight: 480 }}>
                <Table size="small" stickyHeader>
                  <TableHead>
                    <TableRow>
                      <TableCell><b>Input</b></TableCell>
                      <TableCell><b>From</b></TableCell>
                      <TableCell><b>To</b></TableCell>
                      <TableCell><b>Result</b></TableCell>
                      <TableCell><b>Category</b></TableCell>
                      <TableCell><b>Time</b></TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {history.length === 0 ? (
                      <TableRow>
                        <TableCell colSpan={6} align="center" sx={{ py: 4, color: 'text.secondary' }}>
                          No conversions yet — try the converter!
                        </TableCell>
                      </TableRow>
                    ) : history.map(h => (
                      <TableRow key={h.id} hover>
                        <TableCell>{h.fromValue}</TableCell>
                        <TableCell>
                          <Chip label={h.fromUnit} size="small" variant="outlined" />
                        </TableCell>
                        <TableCell>
                          <Chip label={h.toUnit} size="small" variant="outlined" />
                        </TableCell>
                        <TableCell><b>{parseFloat(h.result.toFixed(6))}</b></TableCell>
                        <TableCell>
                          <Chip label={h.category} size="small" color="primary" variant="outlined" />
                        </TableCell>
                        <TableCell sx={{ fontSize: '0.72rem', color: 'text.secondary', whiteSpace: 'nowrap' }}>
                          {h.createdAt ? new Date(h.createdAt).toLocaleString() : '—'}
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
            )}
          </Paper>
        </Grid>

      </Grid>
    </Container>
  );
}
