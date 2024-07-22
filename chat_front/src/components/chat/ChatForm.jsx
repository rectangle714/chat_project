import { useState, useEffect, useRef } from 'react';
import { useAuth } from '../../store/AuthProvider';
import { useNavigate } from 'react-router-dom';
import api from '../../store/api';
import * as StompJs from "@stomp/stompjs";
import "./ChatForm.css";

const ChatForm = () => {
    const URL = process.env.REACT_APP_WS_URL;
    const navigate = useNavigate();
    const { accessToken, reissue } = useAuth();
    const chatRef = useRef(null);
    const [member, setMember] = useState({email: '', nickname: ''});

    const [textareaValue, setTextareaValue] = useState('');
    const [messages, setMessages] = useState([]);
    const [client, setClient] = useState(null);

    const getMember = async() => {
        try {
            const response = await api.get('/api/member/info', null);
            setMember({email: response.data.email, nickname: response.data.nickname});
        } catch(error) {
            console.log('error ', error);
            navigate('/login');
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

    const disConnection = () => {
        if(client === null) { return; }
        client.deactivate();
    }


    const callback = function(message) {
        if(message.body) {
            const msg = JSON.parse(message.body);
            if(msg.sender != member.nickname) {
                appendMessageTag('right', msg.sender, msg.message);
            }
        }
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

        appendMessageTag('left', member.nickname, message);
        // resive(data);
    }

    const appendMessageTag = (LR_className, senderName, message) => {
        const newMessage = { LR_className, senderName, message };
        setMessages(prevMessages => [...prevMessages, newMessage]);
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const scrollToBottom = () => {
        if(chatRef.current) {
            const { scrollHeight, clientHeight } = chatRef.current;
            chatRef.current.scrollTop = chatRef.current.scrollTop + scrollHeight;
        }
    }

    useEffect(() => {
        if(accessToken != null) {
            getMember();
            connection();
        } else {
            reissue();
        }
    }, [])

    return (
        <div className="chat_wrap">
            <div className="header">CHAT</div>
            <div className="chat" ref={chatRef}>
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
