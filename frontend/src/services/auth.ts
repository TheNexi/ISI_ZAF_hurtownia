import axios from 'axios';

export const login = async (data: { username: string; password: string }) => {
  try {
    const res = await axios.post('http://localhost:8080/api/auth/login', data);
    return res.data.token; //JWT
  } catch (error) {
    throw error;
  }
};


export const register = async (data: { username: string; email: string; password: string }) => {
  return axios.post('http://localhost:8080/auth/register', data)
}
