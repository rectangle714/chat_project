import { useState, useEffect, useRef } from 'react';
import { useAuth } from '@stores/authProvider';
import { IconButton } from "@mui/material";
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import * as StompJs from "@stomp/stompjs";
import api from '@stores/api';
import "@styles/chat/ChatForm.css";
import logoutImg from '@assets/images/logout.png'
import settingImg from '@assets/images/setting.png'
import sendImg from '@assets/images/send.png'
import Cookies from 'js-cookie';

const ChatForm = () => {
    const URL = process.env.REACT_APP_WS_URL;
    const navigate = useNavigate();
    const { accessToken, reissue } = useAuth();
    const chatEndRef = useRef(null);
    const [member, setMember] = useState({email: '', nickname: ''});

    const [textareaValue, setTextareaValue] = useState('');
    const [messages, setMessages] = useState([]);
    const [client, setClient] = useState(null);
    const { id } = useParams();
    const location = useLocation();
    const roomName = location.state.roomName;

    /* 사용자 정보 조회 */
    const getMember = async() => {
        try {
            const response = await api.get('/api/member/info', null);
            setMember((prevMember) => {
                prevMember.email = response.data.email;
                prevMember.nickname = response.data.nickname;
                return prevMember;
            });

            getChatting();
            connection();
        } catch(error) {
            console.log('error ', error);
            navigate('/login');
        }
    }

    /* 채팅목록 조회 */
    const getChatting = async() => {
        try {
            const response = await api.get('/api/chat/list', {
                params : {
                    chatRoomId : id
                }
            });

            const chatArray = response.data;
            chatArray.forEach(chat => {
                if(chat.sender != member.nickname) {
                    appendMessageTag('left', chat.sender, chat.message, chat.registerDate);
                } else {
                    appendMessageTag('right', chat.sender, chat.message, chat.registerDate);
                }
            });
        } catch(error) {
            console.log(error);
        }
    }

    /* 웹소켓 연결 */
    const connection = () => {
        try{
            const clientData = new StompJs.Client({
                brokerURL: `ws://${URL}/ws`,
                connectHeaders: {
                    Authorization : accessToken
                },
                body: {
                    chat_room_id : id
                },
                debug: function(val) {
                    // console.log('val ',val);
                },
                reconnectDelay: 5000,
                heartbeatIncoming: 4000,
                heartbeatOutgoing: 4000
            });
    
            clientData.onConnect = function() {
                clientData.subscribe('/sub/chat/room/' + id, callback);
            };
    
            clientData.activate();
            setClient(clientData);
        } catch(error) {
            console.log(error);
        }
    }

    const callback = function(message) {
        if(message.body) {
            const msg = JSON.parse(message.body);
            if(msg.sender != member.nickname) {
                appendMessageTag('left', msg.sender, msg.message, msg.registerDate);
            } else {
                appendMessageTag('right', msg.sender, msg.message, msg.registerDate);
            }
        }
    }

    const disConnection = () => {
        if(client === null) { return; }
        client.deactivate();
    }

    const sendMessage = async(message) => {
        if(message == '') { return false; }
        
        if(Cookies.get('accessToken')) {
            client.publish({
                destination: '/pub/message',
                body: JSON.stringify({
                    "chatRoomId" : id,
                    "chatType" : "SEND",
                    "message" : message,
                    "accessToken" : accessToken
                })
            })
        } else {
            await reissue();

            client.publish({
                destination: '/pub/message',
                body: JSON.stringify({
                    "chatRoomId" : id,
                    "chatType" : "SEND",
                    "message" : message,
                    "accessToken" : Cookies.get('accessToken')
                })
            })
        }
    }

    const appendMessageTag = (LR_className, senderName, message, registerDate) => {
        const newMessage = { LR_className, senderName, message, registerDate };
        setMessages(prevMessages => [...prevMessages, newMessage]);
    };

    const handleEnter = (e) => {
        if (e.key === "Enter") {
            e.preventDefault();
            setTextareaValue('');
            sendMessage(textareaValue);
        }
    };

    const handleChange = (e) => { setTextareaValue(e.target.value); };

    /* 로그아웃 이벤트 */
    const handleLogoutClick = (e) => {
        if(window.confirm('채팅방을 나가시겠습니까?')) {
            disConnection();
            navigate('/chatRoom');
        }
    }

    /* 전송버튼 이벤트 */
    const handleSendClick = (e) => {
        e.preventDefault();
        setTextareaValue('');
        sendMessage(textareaValue);
    }

    /* 방 설정 이벤트 */
    const handleSettingClick = (e) => {
        navigate(`/chatRoom/${id}`);
    }

    useEffect(() => {
        chatEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    useEffect(() => {
        getMember();
    }, [])

    return (
        <div className="chat_wrap">
            <div className="header">
                    <span style={{flex:2}}>{roomName}</span>
                    <span style={{textAlign:'right'}}>
                        <img src={settingImg} alt='setting image' onClick={handleSettingClick} className={'setting_img'}/>
                        <img src={logoutImg} alt='logout image' onClick={handleLogoutClick} className={'logout_img'}/>
                    </span>
            </div>
            <div className="chat">
                <div className="chatformat">
                    <ul>
                        {messages.map((msg, index) => (
                            <li key={index} className={msg.LR_className}>
                                <div className="sender">
                                    <span>{msg.senderName}</span>
                                </div>
                                {msg.LR_className == 'right' ? <span>{msg.registerDate}</span> : ''}
                                <div className="message">
                                    <span>{msg.message}</span>
                                </div>
                                {msg.LR_className == 'left' ? <span>{msg.registerDate}</span> : ''}
                            </li>
                        ))}
                    </ul>
                    <div ref={chatEndRef}></div>
                </div>
            </div>
            <div className="input-div">
                <textarea
                    value={textareaValue}
                    onChange={handleChange}
                    onKeyDown={handleEnter}
                    style={{borderBottom: '1px solid black'}}
                />
                <div style={{textAlign:'right'}}>
                    <IconButton variant='contained'>
                        <img src={sendImg} alt='send image' onClick={handleSendClick} className={'send_img'}/>
                    </IconButton>
                </div>          
            </div>
        </div>
    );
};
export default ChatForm;
