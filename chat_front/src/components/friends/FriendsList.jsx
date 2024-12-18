import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { CircularProgress } from "@mui/material";
import { usePopup } from "@context/PopupProvider";
import moment from "moment";
import "moment/locale/ko";

const FriendsList = ({ friendsList }) => {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);
  const { closeFriendsPopup } = usePopup();
  moment.locale("ko");

  /* 친구 클릭 이벤트 */
  const clickFriend = (chatRoomId, chatRoomName) => {
    navigate(`/chat/${chatRoomId}`, {state: {'roomName' : chatRoomName, 'roomType' : 'private'}});
    closeFriendsPopup();
  };

  useEffect(() => {
    if (friendsList) {
      setIsLoading(false);
    }
  }, [friendsList]);

  return (
    <>
      <ul>
        {isLoading ? (
          <div className="loading-container">
            <CircularProgress />
          </div>
        ) : friendsList.length > 0 ? (
          friendsList.map((friends) => (
            <li key={friends.friendsId} className="chat-room" onClick={() => clickFriend(friends.chatRoomId, friends.chatRoomName)}>
              <span style={{ width: "20px" }}></span>
              <div className="chat-room-info">
                <h3>{friends.friendsNickname}</h3>
              </div>
              <p
                className="last-message"
                style={{ marginLeft: "auto", fontSize: "12px", color: "#888" }}
              >
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
};

export default FriendsList;
