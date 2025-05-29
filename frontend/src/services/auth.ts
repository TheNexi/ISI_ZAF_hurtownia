import axios from 'axios';

export const login = async (data: { username: string; password: string }) => {
  try {
    const res = await axios.post('http://localhost:8080/auth/login', data, {
      withCredentials: true,
    });
    return res.data; 
  } catch (error) {
    throw error;
  }
};

export const register = async (data: { username: string; email: string; password: string }) => {
  return axios.post('http://localhost:8080/auth/register', data)
}

export const logout = async () => {
  await axios.post('http://localhost:8080/auth/logout', {}, { withCredentials: true });
};

export const getCurrentUser = async () => {
  const res = await axios.get('http://localhost:8080/auth/me', {
    withCredentials: true,
  });
  return res.data;
};
