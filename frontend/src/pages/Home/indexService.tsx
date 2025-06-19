import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true,
});

export interface Produkt {
  id: number;
  kategoria: string;
}

export interface Dostawca {
  id: number;
  nazwa: string;
}

export interface Klient {
  id: number;
  nazwa: string;
}

export const getProducts = async (): Promise<Produkt[]> => {
  const res = await axiosInstance.get<Produkt[]>('/produkt');
  return res.data;
};

export const getDostawcy = async (): Promise<Dostawca[]> => {
  const res = await axiosInstance.get<Dostawca[]>('/dostawca');
  return res.data;
};

export const getKlienci = async (): Promise<Klient[]> => {
  const res = await axiosInstance.get<Klient[]>('/klient');
  return res.data;
};
