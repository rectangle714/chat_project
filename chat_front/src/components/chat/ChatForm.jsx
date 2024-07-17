import { useState, useEffect, useRef } from 'react';
import "./ChatForm.css";

const ChatForm = () => {
    const [textareaValue, setTextareaValue] = useState('');
    const [messages, setMessages] = useState([]);
    const chatRef = useRef(null);

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
        const data = {
          senderName: "blue",
          message: message,
        };
        appendMessageTag('left', 'User', message);
        // resive(data);
    }

    const appendMessageTag = (LR_className, senderName, message) => {
        const newMessage = { LR_className, senderName, message };
        setMessages(prevMessages => [...prevMessages, newMessage]);
    };

    useEffect(() => {
        if (chatRef.current) {
            chatRef.current.scrollTop = chatRef.current.scrollHeight;
        }
    }, [messages]);

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
