import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import ProductList from '../admin/pages/Products/indexList';
import * as service from '../admin/pages/Products/indexService';

jest.mock('../admin/pages/Products/indexService', () => ({
  deleteProduct: jest.fn(),
}));

describe('ProductList', () => {
  const mockOnRefresh = jest.fn();
  const mockOnEdit = jest.fn();

  const mockProducts = [
    {
      id: 1,
      nazwa: 'Produkt 1',
      kategoria: 'Kategoria A',
      jednostkaMiary: 'szt.',
      cena: 99.99,
    },
    {
      id: 2,
      nazwa: 'Produkt 2',
      kategoria: 'Brak kategorii', 
      jednostkaMiary: 'kg',
      cena: 15.5,
    },
  ];

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders product list with correct data', () => {
    render(<ProductList products={mockProducts} onRefresh={mockOnRefresh} onEdit={mockOnEdit} />);

    expect(screen.getByText('Produkt 1')).toBeInTheDocument();
    expect(screen.getByText('Produkt 2')).toBeInTheDocument();
    expect(screen.getByText('Kategoria A')).toBeInTheDocument();
    expect(screen.getAllByText('Brak kategorii').length).toBeGreaterThan(0);
    expect(screen.getByText('99.99 zł')).toBeInTheDocument();
    expect(screen.getByText('15.50 zł')).toBeInTheDocument();
  });

  test('calls onEdit when "Edytuj" button is clicked', () => {
    render(<ProductList products={mockProducts} onRefresh={mockOnRefresh} onEdit={mockOnEdit} />);

    const editButtons = screen.getAllByText('Edytuj');
    fireEvent.click(editButtons[0]);

    expect(mockOnEdit).toHaveBeenCalledWith(mockProducts[0]);
  });

    test('calls deleteProduct and onRefresh on confirm delete', async () => {
    const mockDelete = jest.spyOn(service, 'deleteProduct').mockResolvedValueOnce(undefined);
    const mockOnRefresh = jest.fn();
    const mockOnEdit = jest.fn();

    render(<ProductList products={mockProducts} onRefresh={mockOnRefresh} onEdit={mockOnEdit} />);

        fireEvent.click(screen.getAllByText('Usuń')[0]);

        await waitFor(() => {
        expect(screen.getByText('Tak')).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText('Tak'));
    });
});
