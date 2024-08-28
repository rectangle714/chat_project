import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import moment from 'moment';
import 'moment/locale/ko';

const ChatRoomList = ({chatRoomList}) => {
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(true);
    moment.locale('ko');

    const clickRoom = (roomId, roomName) => {
        navigate('/chat/'+roomId, {state: {'roomName' : roomName}});
    }

    const roomNameRender = (lastSendDate) => {
        const todayDate = moment().format('YYYY-MM-DD');
        if(!lastSendDate) {
            return '';
        }

        if(moment(lastSendDate).format('YYYY-MM-DD') == todayDate) {
            return moment(lastSendDate).format('A hh:mm');
        } else {
            return moment(lastSendDate).format('YYYY-MM-DD');
        }

    }

    useEffect(() => {
        if (chatRoomList && chatRoomList.length >= 0) {
            setIsLoading(false);
        }
    }, [chatRoomList])

    return (
        <>
            <ul>
                {chatRoomList.length > 0 ? (
                    chatRoomList.map(room => (
                        <li key={room.id} className="chat-room" onClick={() => clickRoom(room.id, room.roomName)}>
                            <img src='https://via.placeholder.com/50' alt={`${room.name} avatar`} />
                            <div className="chat-room-info">
                                <h3>{room.roomName}</h3>
                                <p className="last-message">{room.lastMessage}</p>
                            </div>
                            <div>
                                <p style={{fontSize: '12px', color:'#888', marginLeft:'10px', marginTop:'4px'}}>
                                    {`(${room.memberCount} / ${room.numberPeople})`}
                                </p>
                            </div>
                            <p className="last-message" style={{marginLeft:'auto', fontSize: '12px', color:'#888'}}>
                                { roomNameRender(room.lastSendDate) }
                            </p>
                        </li>
                    ))
                ) : (
                    <li>
                        <div className="chat-room-info">
                            <h3>채팅방이 존재하지 않습니다.</h3>
                        </div>
                    </li>
                )}
            </ul>
        </>
    );
}

export default ChatRoomList