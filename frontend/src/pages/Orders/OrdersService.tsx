import axios from 'axios';

export const createOrder = async (orderData: {
  idKlient: number;
  idCzas: number;
  idDostawca: number;
  idMagazyn: number;
  wartoscCalkowita: number;
  produkty: { idProdukt: number; ilosc: number }[];
}) => {
  const response = await axios.post('http://localhost:8080/zamowienie/create', orderData, { withCredentials: true });
  return response.data;
};
