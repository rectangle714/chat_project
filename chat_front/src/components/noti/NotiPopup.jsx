import React, { useEffect, useState } from 'react';
import ReactDOM from 'react-dom';
import api from '@stores/api';
import checkImg from '@assets/images/check.svg'
import cancelImg from '@assets/images/cancel.svg'
import { Button, CircularProgress } from '@mui/material';
import '@styles/noti/NotiPopup.css';

const NotiPopup = ({ isOpen, onClose }) => {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(false);

  // 요청 리스트 가져오기
  useEffect(() => {
    if (isOpen) {
      fetchRequests();
    }
  }, [isOpen]);

  const fetchRequests = async () => {
    setLoading(true);
    try {
      const response = await api.get('/api/friends/requests/list');
      setRequests(response.data); // 서버에서 요청 리스트 반환
    } catch (error) {
      console.error('Error fetching requests:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAccept = async (requestId) => {
    // try {
    //   await api.post(`/api/friends/requests/${requestId}/accept`);
    //   setRequests((prev) => prev.filter((req) => req.id !== requestId)); // 요청 제거
    //   alert('친구 요청을 수락했습니다.');
    // } catch (error) {
    //   console.error('Error accepting request:', error);
    // }
  };

  const handleReject = async (requestId) => {
    // try {
    //   await api.post(`/api/friends/requests/${requestId}/reject`);
    //   setRequests((prev) => prev.filter((req) => req.id !== requestId)); // 요청 제거
    //   alert('친구 요청을 거절했습니다.');
    // } catch (error) {
    //   console.error('Error rejecting request:', error);
    // }
  };

  if (!isOpen) return null;

  return ReactDOM.createPortal(
    <div className="popup-overlay">
      <div className="popup-content">
        <button className="close-popup" onClick={onClose}>X</button>
        <h2 style={{textAlign:'center'}}>알림 목록</h2>
        {loading ? (
          <div className="loading-container">
            <CircularProgress />
          </div>
        ) : requests.length > 0 ? (
          <ul className="request-list">
            {requests.map((request) => (
              <li key={request.senderId} className="request-item">
                <div className="request-info">
                  <span>{request.senderEmail}</span>
                  <span className="request-date">{request.requestAt}</span>
                </div>
                <div className="request-actions">
                  <Button
                    variant="outlined"
                    color="primary"
                    onClick={() => handleAccept(request.senderId)}
                  >
                    <img src={checkImg}/>
                  </Button>
                  <Button
                    variant="outlined"
                    color="error"
                    onClick={() => handleReject(request.senderId)}
                  >
                    <img src={cancelImg}/>
                  </Button>
                </div>
              </li>
            ))}
          </ul>
        ) : (
          <p className="empty-message">알림이 없습니다.</p>
        )}
      </div>
    </div>,
    document.body
  );
};

export default NotiPopup;
