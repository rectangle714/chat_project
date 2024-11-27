import Header from './Header'
import Footer from './Footer'
import Sidebar from './SideBar';
import styles from '@styles/layout/Layout.module.scss'
import ChatRoomPopup from '../chat/ChatRoomPopup';
import { useState } from 'react';
import AddFriendsPopup from '../friends/AddFriendsPopup';

const Layout = (props) => {
    const [isChatRoomPopupOpen, setIsChatRoomPopupOpen] = useState(false);
    const [isFriendsPopupOpen, setIsFriendsPopupOpen] = useState(false);

    const chatOpen = () => setIsChatRoomPopupOpen(true);
    const chatClosePopup = () => setIsChatRoomPopupOpen(false);
    
    const friendsOpen = () => setIsFriendsPopupOpen(true);
    const friendsClosePopup = () => setIsFriendsPopupOpen(false);

    return (
        <div className={styles.layout}>
            <Header />
            <div className={styles.body}>
                <div className={styles.sidebar}>
                    <Sidebar onChatPopup={chatOpen} onFriendsPopup={friendsOpen} />
                </div>
                <main className={styles.main}>
                    {props.children}
                    <ChatRoomPopup 
                        isOpen={isChatRoomPopupOpen}
                        onClose={chatClosePopup}
                    />
                    <AddFriendsPopup
                        isOpen={isFriendsPopupOpen}
                        onClose={friendsClosePopup}
                    />
                </main>
            </div>
            <Footer />
        </div>
    );
};

export default Layout