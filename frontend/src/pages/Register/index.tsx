import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Input, Button } from 'antd';
import { register as registerService } from '../../services/auth';

const Register: React.FC = () => {
  const navigate = useNavigate();
  const [form] = Form.useForm();

  const onFinish = async (values: { username: string; email: string; password: string }) => {
    try {
      await registerService({
        username: values.username,
        email: values.email,
        password: values.password,
      });
      navigate('/login');
    } catch (error) {
      alert('Rejestracja nie powiodła się');
    }
  };

  return (
    <Form
      form={form}
      name="register"
      onFinish={onFinish}
      layout="vertical"
      style={{ maxWidth: 400, margin: '0 auto', paddingTop: 50 }}
    >
      <h2>Rejestracja</h2>

      <Form.Item
        name="username"
        label="Login"
        rules={[{ required: true, message: 'Proszę podać login' }]}
      >
        <Input placeholder="Login" />
      </Form.Item>

      <Form.Item
        name="email"
        label="Email"
        rules={[
          { type: 'email', message: 'Niepoprawny email' },
          { required: true, message: 'Proszę podać email' },
        ]}
      >
        <Input placeholder="Email" />
      </Form.Item>

      <Form.Item
        name="password"
        label="Hasło"
        rules={[{ required: true, message: 'Proszę podać hasło' }]}
        hasFeedback
      >
        <Input.Password placeholder="Hasło" />
      </Form.Item>

      <Form.Item
        name="confirm"
        label="Potwierdź hasło"
        dependencies={['password']}
        hasFeedback
        rules={[
          { required: true, message: 'Proszę potwierdzić hasło' },
          ({ getFieldValue }) => ({
            validator(_, value) {
              if (!value || getFieldValue('password') === value) {
                return Promise.resolve();
              }
              return Promise.reject(new Error('Hasła nie są takie same!'));
            },
          }),
        ]}
      >
        <Input.Password placeholder="Potwierdź hasło" />
      </Form.Item>

      <Form.Item>
        <Button type="primary" htmlType="submit" style={{ marginRight: 8 }}>
          Zarejestruj się
        </Button>
        <Button type="default" onClick={() => navigate('/login')}>
          Logowanie
        </Button>
      </Form.Item>
    </Form>
  );
};

export default Register;
