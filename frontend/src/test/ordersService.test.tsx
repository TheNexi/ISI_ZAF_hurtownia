import axios from 'axios';
import { createOrder, getMyOrders, ZamowienieResponse } from '../pages/Orders/OrdersService';

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('OrdersService', () => {
  describe('createOrder', () => {
    it('should post order data and return response data', async () => {
      const orderData = {
        idKlient: 1,
        idCzas: 1,
        idDostawca: 1,
        idMagazyn: 1,
        wartoscCalkowita: 100,
        produkty: [{ idProdukt: 1, ilosc: 5 }],
      };
      const responseData = { success: true, orderId: 123 };
      mockedAxios.post.mockResolvedValueOnce({ data: responseData });

      const result = await createOrder(orderData);

      expect(mockedAxios.post).toHaveBeenCalledWith(
        'http://localhost:8080/zamowienie/create',
        orderData,
        { withCredentials: true }
      );
      expect(result).toEqual(responseData);
    });
  });

  describe('getMyOrders', () => {
    it('should fetch orders and return data', async () => {
      const mockOrders: ZamowienieResponse[] = [
        {
          id: 1,
          idKlient: 1,
          idCzas: 1,
          idDostawca: 2,
          idMagazyn: 3,
          wartoscCalkowita: 150.5,
          statusPlatnosci: 'Opłacone',
          produkty: [
            { idProdukt: 1, ilosc: 2, cena: 50 },
            { idProdukt: 2, ilosc: 1, cena: 50.5 },
          ],
        },
      ];
      mockedAxios.get.mockResolvedValueOnce({ data: mockOrders });

      const result = await getMyOrders();

      expect(mockedAxios.get).toHaveBeenCalledWith(
        'http://localhost:8080/zamowienie/my',
        { withCredentials: true }
      );
      expect(result).toEqual(mockOrders);
    });
  });
});
