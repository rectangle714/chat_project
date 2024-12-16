import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { useAuth } from '@stores/AuthProvider';
import api from '@stores/api';
import moment from 'moment';
import 'moment/locale/ko';

const FriendsList = ({ friendsList }) => {
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(true);
    moment.locale('ko');

    /* 채팅방 클릭 이벤트 */
    const clickRoom = async(roomId, roomName, memberCount, numberPeople) => {
        
    }

    useEffect(() => {
        if (friendsList && friendsList.length >= 0) {
            setIsLoading(false);
        }
    }, [friendsList])

    return (
        <>
            <ul>
                {friendsList.length > 0 ? (
                    friendsList.map(friends => (
                        <li key={friends.id} className="chat-room">
                            <span style={{width: '20px'}}></span>
                            <div className="chat-room-info">
                                <h3>{friends.friendsEmail}</h3>
                            </div>
                            <div>
                                {/* <p style={{fontSize: '12px', color:'#888', marginLeft:'10px', marginTop:'4px'}}>
                                    {`(${room.memberCount} / ${room.numberPeople})`}
                                </p> */}
                            </div>
                            <p className="last-message" style={{marginLeft:'auto', fontSize: '12px', color:'#888'}}>
                                {/* { roomNameRender(room.lastSendDate) } */}
                            </p>
                        </li>
                    ))
                ) : (
                    <li>
                        <div className="chat-room-info">
                            <h3>친구 리스트가 비어 있습니다.</h3>
                        </div>
                    </li>
                )}
            </ul>
        </>
    );
}

export default FriendsList