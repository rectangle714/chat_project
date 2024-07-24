import { useState, useEffect, useRef } from 'react';
import { useAuth } from '@stores/authProvider';
import { useNavigate } from 'react-router-dom';
import * as StompJs from "@stomp/stompjs";
import api from '@stores/api';
import "@styles/chat/ChatForm.css";
import logout from '@assets/images/logout.png'

const ChatForm = () => {
    const URL = process.env.REACT_APP_WS_URL;
    const navigate = useNavigate();
    const { accessToken } = useAuth();
    const chatEndRef = useRef(null);
    const [member, setMember] = useState({email: '', nickname: ''});

    const [textareaValue, setTextareaValue] = useState('');
    const [messages, setMessages] = useState([]);
    const [client, setClient] = useState(null);

    // 사용자 정보 조회
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

    // 채팅목록 조회
    const getChatting = async() => {
        try {
            const response = await api.get('/api/chat/list', {
                params : {
                    chatRoomId : 1
                }
            });

            const chatArray = response.data;
            chatArray.forEach(chat => {
                if(chat.sender != member.nickname) {
                    appendMessageTag('left', chat.sender, chat.message);
                } else {
                    appendMessageTag('right', chat.sender, chat.message);
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
                debug: function(val) {
                    // console.log('val ',val);
                },
                reconnectDelay: 5000,
                heartbeatIncoming: 4000,
                heartbeatOutgoing: 4000
            });
    
            clientData.onConnect = function() {
                clientData.subscribe('/sub/chat/room/' + 1, callback);
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
                appendMessageTag('left', msg.sender, msg.message);
            } else {
                appendMessageTag('right', msg.sender, msg.message);
            }
        }
    }

    const disConnection = () => {
        if(client === null) { return; }
        client.deactivate();
    }

    const sendMessage = (message) => {
        if(message == '') { return false; }
        
        client.publish({
            destination: '/pub/message',
            body: JSON.stringify({
                "chatRoomId" : 1,
                "chatType" : "SEND",
                "message" : message,
                "accessToken" : accessToken
            })
        })
    }

    const appendMessageTag = (LR_className, senderName, message) => {
        const newMessage = { LR_className, senderName, message };
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

    const handleClick = (e) => {
        if(window.confirm('채팅방을 나가시겠습니까?')) {
            disConnection();
            navigate('/login');
        }
    }

    useEffect(() => {
        chatEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    useEffect(() => {
        if(accessToken != null) {
            getMember();
        } else {
            navigate('/login');
        }
    }, [])

    return (
        <div className="chat_wrap">
            <div className="header">
                    <span style={{flex:2}}>CHAT</span>
                    <span style={{textAlign:'right'}}><img src={logout} alt='logout image' onClick={handleClick} className={'logout_img'}/></span>
            </div>
            <div className="chat">
                <div className="chatformat">
                    <ul>
                        {messages.map((msg, index) => (
                            <li key={index} className={msg.LR_className}>
                                <div className="sender">
                                    <span>{msg.senderName}</span>
                                </div>
                                <div className="message">
                                    <span>{msg.message}</span>
                                </div>
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
                    placeholder="Press Enter for send message."
                />
            </div>
        </div>
    );
};
export default ChatForm;
