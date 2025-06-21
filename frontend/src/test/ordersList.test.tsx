import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import OrdersList from '../pages/Orders/ordersList';
import { ProduktResponse } from '../pages/Products/indexService';

describe('OrdersList component', () => {
  const mockProducts: ProduktResponse[] = [
    { id: 1, nazwa: 'Produkt A', kategoria: 'Kategoria 1', jednostkaMiary: 'szt', cena: 10 },
    { id: 2, nazwa: 'Produkt B', kategoria: 'Kategoria 2', jednostkaMiary: 'kg', cena: 25.5 },
  ];

  test('renders headers and products correctly', () => {
    render(<OrdersList products={mockProducts} selectedProducts={{}} updateQuantity={jest.fn()} />);

    expect(screen.getByText('Nazwa')).toBeInTheDocument();
    expect(screen.getByText('Cena')).toBeInTheDocument();
    expect(screen.getByText('Ilość')).toBeInTheDocument();

    expect(screen.getByText('Produkt A')).toBeInTheDocument();
    expect(screen.getByText('10.00 zł')).toBeInTheDocument();

    expect(screen.getByText('Produkt B')).toBeInTheDocument();
    expect(screen.getByText('25.50 zł')).toBeInTheDocument();
  });

  test('shows correct quantity values from selectedProducts', () => {
    render(<OrdersList products={mockProducts} selectedProducts={{ 1: 3, 2: 0 }} updateQuantity={jest.fn()} />);

    const inputs = screen.getAllByRole('spinbutton');
    expect(inputs[0]).toHaveValue(3);
    expect(inputs[1]).toHaveValue(0);
  });

  test('calls updateQuantity when input value changes', () => {
    const updateQuantityMock = jest.fn();
    render(<OrdersList products={mockProducts} selectedProducts={{}} updateQuantity={updateQuantityMock} />);

    const inputA = screen.getAllByRole('spinbutton')[0];
    fireEvent.change(inputA, { target: { value: '5' } });

    expect(updateQuantityMock).toHaveBeenCalledTimes(1);
    expect(updateQuantityMock).toHaveBeenCalledWith(1, 5);
  });
});
