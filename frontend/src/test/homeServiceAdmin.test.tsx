import * as service from '../admin/pages/Home/indexService'; 
import { axiosInstance } from '../admin/pages/Home/indexService';

describe('indexService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    (axiosInstance.get as jest.Mock) = jest.fn();
  });

  test('getProducts should fetch and return product list', async () => {
    const mockProducts = [
      { id: 1, kategoria: 'Elektronika' },
      { id: 2, kategoria: 'RTV' },
    ];
    (axiosInstance.get as jest.Mock).mockResolvedValueOnce({ data: mockProducts });

    const result = await service.getProducts();

    expect(axiosInstance.get).toHaveBeenCalledWith('/produkt');
    expect(result).toEqual(mockProducts);
  });

  test('getAllOrders should fetch and return orders list', async () => {
    const mockOrders = [
      { id: 1, idCzas: 10, wartoscCalkowita: 100 },
      { id: 2, idCzas: 11, wartoscCalkowita: 200 },
    ];
    (axiosInstance.get as jest.Mock).mockResolvedValueOnce({ data: mockOrders });

    const result = await service.getAllOrders();

    expect(axiosInstance.get).toHaveBeenCalledWith('/zamowienie');
    expect(result).toEqual(mockOrders);
  });

  test('getCzasy should fetch and return time entries list', async () => {
    const mockCzasy = [
      { id: 1, dzien: 10, miesiac: 6, rok: 2025 },
      { id: 2, dzien: 11, miesiac: 6, rok: 2025 },
    ];
    (axiosInstance.get as jest.Mock).mockResolvedValueOnce({ data: mockCzasy });

    const result = await service.getCzasy();

    expect(axiosInstance.get).toHaveBeenCalledWith('/czas');
    expect(result).toEqual(mockCzasy);
  });

  test('getDostawcy should fetch and return dostawcy list', async () => {
    const mockDostawcy = [
      { id: 1, nazwa: 'Dostawca A' },
      { id: 2, nazwa: 'Dostawca B' },
    ];
    (axiosInstance.get as jest.Mock).mockResolvedValueOnce({ data: mockDostawcy });

    const result = await service.getDostawcy();

    expect(axiosInstance.get).toHaveBeenCalledWith('/dostawca');
    expect(result).toEqual(mockDostawcy);
  });

  test('getKlienci should fetch and return klienci list', async () => {
    const mockKlienci = [
      { id: 1, nazwa: 'Klient A' },
      { id: 2, nazwa: 'Klient B' },
    ];
    (axiosInstance.get as jest.Mock).mockResolvedValueOnce({ data: mockKlienci });

    const result = await service.getKlienci();

    expect(axiosInstance.get).toHaveBeenCalledWith('/klient');
    expect(result).toEqual(mockKlienci);
  });

  test('all functions should throw error if axiosInstance.get fails', async () => {
    (axiosInstance.get as jest.Mock).mockRejectedValue(new Error('Network Error'));

    await expect(service.getProducts()).rejects.toThrow('Network Error');
    await expect(service.getAllOrders()).rejects.toThrow('Network Error');
    await expect(service.getCzasy()).rejects.toThrow('Network Error');
    await expect(service.getDostawcy()).rejects.toThrow('Network Error');
    await expect(service.getKlienci()).rejects.toThrow('Network Error');
  });
});
