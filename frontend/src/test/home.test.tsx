jest.mock('recharts', () => ({
  PieChart: ({ children }: any) => <div data-testid="pie-chart">{children}</div>,
  Pie: ({ data }: any) => (
    <div data-testid="pie">{data?.map((d: any) => <div key={d.name}>{d.name}: {d.value}</div>)}</div>
  ),
  Cell: () => null,
  Tooltip: () => <div />,
  ResponsiveContainer: ({ children }: any) => <div>{children}</div>,
}));

import { render, screen, waitFor } from '@testing-library/react';
import HomeUser from '../pages/Home/index';
import * as service from '../pages/Home/indexService';
import { MemoryRouter } from 'react-router-dom';

const mockProducts = [
  { id: 1, kategoria: 'Elektronika' },
  { id: 2, kategoria: 'RTV' },
  { id: 3, kategoria: 'RTV' },
];

const mockDostawcy = [
  { id: 1, nazwa: 'Dostawca A' },
  { id: 2, nazwa: 'Dostawca B' },
];

const mockKlienci = [
  { id: 1, nazwa: 'Klient A' },
];

jest.mock('../pages/Home/indexService', () => ({
  getProducts: jest.fn(),
  getDostawcy: jest.fn(),
  getKlienci: jest.fn(),
}));

const getProductsMock = service.getProducts as jest.Mock;
const getDostawcyMock = service.getDostawcy as jest.Mock;
const getKlienciMock = service.getKlienci as jest.Mock;

describe('HomeUser', () => {
  beforeEach(() => {
    getProductsMock.mockResolvedValue(mockProducts);
    getDostawcyMock.mockResolvedValue(mockDostawcy);
    getKlienciMock.mockResolvedValue(mockKlienci);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test('renders statistics correctly', async () => {
    render(
      <MemoryRouter>
        <HomeUser />
      </MemoryRouter>
    );

    const getCountNearText = async (headerText: RegExp | string) => {
      const header = await screen.findByText(headerText);
      return header.parentElement?.querySelector('.ant-statistic-content-value-int')?.textContent;
    };

    expect(await screen.findByText(/aż tyle produktów do wyboru!/i)).toBeInTheDocument();
    expect(await getCountNearText(/aż tyle produktów do wyboru!/i)).toBe('3');

    expect(await screen.findByText(/wiele dostępnych kategorii!/i)).toBeInTheDocument();
    expect(await getCountNearText(/wiele dostępnych kategorii!/i)).toBe('2');

    expect(await screen.findByText(/tylko sprawdzeni dostawcy!/i)).toBeInTheDocument();
    expect(await getCountNearText(/tylko sprawdzeni dostawcy!/i)).toBe('2');

    expect(await screen.findByText(/sami zadowoleni klienci!/i)).toBeInTheDocument();
    expect(await getCountNearText(/sami zadowoleni klienci!/i)).toBe('1');
  });

    test('renders pie chart with correct slices', async () => {
    render(
        <MemoryRouter>
        <HomeUser />
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
    getDostawcyMock.mockRejectedValue(new Error('API error'));
    getKlienciMock.mockRejectedValue(new Error('API error'));

    jest.spyOn(console, 'error').mockImplementation(() => {});

    render(
      <MemoryRouter>
        <HomeUser />
      </MemoryRouter>
    );

    const zeroElements = await screen.findAllByText('0');
    expect(zeroElements.length).toBeGreaterThanOrEqual(4);

    (console.error as jest.Mock).mockRestore();
  });
});
