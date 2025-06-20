import { render, screen } from '@testing-library/react';
import ProductList from '../pages/Products/indexList';
import { ProduktResponse } from '../pages/Products/indexService';

describe('ProductList', () => {
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
      kategoria: '', 
      jednostkaMiary: 'kg',
      cena: 20.5,
    },
  ];

  it('renders table with correct product data', () => {
    render(<ProductList products={mockProducts} />);

    expect(screen.getByText('Produkt A')).toBeInTheDocument();
    expect(screen.getByText('Elektronika')).toBeInTheDocument();
    expect(screen.getByText('10.00 zł')).toBeInTheDocument();

    expect(screen.getByText('Produkt B')).toBeInTheDocument();
    expect(screen.getByText('Brak kategorii')).toBeInTheDocument();
    expect(screen.getByText('20.50 zł')).toBeInTheDocument();
  });
});
