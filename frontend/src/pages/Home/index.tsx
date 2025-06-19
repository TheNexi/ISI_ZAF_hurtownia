import React, { useEffect, useState } from 'react';
import { Row, Col, Card, Statistic } from 'antd';
import DashboardTabs from '../../components/DashboardTabs';
import {
  PieChart, Pie, Cell, Tooltip, ResponsiveContainer
} from 'recharts';

import {
  getProducts,
  getDostawcy,
  getKlienci,
  Produkt,
} from './indexService';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#AA336A', '#33AA99'];

const HomeUser = () => {
  const [productsCount, setProductsCount] = useState<number>(0);
  const [categoriesCount, setCategoriesCount] = useState<number>(0);
  const [deliveriesCount, setDeliveriesCount] = useState<number>(0);
  const [clientsCount, setClientsCount] = useState<number>(0);

  const [productsByCategory, setProductsByCategory] = useState<{ name: string; value: number }[]>([]);

  useEffect(() => {
    async function fetchData() {
      try {
        const [products, dostawcy, klienci] = await Promise.all([
          getProducts(),
          getDostawcy(),
          getKlienci(),
        ]);

        setProductsCount(products.length);
        setDeliveriesCount(dostawcy.length);
        setClientsCount(klienci.length);

        aggregateProductsByCategory(products);
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
    setCategoriesCount(Object.keys(categoryMap).length);
  }

  return (
    <div className="container">
      <DashboardTabs role="USER" />
      <div className="page-content">
        <h1 className="header">Strona użytkownika</h1>

        <Row gutter={[16, 16]} className="stats-row">
          <Col xs={24} sm={12} md={12} lg={6}>
            <Card>
              <Statistic
                title="Aż tyle produktów do wyboru!"
                value={productsCount}
                valueStyle={{ color: '#3f8600' }}
              />
            </Card>
          </Col>

          <Col xs={24} sm={12} md={12} lg={6}>
            <Card>
              <Statistic
                title={`Wiele dostępnych kategorii!`}
                value={categoriesCount}
                valueStyle={{ color: '#1890ff' }}
              />
            </Card>
          </Col>

          <Col xs={24} sm={12} md={12} lg={6}>
            <Card>
              <Statistic
                title={`Tylko sprawdzeni dostawcy!`}
                value={deliveriesCount}
                valueStyle={{ color: '#cf1322' }}
              />
            </Card>
          </Col>

          <Col xs={24} sm={12} md={12} lg={6}>
            <Card>
              <Statistic
                title={`Sami zadowoleni klienci!`}
                value={clientsCount}
                valueStyle={{ color: '#722ed1' }}
              />
            </Card>
          </Col>
        </Row>

        <Row gutter={[16, 16]}>
          <Col xs={24} md={12}>
            <Card title="Największa różnorodność kategorii na rynku!">
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

export default HomeUser;
