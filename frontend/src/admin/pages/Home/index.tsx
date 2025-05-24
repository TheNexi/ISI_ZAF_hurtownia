import React from 'react'
import DashboardTabs from '../../../components/DashboardTabs'

const AdminHome = () => {
  return (
    <div className="container">
      <DashboardTabs role="ADMIN"/>
      <div className="page-content">
        <h1>Jesteś na stronie administratora</h1>
      </div>
    </div>
  )
}

export default AdminHome
