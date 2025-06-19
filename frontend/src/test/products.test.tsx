import React from 'react';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Products from '../pages/Products/index';
import { getAllProducts, getProductById } from '../pages/Products/indexService';
import { ProduktResponse } from '../pages/Products/indexService';
import { MemoryRouter } from 'react-router-dom';
import { AuthContext, AuthContextType } from '../auth/AuthContext';

const mockedNavigate = jest.fn();
jest.mock('react-router-dom', () => {
  const originalModule = jest.requireActual('react-router-dom');
  return {
    ...originalModule,
    useNavigate: () => mockedNavigate,
  };
});


jest.mock('../pages/Products/indexService');
jest.mock('../components/DashboardTabs', () => () => <div data-testid="mock-dashboard-tabs" />);

const mockedGetAllProducts = getAllProducts as jest.MockedFunction<typeof getAllProducts>;
const mockedGetProductById = getProductById as jest.MockedFunction<typeof getProductById>;

const renderWithProviders = (ui: React.ReactElement) => {
  const mockAuth: AuthContextType = {
    isAuthenticated: true,
    setAuthenticated: jest.fn(),
    user: { login: 'testuser', email: 'testuser@example.com', role: 'USER' },
    setUser: jest.fn(),
    logout: jest.fn(),
  };

  return render(
    <AuthContext.Provider value={mockAuth}>
      <MemoryRouter>{ui}</MemoryRouter>
    </AuthContext.Provider>
  );
};

describe('Products Page', () => {
  const mockProducts: ProduktResponse[] = [
    {
      id: 1,
      nazwa: 'Produkt A',
      kategoria: 'Elektronika',
      jednostkaMiary: 'szt',
      cena: 10,
    },
    {
      id: 2,
      nazwa: 'Produkt B',
      kategoria: 'RTV',
      jednostkaMiary: 'szt',
      cena: 20,
    },
  ];

  const mockProduct: ProduktResponse = {
    id: 2,
    nazwa: 'Produkt B',
    kategoria: 'RTV',
    jednostkaMiary: 'szt',
    cena: 20,
  };

  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.setItem('token', 'fake-token');
  });

  test('renders product list after fetching', async () => {
    mockedGetAllProducts.mockResolvedValueOnce(mockProducts);

    renderWithProviders(<Products />);

    expect(screen.getByText(/ładowanie/i)).toBeInTheDocument();

    expect(await screen.findByText('Produkt A')).toBeInTheDocument();
    expect(await screen.findByText('Produkt B')).toBeInTheDocument();
  });

  test('searches product by ID and shows result', async () => {
    mockedGetAllProducts.mockResolvedValueOnce(mockProducts);
    mockedGetProductById.mockResolvedValueOnce(mockProduct);

    renderWithProviders(<Products />);

    await screen.findByText('Produkt A');

    const input = await screen.findByPlaceholderText(/wpisz id produktu/i);
    const searchButton = await screen.findByRole('button', { name: /szukaj/i });

    await userEvent.clear(input);
    await userEvent.type(input, '2');
    await userEvent.click(searchButton);

    expect(mockedGetProductById).toHaveBeenCalledWith(2);
    expect(await screen.findByText('Produkt B')).toBeInTheDocument();
  });

  test('shows error when searching with empty ID', async () => {
    mockedGetAllProducts.mockResolvedValueOnce(mockProducts);

    renderWithProviders(<Products />);

    await screen.findByText('Produkt A');

    const input = await screen.findByPlaceholderText(/wpisz id produktu/i);
    const searchButton = await screen.findByRole('button', { name: /szukaj/i });

    await userEvent.clear(input);
    await userEvent.type(input, ' ');
    await userEvent.click(searchButton);

    expect(await screen.findByText(/podaj id produktu/i)).toBeInTheDocument();
  });

  test('shows error when product not found', async () => {
    mockedGetAllProducts.mockResolvedValueOnce(mockProducts);
    mockedGetProductById.mockRejectedValueOnce(new Error('Not Found'));

    renderWithProviders(<Products />);

    await screen.findByText('Produkt A');

    const input = await screen.findByPlaceholderText(/wpisz id produktu/i);
    const searchButton = await screen.findByRole('button', { name: /szukaj/i });

    await userEvent.clear(input);
    await userEvent.type(input, '99');
    await userEvent.click(searchButton);

    expect(await screen.findByText(/nie znaleziono produktu/i)).toBeInTheDocument();
  });

  test('clears search input and resets list', async () => {
    mockedGetAllProducts.mockResolvedValueOnce(mockProducts);

    renderWithProviders(<Products />);

    await screen.findByText('Produkt A');

    const input = await screen.findByPlaceholderText(/wpisz id produktu/i);
    const clearButton = await screen.findByRole('button', { name: /wyczyść/i });

    await userEvent.type(input, '2');
    await userEvent.click(clearButton);

    expect(input).toHaveValue('');
    expect(screen.queryByText(/nie znaleziono/i)).not.toBeInTheDocument();
    expect(await screen.findByText('Produkt A')).toBeInTheDocument();
  });
});
