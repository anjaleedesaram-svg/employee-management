# Employee Management Frontend

React TypeScript frontend with SCSS for the Employee Management System.

## Setup

```bash
cd employee-management-frontend
npm install
npm run dev
```

The frontend will run on `http://localhost:3000` and proxy API requests to `http://localhost:8080`.

## Features

- Employee CRUD operations
- Search and filter by department/status
- Pagination
- Real-time status updates
- Statistics dashboard
- Responsive design

## Project Structure

```
src/
├── components/     # Reusable UI components
├── pages/          # Page components
├── services/       # API services
├── types/          # TypeScript interfaces
├── styles/         # SCSS styles
└── App.tsx         # Main app component
```