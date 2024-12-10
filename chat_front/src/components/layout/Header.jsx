import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@stores/AuthProvider';
import NotiPopup from '@components/noti/NotiPopup';
import '@styles/layout/Header.css';
import notiImg from '@assets/images/notification.svg';
import logoutImg from '@assets/images/logout.svg';

const Header = () => {
  const { logout, userEmail } = useAuth();
  const navigate = useNavigate();

  const [ isOpen, setIsOpen ] = useState(false);
  
  const [ onClose, setClose ] = useState(false);

  const onClosePopup = () => {
    setIsOpen(false);
  }

  const onClickLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="header-container">
      <div className="header-left" onClick={() => navigate('/chatRoom')}>
        <h3 className="logo">채팅 서비스</h3>
      </div>

      <div className="header-right">
        <h2 className="user-name">{userEmail} 님</h2>
        <div className="icon-wrapper" onClick={() => {setIsOpen(true)}}>
          <img src={notiImg} alt="알림" className="icon" />
          <span>알림</span>
        </div>
        <div className="icon-wrapper" onClick={onClickLogout}>
          <img src={logoutImg} alt="로그아웃" className="icon" />
          <span>로그아웃</span>
        </div>
      </div>
      <NotiPopup isOpen = {isOpen} onClose = {onClosePopup} />
    </header>
  );
};

export default Header;
