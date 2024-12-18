import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@context/AuthProvider';
import SockJS from 'sockjs-client';
import * as StompJs from "@stomp/stompjs";
import NotiPopup from '@components/noti/NotiPopup';
import notiImg from '@assets/images/notification.svg';
import notiActiveImg from '@assets/images/notification_active.svg';
import logoutImg from '@assets/images/logout.svg';
import '@styles/layout/Header.css';

const Header = () => {
  const navigate = useNavigate();
  const { logout, userEmail } = useAuth();
  const [ isOpen, setIsOpen ] = useState(false);
  const [ notifications, setNotifications ] = useState(false);
  const [client, setClient] = useState(null);
  const URL = process.env.REACT_APP_API_URL;

  const onClosePopup = () => {
    setIsOpen(false);
  }

  const onClickNoti = () => {
    setIsOpen(true);
    setNotifications(false);
  }

  const onClickLogout = () => {
    logout();
    navigate('/login');
  };

  const connectSocket = () => {
    if(client) { return; }

    const socket = new SockJS(`${URL}/ws`);
    const stompClient = new StompJs.Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        stompClient.subscribe(`/sub/notification/${userEmail}`, (message) => {
          setNotifications(true);
        });
      },
      onStompError: (error) => {
        console.log('STOMP error: ', error);
      }
    });

    stompClient.activate();
    setClient(stompClient);
  }

  useEffect(() => {
    if(userEmail && !client) {
      connectSocket();
    }

    return () => {
      if(null != client) {
        client.deactivate()
      }
    }
  }, [userEmail, client]);

  return (
    <header className="header-container">
      <div className="header-left" onClick={() => navigate('/chatRoom/public')}>
        <h3 className="logo">채팅 서비스</h3>
      </div>

      <div className="header-right">
        <h2 className="user-name">{userEmail} 님</h2>
        <div className="icon-wrapper" onClick={() => {onClickNoti()}}>
          <img src={notifications ? notiActiveImg : notiImg} alt="알림" className="icon" />
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
