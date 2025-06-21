import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import AdminOrders from '../admin/pages/Orders/index';
import * as service from '../admin/pages/Orders/indexService';
import type { ZamowienieResponse } from '../admin/pages/Orders/indexService';
import { MemoryRouter } from 'react-router-dom';

jest.mock('../admin/pages/Orders/indexService');

const mockOrders: ZamowienieResponse[] = [
  { id: 1, idKlient: 1, idDostawca: 1, idMagazyn: 1, wartoscCalkowita: 100, idCzas: 1, statusPlatnosci: 'SUCCESS' },
  { id: 2, idKlient: 2, idDostawca: 2, idMagazyn: 2, wartoscCalkowita: 200, idCzas: 2, statusPlatnosci: 'PENDING' },
];

const mockOrder: ZamowienieResponse = {
  id: 1, idKlient: 1, idDostawca: 1, idMagazyn: 1, wartoscCalkowita: 100, idCzas: 1, statusPlatnosci: 'SUCCESS',
};

const mockKlienci = [
  { id: 1, nazwa: 'Klient 1' },
  { id: 2, nazwa: 'Klient 2' },
];

const mockDostawcy = [
  { id: 1, nazwa: 'Dostawca 1' },
  { id: 2, nazwa: 'Dostawca 2' },
];

const mockMagazyny = [
  { id: 1, nazwa: 'Magazyn 1' },
  { id: 2, nazwa: 'Magazyn 2' },
];

const mockCzasy = [
  { id: 1, opis: 'Czas 1', dzien: 1, miesiac: 1, rok: 2023 },
  { id: 2, opis: 'Czas 2', dzien: 2, miesiac: 2, rok: 2023 },
];

describe('AdminOrders', () => {
  beforeEach(() => {
    jest.clearAllMocks();

    (service.getAllOrders as jest.Mock).mockResolvedValue(mockOrders);
    (service.getKlienci as jest.Mock).mockResolvedValue(mockKlienci);
    (service.getDostawcy as jest.Mock).mockResolvedValue(mockDostawcy);
    (service.getMagazyny as jest.Mock).mockResolvedValue(mockMagazyny);
    (service.getCzasy as jest.Mock).mockResolvedValue(mockCzasy);
  });

  const renderWithRouter = (ui: React.ReactElement) => {
    return render(<MemoryRouter>{ui}</MemoryRouter>);
  };

  test('renders basic layout and loads orders', async () => {
    renderWithRouter(<AdminOrders />);

    expect(screen.getByRole('heading', { name: /Zamówienia - Panel Administratora/i })).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/Wpisz ID zamówienia/i)).toBeInTheDocument();

    await waitFor(() => {
      expect(service.getAllOrders).toHaveBeenCalledTimes(1);
    });

    expect(screen.getByRole('button', { name: /Szukaj/i })).toBeInTheDocument();
  });

  test('search by ID success shows filtered order', async () => {
  (service.getOrderById as jest.Mock).mockResolvedValue(mockOrder);

  renderWithRouter(<AdminOrders />);

  await waitFor(() => expect(service.getAllOrders).toHaveBeenCalled());

  const input = screen.getByPlaceholderText(/Wpisz ID zamówienia/i);
  const searchBtn = screen.getByRole('button', { name: /Szukaj/i });

  await userEvent.type(input, '1');
  await userEvent.click(searchBtn);

  await waitFor(() => {
    expect(service.getOrderById).toHaveBeenCalledWith(1);
  });

  await waitFor(() => {
    expect(screen.getByRole('cell', { name: '1' })).toBeInTheDocument();
  });
});

  test('search by ID failure shows error message', async () => {
    (service.getOrderById as jest.Mock).mockRejectedValue(new Error('Not found'));

    renderWithRouter(<AdminOrders />);

    await waitFor(() => expect(service.getAllOrders).toHaveBeenCalled());

    const input = screen.getByPlaceholderText(/Wpisz ID zamówienia/i);
    const searchBtn = screen.getByRole('button', { name: /Szukaj/i });

    await userEvent.type(input, '9999');
    await userEvent.click(searchBtn);

    await waitFor(() => {
      expect(service.getOrderById).toHaveBeenCalledWith(9999);
    });

    expect(screen.getByText(/Nie znaleziono zamówienia o ID: 9999/i)).toBeInTheDocument();
  });

  test('shows error if search input is empty', async () => {
    renderWithRouter(<AdminOrders />);

    const searchBtn = screen.getByRole('button', { name: /Szukaj/i });
    await userEvent.click(searchBtn);

    expect(screen.getByText(/Podaj ID zamówienia do wyszukania/i)).toBeInTheDocument();
  });

  test('clear button resets search', async () => {
    renderWithRouter(<AdminOrders />);

    const input = screen.getByPlaceholderText(/Wpisz ID zamówienia/i);
    const clearBtn = screen.getByRole('button', { name: /Wyczyść/i });

    await userEvent.type(input, '123');
    expect(input).toHaveValue('123');

    await userEvent.click(clearBtn);

    expect(input).toHaveValue('');
    expect(screen.queryByText(/Nie znaleziono/i)).not.toBeInTheDocument();
  });
});
