import React from 'react';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import OrderList from '../admin/pages/Orders/indexList';
import * as service from '../admin/pages/Orders/indexService';
import axios from 'axios';
import { ZamowienieResponse } from '../admin/pages/Orders/indexService';

jest.mock('../admin/pages/Orders/indexService', () => ({
  getKlienci: jest.fn(),
  getDostawcy: jest.fn(),
  getMagazyny: jest.fn(),
  getCzasy: jest.fn(),
  deleteOrder: jest.fn(),
}));

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('OrderList', () => {
  const ordersMock: ZamowienieResponse[] = [
    {
      id: 1,
      idKlient: 1,
      idCzas: 1,
      idDostawca: 1,
      idMagazyn: 1,
      wartoscCalkowita: 123.45,
      statusPlatnosci: 'PENDING',
    },
  ];

  const klientMock = [{ id: 1, nazwa: 'KlientTest' }];
  const dostawcaMock = [{ id: 1, nazwa: 'DostawcaTest' }];
  const magazynMock = [{ id: 1, nazwa: 'MagazynTest' }];
  const czasMock = [{ id: 1, dzien: 10, miesiac: 6, rok: 2025, dzien_tygodnia: undefined }];

  beforeEach(() => {
    (service.getKlienci as jest.Mock).mockResolvedValue(klientMock);
    (service.getDostawcy as jest.Mock).mockResolvedValue(dostawcaMock);
    (service.getMagazyny as jest.Mock).mockResolvedValue(magazynMock);
    (service.getCzasy as jest.Mock).mockResolvedValue(czasMock);
    (service.deleteOrder as jest.Mock).mockResolvedValue({});
    mockedAxios.post.mockResolvedValue({});
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('renders table with orders and dictionary names', async () => {
    render(<OrderList orders={ordersMock} onRefresh={jest.fn()} onEdit={jest.fn()} />);

    await waitFor(() => {
      expect(screen.getByText('KlientTest')).toBeInTheDocument();
      expect(screen.getByText('DostawcaTest')).toBeInTheDocument();
      expect(screen.getByText('MagazynTest')).toBeInTheDocument();
      expect(screen.getByText('10.06.2025')).toBeInTheDocument();
      expect(screen.getByText('123.45 zł')).toBeInTheDocument();
      expect(screen.getByText('PENDING')).toBeInTheDocument();
    });
  });

  it('calls onEdit when edit button clicked', async () => {
    const onEditMock = jest.fn();
    render(<OrderList orders={ordersMock} onRefresh={jest.fn()} onEdit={onEditMock} />);

    await waitFor(() => screen.getByText('Edytuj'));

    fireEvent.click(screen.getByText('Edytuj'));
    expect(onEditMock).toHaveBeenCalledWith(ordersMock[0]);
  });

  it('calls deleteOrder and onRefresh on confirm delete', async () => {
    const onRefreshMock = jest.fn();
    render(<OrderList orders={ordersMock} onRefresh={onRefreshMock} onEdit={jest.fn()} />);

    await waitFor(() => screen.getByText('Usuń'));

    fireEvent.click(screen.getByText('Usuń'));

    await waitFor(() => screen.getByText('Tak'));
    fireEvent.click(screen.getByText('Tak'));

    await waitFor(() => {
      expect(service.deleteOrder).toHaveBeenCalledWith(1);
      expect(onRefreshMock).toHaveBeenCalled();
    });
  });

  it('calls axios.post and onRefresh on approve payment confirm', async () => {
    const onRefreshMock = jest.fn();
    render(<OrderList orders={ordersMock} onRefresh={onRefreshMock} onEdit={jest.fn()} />);

    await waitFor(() => screen.getByText('Zatwierdź płatność'));

    fireEvent.click(screen.getByText('Zatwierdź płatność'));

    await waitFor(() => screen.getByText('Tak'));
    fireEvent.click(screen.getByText('Tak'));

    await waitFor(() => {
      expect(mockedAxios.post).toHaveBeenCalledWith(
        'http://localhost:8080/zamowienie/1/zatwierdz-offline',
        {},
        { withCredentials: true }
      );
      expect(onRefreshMock).toHaveBeenCalled();
    });
  });

  it('renders fallback text when dictionary item missing', async () => {
    (service.getKlienci as jest.Mock).mockResolvedValue([]);
    (service.getDostawcy as jest.Mock).mockResolvedValue([]);
    (service.getMagazyny as jest.Mock).mockResolvedValue([]);
    (service.getCzasy as jest.Mock).mockResolvedValue([]);

    render(<OrderList orders={ordersMock} onRefresh={jest.fn()} onEdit={jest.fn()} />);

    await waitFor(() => {
      expect(screen.getAllByText('ID 1').length).toBeGreaterThanOrEqual(4);
    });
  });
});
