import { useEffect, useState } from 'react';
import DashboardTabs from '../../../components/DashboardTabs';
import ProductList from './indexList';
import ProductForm from './indexForm';
import { ProduktResponse } from './indexService';
import { getAllProducts, getProductById } from './indexService';

const AdminProducts = () => {
  const [products, setProducts] = useState<ProduktResponse[]>([]);
  const [filteredProduct, setFilteredProduct] = useState<ProduktResponse | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [searchId, setSearchId] = useState<string>('');
  const [error, setError] = useState<string>('');
  const [editingProduct, setEditingProduct] = useState<ProduktResponse | null>(null);

  const fetchProducts = async () => {
    setLoading(true);
    try {
      const productsData = await getAllProducts();
      setProducts(productsData);
    } catch (error) {
      console.error('Błąd podczas pobierania produktów:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleSearch = async () => {
    if (!searchId.trim()) {
      setError('Podaj ID produktu do wyszukania');
      setFilteredProduct(null);
      return;
    }

    setError('');
    setLoading(true);

    try {
      const product = await getProductById(Number(searchId));
      setFilteredProduct(product);
    } catch (err) {
      setFilteredProduct(null);
      setError(`Nie znaleziono produktu o ID: ${searchId}`);
    } finally {
      setLoading(false);
    }
  };

  const handleClear = () => {
    setFilteredProduct(null);
    setSearchId('');
    setError('');
  };

  return (
    <div className="container">
      <DashboardTabs role="ADMIN" />
      <div className="page-content">
        <h1>Produkty - Panel Administratora</h1>

        <div className="search-wrapper">
          <input
            type="text"
            placeholder="Wpisz ID produktu"
            value={searchId}
            onChange={(e) => setSearchId(e.target.value)}
            className="search-input"
          />
          <button onClick={handleSearch} className="btn btn-primary search-btn">
            Szukaj
          </button>
          <button onClick={handleClear} className="btn btn-secondary search-btn">
            Wyczyść
          </button>
        </div>

        {error && <p className="error-message">{error}</p>}
        {loading ? (
          <p>Ładowanie...</p>
        ) : filteredProduct ? (
          <ProductList products={[filteredProduct]} onRefresh={fetchProducts} onEdit={setEditingProduct} />
        ) : (
          <ProductList products={products} onRefresh={fetchProducts} onEdit={setEditingProduct} />
        )}
        <ProductForm onUpdate={fetchProducts} editingProduct={editingProduct} setEditingProduct={setEditingProduct} />
      </div>
    </div>
  );
};

export default AdminProducts;
