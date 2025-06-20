import { render, screen, waitFor } from '@testing-library/react';
import HomeAdmin from '../admin/pages/Home/index';  
import * as service from '../admin/pages/Home/indexService';
import { MemoryRouter } from 'react-router-dom';

jest.mock('recharts', () => ({
  PieChart: ({ children }: any) => <div data-testid="pie-chart">{children}</div>,
  Pie: ({ data }: any) => (
    <div data-testid="pie">{data?.map((d: any) => <div key={d.name}>{d.name}: {d.value}</div>)}</div>
  ),
  Cell: () => null,
  Tooltip: () => <div />,
  ResponsiveContainer: ({ children }: any) => <div>{children}</div>,
  LineChart: ({ children }: any) => <div data-testid="line-chart">{children}</div>,
  Line: () => <div data-testid="line" />,
  XAxis: () => <div />,
  YAxis: () => <div />,
  CartesianGrid: () => <div />,
  Legend: () => <div />,
}));

const mockProducts = [
  { id: 1, kategoria: 'Elektronika' },
  { id: 2, kategoria: 'RTV' },
  { id: 3, kategoria: 'RTV' },
];

const mockOrders = [
  { id: 1, idCzas: 1 },
  { id: 2, idCzas: 2 },
  { id: 3, idCzas: 1 },
];

const mockCzasy = [
  { id: 1, rok: 2025, miesiac: 6, dzien: 19 },
  { id: 2, rok: 2025, miesiac: 6, dzien: 18 },
];

const mockDostawcy = [
  { id: 1, nazwa: 'Dostawca A' },
  { id: 2, nazwa: 'Dostawca B' },
];

const mockKlienci = [
  { id: 1, nazwa: 'Klient A' },
];

jest.mock('../admin/pages/Home/indexService', () => ({
  getProducts: jest.fn(),
  getAllOrders: jest.fn(),
  getCzasy: jest.fn(),
  getDostawcy: jest.fn(),
  getKlienci: jest.fn(),
}));

const getProductsMock = service.getProducts as jest.Mock;
const getAllOrdersMock = service.getAllOrders as jest.Mock;
const getCzasyMock = service.getCzasy as jest.Mock;
const getDostawcyMock = service.getDostawcy as jest.Mock;
const getKlienciMock = service.getKlienci as jest.Mock;

describe('HomeAdmin', () => {
  beforeEach(() => {
    getProductsMock.mockResolvedValue(mockProducts);
    getAllOrdersMock.mockResolvedValue(mockOrders);
    getCzasyMock.mockResolvedValue(mockCzasy);
    getDostawcyMock.mockResolvedValue(mockDostawcy);
    getKlienciMock.mockResolvedValue(mockKlienci);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

    test('renders statistics correctly', async () => {
    render(
        <MemoryRouter>
        <HomeAdmin />
        </MemoryRouter>
    );

    const produktyStat = await screen.findByText('Produkty', { selector: '.ant-statistic-title' });
    expect(produktyStat).toBeInTheDocument();

    const zamowieniaStat = await screen.findByText('Zamówienia', { selector: '.ant-statistic-title' });
    expect(zamowieniaStat).toBeInTheDocument();

    const dostawcyStat = await screen.findByText('Dostawcy', { selector: '.ant-statistic-title' });
    expect(dostawcyStat).toBeInTheDocument();

    const klienciStat = await screen.findByText('Klienci', { selector: '.ant-statistic-title' });
    expect(klienciStat).toBeInTheDocument();

    const getCountNearText = async (headerText: string) => {
        const header = await screen.findByText(headerText, { selector: '.ant-statistic-title' });
        return header.parentElement?.querySelector('.ant-statistic-content-value-int')?.textContent;
    };

    expect(await getCountNearText('Produkty')).toBe('3');
    expect(await getCountNearText('Zamówienia')).toBe('3');
    expect(await getCountNearText('Dostawcy')).toBe('2');
    expect(await getCountNearText('Klienci')).toBe('1');
    });

  test('renders line chart with order data for last 7 days', async () => {
    render(
      <MemoryRouter>
        <HomeAdmin />
      </MemoryRouter>
    );

    expect(await screen.findByTestId('line-chart')).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByTestId('line')).toBeInTheDocument();
    });
  });

  test('renders pie chart with correct product category slices', async () => {
    render(
      <MemoryRouter>
        <HomeAdmin />
      </MemoryRouter>
    );

    const pie = await screen.findByTestId('pie');
    await waitFor(() => {
      expect(pie.textContent).toMatch(/Elektronika:\s*1/);
      expect(pie.textContent).toMatch(/RTV:\s*2/);
    });
  });

  test('handles API errors gracefully', async () => {
    getProductsMock.mockRejectedValue(new Error('API error'));
    getAllOrdersMock.mockRejectedValue(new Error('API error'));
    getCzasyMock.mockRejectedValue(new Error('API error'));
    getDostawcyMock.mockRejectedValue(new Error('API error'));
    getKlienciMock.mockRejectedValue(new Error('API error'));

    jest.spyOn(console, 'error').mockImplementation(() => {});

    render(
      <MemoryRouter>
        <HomeAdmin />
      </MemoryRouter>
    );

    const zeroElements = await screen.findAllByText('0');
    expect(zeroElements.length).toBeGreaterThanOrEqual(4);

    (console.error as jest.Mock).mockRestore();
  });
});
