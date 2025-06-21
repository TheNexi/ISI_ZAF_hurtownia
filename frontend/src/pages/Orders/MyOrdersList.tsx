import React from 'react';
import { ZamowienieResponse } from './OrdersService';

interface MyOrdersListProps {
  orders: ZamowienieResponse[];
}

const MyOrdersList: React.FC<MyOrdersListProps> = ({ orders }) => {
  if (orders.length === 0) {
    return <p>Nie masz jeszcze żadnych zamówień.</p>;
  }

  return (
    <div className="table-responsive" style={{ marginTop: '2rem' }}>
      <h2>Twoje zamówienia</h2>
      <table className="table table-bordered">
        <thead>
          <tr>
            <th>ID</th>
            <th>Wartość</th>
            <th>Status płatności</th>
          </tr>
        </thead>
        <tbody>
          {orders.map(order => (
            <tr key={order.id}>
              <td>{order.id}</td>
              <td>{order.wartoscCalkowita.toFixed(2)} zł</td>
              <td>{order.statusPlatnosci}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default MyOrdersList;
