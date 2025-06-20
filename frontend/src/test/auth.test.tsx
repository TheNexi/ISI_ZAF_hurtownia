import axios from 'axios';
import * as authService from '../services/auth'; 

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('auth service', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  test('login should post to /auth/login and return data', async () => {
    const mockResponse = { data: { token: 'fake-token' } };
    mockedAxios.post.mockResolvedValueOnce(mockResponse);

    const data = { username: 'user', password: 'pass' };
    const result = await authService.login(data);

    expect(mockedAxios.post).toHaveBeenCalledWith(
      'http://localhost:8080/auth/login',
      data,
      { withCredentials: true }
    );
    expect(result).toEqual(mockResponse.data);
  });

  test('register should post to /auth/register and return response', async () => {
    const mockResponse = { data: { message: 'Registered' } };
    mockedAxios.post.mockResolvedValueOnce(mockResponse);

    const data = { username: 'user', email: 'user@example.com', password: 'pass' };
    const result = await authService.register(data);

    expect(mockedAxios.post).toHaveBeenCalledWith(
      'http://localhost:8080/auth/register',
      data
    );
    expect(result).toEqual(mockResponse);
  });

  test('logout should post to /auth/logout with credentials', async () => {
    mockedAxios.post.mockResolvedValueOnce({});

    await authService.logout();

    expect(mockedAxios.post).toHaveBeenCalledWith(
      'http://localhost:8080/auth/logout',
      {},
      { withCredentials: true }
    );
  });

  test('getCurrentUser should get /auth/me with credentials and return data', async () => {
    const mockResponse = { data: { id: 1, username: 'user' } };
    mockedAxios.get.mockResolvedValueOnce(mockResponse);

    const result = await authService.getCurrentUser();

    expect(mockedAxios.get).toHaveBeenCalledWith(
      'http://localhost:8080/auth/me',
      { withCredentials: true }
    );
    expect(result).toEqual(mockResponse.data);
  });
});
