import React from 'react'
import DashboardTabs from '../../components/DashboardTabs'

const Home = () => {
  return (
  <div className="container">
    <DashboardTabs role="USER"/> 
    <div className="page-content">
      <h1>Zawartość strony głównej</h1>
    </div>
  </div>

  )
}

export default Home
