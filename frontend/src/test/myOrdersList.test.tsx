import React from 'react';
import { render, screen } from '@testing-library/react';
import MyOrdersList from '../pages/Orders/MyOrdersList';
import { ZamowienieResponse } from '../pages/Orders/OrdersService';

describe('MyOrdersList component', () => {
  test('renders message if no orders', () => {
    render(<MyOrdersList orders={[]} />);
    expect(screen.getByText(/nie masz jeszcze żadnych zamówień/i)).toBeInTheDocument();
  });

  test('renders table with orders', () => {
    const mockOrders: ZamowienieResponse[] = [
      {
        id: 1,
        idKlient: 100,
        idCzas: 200,
        idDostawca: 300,
        idMagazyn: 400,
        wartoscCalkowita: 123.45,
        statusPlatnosci: 'Opłacone',
        produkty: [
          { idProdukt: 1, ilosc: 2, cena: 50 },
          { idProdukt: 2, ilosc: 1, cena: 23.45 },
        ],
      },
      {
        id: 2,
        idKlient: 101,
        idCzas: 201,
        idDostawca: 301,
        idMagazyn: 401,
        wartoscCalkowita: 67.89,
        statusPlatnosci: 'Oczekujące',
      },
    ];

    render(<MyOrdersList orders={mockOrders} />);

    expect(screen.getByText('ID')).toBeInTheDocument();
    expect(screen.getByText('Wartość')).toBeInTheDocument();
    expect(screen.getByText('Status płatności')).toBeInTheDocument();

    expect(screen.getByText('1')).toBeInTheDocument();
    expect(screen.getByText('123.45 zł')).toBeInTheDocument();
    expect(screen.getByText('Opłacone')).toBeInTheDocument();

    expect(screen.getByText('2')).toBeInTheDocument();
    expect(screen.getByText('67.89 zł')).toBeInTheDocument();
    expect(screen.getByText('Oczekujące')).toBeInTheDocument();
  });
});
