jest.mock('axios', () => {
  const mockedAxiosInstance = {
    get: jest.fn(),
    put: jest.fn(),
    delete: jest.fn(),
    interceptors: {
      request: { use: jest.fn() },
    },
  };
  return {
    __esModule: true,
    default: {
      create: jest.fn(() => mockedAxiosInstance),
      interceptors: mockedAxiosInstance.interceptors,
      get: mockedAxiosInstance.get,
      put: mockedAxiosInstance.put,
      delete: mockedAxiosInstance.delete,
    },
  };
});

import axios from 'axios';
const mockedAxiosInstance = (axios.create as jest.Mock).mock.results[0].value;

import {
  getAllOrders,
  getOrderById,
  updateOrder,
  deleteOrder,
  updateCzas,
  getKlienci,
  getDostawcy,
  getMagazyny,
  getCzasy,
  ZamowienieResponse,
  Klient,
  Dostawca,
  Magazyn,
  Czas,
} from '../admin/pages/Orders/indexService'; 

describe('API tests', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it('getAllOrders returns data', async () => {
    const mockData: ZamowienieResponse[] = [
      { id: 1, idKlient: 1, idCzas: 1, idDostawca: 1, idMagazyn: 1, wartoscCalkowita: 100, statusPlatnosci: 'SUCCESS' },
    ];
    mockedAxiosInstance.get.mockResolvedValueOnce({ data: mockData });

    const result = await getAllOrders();
    expect(mockedAxiosInstance.get).toHaveBeenCalledWith('/zamowienie');
    expect(result).toEqual(mockData);
  });

  it('getOrderById returns data', async () => {
    const mockData: ZamowienieResponse = {
      id: 2, idKlient: 2, idCzas: 2, idDostawca: 2, idMagazyn: 2, wartoscCalkowita: 200, statusPlatnosci: 'PENDING',
    };
    mockedAxiosInstance.get.mockResolvedValueOnce({ data: mockData });

    const result = await getOrderById(2);
    expect(mockedAxiosInstance.get).toHaveBeenCalledWith('/zamowienie/2');
    expect(result).toEqual(mockData);
  });

  it('updateOrder calls put with correct params', async () => {
    mockedAxiosInstance.put.mockResolvedValueOnce({});

    const data: Partial<ZamowienieResponse> = { statusPlatnosci: 'FAILED' };
    await updateOrder(3, data);
    expect(mockedAxiosInstance.put).toHaveBeenCalledWith('/zamowienie/3', data);
  });

  it('deleteOrder calls delete with correct id', async () => {
    mockedAxiosInstance.delete.mockResolvedValueOnce({});

    await deleteOrder(4);
    expect(mockedAxiosInstance.delete).toHaveBeenCalledWith('/zamowienie/4');
  });

  it('updateCzas calls put with correct params', async () => {
    mockedAxiosInstance.put.mockResolvedValueOnce({});

    const data: Partial<Czas> = { opis: 'Nowy opis' };
    await updateCzas(5, data);
    expect(mockedAxiosInstance.put).toHaveBeenCalledWith('/czas/5', data);
  });

  it('getKlienci returns data', async () => {
    const mockData: Klient[] = [{ id: 1, nazwa: 'Klient A' }];
    mockedAxiosInstance.get.mockResolvedValueOnce({ data: mockData });

    const result = await getKlienci();
    expect(mockedAxiosInstance.get).toHaveBeenCalledWith('/klient');
    expect(result).toEqual(mockData);
  });

  it('getDostawcy returns data', async () => {
    const mockData: Dostawca[] = [{ id: 1, nazwa: 'Dostawca A' }];
    mockedAxiosInstance.get.mockResolvedValueOnce({ data: mockData });

    const result = await getDostawcy();
    expect(mockedAxiosInstance.get).toHaveBeenCalledWith('/dostawca');
    expect(result).toEqual(mockData);
  });

  it('getMagazyny returns data', async () => {
    const mockData: Magazyn[] = [{ id: 1, nazwa: 'Magazyn A' }];
    mockedAxiosInstance.get.mockResolvedValueOnce({ data: mockData });

    const result = await getMagazyny();
    expect(mockedAxiosInstance.get).toHaveBeenCalledWith('/magazyn');
    expect(result).toEqual(mockData);
  });

  it('getCzasy returns data', async () => {
    const mockData: Czas[] = [
      { id: 1, opis: 'Opis', dzien: 1, miesiac: 1, rok: 2025 },
    ];
    mockedAxiosInstance.get.mockResolvedValueOnce({ data: mockData });

    const result = await getCzasy();
    expect(mockedAxiosInstance.get).toHaveBeenCalledWith('/czas');
    expect(result).toEqual(mockData);
  });
});
