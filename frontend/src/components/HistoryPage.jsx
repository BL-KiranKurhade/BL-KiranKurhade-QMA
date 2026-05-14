import React, { useState, useEffect } from 'react';
import {
  Card, CardContent, Typography, List, ListItem, ListItemText,
  Button, Divider, Box, Chip,
} from '@mui/material';
import { conversionApi } from '../services/api';

/**
 * Conversion History component — shows the authenticated user's past conversions.
 * Uses conversionApi.getHistory() → GET /api/convert/history (conversion-service).
 */
export default function HistoryPage() {
  const [history, setHistory]   = useState([]);
  const [loading, setLoading]   = useState(false);
  const [error, setError]       = useState('');

  const load = async () => {
    setLoading(true); setError('');
    try {
      const { data } = await conversionApi.getHistory();
      setHistory(Array.isArray(data) ? data : data.content || []);
    } catch (e) {
      setError('Could not load history — please make sure you are logged in and the server is running.');
      setHistory([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const handleClearAll = async () => {
    try {
      await conversionApi.clearHistory();
      setHistory([]);
    } catch {
      setError('Failed to clear history.');
    }
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    return new Date(dateStr).toLocaleString();
  };

  return (
    <Card elevation={3}>
      <CardContent>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Typography variant="h5" fontWeight={700}>Conversion History</Typography>
          <Box sx={{ display: 'flex', gap: 1 }}>
            <Button variant="outlined" onClick={load} disabled={loading}>
              {loading ? 'Loading…' : 'Refresh'}
            </Button>
            <Button variant="outlined" color="error" onClick={handleClearAll} disabled={loading || history.length === 0}>
              Clear All
            </Button>
          </Box>
        </Box>

        {error && <Typography color="error" mb={2}>{error}</Typography>}

        <List>
          {history.map((h, i) => (
            <React.Fragment key={h.id}>
              <ListItem>
                <ListItemText
                  primary={
                    <Box sx={{ display: 'flex', gap: 1, alignItems: 'center', flexWrap: 'wrap' }}>
                      <Typography fontWeight={600}>{h.fromValue}</Typography>
                      <Chip label={h.fromUnit} size="small" variant="outlined" />
                      <Typography>→</Typography>
                      <Typography fontWeight={600}>{h.result !== undefined ? Number(h.result).toFixed(4) : ''}</Typography>
                      <Chip label={h.toUnit} size="small" color="primary" variant="outlined" />
                      <Chip label={h.category} size="small" />
                    </Box>
                  }
                  secondary={formatDate(h.createdAt)}
                />
              </ListItem>
              {i < history.length - 1 && <Divider />}
            </React.Fragment>
          ))}
          {history.length === 0 && !loading && (
            <ListItem>
              <ListItemText primary="No conversion history found. Try converting some units first!" />
            </ListItem>
          )}
        </List>
      </CardContent>
    </Card>
  );
}
