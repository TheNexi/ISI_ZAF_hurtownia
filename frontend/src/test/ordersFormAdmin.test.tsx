import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import '@testing-library/jest-dom';
import OrderForm from '../admin/pages/Orders/indexForm';
import {
  getKlienci,
  getDostawcy,
  getMagazyny,
  getCzasy,
  updateOrder,
  updateCzas,
} from '../admin/pages/Orders/indexService';
import type { ZamowienieResponse } from '../admin/pages/Orders/indexService';

jest.mock('../admin/pages/Orders/indexService');

const mockOnUpdate = jest.fn();
const mockSetEditingOrder = jest.fn();

const mockKlienci = [
  { id: 1, nazwa: 'Klient A' },
  { id: 2, nazwa: 'Klient B' },
];

const mockDostawcy = [
  { id: 1, nazwa: 'Dostawca A' },
  { id: 2, nazwa: 'Dostawca B' },
];

const mockMagazyny = [
  { id: 1, nazwa: 'Magazyn A' },
  { id: 2, nazwa: 'Magazyn B' },
];

const mockCzasy = [
  { id: 1, dzien: 1, miesiac: 6, rok: 2025, dzien_tygodnia: 'Niedziela', kwartal: 2 },
];

function getSelectedText(labelText: string) {
  const selectElement = screen.getByLabelText(labelText);
  return selectElement.closest('.ant-form-item-control')?.querySelector('.ant-select-selection-item');
}

describe('OrderForm', () => {
  beforeEach(async () => {
    (getKlienci as jest.Mock).mockResolvedValue(mockKlienci);
    (getDostawcy as jest.Mock).mockResolvedValue(mockDostawcy);
    (getMagazyny as jest.Mock).mockResolvedValue(mockMagazyny);
    (getCzasy as jest.Mock).mockResolvedValue(mockCzasy);
    jest.clearAllMocks();
  });

  test('renders form fields', async () => {
    render(
      <OrderForm
        onUpdate={mockOnUpdate}
        editingOrder={null}
        setEditingOrder={mockSetEditingOrder}
      />
    );

    expect(screen.getByText('Edytuj Zamówienie')).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByLabelText('Klient')).toBeInTheDocument();
      expect(screen.getByLabelText('Dostawca')).toBeInTheDocument();
      expect(screen.getByLabelText('Magazyn')).toBeInTheDocument();
      expect(screen.getByLabelText('Data')).toBeInTheDocument();
      expect(screen.getByLabelText('Wartość Całkowita')).toBeInTheDocument();
    });
  });

  test('allows editing an order and saving', async () => {
    const editingOrder: ZamowienieResponse = {
      id: 123,
      idKlient: 1,
      idDostawca: 2,
      idMagazyn: 1,
      wartoscCalkowita: 500.0,
      idCzas: 1,
      statusPlatnosci: 'SUCCESS',
    };

    (updateOrder as jest.Mock).mockResolvedValue({});
    (updateCzas as jest.Mock).mockResolvedValue({});

    render(
      <OrderForm
        onUpdate={mockOnUpdate}
        editingOrder={editingOrder}
        setEditingOrder={mockSetEditingOrder}
      />
    );

    await waitFor(() => {
    expect(getSelectedText('Klient')).toHaveTextContent('Klient A');
    expect(getSelectedText('Dostawca')).toHaveTextContent('Dostawca B');
    expect(getSelectedText('Magazyn')).toHaveTextContent('Magazyn A');
    
    const wartoscInput = screen.getByLabelText('Wartość Całkowita') as HTMLInputElement;
    expect(parseFloat(wartoscInput.value)).toBe(500);

    });


    const newValue = '999.99';
    const valueInput = screen.getByLabelText('Wartość Całkowita');

    await userEvent.clear(valueInput);
    await userEvent.type(valueInput, newValue);

    const saveButton = screen.getByRole('button', { name: 'Zapisz zmiany' });
    await userEvent.click(saveButton);

    await waitFor(() => {
      expect(updateOrder).toHaveBeenCalledWith(123, expect.objectContaining({
        idKlient: 1,
        idDostawca: 2,
        idMagazyn: 1,
        wartoscCalkowita: parseFloat(newValue),
        idCzas: 1,
      }));

      expect(updateCzas).toHaveBeenCalledWith(1, expect.any(Object));
      expect(mockOnUpdate).toHaveBeenCalled();
      expect(mockSetEditingOrder).toHaveBeenCalledWith(null);
    });
  });

  test('cancel edit resets form', async () => {
    const editingOrder: ZamowienieResponse = {
      id: 123,
      idKlient: 1,
      idDostawca: 2,
      idMagazyn: 1,
      wartoscCalkowita: 500.0,
      idCzas: 1,
      statusPlatnosci: 'PENDING',
    };

    render(
      <OrderForm
        onUpdate={mockOnUpdate}
        editingOrder={editingOrder}
        setEditingOrder={mockSetEditingOrder}
      />
    );

    await waitFor(() => {
      expect(screen.getByText('Anuluj')).toBeInTheDocument();
    });

    const cancelButton = screen.getByRole('button', { name: 'Anuluj' });
    await userEvent.click(cancelButton);

    await waitFor(() => {
      expect(mockSetEditingOrder).toHaveBeenCalledWith(null);
    });
  });
});
