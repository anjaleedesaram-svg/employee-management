import { BrowserRouter, Routes, Route } from 'react-router-dom';
import EmployeePage from './pages/EmployeePage';
import './styles/App.scss';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<EmployeePage />} />
      </Routes>
    </BrowserRouter>
  );
}