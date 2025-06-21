import axios, { type InternalAxiosRequestConfig } from 'axios';

export const instance = axios.create({
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

export interface ProduktResponse {
  id: number;
  nazwa: string;
  kategoria: string;
  jednostkaMiary: string;
  cena: number;
}

export interface ProduktRequest {
  nazwa: string;
  kategoria: string;
  jednostkaMiary: string;
  cena: number;
}

export const getAllProducts = async (): Promise<ProduktResponse[]> => {
    const response = await instance.get<ProduktResponse[]>('/produkt');
    return response.data;
};

export const getProductById = async (id: number): Promise<ProduktResponse> => {
  const response = await instance.get<ProduktResponse>(`/produkt/${id}`);
  return response.data;
};
