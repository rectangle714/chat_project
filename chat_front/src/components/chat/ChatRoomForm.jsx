import '@styles/chat/ChatRoom.css'
import Layout from '@layout/Layout';
import ChatRoomList from './ChatRoomList';
import { useEffect } from 'react';
import api from '@stores/api'

const ChatRoomForm = () => {

    const getChatRoomList = async() => {
        try {
            const response = await api.get('/api/chatRoom/list', null);
            console.log(response);
        } catch(error) {
            console.log('채팅방 조회 실패 ', error);
        }
    }

    useEffect(() => {
        getChatRoomList();
    }, [])

    return (
        <Layout>
            <div className="chat-room-wrap">
                <div className="chat-room-list">
                    <div className="chat-room-header">
                        <h2>채팅방 목록</h2>
                    </div>
                    <div>
                        <ChatRoomList />
                    </div>
                </div>
            </div>
        </Layout>
    );
}

export default ChatRoomForm