const ChatRoomList = () => {

    return (
        <>
        <section className="p-3 size-full flex flex-col gap-3 overflow-y-scroll scrollbar-hide">
          <h3 className="sr-only">채팅방 리스트</h3>
          {chatList.length > 0 ? (
            chatList.map((item) => {
              return (
                <ChatRoomCard
                  key={item.id}
                  item={item}
                  me={userId}
                  opener={handleOpenChatRoom(item.id)}
                  pb={pb}
                />
              );
            })
          ) : (
            <span className="grid place-items-center h-4/5">
              대화중인 상대가 없습니다
            </span>
          )}
        </section>
        {chatRoom.length > 0 && (
          <ChatRoom closer={handleCloseChatRoom} chatRoomId={chatRoom} me={userId} pb={pb} />
        )}
      </>
    )
}
export default ChatRoomList;