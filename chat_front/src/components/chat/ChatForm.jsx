import { useState, useEffect, useRef } from 'react';
import { useAuth } from '../../store/AuthProvider';
import { useNavigate, Navigate } from 'react-router-dom';
import api from '../../store/api';
import * as StompJs from "@stomp/stompjs";
import "./ChatForm.css";

const ChatForm = () => {
    const URL = process.env.REACT_APP_WS_URL;
    const navigate = useNavigate();
    const { accessToken, reissue } = useAuth();
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
            console.log('response: ',response);
        } catch(error) {
            console.log(error);
        }
    }

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
                appendMessageTag('right', msg.sender, msg.message);
            } else {
                appendMessageTag('left', msg.sender, msg.message);
            }
        }
    }

    const disConnection = () => {
        if(client === null) { return; }
        client.deactivate();
    }

    const handleEnter = (e) => {
        if (e.key === "Enter") {
            e.preventDefault();
            setTextareaValue('');
            sendMessage(textareaValue);
        }
    };

    const handleChange = (event) => {
        setTextareaValue(event.target.value);
    };

    const sendMessage = (message) => {
        if(messages === '') { return; }
        
        client.publish({
            destination: '/pub/message',
            body: JSON.stringify({
                "chatRoomId" : 1,
                "chatRoomName" : "test",
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

    useEffect(() => {
        chatEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    useEffect(() => {
        if(accessToken != null) {
            getMember();
            getChatting();
            connection();
        } else {
            navigate('/login');
        }
    }, [])

    return (
        <div className="chat_wrap">
            <div className="header">CHAT</div>
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
