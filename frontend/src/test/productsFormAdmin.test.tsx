import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import ProductForm from '../admin/pages/Products/indexForm'; // dostosuj ścieżkę
import { createProduct, updateProduct } from '../admin/pages/Products/indexService';

jest.mock('../admin/pages/Products/indexService', () => ({
  createProduct: jest.fn(),
  updateProduct: jest.fn(),
}));

const mockOnUpdate = jest.fn();
const mockSetEditingProduct = jest.fn();

describe('ProductForm', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders form for new product', () => {
    render(
      <ProductForm
        onUpdate={mockOnUpdate}
        editingProduct={null}
        setEditingProduct={mockSetEditingProduct}
      />
    );

    expect(screen.getByText('Dodaj Nowy Produkt')).toBeInTheDocument();
    expect(screen.getByLabelText('Nazwa')).toBeInTheDocument();
    expect(screen.getByLabelText('Kategoria')).toBeInTheDocument();
    expect(screen.getByLabelText('Jednostka Miary')).toBeInTheDocument();
    expect(screen.getByLabelText('Cena')).toBeInTheDocument();
  });

  it('submits form and calls createProduct for new product', async () => {
    render(
      <ProductForm
        onUpdate={mockOnUpdate}
        editingProduct={null}
        setEditingProduct={mockSetEditingProduct}
      />
    );

    fireEvent.change(screen.getByLabelText('Nazwa'), { target: { value: 'Test Produkt' } });
    fireEvent.change(screen.getByLabelText('Kategoria'), { target: { value: 'Test Kategoria' } });
    fireEvent.change(screen.getByLabelText('Jednostka Miary'), { target: { value: 'szt.' } });
    fireEvent.change(screen.getByLabelText('Cena'), { target: { value: '123.45' } });

    fireEvent.click(screen.getByText('Dodaj produkt'));

    await waitFor(() => {
      expect(createProduct).toHaveBeenCalledWith({
        nazwa: 'Test Produkt',
        kategoria: 'Test Kategoria',
        jednostkaMiary: 'szt.',
        cena: 123.45,
      });
      expect(mockOnUpdate).toHaveBeenCalled();
      expect(mockSetEditingProduct).toHaveBeenCalledWith(null);
    });
  });

  it('submits form and calls updateProduct for editing product', async () => {
    render(
      <ProductForm
        onUpdate={mockOnUpdate}
        editingProduct={{
          id: 1,
          nazwa: 'Produkt stary',
          kategoria: 'Kategoria',
          jednostkaMiary: 'szt.',
          cena: 50,
        }}
        setEditingProduct={mockSetEditingProduct}
      />
    );

    fireEvent.change(screen.getByLabelText('Cena'), { target: { value: '200.00' } });

    fireEvent.click(screen.getByText('Zapisz zmiany'));

    await waitFor(() => {
      expect(updateProduct).toHaveBeenCalledWith(1, {
        nazwa: 'Produkt stary',
        kategoria: 'Kategoria',
        jednostkaMiary: 'szt.',
        cena: 200.0,
      });
      expect(mockOnUpdate).toHaveBeenCalled();
      expect(mockSetEditingProduct).toHaveBeenCalledWith(null);
    });
  });

  it('cancels editing and resets form', async () => {
    render(
      <ProductForm
        onUpdate={mockOnUpdate}
        editingProduct={{
          id: 1,
          nazwa: 'Test',
          kategoria: 'Test',
          jednostkaMiary: 'szt.',
          cena: 123,
        }}
        setEditingProduct={mockSetEditingProduct}
      />
    );

    const cancelButton = screen.getByText('Anuluj');
    fireEvent.click(cancelButton);

    expect(mockSetEditingProduct).toHaveBeenCalledWith(null);
  });
});
