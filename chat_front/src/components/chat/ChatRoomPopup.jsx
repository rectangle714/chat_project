import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import Box from '@mui/material/Box';
import { useNavigate, useParams } from 'react-router-dom';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import api from '@stores/api';
import '@styles/chat/ChatRoomPopup.css';

const ChatRoomPopup = ({ isOpen, onClose }) => {
    const navigate = useNavigate();
    const [roomName, setRoomName] = useState('');
    const [numberPeople, setNumberPeople] = useState('');
    const [member, setMember] = useState({email: '', nickname: ''});

    /* 사용자 정보 조회 */
    const getMember = async() => {
        try {
            const response = await api.get('/api/member/info', null);
    
            setMember((prevMember) => {
                prevMember.email = response.data.email;
                prevMember.nickname = response.data.nickname;
                return prevMember;
            });
        } catch(error) {
            navigate('/login');
        }
    }

    const onClickCreateRoom = async() => {
        if(roomName === '') { 
            alert('방 이름을 입력해주세요.');
            return false;
        }

        if(numberPeople === '') {
            alert('인원 수를 선택해주세요.');
            return false;
        }

        try {
            await api.post('/api/chatRoom/add', null, {
                params : {
                    roomName: roomName,
                    numberPeople: numberPeople,
                    email: member.email
                }
            });

            alert('채팅방이 생성 되었습니다.');
            window.location.reload();
        } catch(e) {
            alert('채팅방 생성에 실패했습니다.');
            console.log(e);
        }
        

        onClose();
    };

    useEffect(() => {
        const fetchData = async () => {
            await getMember();
        };

        fetchData();
    }, [])
    
    if (!isOpen) return null;

    return ReactDOM.createPortal(
        <div className="popup-overlay">
            <div className="popup-content">
                <button className="close-popup" onClick={onClose}>
                    X
                </button>
                <h2>채팅방 생성</h2>
                <input
                    type="text"
                    className="room-name-input"
                    placeholder="채팅방 이름을 입력하세요"
                    value={roomName}
                    onChange={(e) => setRoomName(e.target.value)}
                />
                <div style={{paddingTop:'15px'}}>
                    <Box>
                        <FormControl sx={{textAlign: 'center'}} fullWidth>
                            <InputLabel sx={{fontSize: '0.830rem',}} id="demo-simple-select-label">인원 수</InputLabel>
                            <Select
                                id="demo-simple-select"
                                value={numberPeople}
                                sx={{padding:'0px', fontSize: '0.875rem'}}
                                label="Age"
                                onChange={(e) => setNumberPeople(e.target.value)}
                            >
                                <MenuItem value={1}>1</MenuItem>
                                <MenuItem value={2}>2</MenuItem>
                                <MenuItem value={3}>3</MenuItem>
                                <MenuItem value={4}>4</MenuItem>
                                <MenuItem value={5}>5</MenuItem>
                            </Select>
                        </FormControl>
                    </Box>
                </div>
                <button className="create-room-button" onClick={onClickCreateRoom}>생성</button>
            </div>
        </div>,
        document.body // 팝업을 최상위 레벨에 렌더링
    );
};

export default ChatRoomPopup;
