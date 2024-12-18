import "moment/locale/ko";
import api from "@stores/api";
import { useEffect, useState } from "react";
import { Box, Pagination } from "@mui/material";
import AddFriendsPopup from "@components/friends/AddFriendsPopup";
import FriendsList from "@components/friends/FriendsList";
import friendsAddImg from "@assets/images/friends_add.svg";

const FriendsListForm = () => {
  const [page, setPage] = useState(1);
  const [friendsList, setFriendsList] = useState([]);
  const [totalPage, setTotalPage] = useState(0);
  const [isChatRoomPopupOpen, setIsChatRoomPopupOpen] = useState(false);
  const chatOpen = () => setIsChatRoomPopupOpen(true);
  const chatClosePopup = () => setIsChatRoomPopupOpen(false);

  const handleChangePage = (event, newPage) => {
    setPage((page) => (page = newPage));
  };

  const getFriendsList = async () => {
    const response = await api.get(
      `/api/friends/list?page=${page - 1}&size=${10}`
    );
    setFriendsList(response.data.content);
    setTotalPage(response.data.totalPages);
  };

  useEffect(() => {
    getFriendsList();
  }, [page]);

  return (
    <>
      <div className="chat-room-wrap">
        <div className="chat-room-list">
          <div className="chat-room-header">
            <h2>친구</h2>
            <img
              src={friendsAddImg}
              alt="Add friends"
              className="icon"
              onClick={chatOpen}
            />
          </div>
          <div>
            <FriendsList friendsList={friendsList} />
          </div>
          <Box display="flex" justifyContent="center" marginTop={2}>
            <Pagination
              count={totalPage}
              page={page}
              onChange={handleChangePage}
              color="primary"
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
    </>
  );
};

export default FriendsListForm;
