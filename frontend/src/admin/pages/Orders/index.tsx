import React, { useEffect, useState } from 'react';
import DashboardTabs from '../../../components/DashboardTabs';
import OrderList from './indexList';
import OrderForm from './indexForm';
import { ZamowienieResponse } from './indexService';
import { getAllOrders, getOrderById } from './indexService';

const AdminOrders = () => {
  const [orders, setOrders] = useState<ZamowienieResponse[]>([]);
  const [filteredOrder, setFilteredOrder] = useState<ZamowienieResponse | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [searchId, setSearchId] = useState<string>('');
  const [error, setError] = useState<string>('');
  const [editingOrder, setEditingOrder] = useState<ZamowienieResponse | null>(null);

  const fetchOrders = async () => {
    setLoading(true);
    try {
      const ordersData = await getAllOrders();
      setOrders(ordersData);
    } catch (error) {
      console.error('Błąd podczas pobierania zamówień:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  const handleSearch = async () => {
    if (!searchId.trim()) {
      setError('Podaj ID zamówienia do wyszukania');
      setFilteredOrder(null);
      return;
    }

    setError('');
    setLoading(true);

    try {
      const order = await getOrderById(Number(searchId));
      setFilteredOrder(order);
    } catch (err) {
      setFilteredOrder(null);
      setError(`Nie znaleziono zamówienia o ID: ${searchId}`);
    } finally {
      setLoading(false);
    }
  };

  const handleClear = () => {
    setFilteredOrder(null);
    setSearchId('');
    setError('');
  };

  return (
    <div className="container">
      <DashboardTabs role="ADMIN" />
      <div className="page-content">
        <h1>Zamówienia - Panel Administratora</h1>

        <div className="search-wrapper">
          <input
            type="text"
            placeholder="Wpisz ID zamówienia"
            value={searchId}
            onChange={(e) => setSearchId(e.target.value)}
            className="search-input"
          />
          <button onClick={handleSearch} className="btn btn-primary search-btn">Szukaj</button>
          <button onClick={handleClear} className="btn btn-secondary search-btn">Wyczyść</button>
        </div>

        {error && <p className="error-message">{error}</p>}
        {loading ? (
          <p>Ładowanie...</p>
        ) : filteredOrder ? (
          <OrderList orders={[filteredOrder]} onRefresh={fetchOrders} onEdit={setEditingOrder} />
        ) : (
          <OrderList orders={orders} onRefresh={fetchOrders} onEdit={setEditingOrder} />
        )}

        <OrderForm onUpdate={fetchOrders} editingOrder={editingOrder} setEditingOrder={setEditingOrder} />
      </div>
    </div>
  );
};

export default AdminOrders;
