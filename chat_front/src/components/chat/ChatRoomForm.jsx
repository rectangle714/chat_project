import '@styles/chat/ChatRoom.css'
import Layout from '@layout/Layout';
import ChatRoomList from './ChatRoomList';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Button, Pagination } from '@mui/material';
import chatAddImg from '@assets/images/chat_add.svg'
import api from '@stores/api';

const ChatRoomForm = () => {
    const navigate = useNavigate();
    const [page, setPage] = useState(1);
    const [chatRoomList, setChatRoomList] = useState([]);
    const [totalPage, setTotalPage] = useState(0);
    const [isPopupOpen, setIsPopupOpen] = useState(false);


    const handleClosePopup = () => {
        setIsPopupOpen(false);
    };

    const handleChangePage = (event, newPage) => {
        setPage(page => page = newPage);
    }

    const getChatRoomList = async() => {
        try {
            const response = await api.get(`/api/chatRoom/list?page=${page-1}&size=${10}`, null);
            setChatRoomList(response.data.content);
            setTotalPage(response.data.totalPages);
        } catch(error) {
            console.log('채팅방 조회 실패 ', error);
        }
    }

    useEffect(() => {
        getChatRoomList();
    }, [page])

    return (
        <Layout>
            <div className="chat-room-wrap">
                <div className="chat-room-list">
                    <div className="chat-room-header">
                        <h2>채팅</h2> 
                        <img src={chatAddImg} alt="Create Room" className="icon" />
                    </div>
                    <div>
                        <ChatRoomList chatRoomList = {chatRoomList}/>
                    </div>
                    <Box display="flex" justifyContent="center" marginTop={2}>
                        <Pagination
                            count={totalPage}
                            page={page}
                            onChange={handleChangePage}
                            color = 'primary'
                            showFirstButton
                            showLastButton />
                    </Box>
                </div>
            </div>
        </Layout>
    );
}

export default ChatRoomForm