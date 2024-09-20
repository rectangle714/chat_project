import { useState, useEffect, useRef } from 'react';
import { useAuth } from '@stores/authProvider';
import { IconButton } from "@mui/material";
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import SockJS from 'sockjs-client';
import * as StompJs from "@stomp/stompjs";
import api from '@stores/api';
import "@styles/chat/ChatForm.css";
import logoutImg from '@assets/images/logout.png'
import settingImg from '@assets/images/setting.png'
import attachImg from '@assets/images/attach.png'
import sendImg from '@assets/images/send.png'
import Cookies from 'js-cookie';
import ReactDOM from 'react-dom';

const ChatForm = () => {
    const URL = process.env.REACT_APP_API_URL;
    const navigate = useNavigate();
    const { accessToken, reissue } = useAuth();
    const chatEndRef = useRef(null);
    const [member, setMember] = useState({email: '', nickname: ''});

    // 선택된 파일들을 저장할 state
    const [selectedFiles, setSelectedFiles] = useState([]);
    const fileInputRef = useRef(null);
    const [isPopupOpen, setIsPopupOpen] = useState(false);

    const [textareaValue, setTextareaValue] = useState('');
    const [messages, setMessages] = useState([]);
    const [client, setClient] = useState(null);
    const { id } = useParams();
    const location = useLocation();
    const roomName = location.state.roomName ? location.state.roomName : '';

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

    // 이미지 클릭 시 파일 선택 창 열기
    const handleImageClick = () => {
        fileInputRef.current.click(); // 숨겨진 input 요소 클릭
    };
    
    // 파일 팝업 닫기
    const handleClosePopup = () => {
        setIsPopupOpen(false);
    };

    // 파일이 선택되었을 때 처리
    const handleFileChange = (event) => {
        const files = Array.from(event.target.files);
        setSelectedFiles(prevFiles => [...prevFiles, ...files]); // 선택된 파일들을 배열에 추가
        setIsPopupOpen(true);  // 파일 선택 후 팝업 열기
    };


    // 파일 리스트에서 특정 파일 제거
    const handleRemoveFile = (index) => {
        setSelectedFiles(prevFiles => prevFiles.filter((_, i) => i !== index));
        if(selectedFiles.length == 1) handleClosePopup();
    };

    // 파일 다운로드
    const handleFileDownload = async(fileId, fileName) => {
        const response = await api.get(`/api/file/download/${fileId}`, {
            responseType: 'blob'
        })
        if(response) {
            // Blob을 이용해 파일 다운로드
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', fileName);  // 원하는 파일명과 확장자 설정
            document.body.appendChild(link);
            link.click();
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
                if(chat.isAlert != 'Y') {
                    if(chat.sender != member.nickname) {
                        if(chat.isFile != 'Y') {
                            appendMessageTag('left', chat.sender, chat.message, chat.registerDate);
                        } else {
                            const fileTag = <a style={{
                                                color:'blue',
                                                textDecoration:'underline',
                                                cursor:'pointer'
                                            }} onClick={() => handleFileDownload(chat.fileId, chat.message)}>{chat.message}</a>;
                            appendMessageTag('left', chat.sender, fileTag, chat.registerDate);
                        }
                    } else {
                        if(chat.isFile != 'Y') {
                            appendMessageTag('right', chat.sender, chat.message, chat.registerDate);
                        } else {
                            const fileTag = <a style={{
                                                    color:'blue',
                                                    textDecoration:'underline',
                                                    cursor:'pointer'
                                            }} onClick={() => handleFileDownload(chat.fileId, chat.message)}>{chat.message}</a>;
                            appendMessageTag('right', chat.sender, fileTag, chat.registerDate);
                        }
                    }
                } else {
                    appendMessageTag('center', chat.sender, chat.message, chat.registerDate);
                }
            });
        } catch(error) {
            console.log(error);
        }
    }

    /* 웹소켓 연결 */
    const connection = () => {
        try{
            const socket = new SockJS(`${URL}/ws`);
            
            const clientData = new StompJs.Client({
                webSocketFactory: () => socket,
                connectHeaders: {
                    Authorization: accessToken,
                    room_id: id
                },
                debug: function(val) {
                    // console.log('val ',val);
                },
                reconnectDelay: 5000,
                heartbeatIncoming: 4000,
                heartbeatOutgoing: 4000,
                onConnect: function(frame) {
                    clientData.subscribe(`/sub/chat/room/${id}`, callback);

                    try {
                        clientData.publish({
                            destination: `/pub/chatRoom/join/${id}`,
                            headers: {
                                Authorization: accessToken
                            }
                        });
                    } catch(error) {
                        console.log('error ',error);
                    }

                },
            });
    
            clientData.activate();
            setClient(clientData);
        } catch(error) {
            console.log(error);
        }
    }

    const callback = function(message) {
        if(message.body) {
            const msg = JSON.parse(message.body);
            if(msg.isAlert != 'Y') {
                if(msg.sender != member.nickname) {
                    if(msg.isFile != 'Y') {
                        appendMessageTag('left', msg.sender, msg.message, msg.registerDate);
                    } else {
                        const fileTag = <a style={{
                            color:'blue',
                            textDecoration:'underline',
                            cursor:'pointer'
                        }} onClick={() => handleFileDownload(msg.fileId, msg.message)}>{msg.message}</a>;
                        appendMessageTag('left', msg.sender, fileTag, msg.registerDate);
                    }
                } else {
                    if(msg.isFile != 'Y') {
                        appendMessageTag('right', msg.sender, msg.message, msg.registerDate);
                    } else {
                        const fileTag = <a style={{
                            color:'blue',
                            textDecoration:'underline',
                            cursor:'pointer'
                        }} onClick={() => handleFileDownload(msg.fileId, msg.message)}>{msg.message}</a>;
                        appendMessageTag('right', msg.sender, fileTag, msg.registerDate);
                    }
                }
            } else {
                appendMessageTag('center', msg.sender, msg.message, msg.registerDate);
            }
        }
    }

    const disConnection = () => {
        if(client === null) { return; }

        try {
            client.publish({
                destination: `/pub/chatRoom/exit/${id}`,
                headers: {
                    Authorization: accessToken
                }
            });
            client.deactivate();
        } catch(error) {
            console.log('error ',error);
        }
    }

    /* 메세지 전송 */
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

    /* Enter Key 이벤트 */
    const handleEnter = (e) => {
        if (e.key === "Enter") {
            e.preventDefault();
            setTextareaValue('');
            sendMessage(textareaValue);
        }
    };

    const handleChange = (e) => { setTextareaValue(e.target.value); };

    /* 퇴장 이벤트 */
    const handleExitClick = async(e) => {
        if(window.confirm('채팅방을 나가시겠습니까?')) {
            await disConnection();
            window.location.href = '/chatRoom';
        }
    }

    /* 전송버튼 이벤트 */
    const handleSendClick = (e) => {
        e.preventDefault();
        setTextareaValue('');
        sendMessage(textareaValue);
    }

    // 팝업 닫기 및 파일 전송
    const handleConfirmFiles = async() => {
        const readFile = (file) => {
            return new Promise((resolve, reject) => {
                const reader = new FileReader();
                reader.onload = () => {
                    const base64Data  = btoa(
                        new Uint8Array(reader.result)
                            .reduce((data, byte) => data + String.fromCharCode(byte), '')
                    );
                    resolve({
                        fileName: file.name,
                        fileData: base64Data,
                        fileType: file.type,
                        fileSize: file.size
                    });
                };
                reader.onerror = (error) => {
                    console.error('Error reading file:', error);
                    reject(error);
                };
                reader.readAsArrayBuffer(file);
            });
        };

        try {
            const fileData = await readFile(selectedFiles[0]);
            if(Cookies.get('accessToken')) {
                client.publish({
                    destination: '/pub/sendFile',
                    headers: {
                        Authorization: accessToken
                    },
                    body: JSON.stringify({
                        chatRoomId: id,
                        file: fileData,
                        "chatType" : "SEND",
                        "accessToken" : accessToken
                    })
                });
            } else {
                await reissue();

                client.publish({
                    destination: '/pub/sendFile',
                    headers: {
                        Authorization: Cookies.get('accessToken')
                    },
                    body: JSON.stringify({
                        chatRoomId: id,
                        file: fileData,
                        "chatType" : "SEND",
                        "accessToken" : Cookies.get('accessToken')
                    })
                });
            }
        } catch (error) {
            console.error('파일 처리 중 오류 발생:', error);
        }
        
        setIsPopupOpen(false);  // 팝업 닫기
        setSelectedFiles([]);
    };

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
                    <span style={{flex:2, fontWeight: 'bold'}}>{roomName}</span>
                    <span style={{textAlign:'right'}}>
                        <img src={settingImg} alt='setting image' onClick={handleSettingClick} className={'setting_img'}/>
                        <img src={logoutImg} alt='logout image' onClick={handleExitClick} className={'logout_img'}/>
                    </span>
            </div>
            <div className="chat">
                <div className="chatformat">
                    <ul style={{padding:'0px'}}>
                        {messages.map((msg, index) => (
                            <li key={index} className={msg.LR_className}>
                                {msg.LR_className != 'center' ?
                                    <div className = 'sender'>
                                        <span>{msg.senderName}</span>
                                    </div> : ""
                                }
                                {msg.LR_className == 'right' ? <span style={{fontSize:'13px'}}>{msg.registerDate}</span> : ''}
                                <div className="message">
                                    <span>{msg.message}</span>
                                </div>
                                {msg.LR_className == 'left' ? <span style={{fontSize:'13px'}}>{msg.registerDate}</span> : ''}
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
                    <span>
                        <IconButton variant='contained' onClick={handleImageClick}>
                            <img src={attachImg} alt='attach image' className={'attach_img'}/>
                        </IconButton>
                    </span>
                    <span>
                        <IconButton variant='contained' onClick={handleSendClick}>
                            <img src={sendImg} alt='send image' className={'send_img'}/>
                        </IconButton>
                        <input
                            type="file" 
                            ref={fileInputRef} 
                            style={{ display: 'none' }} 
                            onChange={handleFileChange} 
                        />
                    </span>
                </div>
                {/* 팝업 창 */}
                {isPopupOpen && ReactDOM.createPortal(
                    <div className="popup-overlay">
                        <div className="popup-content">
                            <button className="close-popup" onClick={handleClosePopup}>X</button>
                            <h2>파일 전송</h2>
                            <div className="file-list">
                            {selectedFiles.map((file, index) => (
                                <div key={index} className="file-item">
                                    <span className="file-name">{file.name}</span>
                                    <button className="remove-button" onClick={() => handleRemoveFile(index)}>X</button>
                                </div>
                            ))}
                            </div>
                            <button className="confirm-button" onClick={handleConfirmFiles}>전송</button>
                        </div>
                    </div>,
                    document.body  // 팝업을 최상위 레벨에 렌더링
                )}
            </div>
        </div>
    );
};
export default ChatForm;
