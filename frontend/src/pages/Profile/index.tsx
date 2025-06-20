import DashboardTabs from '../../components/DashboardTabs';
import ProfileForm from './indexForm';

const Profile = () => {
  return (
    <div className="container">
      <DashboardTabs role="USER" />
      <div className="page-content">
        <h1 className="header">Twój profil</h1>
        <ProfileForm />
      </div>
    </div>
  );
};

export default Profile;
