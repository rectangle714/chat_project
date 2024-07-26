const ChatRoomList = (props) => {

    // 테스트 데이터
    const chatRooms = [
        { id: 1, name: '테스트 1', lastMessage: 'ㄴㅇㅁㄴㅇㅁㄴㅇ', avatar: 'https://via.placeholder.com/50' },
        { id: 2, name: '테스트 2', lastMessage: '?????????????', avatar: 'https://via.placeholder.com/50' },
        { id: 3, name: '테스트 3', lastMessage: 'ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ', avatar: 'https://via.placeholder.com/50' },
        { id: 4, name: '테스트 4', lastMessage: 'ㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣ', avatar: 'https://via.placeholder.com/50' },
        { id: 5, name: '테스트 5', lastMessage: 'ㅇㅇㅇㅇㅇㅇㅇㅇㅇ', avatar: 'https://via.placeholder.com/50' },
    ];

    return (
        <>
            <ul>
                {chatRooms.map(room => (
                    <li key={room.id} className="chat-room">
                        <img src={room.avatar} alt={`${room.name} avatar`} />
                        <div className="chat-room-info">
                        <h3>{room.name}</h3>
                        <p className="last-message">{room.lastMessage}</p>
                        </div>
                    </li>
                ))}
            </ul>
        </>
    );
}

export default ChatRoomList