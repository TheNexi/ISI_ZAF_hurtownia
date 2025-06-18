import axios, { type InternalAxiosRequestConfig } from 'axios';

const instance = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true,
});

instance.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('token');
    if (token) {
      if (!config.headers) {
        config.headers = new axios.AxiosHeaders();
      }
      config.headers.set('Authorization', `Bearer ${token}`);
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default instance;

export interface Klient {
  id: number;
  nazwa: string;
}

export interface Dostawca {
  id: number;
  nazwa: string;
}

export interface Magazyn {
  id: number;
  nazwa: string;
}

export interface Czas {
  id: number;
  opis: string;
  dzien: number;
  miesiac: number;
  rok: number;
  kwartal?: number;
  dzien_tygodnia?: string;  
}

export interface ZamowienieResponse {
  id: number;
  idKlient: number;
  idCzas: number;
  idDostawca: number;
  idMagazyn: number;
  wartoscCalkowita: number;
}

export const getAllOrders = async (): Promise<ZamowienieResponse[]> => {
  const response = await instance.get<ZamowienieResponse[]>('/zamowienie');
  return response.data;
};

export const getOrderById = async (id: number): Promise<ZamowienieResponse> => {
  const response = await instance.get<ZamowienieResponse>(`/zamowienie/${id}`);
  return response.data;
};

export const updateOrder = async (id: number, data: Partial<ZamowienieResponse>): Promise<void> => {
  await instance.put(`/zamowienie/${id}`, data);
};

export const deleteOrder = async (id: number): Promise<void> => {
  await instance.delete(`/zamowienie/${id}`);
};

export const updateCzas = async (id: number, data: Partial<Czas>): Promise<void> => {
  await instance.put(`/czas/${id}`, data);
};

export const getKlienci = async (): Promise<Klient[]> => {
  const res = await instance.get<Klient[]>('/klient');
  return res.data;
};

export const getDostawcy = async (): Promise<Dostawca[]> => {
  const res = await instance.get<Dostawca[]>('/dostawca');
  return res.data;
};

export const getMagazyny = async (): Promise<Magazyn[]> => {
  const res = await instance.get<Magazyn[]>('/magazyn');
  return res.data;
};

export const getCzasy = async (): Promise<Czas[]> => {
  const response = await instance.get<Czas[]>('/czas');
  return response.data;
};