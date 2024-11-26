import Header from './Header'
import Footer from './Footer'
import Sidebar from './SideBar';
import styles from '@styles/layout/Layout.module.scss'
import ChatRoomPopup from '../chat/ChatRoomPopup';
import { useState } from 'react';

const Layout = (props) => {
    const [isChatRoomPopupOpen, setIsChatRoomPopupOpen] = useState(false);
    const [isFriendsPopupOpen, setIsFriendsPopupOpen] = useState(false);
    
    const openPopup = () => setIsChatRoomPopupOpen(true);
    const closePopup = () => setIsChatRoomPopupOpen(false);



    return (
        <div className={styles.layout}>
            <Header />
            <div className={styles.body}>
                <div className={styles.sidebar}>
                    <Sidebar onOpenPopup={openPopup} />
                </div>
                <main className={styles.main}>
                    {props.children}
                    <ChatRoomPopup 
                        isOpen={isChatRoomPopupOpen}
                        onClose={closePopup}
                    />
                </main>
            </div>
            <Footer />
        </div>
    );
};

export default Layout