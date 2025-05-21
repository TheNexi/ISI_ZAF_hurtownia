import React, { useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../../auth/AuthContext';
import axios from 'axios';
import type { FormProps } from 'antd';
import { Button, Checkbox, Form, Input } from 'antd';

type FieldType = {
  username?: string;
  password?: string;
  remember?: boolean;
};

const Login: React.FC = () => {
  const { setAuthenticated, isAuthenticated } = useContext(AuthContext);
  const navigate = useNavigate();

  const onFinish: FormProps<FieldType>['onFinish'] = async (values) => {
    try {
      await axios.post(
        'http://localhost:8080/api/auth/login',
        {
          username: values.username,
          password: values.password,
        },
        { withCredentials: true }
      );
      setAuthenticated(true);
      navigate('/');
    } catch {
      alert('Nieprawidłowy login lub hasło');
    }
  };

  const onFinishFailed: FormProps<FieldType>['onFinishFailed'] = (errorInfo) => {
    console.log('Failed:', errorInfo);
  };

  useEffect(() => {
    if (isAuthenticated) {
      navigate('/');
    }
  }, [isAuthenticated, navigate]);

  const handleGoogleLogin = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  };

  const handleRegister = () => {
    navigate('/register');
  };

  return (
    <div style={{ maxWidth: 600, margin: 'auto', padding: '2rem' }}>
      <h2>Logowanie</h2>
      <Form
        name="login-form"
        labelCol={{ span: 8 }}
        wrapperCol={{ span: 16 }}
        initialValues={{ remember: true }}
        onFinish={onFinish}
        onFinishFailed={onFinishFailed}
        autoComplete="off"
      >
        <Form.Item<FieldType>
          label="Username"
          name="username"
          rules={[{ required: true, message: 'Please input your username!' }]}
        >
          <Input />
        </Form.Item>

        <Form.Item<FieldType>
          label="Password"
          name="password"
          rules={[{ required: true, message: 'Please input your password!' }]}
        >
          <Input.Password />
        </Form.Item>
{/*
        <Form.Item<FieldType> name="remember" valuePropName="checked" wrapperCol={{ offset: 8, span: 16 }}>
          <Checkbox>Remember me</Checkbox>
        </Form.Item>
*/}
        <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
          <Button type="primary" htmlType="submit" style={{ marginRight: '1rem' }}>
            Zaloguj
          </Button>
          <Button type="default" onClick={handleGoogleLogin} style={{ marginRight: '1rem' }}>
            Zaloguj przez Google
          </Button>
          <Button type="link" onClick={handleRegister}>
            Rejestracja
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default Login;
