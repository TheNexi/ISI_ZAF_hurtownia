import React, { useEffect, useState } from 'react';
import { Table, Popconfirm } from 'antd';
import axios from 'axios';
import type { ColumnsType } from 'antd/es/table';
import {
  ZamowienieResponse,
  deleteOrder,
  getKlienci,
  getDostawcy,
  getMagazyny,
  getCzasy,
  Klient,
  Dostawca,
  Magazyn,
  Czas
} from './indexService';

interface OrderListProps {
  orders: ZamowienieResponse[];
  onRefresh: () => void;
  onEdit: (order: ZamowienieResponse) => void;
}

const OrderList: React.FC<OrderListProps> = ({ orders, onRefresh, onEdit }) => {
  const [klienci, setKlienci] = useState<Klient[]>([]);
  const [dostawcy, setDostawcy] = useState<Dostawca[]>([]);
  const [magazyny, setMagazyny] = useState<Magazyn[]>([]);
  const [czasy, setCzasy] = useState<Czas[]>([]);

  useEffect(() => {
    const fetchDictionaries = async () => {
      const [k, d, m, c] = await Promise.all([getKlienci(), getDostawcy(), getMagazyny(), getCzasy()]);
      setKlienci(k);
      setDostawcy(d);
      setMagazyny(m);
      setCzasy(c);
    };
    fetchDictionaries();
  }, []);

  const getKlientName = (id: number) => klienci.find(k => k.id === id)?.nazwa || `ID ${id}`;
  const getDostawcaName = (id: number) => dostawcy.find(d => d.id === id)?.nazwa || `ID ${id}`;
  const getMagazynName = (id: number) => magazyny.find(m => m.id === id)?.nazwa || `ID ${id}`;

  const getCzasOpis = (id: number) => {
    const c = czasy.find(c => c.id === id);
    if (!c) return `ID ${id}`;
    const dzienTyg = c.dzien_tygodnia || null;
    const dataStr = `${c.dzien.toString().padStart(2, '0')}.${c.miesiac.toString().padStart(2, '0')}.${c.rok}`;
    return dzienTyg ? `${dataStr} (${dzienTyg})` : dataStr;
  };

  const handleDelete = async (id: number) => {
    try {
      await deleteOrder(id);
      onRefresh();
    } catch (error) {
      console.error('Błąd podczas usuwania:', error);
    }
  };

  const handleApprovePayment = async (orderId: number) => {
  try {
    await axios.post(`http://localhost:8080/zamowienie/${orderId}/zatwierdz-offline`, {}, { withCredentials: true });
    onRefresh();
  } catch (error) {
    console.error('Błąd podczas zatwierdzania płatności offline:', error);
  }
  };



  const columns: ColumnsType<ZamowienieResponse> = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Klient',
      dataIndex: 'idKlient',
      key: 'idKlient',
      render: (id: number) => getKlientName(id),
    },
    {
      title: 'Czas',
      dataIndex: 'idCzas',
      key: 'idCzas',
      render: (id: number) => getCzasOpis(id),
    },
    {
      title: 'Dostawca',
      dataIndex: 'idDostawca',
      key: 'idDostawca',
      render: (id: number) => getDostawcaName(id),
    },
    {
      title: 'Magazyn',
      dataIndex: 'idMagazyn',
      key: 'idMagazyn',
      render: (id: number) => getMagazynName(id),
    },
    {
      title: 'Wartość całkowita',
      dataIndex: 'wartoscCalkowita',
      key: 'wartoscCalkowita',
      render: (value: number) => `${value.toFixed(2)} zł`,
    },
    {
      title: 'Status płatności',
      dataIndex: 'statusPlatnosci',
      key: 'statusPlatnosci',
      render: (status: string) => {
        const color = {
          PENDING: 'orange',
          SUCCESS: 'green',
          FAILED: 'red',
          OFFLINE: 'blue',
        }[status] || 'black';
        return <span style={{ color }}>{status}</span>;
      },
    }
    ,
    {
      title: 'Akcje',
      key: 'akcje',
      render: (_text, record) => (
        <div style={{ display: 'flex', gap: 8 }}>
          <button className="btn-action btn-edit" onClick={() => onEdit(record)}>Edytuj</button>
          <Popconfirm
            title="Na pewno usunąć?"
            onConfirm={() => handleDelete(record.id)}
            okText="Tak"
            cancelText="Nie"
            okButtonProps={{ className: 'btn-action btn-edit' }}
            cancelButtonProps={{ className: 'btn-action btn-delete' }}
          >
            <button className="btn-action btn-delete">Usuń</button>
          </Popconfirm>

          {record.statusPlatnosci === 'PENDING' && (
          <Popconfirm
            title="Zatwierdzić płatność offline?"
            onConfirm={() => handleApprovePayment(record.id)}
            okText="Tak"
            cancelText="Nie"
            okButtonProps={{ className: 'btn-action btn-edit' }}
            cancelButtonProps={{ className: 'btn-action btn-delete' }}
          >
            <button className="btn-action btn-success">Zatwierdź płatność</button>
          </Popconfirm>
          )}
        </div>
      ),
    },
  ];

  return (
    <div className="table-responsive">
      <Table
        dataSource={orders}
        columns={columns}
        rowKey="id"
        pagination={{ pageSize: 10 }}
        scroll={{ x: 'max-content' }}
      />
    </div>
  );
};

export default OrderList;
