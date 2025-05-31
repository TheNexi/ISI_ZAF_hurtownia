import React, { useEffect, useState } from 'react';
import { Form, InputNumber, Select, DatePicker } from 'antd';
import moment, { Moment } from 'moment';
import {
  ZamowienieResponse,
  updateOrder,
  updateCzas,
  getKlienci,
  getDostawcy,
  getMagazyny,
  getCzasy,
  Klient,
  Dostawca,
  Magazyn,
  Czas,
} from './indexService';

interface OrderFormProps {
  onUpdate: () => void;
  editingOrder: ZamowienieResponse | null;
  setEditingOrder: (order: ZamowienieResponse | null) => void;
}

const dniTygodnia = ['Niedziela', 'Poniedziałek', 'Wtorek', 'Środa', 'Czwartek', 'Piątek', 'Sobota'];

const OrderForm: React.FC<OrderFormProps> = ({ onUpdate, editingOrder, setEditingOrder }) => {
  const [form] = Form.useForm();

  const [klienci, setKlienci] = useState<Klient[]>([]);
  const [dostawcy, setDostawcy] = useState<Dostawca[]>([]);
  const [magazyny, setMagazyny] = useState<Magazyn[]>([]);
  const [czasy, setCzasy] = useState<Czas[]>([]);
  const [selectedDate, setSelectedDate] = useState<Moment | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      setKlienci(await getKlienci());
      setDostawcy(await getDostawcy());
      setMagazyny(await getMagazyny());
      setCzasy(await getCzasy());
    };
    fetchData();
  }, []);

  useEffect(() => {
    if (editingOrder && czasy.length > 0) {
      const czas = czasy.find((c) => c.id === editingOrder.idCzas);
      if (czas) {
        const date = moment(`${czas.rok}-${czas.miesiac}-${czas.dzien}`, 'YYYY-M-D');
        setSelectedDate(date);

        form.setFieldsValue({
          idKlient: editingOrder.idKlient,
          idDostawca: editingOrder.idDostawca,
          idMagazyn: editingOrder.idMagazyn,
          wartoscCalkowita: editingOrder.wartoscCalkowita,
          data: date,
        });
      } else {
        setSelectedDate(null);
        form.resetFields();
      }
    } else {
      form.resetFields();
      setSelectedDate(null);
    }
  }, [editingOrder, czasy, form]);

  const onFinish = async (values: any) => {
    try {
      if (!editingOrder) return;
      if (!selectedDate) throw new Error('Data jest wymagana');

      const dzien = selectedDate.date();
      const miesiac = selectedDate.month() + 1;
      const rok = selectedDate.year();
      const dzien_tygodnia_str = dniTygodnia[selectedDate.day()];
      const kwartal = Math.ceil(miesiac / 3);

      await updateOrder(editingOrder.id, {
        idKlient: values.idKlient,
        idDostawca: values.idDostawca,
        idMagazyn: values.idMagazyn,
        wartoscCalkowita: values.wartoscCalkowita,
        idCzas: editingOrder.idCzas,
      });

      await updateCzas(editingOrder.idCzas, {
        dzien,
        miesiac,
        rok,
        dzien_tygodnia: dzien_tygodnia_str,
        kwartal,
      });

      onUpdate();
      setEditingOrder(null);
      form.resetFields();
      setSelectedDate(null);
    } catch (err) {
      console.error('Błąd zapisu:', err);
    }
  };

  const handleCancelEdit = () => {
    setEditingOrder(null);
    form.resetFields();
    setSelectedDate(null);
  };

  const selectProps = {
    showSearch: true,
    optionFilterProp: 'children',
    filterOption: (input: string, option: any) =>
      typeof option?.children === 'string' && option.children.toLowerCase().includes(input.toLowerCase()),
    filterSort: (optionA: any, optionB: any) =>
      (optionA.children as string).localeCompare(optionB.children as string),
  };

  return (
    <div className="form-wrapper">
      <h2 className="form-title">Edytuj Zamówienie</h2>

      <Form form={form} layout="vertical" onFinish={onFinish} className="styled-form">
        <Form.Item label="Klient" name="idKlient" rules={[{ required: true, message: 'Wybierz klienta' }]}>
          <Select {...selectProps} placeholder="Wybierz klienta">
            {klienci.map((k) => (
              <Select.Option key={k.id} value={k.id}>
                {k.nazwa}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item label="Dostawca" name="idDostawca" rules={[{ required: true, message: 'Wybierz dostawcę' }]}>
          <Select {...selectProps} placeholder="Wybierz dostawcę">
            {dostawcy.map((d) => (
              <Select.Option key={d.id} value={d.id}>
                {d.nazwa}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item label="Magazyn" name="idMagazyn" rules={[{ required: true, message: 'Wybierz magazyn' }]}>
          <Select {...selectProps} placeholder="Wybierz magazyn">
            {magazyny.map((m) => (
              <Select.Option key={m.id} value={m.id}>
                {m.nazwa}
              </Select.Option>
            ))}
          </Select>
        </Form.Item>

        <Form.Item label="Data" name="data" rules={[{ required: true, message: 'Wybierz datę' }]}>
          <DatePicker
            value={selectedDate}
            onChange={(date) => setSelectedDate(date)}
            format="DD.MM.YYYY"
            disabledDate={(current) => current && current > moment().endOf('day')}
          />
        </Form.Item>

        <Form.Item
          label="Wartość Całkowita"
          name="wartoscCalkowita"
          rules={[{ required: true, message: 'Podaj wartość' }]}
        >
          <InputNumber min={0} step={0.01} style={{ width: '100%' }} />
        </Form.Item>

        <button type="submit" className="btn btn-primary" style={{ marginRight: 10 }}>
          Zapisz zmiany
        </button>

        {editingOrder && (
          <button type="button" className="btn btn-secondary" onClick={handleCancelEdit}>
            Anuluj
          </button>
        )}
      </Form>
    </div>
  );
};

export default OrderForm;
