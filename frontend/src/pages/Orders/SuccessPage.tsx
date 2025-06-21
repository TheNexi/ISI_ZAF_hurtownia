import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

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

  if (errorCode) {
    return (
      <div className="payment-result">
        <h2>Wystąpił błąd!</h2>
        <p>{getErrorMessage(errorCode)}</p>
        <button onClick={() => navigate('/')}>Powrót do strony głównej</button>
      </div>
    );
  }

  return (
    <div className="payment-result">
      <h2>Dziękujemy za płatność!</h2>
      <p>Twoje zamówienie zostało opłacone. Wkrótce je zrealizujemy.</p>
      <button onClick={() => navigate('/')}>Powrót do strony głównej</button>
    </div>
  );
};

export default PaymentResult;
