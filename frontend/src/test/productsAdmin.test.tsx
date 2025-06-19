import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import AdminProducts from '../admin/pages/Products/index';
import * as service from '../admin/pages/Products/indexService';

jest.mock('../components/DashboardTabs', () => () => <div>Mocked DashboardTabs</div>);
jest.mock('../admin/pages/Products/indexList', () => (props: any) => (
  <div>
    Mocked ProductList - Products count: {props.products.length}
    <button onClick={() => props.onEdit(props.products[0])}>Edit First</button>
    <button onClick={props.onRefresh}>Refresh</button>
  </div>
));
jest.mock('../admin/pages/Products/indexForm', () => (props: any) => (
  <div>
    Mocked ProductForm - Editing: {props.editingProduct ? 'Yes' : 'No'}
  </div>
));

describe('AdminProducts', () => {
  const mockProducts = [
    { id: 1, nazwa: 'Produkt 1', kategoria: 'Kategoria A', jednostkaMiary: 'szt.', cena: 10 },
    { id: 2, nazwa: 'Produkt 2', kategoria: 'Kategoria B', jednostkaMiary: 'kg', cena: 20 },
  ];

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders loading and then products list', async () => {
    jest.spyOn(service, 'getAllProducts').mockResolvedValueOnce(mockProducts);

    render(<AdminProducts />);

    expect(screen.getByText(/Ładowanie.../i)).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByText(/Mocked ProductList - Products count: 2/i)).toBeInTheDocument();
    });
  });

  test('searches product by ID successfully', async () => {
    jest.spyOn(service, 'getAllProducts').mockResolvedValueOnce(mockProducts);
    jest.spyOn(service, 'getProductById').mockResolvedValueOnce(mockProducts[0]);

    render(<AdminProducts />);

    await waitFor(() => screen.getByText(/Mocked ProductList - Products count: 2/i));

    fireEvent.change(screen.getByPlaceholderText(/Wpisz ID produktu/i), { target: { value: '1' } });
    fireEvent.click(screen.getByText('Szukaj'));

    expect(screen.getByText(/Ładowanie.../i)).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByText(/Mocked ProductList - Products count: 1/i)).toBeInTheDocument();
    });

    expect(screen.queryByText(/Błąd/i)).not.toBeInTheDocument();
  });

  test('shows error on empty search', async () => {
    jest.spyOn(service, 'getAllProducts').mockResolvedValueOnce(mockProducts);

    render(<AdminProducts />);

    await waitFor(() => screen.getByText(/Mocked ProductList - Products count: 2/i));

    fireEvent.change(screen.getByPlaceholderText(/Wpisz ID produktu/i), { target: { value: ' ' } });
    fireEvent.click(screen.getByText('Szukaj'));

    expect(screen.getByText(/Podaj ID produktu do wyszukania/i)).toBeInTheDocument();
  });

  test('shows error when product not found', async () => {
    jest.spyOn(service, 'getAllProducts').mockResolvedValueOnce(mockProducts);
    jest.spyOn(service, 'getProductById').mockRejectedValueOnce(new Error('Not found'));

    render(<AdminProducts />);

    await waitFor(() => screen.getByText(/Mocked ProductList - Products count: 2/i));

    fireEvent.change(screen.getByPlaceholderText(/Wpisz ID produktu/i), { target: { value: '999' } });
    fireEvent.click(screen.getByText('Szukaj'));

    await waitFor(() => {
      expect(screen.getByText(/Nie znaleziono produktu o ID: 999/i)).toBeInTheDocument();
    });
  });

  test('clears search correctly', async () => {
    jest.spyOn(service, 'getAllProducts').mockResolvedValueOnce(mockProducts);

    render(<AdminProducts />);

    await waitFor(() => screen.getByText(/Mocked ProductList - Products count: 2/i));

    fireEvent.change(screen.getByPlaceholderText(/Wpisz ID produktu/i), { target: { value: '1' } });
    fireEvent.click(screen.getByText('Wyczyść'));

    expect(screen.getByPlaceholderText(/Wpisz ID produktu/i)).toHaveValue('');
    expect(screen.queryByText(/Błąd/i)).not.toBeInTheDocument();
    expect(screen.getByText(/Mocked ProductList - Products count: 2/i)).toBeInTheDocument();
  });
});
