import React, { useEffect, useState } from 'react';
import { Row, Col, Card, Statistic } from 'antd';
import DashboardTabs from '../../../components/DashboardTabs';
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend,
  PieChart, Pie, Cell
} from 'recharts';

import {
  getProducts,
  getAllOrders,
  getCzasy,
  getDostawcy,
  getKlienci,
  Produkt,
  Czas,
  Zamowienie,
} from './indexService';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#AA336A', '#33AA99'];

const HomeAdmin = () => {
  const [productsCount, setProductsCount] = useState<number>(0);
  const [ordersCount, setOrdersCount] = useState<number>(0);
  const [deliveriesCount, setDeliveriesCount] = useState<number>(0);
  const [clientsCount, setClientsCount] = useState<number>(0);

  const [ordersLast7Days, setOrdersLast7Days] = useState<{ date: string; count: number }[]>([]);
  const [productsByCategory, setProductsByCategory] = useState<{ name: string; value: number }[]>([]);

  useEffect(() => {
    async function fetchData() {
      try {
        const [products, orders, czasy, dostawcy, klienci] = await Promise.all([
          getProducts(),
          getAllOrders(),
          getCzasy(),
          getDostawcy(),
          getKlienci(),
        ]);

        setProductsCount(products.length);
        setOrdersCount(orders.length);
        setDeliveriesCount(dostawcy.length);
        setClientsCount(klienci.length);

        aggregateProductsByCategory(products);

        const czasMap: Record<number, Czas> = {};
        czasy.forEach(c => {
          czasMap[c.id] = c;
        });

        aggregateOrdersLast7Days(orders, czasMap);
      } catch (error) {
        console.error('Błąd podczas pobierania danych:', error);
      }
    }
    fetchData();
  }, []);

  function aggregateProductsByCategory(products: Produkt[]) {
    const categoryMap: Record<string, number> = {};
    products.forEach(prod => {
      const cat = prod.kategoria || 'Inne';
      categoryMap[cat] = (categoryMap[cat] || 0) + 1;
    });
    const data = Object.entries(categoryMap).map(([name, value]) => ({ name, value }));
    setProductsByCategory(data);
  }

  function aggregateOrdersLast7Days(orders: Zamowienie[], czasMap: Record<number, Czas>) {
    const today = new Date();
    const datesMap: Record<string, number> = {};

    for (let i = 6; i >= 0; i--) {
      const d = new Date(today);
      d.setDate(today.getDate() - i);
      const key = d.toISOString().slice(0, 10);
      datesMap[key] = 0;
    }

    orders.forEach(order => {
      const czas = czasMap[order.idCzas];
      if (!czas) return;

       const orderDate = `${czas.rok.toString().padStart(4, '0')}-${(czas.miesiac).toString().padStart(2, '0')}-${czas.dzien.toString().padStart(2, '0')}`;

      if (orderDate in datesMap) {
        datesMap[orderDate]++;
      }
    });

    const chartData = Object.entries(datesMap).map(([date, count]) => ({ date, count }));
    setOrdersLast7Days(chartData);
  }

  return (
    <div className="container">
      <DashboardTabs role="ADMIN" />
      <div className="page-content">
        <h1 className="header">Strona główna</h1>

        <Row gutter={[16, 16]} className="stats-row">
          <Col xs={24} sm={12} md={12} lg={6}>
            <Card><Statistic title="Produkty" value={productsCount} /></Card>
          </Col>

          <Col xs={24} sm={12} md={12} lg={6}>
            <Card><Statistic title="Zamówienia" value={ordersCount} /></Card>
          </Col>

          <Col xs={24} sm={12} md={12} lg={6}>
            <Card><Statistic title="Dostawcy" value={deliveriesCount} /></Card>
          </Col>

          <Col xs={24} sm={12} md={12} lg={6}>
            <Card><Statistic title="Klienci" value={clientsCount} /></Card>
          </Col>
        </Row>

        <Row gutter={[16, 16]}>
          <Col xs={24} md={12}>
            <Card title="Liczba zamówień w ostatnich 7 dniach">
              <ResponsiveContainer width="100%" height={300}>
                <LineChart data={ordersLast7Days} margin={{ top: 5, right: 20, bottom: 5, left: 0 }}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="date" />
                  <YAxis allowDecimals={false} />
                  <Tooltip />
                  <Legend />
                  <Line type="monotone" dataKey="count" stroke="#8884d8" />
                </LineChart>
              </ResponsiveContainer>
            </Card>
          </Col>

          <Col xs={24} md={12}>
            <Card title="Produkty według kategorii">
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie
                    data={productsByCategory}
                    cx="50%"
                    cy="50%"
                    outerRadius={80}
                    fill="#8884d8"
                    dataKey="value"
                    label
                  >
                    {productsByCategory.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            </Card>
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default HomeAdmin;
