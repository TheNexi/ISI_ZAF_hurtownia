import MockAdapter from 'axios-mock-adapter';
import {
  instance,
  getAllProducts,
  getProductById,
  createProduct,
  updateProduct,
  deleteProduct,
  ProduktResponse,
  ProduktRequest,
} from '../admin/pages/Products/indexService';

describe('indexService API calls', () => {
  let mock: MockAdapter;

  beforeAll(() => {
    mock = new MockAdapter(instance);
  });

  afterEach(() => {
    mock.reset();
  });

  afterAll(() => {
    mock.restore();
  });

  const mockProduct: ProduktResponse = {
    id: 1,
    nazwa: 'Produkt testowy',
    kategoria: 'Kategoria testowa',
    jednostkaMiary: 'szt.',
    cena: 100,
  };

  const productRequest: ProduktRequest = {
    nazwa: 'Produkt testowy',
    kategoria: 'Kategoria testowa',
    jednostkaMiary: 'szt.',
    cena: 100,
  };

  it('getAllProducts should fetch products list', async () => {
    mock.onGet('/produkt').reply(200, [mockProduct]);

    const products = await getAllProducts();

    expect(products).toEqual([mockProduct]);
  });

  it('getProductById should fetch single product by id', async () => {
    mock.onGet('/produkt/1').reply(200, mockProduct);

    const product = await getProductById(1);

    expect(product).toEqual(mockProduct);
  });

  it('createProduct should post new product', async () => {
    mock.onPost('/produkt', productRequest).reply(201);

    await expect(createProduct(productRequest)).resolves.toBeUndefined();

    const postCalls = mock.history.post;
    expect(postCalls.length).toBe(1);
    expect(JSON.parse(postCalls[0].data)).toEqual(productRequest);
  });

  it('updateProduct should put updated product', async () => {
    mock.onPut('/produkt/1', productRequest).reply(200);

    await expect(updateProduct(1, productRequest)).resolves.toBeUndefined();

    const putCalls = mock.history.put;
    expect(putCalls.length).toBe(1);
    expect(JSON.parse(putCalls[0].data)).toEqual(productRequest);
    expect(putCalls[0].url).toBe('/produkt/1');
  });

  it('deleteProduct should delete product by id', async () => {
    mock.onDelete('/produkt/1').reply(204);

    await expect(deleteProduct(1)).resolves.toBeUndefined();

    const deleteCalls = mock.history.delete;
    expect(deleteCalls.length).toBe(1);
    expect(deleteCalls[0].url).toBe('/produkt/1');
  });
});
