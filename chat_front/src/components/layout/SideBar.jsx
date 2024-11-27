import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '@styles/layout/SideBar.css';

const Sidebar = ({ onChatPopup, onFriendsPopup }) => {
    const navigate = useNavigate();
    const [isMenuOpen, setIsMenuOpen] = useState({chat: false, friends: false});
    
    const toggleMenu = (menu) => {
        setIsMenuOpen(preState => ({
            ...preState,
            [menu]: !preState[menu]
        }));
    }

    return ( 
        <>
            <aside className='sidebar'>
                <div className="fixed-menu">
                    <h3 onClick={() => toggleMenu('chat')}>채팅</h3>
                    <ul style={{display: isMenuOpen.chat ? 'none' : 'block'}}>
                        <li className="menu-item" onClick={() => {navigate('/chatRoom')}}>
                            <span className="menu-text">목록</span>
                        </li>
                        <li className='menu-item' onClick={onChatPopup}>
                            <span className="menu-text">방생성</span>
                        </li>
                    </ul>
                </div>

                <div className="fixed-menu">
                    <h3 onClick={() => toggleMenu('friends')}>친구</h3>
                    <ul style={{display: isMenuOpen.friends ? 'none' : 'block'}}>
                        <li className="menu-item">
                            <span className="menu-text">목록</span>
                        </li>
                        <li className='menu-item' onClick={onFriendsPopup}>
                            <span className="menu-text">친구추가</span>
                        </li>
                        <li className='menu-item' onClick={onFriendsPopup}>
                            <span className="menu-text">추가요청</span>
                        </li>
                    </ul>
                </div>

                <div className="fixed-menu">
                    <h3 onClick={() => toggleMenu('friends')}>알림</h3>
                    <ul style={{display: isMenuOpen.friends ? 'none' : 'block'}}>
                        <li className='menu-item' onClick={onFriendsPopup}>
                            <span className="menu-text">요청확인</span>
                        </li>
                    </ul>
                </div>
            </aside>
        </>
    );
};

export default Sidebar