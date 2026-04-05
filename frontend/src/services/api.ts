import axios from 'axios';
import { EmployeeRequest, EmployeeResponse, PageResponse, EmployeeStats } from '../types/employee';

const api = axios.create({
  baseURL: '/api/v1/employees',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const employeeService = {
  getAll: (page = 0, size = 10, sortBy = 'lastName', direction = 'asc') =>
    api.get<PageResponse<EmployeeResponse>>('', { params: { page, size, sortBy, direction } }),

  search: (search?: string, department?: string, status?: string, page = 0, size = 10) =>
    api.get<PageResponse<EmployeeResponse>>('/search', { params: { search, department, status, page, size } }),

  getById: (id: number) =>
    api.get<EmployeeResponse>(`/${id}`),

  create: (data: EmployeeRequest) =>
    api.post<EmployeeResponse>('', data),

  update: (id: number, data: EmployeeRequest) =>
    api.put<EmployeeResponse>(`/${id}`, data),

  delete: (id: number) =>
    api.delete(`/${id}`),

  updateStatus: (id: number, status: string) =>
    api.patch<EmployeeResponse>(`/${id}/status`, null, { params: { status } }),

  getDepartments: () =>
    api.get<string[]>('/departments'),

  getStats: () =>
    api.get<EmployeeStats>('/stats'),
};

export default api;