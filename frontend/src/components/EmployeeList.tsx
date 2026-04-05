import { EmployeeResponse, EmployeeStatus } from '../types/employee';
import './EmployeeList.scss';

interface Props {
  employees: EmployeeResponse[];
  loading: boolean;
  onEdit: (employee: EmployeeResponse) => void;
  onDelete: (id: number) => void;
  onStatusChange: (id: number, status: EmployeeStatus) => void;
}

export default function EmployeeList({ employees, loading, onEdit, onDelete, onStatusChange }: Props) {
  if (loading) return <div className="loading">Loading...</div>;

  if (employees.length === 0) {
    return <div className="empty-state">No employees found</div>;
  }

  const getStatusClass = (status: EmployeeStatus) => {
    return `badge badge--${status.toLowerCase().replace('_', '-')}`;
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString();
  };

  const formatSalary = (salary: number) => {
    return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(salary);
  };

  return (
    <table className="table">
      <thead>
        <tr>
          <th>Name</th>
          <th>Email</th>
          <th>Department</th>
          <th>Position</th>
          <th>Salary</th>
          <th>Hire Date</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {employees.map((employee) => (
          <tr key={employee.id}>
            <td>{employee.firstName} {employee.lastName}</td>
            <td>{employee.email}</td>
            <td>{employee.department}</td>
            <td>{employee.position}</td>
            <td>{formatSalary(employee.salary)}</td>
            <td>{formatDate(employee.hireDate)}</td>
            <td>
              <select
                className={getStatusClass(employee.status)}
                value={employee.status}
                onChange={(e) => onStatusChange(employee.id, e.target.value as EmployeeStatus)}
                style={{ background: 'transparent', border: 'none', cursor: 'pointer' }}
              >
                {Object.values(EmployeeStatus).map((status) => (
                  <option key={status} value={status}>{status}</option>
                ))}
              </select>
            </td>
            <td>
              <button className="btn btn--sm btn--secondary" onClick={() => onEdit(employee)}>
                Edit
              </button>
              <button
                className="btn btn--sm btn--danger"
                onClick={() => onDelete(employee.id)}
                style={{ marginLeft: '8px' }}
              >
                Delete
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}