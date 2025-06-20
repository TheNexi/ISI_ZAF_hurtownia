import * as service from '../pages/Products/indexService';
import { ProduktResponse } from '../pages/Products/indexService';

jest.mock('../pages/Products/indexService', () => {
  const original = jest.requireActual('../pages/Products/indexService');
  return {
    ...original,
    default: {
      get: jest.fn(),
    },
    getAllProducts: jest.fn(),
    getProductById: jest.fn(),
  };
});

describe('indexService', () => {
  const mockProducts: ProduktResponse[] = [
    {
      id: 1,
      nazwa: 'Produkt A',
      kategoria: 'Elektronika',
      jednostkaMiary: 'szt',
      cena: 10,
    },
  ];

  it('should return all products', async () => {
    (service.getAllProducts as jest.Mock).mockResolvedValueOnce(mockProducts);

    const result = await service.getAllProducts();
    expect(result).toEqual(mockProducts);
  });

  it('should return one product by ID', async () => {
    const mockProduct = mockProducts[0];
    (service.getProductById as jest.Mock).mockResolvedValueOnce(mockProduct);

    const result = await service.getProductById(1);
    expect(result).toEqual(mockProduct);
  });
});
