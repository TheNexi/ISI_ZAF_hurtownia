import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { MemoryRouter, Route, Routes, useNavigate, useLocation } from 'react-router-dom';

const mockedNavigate = jest.fn();
const mockedLocation = {
  search: '',
};

jest.mock('react-router-dom', () => {
  const originalModule = jest.requireActual('react-router-dom');
  return {
    ...originalModule,
    useNavigate: () => mockedNavigate,
    useLocation: () => mockedLocation,
  };
});

const PaymentResult: React.FC = () => {
  const navigate = useNavigate();
  const query = new URLSearchParams(useLocation().search);
  const errorCode = query.get('error');

  const getErrorMessage = (code: string | null): string => {
    switch (code) {
      case '501':
        return 'Płatność została odrzucona. Prosimy spróbować ponownie lub wybrać inną metodę płatności.';
      case '502':
        return 'Błąd serwera płatności. Spróbuj ponownie później.';
      case '503':
        return 'Usługa płatności jest tymczasowo niedostępna. Prosimy spróbować za chwilę.';
      case '504':
        return 'Przekroczono limit czasu odpowiedzi od bramki płatności.';
      default:
        return 'Wystąpił nieznany błąd podczas przetwarzania płatności.';
    }
  };

  const handleBackToHome = (): void => {
    navigate('/');
  };

  return (
    <div className="payment-result">
      {errorCode ? (
        <>
          <h2 className="payment-result-title">Wystąpił błąd!</h2>
          <p className="payment-result-text">{getErrorMessage(errorCode)}</p>
          <button className="btn-browse" onClick={handleBackToHome}>
            Powrót do strony głównej
          </button>
        </>
      ) : (
        <>
          <h2 className="payment-result-title">Dziękujemy za płatność!</h2>
          <p className="payment-result-text">Twoje zamówienie zostało opłacone. Wkrótce je zrealizujemy.</p>
          <button className="btn-browse" onClick={handleBackToHome}>
            Powrót do strony głównej
          </button>
        </>
      )}
    </div>
  );
};

describe('PaymentResult component', () => {
  beforeEach(() => {
    mockedNavigate.mockClear();
  });

  const renderWithRouter = (initialEntries: string[]) => {
    mockedLocation.search = new URL(initialEntries[0], 'http://localhost').search;

    return render(
      <MemoryRouter initialEntries={initialEntries}>
        <Routes>
          <Route path="*" element={<PaymentResult />} />
        </Routes>
      </MemoryRouter>
    );
  };

  it('shows success message when there is no error query param', () => {
    renderWithRouter(['/payment-result']);

    expect(screen.getByText(/Dziękujemy za płatność!/i)).toBeInTheDocument();
    expect(screen.getByText(/Twoje zamówienie zostało opłacone/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Powrót do strony głównej/i })).toBeInTheDocument();
  });

  it('shows correct error message for known error codes', () => {
    const errorMessages: Record<string, string> = {
      '501': 'Płatność została odrzucona. Prosimy spróbować ponownie lub wybrać inną metodę płatności.',
      '502': 'Błąd serwera płatności. Spróbuj ponownie później.',
      '503': 'Usługa płatności jest tymczasowo niedostępna. Prosimy spróbować za chwilę.',
      '504': 'Przekroczono limit czasu odpowiedzi od bramki płatności.',
    };

    for (const [code, message] of Object.entries(errorMessages)) {
      const { unmount } = renderWithRouter([`/payment-result?error=${code}`]);

      expect(screen.getByText(/Wystąpił błąd!/i)).toBeInTheDocument();
      expect(screen.getByText(message)).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /Powrót do strony głównej/i })).toBeInTheDocument();

      unmount();
    }
  });

  it('shows default error message for unknown error code', () => {
    renderWithRouter(['/payment-result?error=999']);

    expect(screen.getByText(/Wystąpił błąd!/i)).toBeInTheDocument();
    expect(screen.getByText(/Wystąpił nieznany błąd podczas przetwarzania płatności./i)).toBeInTheDocument();
  });

  it('navigates to home page on button click', () => {
    renderWithRouter(['/payment-result?error=501']);

    const button = screen.getByRole('button', { name: /Powrót do strony głównej/i });
    fireEvent.click(button);

    expect(mockedNavigate).toHaveBeenCalledWith('/');
  });
});
