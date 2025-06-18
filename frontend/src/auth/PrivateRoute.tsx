import React, { useContext, useState, useEffect, JSX } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { AuthContext } from './AuthContext'
import { Modal, Button } from 'antd'

type PrivateRouteProps = {
  children: JSX.Element;
  requiredRole?: string;
}

const PrivateRoute = ({ children, requiredRole }: PrivateRouteProps) => {
  const { isAuthenticated, user } = useContext(AuthContext);
  const location = useLocation();

  const [modalVisible, setModalVisible] = useState(false);
  const [redirectToLogin, setRedirectToLogin] = useState(false);
  const [stayOnPage, setStayOnPage] = useState(false);

  useEffect(() => {
    if (!isAuthenticated) {
      setModalVisible(true);
      setStayOnPage(false);
    } else {
      setModalVisible(false);
      setStayOnPage(false);
    }
  }, [isAuthenticated]);

  const handleCancel = () => {
    setModalVisible(false);
    setStayOnPage(true);
  };

  const handleLogin = () => {
    setRedirectToLogin(true);
  };

  if (redirectToLogin) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  if (!isAuthenticated) {
    if (stayOnPage) {
      return <Navigate to="/" replace />;
    }

    return (
      <Modal
        title="Dostęp wymaga zalogowania"
        open={modalVisible}
        onCancel={handleCancel}
        footer={[
          <Button
            key="stay"
            onClick={handleCancel}
            className="btn-secondary"
          >
            Wróć na stronę główną
          </Button>,
          <Button
            key="login"
            type="primary"
            onClick={handleLogin}
            className="btn-primary"
          >
            Przejdź do logowania
          </Button>,
        ]}
        closable={false}
        className="private-route-modal"
      >
        <p className="private-route-message">
          Przykro nam, aby wejść do tej zakładki musisz się zalogować.
        </p>
      </Modal>
    );
  }

  if (requiredRole && user?.role !== requiredRole) {
    return <Navigate to="/" replace />;
  }

  return children;
}

export default PrivateRoute
