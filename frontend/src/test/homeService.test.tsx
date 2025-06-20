import * as service from '../pages/Home/indexService';
import { axiosInstance } from '../pages/Home/indexService';

describe('homeService', () => {
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

  test('should throw error if axiosInstance.get fails', async () => {
    (axiosInstance.get as jest.Mock).mockRejectedValue(new Error('Network Error'));

    await expect(service.getProducts()).rejects.toThrow('Network Error');
    await expect(service.getDostawcy()).rejects.toThrow('Network Error');
    await expect(service.getKlienci()).rejects.toThrow('Network Error');
  });
});
