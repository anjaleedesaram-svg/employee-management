import { useState } from 'react';
import { EmployeeRequest, EmployeeResponse, EmployeeStatus } from '../types/employee';

interface Props {
  employee?: EmployeeResponse | null;
  onSubmit: (data: EmployeeRequest) => void;
  onCancel: () => void;
}

export default function EmployeeForm({ employee, onSubmit, onCancel }: Props) {
  const [formData, setFormData] = useState<EmployeeRequest>({
    firstName: employee?.firstName || '',
    lastName: employee?.lastName || '',
    email: employee?.email || '',
    department: employee?.department || '',
    position: employee?.position || '',
    salary: employee?.salary || 0,
    hireDate: employee?.hireDate || '',
    phoneNumber: employee?.phoneNumber || '',
    profileImageUrl: employee?.profileImageUrl || '',
    status: employee?.status || EmployeeStatus.ACTIVE,
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const validate = (): boolean => {
    const newErrors: Record<string, string> = {};
    if (!formData.firstName) newErrors.firstName = 'First name is required';
    if (!formData.lastName) newErrors.lastName = 'Last name is required';
    if (!formData.email) newErrors.email = 'Email is required';
    if (!formData.department) newErrors.department = 'Department is required';
    if (!formData.position) newErrors.position = 'Position is required';
    if (formData.salary <= 0) newErrors.salary = 'Salary must be positive';
    if (!formData.hireDate) newErrors.hireDate = 'Hire date is required';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (validate()) {
      onSubmit(formData);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'salary' ? parseFloat(value) || 0 : value,
    }));
  };

  return (
    <form className="form" onSubmit={handleSubmit}>
      <div className="form__row">
        <div className="form__group">
          <label className="form__label">First Name *</label>
          <input
            type="text"
            name="firstName"
            className="form__input"
            value={formData.firstName}
            onChange={handleChange}
          />
          {errors.firstName && <span className="form__error">{errors.firstName}</span>}
        </div>
        <div className="form__group">
          <label className="form__label">Last Name *</label>
          <input
            type="text"
            name="lastName"
            className="form__input"
            value={formData.lastName}
            onChange={handleChange}
          />
          {errors.lastName && <span className="form__error">{errors.lastName}</span>}
        </div>
      </div>

      <div className="form__group">
        <label className="form__label">Email *</label>
        <input
          type="email"
          name="email"
          className="form__input"
          value={formData.email}
          onChange={handleChange}
        />
        {errors.email && <span className="form__error">{errors.email}</span>}
      </div>

      <div className="form__row">
        <div className="form__group">
          <label className="form__label">Department *</label>
          <input
            type="text"
            name="department"
            className="form__input"
            value={formData.department}
            onChange={handleChange}
          />
          {errors.department && <span className="form__error">{errors.department}</span>}
        </div>
        <div className="form__group">
          <label className="form__label">Position *</label>
          <input
            type="text"
            name="position"
            className="form__input"
            value={formData.position}
            onChange={handleChange}
          />
          {errors.position && <span className="form__error">{errors.position}</span>}
        </div>
      </div>

      <div className="form__row">
        <div className="form__group">
          <label className="form__label">Salary *</label>
          <input
            type="number"
            name="salary"
            className="form__input"
            value={formData.salary}
            onChange={handleChange}
            min="0"
            step="0.01"
          />
          {errors.salary && <span className="form__error">{errors.salary}</span>}
        </div>
        <div className="form__group">
          <label className="form__label">Hire Date *</label>
          <input
            type="date"
            name="hireDate"
            className="form__input"
            value={formData.hireDate}
            onChange={handleChange}
          />
          {errors.hireDate && <span className="form__error">{errors.hireDate}</span>}
        </div>
      </div>

      <div className="form__group">
        <label className="form__label">Phone Number</label>
        <input
          type="tel"
          name="phoneNumber"
          className="form__input"
          value={formData.phoneNumber || ''}
          onChange={handleChange}
        />
      </div>

      <div className="form__group">
        <label className="form__label">Profile Image URL</label>
        <input
          type="url"
          name="profileImageUrl"
          className="form__input"
          value={formData.profileImageUrl || ''}
          onChange={handleChange}
        />
      </div>

      {employee && (
        <div className="form__group">
          <label className="form__label">Status</label>
          <select
            name="status"
            className="form__select"
            value={formData.status}
            onChange={handleChange}
          >
            {Object.values(EmployeeStatus).map((status) => (
              <option key={status} value={status}>{status}</option>
            ))}
          </select>
        </div>
      )}

      <div className="form__actions">
        <button type="button" className="btn btn--secondary" onClick={onCancel}>
          Cancel
        </button>
        <button type="submit" className="btn btn--primary">
          {employee ? 'Update' : 'Create'}
        </button>
      </div>
    </form>
  );
}