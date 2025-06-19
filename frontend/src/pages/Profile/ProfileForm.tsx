import React, { useEffect } from 'react';
import { Form, Input } from 'antd';
import axios from 'axios';

const ProfileForm: React.FC = () => {
  const [form] = Form.useForm();

  useEffect(() => {
    const fetchClientData = async () => {
      try {
        const res = await axios.get('http://localhost:8080/profile/client', { withCredentials: true });
        if (res.data) {
          form.setFieldsValue(res.data);
        }
      } catch (err) {
        console.error('Błąd podczas pobierania danych klienta', err);
      }
    };

    fetchClientData();
  }, [form]);

  const onFinish = async (values: any) => {
    try {
      await axios.post('http://localhost:8080/profile/updateData', values, { withCredentials: true });
      alert('Dane klienta zapisane');
    } catch (err) {
      alert('Wystąpił błąd przy zapisie danych klienta');
    }
  };

  return (
    <div className="form-wrapper">
      <Form
        form={form}
        layout="vertical"
        onFinish={onFinish}
        className="styled-form"
      >
        <Form.Item label="Nazwa firmy" name="nazwa">
          <Input />
        </Form.Item>

        <Form.Item label="NIP" name="nip">
          <Input />
        </Form.Item>

        <Form.Item label="Kraj" name="kraj">
          <Input />
        </Form.Item>

        <Form.Item label="Miasto" name="miasto">
          <Input />
        </Form.Item>

        <Form.Item label="Ulica" name="ulica">
          <Input />
        </Form.Item>

        <Form.Item label="Kod pocztowy" name="kodPocztowy">
          <Input />
        </Form.Item>

        <button type="submit" className="btn btn-primary">
          Zapisz dane
        </button>
      </Form>
    </div>
  );
};

export default ProfileForm;
