import React from 'react';
import { Table, Popconfirm } from 'antd';
import { ProduktResponse, deleteProduct } from './indexService';

interface ProductListProps {
  products: ProduktResponse[];
  onRefresh: () => void;
  onEdit: (product: ProduktResponse) => void;
}

const ProductList: React.FC<ProductListProps> = ({ products, onRefresh, onEdit }) => {
  const handleDelete = async (id: number) => {
    await deleteProduct(id);
    onRefresh();
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id' },
    { title: 'Nazwa', dataIndex: 'nazwa', key: 'nazwa' },
    {
      title: 'Kategoria',
      dataIndex: 'kategoria',
      key: 'kategoria',
      render: (kategoria: string | null) => kategoria || 'Brak kategorii',
    },
    { title: 'Jednostka Miary', dataIndex: 'jednostkaMiary', key: 'jednostkaMiary' },
    {
      title: 'Cena',
      dataIndex: 'cena',
      key: 'cena',
      render: (cena: number) => `${cena.toFixed(2)} zł`,
    },
    {
      title: 'Akcje',
      key: 'akcje',
      render: (_: any, record: ProduktResponse) => (
        <div style={{ display: 'flex', gap: '0.5rem' }}>
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
        </div>
      ),
    },
  ];

  return (
    <div className="table-responsive">
      <Table
        dataSource={products}
        columns={columns}
        rowKey="id"
        pagination={{ pageSize: 10 }}
        scroll={{ x: 'max-content' }}  
      />
    </div>
  );
};

export default ProductList;
