import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import Orders from '../pages/Orders/index';
import * as productService from '../pages/Products/indexService';
import * as ordersService from '../pages/Orders/OrdersService';

jest.mock('../pages/Products/indexService', () => ({
  getAllProducts: jest.fn(),
}));

jest.mock('../pages/Orders/OrdersService', () => ({
  createOrder: jest.fn(),
  getMyOrders: jest.fn(),
}));

jest.mock('../components/DashboardTabs', () => () => <div data-testid="dashboard-tabs">DashboardTabs</div>);
jest.mock('../pages/Orders/ordersList', () => () => <div data-testid="orders-list">OrdersList</div>);
jest.mock('../pages/Orders/MyOrdersList', () => ({ orders }: any) => <div data-testid="my-orders-list">MyOrdersList ({orders.length})</div>);

const mockGet = jest.fn();
const mockPost = jest.fn();

jest.mock('axios', () => {
  return {
    __esModule: true,
    default: {
      create: jest.fn(() => ({
        interceptors: { request: { use: jest.fn() }, response: { use: jest.fn() } },
        get: mockGet,
        post: mockPost,
        put: jest.fn(),
        delete: jest.fn(),
      })),
    },
  };
});

describe('Orders component', () => {
  const mockUser = { id: 1, imie: 'Jan', nazwisko: 'Kowalski', email: 'jan@kowalski.pl' };
  const mockSuppliers = [{ id: 10, nazwa: 'Dostawca A' }];
  const mockWarehouses = [{ id: 20, nazwa: 'Magazyn A' }];
  const mockProducts = [
    { id: 1, nazwa: 'Produkt 1', kategoria: '', jednostkaMiary: '', cena: 10 },
  ];
  const setupCommonMocks = () => {
    (productService.getAllProducts as jest.Mock).mockResolvedValue(mockProducts);
    (ordersService.getMyOrders as jest.Mock).mockResolvedValue([]);

    mockGet.mockImplementation((url: string) => {
      if (url.includes('/user') || url.includes('/profile/client')) {
        return Promise.resolve({ data: mockUser });
      }
      if (url.includes('/dostawca')) {
        return Promise.resolve({ data: mockSuppliers });
      }
      if (url.includes('/magazyn')) {
        return Promise.resolve({ data: mockWarehouses });
      }
      return Promise.resolve({ data: {} });
    });

    mockPost.mockResolvedValue({ data: { id: 123 } });
  };

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders loading state initially', async () => {
    (productService.getAllProducts as jest.Mock).mockResolvedValue([]);
    (ordersService.getMyOrders as jest.Mock).mockResolvedValue([]);

    mockGet.mockImplementation((url: string) => {
      if (url.includes('/profile/client')) return Promise.resolve({ data: mockUser });
      if (url.includes('/dostawca')) return Promise.resolve({ data: mockSuppliers });
      if (url.includes('/magazyn')) return Promise.resolve({ data: mockWarehouses });
      return Promise.resolve({ data: {} });
    });

    render(<Orders />);
    expect(screen.getByText(/Ładowanie danych/i)).toBeInTheDocument();
    await waitFor(() => {
      expect(screen.queryByText(/Ładowanie danych/i)).not.toBeInTheDocument();
    });
  });

  test('renders select inputs and buttons after data load', async () => {
    setupCommonMocks();

    render(<Orders />);

    await waitFor(() => {
      expect(screen.getByLabelText(/Wybierz dostawcę/i)).toBeInTheDocument();
      expect(screen.getByLabelText(/Wybierz magazyn/i)).toBeInTheDocument();
      expect(screen.getByText(/Zapłać gotówką/i)).toBeInTheDocument();
      expect(screen.getByText(/Zapłać kartą/i)).toBeInTheDocument();
    });
  });
});
