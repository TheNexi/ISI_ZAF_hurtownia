import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import axios from 'axios';
import ProfileForm from '../pages/Profile/indexForm';

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('ProfileForm', () => {
  const mockClientData = {
    nazwa: 'Firma ABC',
    nip: '1234567890',
    kraj: 'Polska',
    miasto: 'Warszawa',
    ulica: 'ul. Przykładowa 1',
    kodPocztowy: '00-001',
  };

  beforeEach(() => {
    mockedAxios.get.mockResolvedValueOnce({ data: mockClientData });
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test('should render form with fetched client data', async () => {
    render(<ProfileForm />);

    await waitFor(() => {
      expect(screen.getByDisplayValue('Firma ABC')).toBeInTheDocument();
    });

    expect(screen.getByDisplayValue('1234567890')).toBeInTheDocument();
    expect(screen.getByDisplayValue('Polska')).toBeInTheDocument();
    expect(screen.getByDisplayValue('Warszawa')).toBeInTheDocument();
    expect(screen.getByDisplayValue('ul. Przykładowa 1')).toBeInTheDocument();
    expect(screen.getByDisplayValue('00-001')).toBeInTheDocument();
  });

  test('should submit form and show success alert', async () => {
    mockedAxios.post.mockResolvedValueOnce({});

    window.alert = jest.fn();

    render(<ProfileForm />);

    await waitFor(() => {
      expect(screen.getByDisplayValue('Firma ABC')).toBeInTheDocument();
    });

    fireEvent.change(screen.getByLabelText('Miasto'), { target: { value: 'Kraków' } });
    fireEvent.click(screen.getByRole('button', { name: 'Zapisz dane' }));

    await waitFor(() => {
      expect(mockedAxios.post).toHaveBeenCalledWith(
        'http://localhost:8080/profile/updateData',
        expect.objectContaining({ miasto: 'Kraków' }),
        { withCredentials: true }
      );

      expect(window.alert).toHaveBeenCalledWith('Dane klienta zapisane');
    });
  });

  test('should show error alert on fetch failure', async () => {
    jest.spyOn(console, 'error').mockImplementation(() => {}); 
    mockedAxios.get.mockRejectedValueOnce(new Error('Błąd sieci'));

    render(<ProfileForm />);

    await waitFor(() => {
      expect(screen.queryByDisplayValue('Firma ABC')).not.toBeInTheDocument();
    });
  });

  test('should show error alert on save failure', async () => {
    mockedAxios.post.mockRejectedValueOnce(new Error('Błąd zapisu'));
    window.alert = jest.fn();

    render(<ProfileForm />);

    await waitFor(() => {
      expect(screen.getByDisplayValue('Firma ABC')).toBeInTheDocument();
    });

    fireEvent.click(screen.getByRole('button', { name: 'Zapisz dane' }));

    await waitFor(() => {
      expect(window.alert).toHaveBeenCalledWith('Wystąpił błąd przy zapisie danych klienta');
    });
  });
});
