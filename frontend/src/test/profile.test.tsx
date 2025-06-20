import React from 'react';
import { render, screen } from '@testing-library/react';
import Profile from '../pages/Profile/index';

jest.mock('../components/DashboardTabs', () => () => <div data-testid="dashboard-tabs">Mock DashboardTabs</div>);
jest.mock('../pages/Profile/indexForm', () => () => <div data-testid="profile-form">Mock ProfileForm</div>);

describe('Profile page', () => {
  test('renders page title and components', () => {
    render(<Profile />);

    expect(screen.getByRole('heading', { name: /twój profil/i })).toBeInTheDocument();
    expect(screen.getByTestId('dashboard-tabs')).toBeInTheDocument();
    expect(screen.getByTestId('profile-form')).toBeInTheDocument();
  });
});
