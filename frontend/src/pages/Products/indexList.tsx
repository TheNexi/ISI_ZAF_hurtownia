import React from 'react';
import { Table } from 'antd';
import { ProduktResponse } from './indexService';

interface ProductListProps {
  products: ProduktResponse[];
}

const ProductList: React.FC<ProductListProps> = ({ products }) => {
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
  ];

  
  return (
    <div className="table-responsive">
      <Table
        dataSource={products}
        columns={columns}
        rowKey="id"
        pagination={{ pageSize: 10 }}
      />
    </div>
  );
};

export default ProductList;
