import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { useAuth } from '@stores/AuthProvider';
import api from '@stores/api';
import moment from 'moment';
import 'moment/locale/ko';

const ChatRoomList = ({chatRoomList}) => {
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(true);
    const { accessToken, reissue } = useAuth();
    moment.locale('ko');

    /* 사용자가 채팅방에 참여했는지 체크 */
    const isUserInChatRoom = async(roomId, email) => {
        try {
            const response = await api.post('/api/chatRoom/isUserInChatRoom', null, {
                params : {
                    roomId: roomId
                }
            })
            return response.data;
        } catch(e) {
            console.log(`error : ${e}`);
        }
    }

    /* 채팅방 클릭 이벤트 */
    const clickRoom = async(roomId, roomName, memberCount, numberPeople) => {
        // 채팅방 참여 가능 인원 체크
        if(memberCount >= numberPeople) {
            const result = await isUserInChatRoom(roomId);
            result == 'success' ? navigate('/chat/'+roomId, {state: {'roomName' : roomName}}) : alert('채팅방 정원이 초과되었습니다.');
        } else {
            navigate('/chat/'+roomId, {state: {'roomName' : roomName}});
        }
    }

    const roomNameRender = (lastSendDate) => {
        if(!lastSendDate) { return ''; }

        const todayDate = moment().format('YYYY-MM-DD');
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
                        <li key={room.id} className="chat-room" onClick={
                            (e) => clickRoom(room.id, room.roomName, room.memberCount, room.numberPeople)
                        }>
                            <span style={{width: '20px'}}></span>
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