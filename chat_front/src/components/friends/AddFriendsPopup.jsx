import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import api from '@stores/api';
import searchBtn from '@assets/images/search.svg'
import { Button } from '@mui/material';
import '@styles/friends/AddFriendsPopup.css';

const AddFriendsPopup = ({ isOpen, onClose }) => {
    const [message, setMessage] = useState({message: '이메일을 입력해주세요.', color: 'red'});
    const [email, setEmail] = useState('');
    const [validation, setValidation] = useState(false);
    const [receiverId, setReceiverId] = useState(0);

    const emailValidation = async() => {
        if(email == '') {
            setMessage({message: '이메일을 입력해주세요.', color:'red'});
            return false;
        }

        try {
            const response = await api.get('/api/member/info/email', {params: {email: email}});
            setMessage({message: ''});
            setReceiverId(response.data.memberId);
            setValidation(true);
        } catch(error) {
            setMessage({message: '존재하지 않는 이메일 입니다.', color:'red'});
            setReceiverId(0);
            setValidation(false);
        }
    }

    const addFriendsBtn = async() => {
        try {
            const response = await api.post('/api/friends/request', null, {
                param : {
                    receiverId: receiverId
                }
            })

            console.log('response',response);
        } catch(error) {
            alert('친구 추가 요청에 실패했습니다.');
        }
        
    }
    
    if (!isOpen) return null;

    return ReactDOM.createPortal(
        <div className="popup-overlay">
            <div className="popup-content">
                <button className="close-popup" onClick={onClose}>
                    X
                </button>
                <h2>친구 추가</h2>
                <div style={{display: 'flex'}}>
                    <input
                        type="text"
                        className="room-name-input"
                        placeholder="이메일을 입력하세요"
                        onChange={(e) => setEmail(e.target.value)}
                    />
                    <img className='search' src={searchBtn} alt="search" onClick={emailValidation} />
                </div>
                <div style={{textAlign:'right', color: message.color, paddingBottom: '40px'}}>{message.message}</div>
                <Button onClick={addFriendsBtn} variant="contained" disabled={!validation}>추가</Button>
            </div>
        </div>,
        document.body // 팝업을 최상위 레벨에 렌더링
    );
};

export default AddFriendsPopup;
