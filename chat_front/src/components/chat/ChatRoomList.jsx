import { useState } from "react";
import { useNavigate } from "react-router-dom";

const ChatRoomList = ({chatRoomList}) => {
    const navigate = useNavigate();
    const [roomId, setRoomId] = useState();

    const cliickRoom = (roomId) => {
        navigate('/chat/'+roomId);
    }

    return (
        <>
            <ul>
                {chatRoomList.length > 0 ? chatRoomList.map(room => (
                    <li key={room.id} className="chat-room" onClick={() => cliickRoom(room.id)}>
                        <img src='https://via.placeholder.com/50' alt={`${room.name} avatar`} />
                        <div className="chat-room-info">
                            <h3>{room.roomName}</h3>
                            <p className="last-message">{room.lastMessage}</p>
                        </div>
                    </li>
                )) : 
                    <li>
                        <div className="chat-room-info">
                            <h3>채팅방이 존재하지 않습니다.</h3>
                        </div>
                    </li>}
            </ul>
        </>
    );
}

export default ChatRoomList