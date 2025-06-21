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

export interface ZamowienieResponse {
  id: number;
  idKlient: number;
  idCzas: number;
  idDostawca: number;
  idMagazyn: number;
  wartoscCalkowita: number;
  statusPlatnosci: string;
  produkty?: ProduktWZamowieniuResponse[];
}

export interface ProduktWZamowieniuResponse {
  idProdukt: number;
  ilosc: number;
  cena: number;
}

export const getMyOrders = async (): Promise<ZamowienieResponse[]> => {
  const response = await axios.get('http://localhost:8080/zamowienie/my', { withCredentials: true });
  return response.data;
};