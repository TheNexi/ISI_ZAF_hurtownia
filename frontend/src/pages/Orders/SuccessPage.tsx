import { useLocation, useNavigate } from 'react-router-dom';
import { Button } from 'antd';

const PaymentResult = () => {
  const navigate = useNavigate();
  const query = new URLSearchParams(useLocation().search);
  const errorCode = query.get('error');

  const getErrorMessage = (code?: string) => {
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

  const handleBackToHome = () => {
    navigate('/');
  };

  return (
    <div className="payment-result">
      {errorCode ? (
        <>
          <h2 className="payment-result-title">Wystąpił błąd!</h2>
          <p className="payment-result-text">{getErrorMessage(errorCode)}</p>
          <Button className="btn-browse" onClick={handleBackToHome}>
            Powrót do strony głównej
          </Button>
        </>
      ) : (
        <>
          <h2 className="payment-result-title">Dziękujemy za płatność!</h2>
          <p className="payment-result-text">Twoje zamówienie zostało opłacone. Wkrótce je zrealizujemy.</p>
          <Button className="btn-browse" onClick={handleBackToHome}>
            Powrót do strony głównej
          </Button>
        </>
      )}
    </div>
  );
};

export default PaymentResult;
