import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import api from '@stores/api';
import searchBtn from '@assets/images/search.svg'
import { Button } from '@mui/material';
import { useAuth } from '@context/AuthProvider';
import '@styles/friends/AddFriendsPopup.css';

const AddFriendsPopup = ({ isOpen, onClose }) => {
    const { logout, userEmail } = useAuth();
    const [message, setMessage] = useState({message: '이메일을 입력해주세요.', color: 'red'});
    const [email, setEmail] = useState('');
    const [validation, setValidation] = useState(false);
    const [receiverId, setReceiverId] = useState(0);

    const emailValidation = async() => {
        if(email == '') {
            setMessage({message: '이메일을 입력해주세요.', color:'red'});
            return false;
        }

        if(email === userEmail) {
            setMessage({message: '자신의 아이디는 친구로 추가할 수 없습니다.', color:'red'});
            return false;
        }
        
        try {
            const memberInfoResponse = await api.get('/api/member/info/email', { params: {email: email}} );
            const receiverId = memberInfoResponse.data.memberId;

            try {
                const checkFriendsResponse = await api.get('/api/friends/request/check', { params: { receiverId: receiverId } });

                if(checkFriendsResponse.data.requestStatus == '') {
                    setMessage({message: ''});
                    setReceiverId(receiverId);
                    setValidation(true);
                } else if(checkFriendsResponse.data.requestStatus == 'PENDING') {
                    setMessage({message: '추가 요청을 진행중인 사용자입니다.', color:'red'});
                    setReceiverId(0);
                    setValidation(false);
                } else if(checkFriendsResponse.data.requestStatus == 'ACCEPTED') {
                    setMessage({message: '이미 추가된 사용자입니다.', color:'red'});
                    setReceiverId(0);
                    setValidation(false);
                } else if(checkFriendsResponse.data.requestStatus == 'REJECTED') {
                    setMessage({message: ''});
                    setReceiverId(receiverId);
                    setValidation(true);
                }
            } catch(error) {
                console.log(error);
                setMessage({message: ''});
                setReceiverId(0);
                setValidation(false);
            }
        } catch(error) {
            setMessage({message: '존재하지 않는 이메일 입니다.', color:'red'});
            setReceiverId(0);
            setValidation(false);
        }
    }

    const onEnterEvent = (e) => {
        if(e.key === 'Enter') {
            e.preventDefault();
            emailValidation()
        }
    }

    const addFriendsBtn = async() => {
        try {
            const response = await api.post('/api/friends/request', null, {
                params : {
                    receiverId: receiverId
                }
            })

            if(response.status == '200') {
                alert('친구 추가 요청이 완료됐습니다.');
                onClose();
            } else {
                alert('친구 추가 요청에 실패했습니다.');
            }
        } catch(error) {
            alert('친구 추가 요청에 실패했습니다.');
        }
        
    }

    useEffect(() => {
        setEmail('');
        setMessage({ message: '', color: 'black' });
        setReceiverId(0);
        setValidation(false);
    }, [isOpen])
    
    if (!isOpen) return null;

    return ReactDOM.createPortal(
        <div className="popup-overlay">
            <div className="popup-content" style={{textAlign:'center'}} >
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
                        onKeyDown={(e) => onEnterEvent(e)}
                    />
                    <img className='search' src={searchBtn} alt="search" onClick={emailValidation} />
                </div>
                <div style={{textAlign:'center', color: message.color, paddingBottom: '40px'}}>{message.message}</div>
                <Button onClick={addFriendsBtn} variant="contained" disabled={!validation}>추가</Button>
            </div>
        </div>,
        document.body // 팝업을 최상위 레벨에 렌더링
    );
};

export default AddFriendsPopup;
