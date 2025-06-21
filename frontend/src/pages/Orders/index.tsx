import React, { useEffect, useState } from 'react';
import DashboardTabs from '../../components/DashboardTabs';
import OrdersList from './ordersList';
import { ProduktResponse, getAllProducts } from '../Products/indexService';
import { createOrder, ZamowienieResponse, getMyOrders } from './OrdersService';
import axios from 'axios';
import MyOrdersList from './MyOrdersList';

interface Dostawca {
  id: number;
  nazwa: string;
}

interface Magazyn {
  id: number;
  nazwa: string;
}

interface User {
  id: number;
}

const Orders = () => {
  const [products, setProducts] = useState<ProduktResponse[]>([]);
  const [selectedProducts, setSelectedProducts] = useState<Record<number, number>>({});
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>('');
  const [orderId, setOrderId] = useState<number | null>(null);

  const [user, setUser] = useState<User | null>(null);
  const [dostawcy, setDostawcy] = useState<Dostawca[]>([]);
  const [magazyny, setMagazyny] = useState<Magazyn[]>([]);

  const [selectedDostawcaId, setSelectedDostawcaId] = useState<number | null>(null);
  const [selectedMagazynId, setSelectedMagazynId] = useState<number | null>(null);

  const [myOrders, setMyOrders] = useState<ZamowienieResponse[]>([]);
  const [loadingOrders, setLoadingOrders] = useState<boolean>(false);
  const [ordersError, setOrdersError] = useState<string>('');

  useEffect(() => {
    async function fetchInitialData() {
      setLoading(true);
      try {
        const prods = await getAllProducts();
        setProducts(prods);

        const userResponse = await axios.get<User>('http://localhost:8080/profile/client', { withCredentials: true });
        setUser(userResponse.data);

        const dostawcaResponse = await axios.get<Dostawca[]>('http://localhost:8080/dostawca', { withCredentials: true });
        setDostawcy(dostawcaResponse.data);

        const magazynResponse = await axios.get<Magazyn[]>('http://localhost:8080/magazyn', { withCredentials: true });
        setMagazyny(magazynResponse.data);

        if (dostawcaResponse.data.length > 0) {
          setSelectedDostawcaId(dostawcaResponse.data[0].id);
        }
        if (magazynResponse.data.length > 0) {
          setSelectedMagazynId(magazynResponse.data[0].id);
        }
      } catch (e) {
        setError('Błąd podczas pobierania danych');
      } finally {
        setLoading(false);
      }
    }
    fetchInitialData();
  }, []);

  useEffect(() => {
  async function fetchUserOrders() {
    setLoadingOrders(true);
    setOrdersError('');
    try {
      const orders = await getMyOrders();
      console.log('Pobrane zamówienia:', orders);
      setMyOrders(orders);
    } catch (e: any) {
      console.error('Błąd pobierania zamówień:', e);
      if (e.response && e.response.status === 401) {
        setOrdersError('Nie jesteś zalogowany.');
      } else if (e.response && e.response.status === 404) {
        setOrdersError('Nie znaleziono zamówień dla Twojego konta.');
      } else {
        setOrdersError('Nie udało się pobrać Twoich zamówień.');
      }
    } finally {
      setLoadingOrders(false);
    }
  }
  fetchUserOrders();
}, []);


  const updateQuantity = (productId: number, qty: number) => {
    setSelectedProducts(prev => {
      const copy = { ...prev };
      if (qty <= 0) {
        delete copy[productId];
      } else {
        copy[productId] = qty;
      }
      return copy;
    });
  };

  const totalPrice = Object.entries(selectedProducts).reduce((sum, [id, qty]) => {
    const prod = products.find(p => p.id === Number(id));
    if (!prod) return sum;
    return sum + prod.cena * qty;
  }, 0);

  const handleCreateOrder = async (paymentType: 'CASH' | 'CARD') => {
    setError('');

    if (!user || !user.id) {
      setError('Brak danych klienta. Uzupełnij dane w profilu.');
      return;
    }

    if (!selectedDostawcaId) {
      setError('Wybierz dostawcę');
      return;
    }

    if (!selectedMagazynId) {
      setError('Wybierz magazyn');
      return;
    }

    const selectedProductEntries = Object.entries(selectedProducts).filter(([_, qty]) => qty > 0);

    if (selectedProductEntries.length === 0) {
      setError('Wybierz przynajmniej jeden produkt');
      return;
    }

    try {
      const idCzas = 2; 

      const orderData = {
        idKlient: user.id,
        idCzas,
        idDostawca: selectedDostawcaId,
        idMagazyn: selectedMagazynId,
        wartoscCalkowita: totalPrice,
        produkty: Object.entries(selectedProducts).map(([productId, qty]) => ({
          idProdukt: Number(productId),
          ilosc: qty,
        })),
      };

      const response = await createOrder(orderData);
      const createdOrderId = response.id || response.orderId;
      setOrderId(createdOrderId);

      const orders = await getMyOrders();
      setMyOrders(orders);

      if (paymentType === 'CARD') {
        const payResponse = await axios.post(`http://localhost:8080/zamowienie/${createdOrderId}/payu`, {}, { withCredentials: true });
        if (payResponse?.data?.redirectUrl) {
          window.location.href = payResponse.data.redirectUrl;
        } else {
          setError('Nie udało się uzyskać linku do płatności.');
        }
      }
    } catch (err) {
      setError('Błąd przy składaniu zamówienia.');
    }
  };

  return (
    <div className="container">
      <DashboardTabs role="USER" />
      <div className="page-content">
        <h1 className="header">Zamówienia</h1>
        {error && <p className="error-message">{error}</p>}

        {loading ? (
          <p>Ładowanie danych...</p>
        ) : (
          <>
          {loadingOrders ? (
              <p>Ładowanie zamówień...</p>
            ) : ordersError ? (
              <p style={{ color: 'red' }}>{ordersError}</p>
            ) : (
              <MyOrdersList orders={myOrders} />
            )}


            <OrdersList products={products} selectedProducts={selectedProducts} updateQuantity={updateQuantity} />

              <div className="form-wrapper">
                <label className="form-label" htmlFor="dostawca-select">Wybierz dostawcę:</label>
                <select
                  id="dostawca-select"
                  className="select-input"
                  value={selectedDostawcaId ?? ''}
                  onChange={e => setSelectedDostawcaId(Number(e.target.value))}
                >
                  {dostawcy.map(d => (
                    <option key={d.id} value={d.id}>
                      {d.nazwa}
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-wrapper">
                <label className="form-label" htmlFor="magazyn-select">Wybierz magazyn:</label>
                <select
                  id="magazyn-select"
                  className="select-input"
                  value={selectedMagazynId ?? ''}
                  onChange={e => setSelectedMagazynId(Number(e.target.value))}
                >
                  {magazyny.map(m => (
                    <option key={m.id} value={m.id}>
                      {m.nazwa}
                    </option>
                  ))}
                </select>
              </div>


            <h3 className="headernumber3">
              Łączna kwota: {totalPrice.toFixed(2)} zł
            </h3>

            <button onClick={() => handleCreateOrder('CASH')} className="btn btn-pay">
              Zapłać gotówką
            </button>

            <button onClick={() => handleCreateOrder('CARD')} className="btn btn-pay">
              Zapłać kartą
            </button>

            {orderId && (
              <p style={{ color: 'green', marginTop: '1rem' }}>
                Zamówienie nr {orderId} zostało utworzone. Dziękujemy!
              </p>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default Orders;
