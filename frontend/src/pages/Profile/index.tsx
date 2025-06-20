import React from 'react';
import DashboardTabs from '../../components/DashboardTabs';
import ProfileForm from './ProfileForm';

const Profile = () => {
  return (
    <div className="container">
      <DashboardTabs role="USER" />
      <div className="page-content">
        <h1>Twój profil</h1>
        <ProfileForm />
      </div>
    </div>
  );
};

export default Profile;
