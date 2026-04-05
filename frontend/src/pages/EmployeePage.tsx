import { useState, useEffect } from 'react';
import { EmployeeResponse, EmployeeRequest, EmployeeStatus } from '../types/employee';
import { employeeService } from '../services/api';
import EmployeeForm from '../components/EmployeeForm';
import EmployeeList from '../components/EmployeeList';
import StatsCard from '../components/StatsCard';

export default function EmployeePage() {
  const [employees, setEmployees] = useState<EmployeeResponse[]>([]);
  const [departments, setDepartments] = useState<string[]>([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [search, setSearch] = useState('');
  const [departmentFilter, setDepartmentFilter] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingEmployee, setEditingEmployee] = useState<EmployeeResponse | null>(null);
  const [stats, setStats] = useState<Record<string, number>>({});

  useEffect(() => {
    fetchEmployees();
    fetchDepartments();
    fetchStats();
  }, [page, search, departmentFilter, statusFilter]);

  const fetchEmployees = async () => {
    try {
      setLoading(true);
      const response = search || departmentFilter || statusFilter
        ? await employeeService.search(search, departmentFilter || undefined, statusFilter || undefined, page)
        : await employeeService.getAll(page);
      setEmployees(response.data.content);
      setTotalPages(response.data.totalPages);
      setTotalElements(response.data.totalElements);
    } catch (error) {
      console.error('Error fetching employees:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchDepartments = async () => {
    try {
      const response = await employeeService.getDepartments();
      setDepartments(response.data);
    } catch (error) {
      console.error('Error fetching departments:', error);
    }
  };

  const fetchStats = async () => {
    try {
      const response = await employeeService.getStats();
      setStats(response.data);
    } catch (error) {
      console.error('Error fetching stats:', error);
    }
  };

  const handleCreate = async (data: EmployeeRequest) => {
    await employeeService.create(data);
    setIsModalOpen(false);
    fetchEmployees();
    fetchStats();
  };

  const handleUpdate = async (data: EmployeeRequest) => {
    if (editingEmployee) {
      await employeeService.update(editingEmployee.id, data);
      setEditingEmployee(null);
      fetchEmployees();
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm('Are you sure you want to delete this employee?')) {
      await employeeService.delete(id);
      fetchEmployees();
      fetchStats();
    }
  };

  const handleEdit = (employee: EmployeeResponse) => {
    setEditingEmployee(employee);
  };

  const handleStatusChange = async (id: number, status: EmployeeStatus) => {
    await employeeService.updateStatus(id, status);
    fetchEmployees();
    fetchStats();
  };

  return (
    <div className="container">
      <header className="header">
        <div className="header__inner">
          <h1 className="header__title">Employee Management</h1>
          <button className="btn btn--primary" onClick={() => setIsModalOpen(true)}>
            + Add Employee
          </button>
        </div>
      </header>

      <StatsCard stats={stats} />

      <div className="search-bar">
        <input
          type="text"
          className="search-bar__input"
          placeholder="Search employees..."
          value={search}
          onChange={(e) => { setSearch(e.target.value); setPage(0); }}
        />
        <select
          className="search-bar__select"
          value={departmentFilter}
          onChange={(e) => { setDepartmentFilter(e.target.value); setPage(0); }}
        >
          <option value="">All Departments</option>
          {departments.map((dept) => (
            <option key={dept} value={dept}>{dept}</option>
          ))}
        </select>
        <select
          className="search-bar__select"
          value={statusFilter}
          onChange={(e) => { setStatusFilter(e.target.value); setPage(0); }}
        >
          <option value="">All Status</option>
          {Object.values(EmployeeStatus).map((status) => (
            <option key={status} value={status}>{status}</option>
          ))}
        </select>
      </div>

      <div className="card">
        <EmployeeList
          employees={employees}
          loading={loading}
          onEdit={handleEdit}
          onDelete={handleDelete}
          onStatusChange={handleStatusChange}
        />
      </div>

      {totalPages > 1 && (
        <div className="pagination">
          <button
            className="pagination__btn"
            disabled={page === 0}
            onClick={() => setPage(page - 1)}
          >
            Previous
          </button>
          <span style={{ padding: '8px 12px' }}>
            Page {page + 1} of {totalPages} ({totalElements} total)
          </span>
          <button
            className="pagination__btn"
            disabled={page >= totalPages - 1}
            onClick={() => setPage(page + 1)}
          >
            Next
          </button>
        </div>
      )}

      {(isModalOpen || editingEmployee) && (
        <div className="modal">
          <div className="modal__content">
            <div className="modal__header">
              <h2>{editingEmployee ? 'Edit Employee' : 'Add Employee'}</h2>
              <button
                className="modal__close"
                onClick={() => { setIsModalOpen(false); setEditingEmployee(null); }}
              >
                ×
              </button>
            </div>
            <EmployeeForm
              employee={editingEmployee}
              onSubmit={editingEmployee ? handleUpdate : handleCreate}
              onCancel={() => { setIsModalOpen(false); setEditingEmployee(null); }}
            />
          </div>
        </div>
      )}
    </div>
  );
}