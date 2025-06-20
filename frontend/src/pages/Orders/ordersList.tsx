import React from 'react';
import { ProduktResponse } from '../Products/indexService';

interface OrdersListProps {
  products: ProduktResponse[];
  selectedProducts: Record<number, number>;
  updateQuantity: (productId: number, qty: number) => void;
}

const OrdersList: React.FC<OrdersListProps> = ({ products, selectedProducts, updateQuantity }) => {
  return (
    <table className="table table-bordered">
      <thead>
        <tr>
          <th>Nazwa</th>
          <th>Cena</th>
          <th>Ilość</th>
        </tr>
      </thead>
      <tbody>
        {products.map(prod => (
          <tr key={prod.id}>
            <td>{prod.nazwa}</td>
            <td>{prod.cena.toFixed(2)} zł</td>
            <td>
              <input
                type="number"
                min={0}
                value={selectedProducts[prod.id] || 0}
                onChange={e => updateQuantity(prod.id, Number(e.target.value))}
                style={{ width: '60px' }}
              />
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default OrdersList;
