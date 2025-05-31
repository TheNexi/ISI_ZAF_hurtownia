import React, { useEffect } from 'react';
import { Form, Input, InputNumber } from 'antd';
import { ProduktRequest, ProduktResponse, createProduct, updateProduct } from './indexService';

interface ProductFormProps {
  onUpdate: () => void;
  editingProduct: ProduktResponse | null;
  setEditingProduct: (product: ProduktResponse | null) => void;
}

const ProductForm: React.FC<ProductFormProps> = ({ onUpdate, editingProduct, setEditingProduct }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (editingProduct) {
      form.setFieldsValue(editingProduct);
    } else {
      form.resetFields();
    }
  }, [editingProduct, form]);

  const onFinish = async (values: ProduktRequest) => {
    try {
      if (editingProduct) {
        await updateProduct(editingProduct.id, values);
      } else {
        await createProduct(values);
      }
      onUpdate();
      setEditingProduct(null);
      form.resetFields();
    } catch (err) {
      console.error('Błąd zapisu produktu:', err);
    }
  };

  const handleCancelEdit = () => {
    setEditingProduct(null);
    form.resetFields();
  };

  return (
    <div className="form-wrapper">
      <h2 className="form-title">{editingProduct ? 'Edytuj Produkt' : 'Dodaj Nowy Produkt'}</h2>

      <Form form={form} layout="vertical" onFinish={onFinish} className="styled-form">
        <Form.Item
          label="Nazwa"
          name="nazwa"
          rules={[{ required: true, message: 'Podaj nazwę produktu' }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="Kategoria"
          name="kategoria"
          rules={[{ required: true, message: 'Podaj kategorię' }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="Jednostka Miary"
          name="jednostkaMiary"
          rules={[{ required: true, message: 'Podaj jednostkę miary' }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="Cena"
          name="cena"
          rules={[{ required: true, message: 'Podaj cenę' }]}
        >
          <InputNumber min={0} step={0.01} style={{ width: '100%' }} />
        </Form.Item>

        <button type="submit" className="btn btn-primary">
          {editingProduct ? 'Zapisz zmiany' : 'Dodaj produkt'}
        </button>

        {editingProduct && (
          <button type="button" className="btn btn-secondary" onClick={handleCancelEdit}>
            Anuluj
          </button>
        )}
      </Form>
    </div>
  );
};

export default ProductForm;
