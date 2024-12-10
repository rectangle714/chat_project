import 'moment/locale/ko';
import { useState } from 'react';
import { Box, Pagination } from '@mui/material';
import Layout from '@components/layout/Layout';
import AddFriendsPopup from '@components/friends/AddFriendsPopup';
import FriendsList from '@components/friends/FriendsList';
import friendsAddImg from '@assets/images/friends_add.svg';

const FriendsListForm = () => {
    const [page, setPage] = useState(1);
    const [friendsList, setFriendsList] = useState([]);
    const [totalPage, setTotalPage] = useState(0);

    const chatOpen = () => setIsChatRoomPopupOpen(true);
    const [isChatRoomPopupOpen, setIsChatRoomPopupOpen] = useState(false);
    const chatClosePopup = () => setIsChatRoomPopupOpen(false);

    const handleChangePage = (event, newPage) => {
        setPage(page => page = newPage);
    }

    return (
        <Layout>
            <div className="chat-room-wrap">
                <div className="chat-room-list">
                    <div className="chat-room-header">
                        <h2>친구</h2>
                        <img src={friendsAddImg} alt="Add friends" className="icon" onClick={chatOpen} />
                    </div>
                    <div>
                        <FriendsList friendsList = {friendsList}/>
                    </div>
                    <Box display="flex" justifyContent="center" marginTop={2}>
                        <Pagination
                            count={totalPage}
                            page={page}
                            onChange={handleChangePage}
                            color = 'primary'
                            showFirstButton
                            showLastButton
                        />
                    </Box>
                    <AddFriendsPopup
                        isOpen={isChatRoomPopupOpen}
                        onClose={chatClosePopup}
                    />
                </div>
            </div>
        </Layout>
    );
}

export default FriendsListForm