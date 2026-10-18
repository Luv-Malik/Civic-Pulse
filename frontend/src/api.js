const API = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
async function request(path, options = {}) {
  const response = await fetch(`${API}${path}`, { headers: { 'Content-Type': 'application/json', ...(options.headers || {}) }, ...options });
  if (!response.ok) { let message = `Request failed (${response.status})`; try { const body = await response.json(); message = body.error || body.message || message; } catch {} throw new Error(message); }
  if (response.status === 204) return null;
  return response.json();
}
export const api = {
  stats: () => request('/stats'),
  reports: () => request('/reports'),
  incidents: () => request('/incidents'),
  createReport: (payload) => request('/reports', { method: 'POST', body: JSON.stringify(payload) }),
  updateReport: (id, payload) => request(`/reports/${id}`, { method: 'PATCH', body: JSON.stringify(payload) }),
};
