import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '@styles/layout/SideBar.css';

const Sidebar = ({ onChatPopup, onFriendsPopup }) => {
    const navigate = useNavigate();
    const [isMenuOpen, setIsMenuOpen] = useState({chat: false, friends: false, notifications: false, admin: false});
    
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
                    <ul style={{display: isMenuOpen.chat ? 'block' : 'none'}}>
                        <li className="menu-item" onClick={() => {navigate('/chatRoom')}}>
                            <span className="menu-text">목록</span>
                        </li>

                        {/* 방 생성 이미지 클릭으로 변경 */}
                        {/* <li className='menu-item' onClick={onChatPopup}>
                            <span className="menu-text">방생성</span>
                        </li> */}
                    </ul>
                </div>

                <div className="fixed-menu">
                    <h3 onClick={() => toggleMenu('friends')}>친구</h3>
                    <ul style={{display: isMenuOpen.friends ? 'block' : 'none'}}>
                        <li className="menu-item" onClick={() => {navigate('/friends')}}>
                            <span className="menu-text">목록</span>
                        </li>

                        {/* 친구 추가 이미지 클릭으로 변경 */}
                        {/* <li className='menu-item' onClick={onFriendsPopup}>
                            <span className="menu-text">친구추가</span>
                        </li> */}
                    </ul>
                </div>

                <div className="fixed-menu">
                    <h3 onClick={() => toggleMenu('notifications')}>알림</h3>
                    <ul style={{display: isMenuOpen.notifications ? 'block' : 'none'}}>
                        <li className='menu-item' onClick={onFriendsPopup}>
                            <span className="menu-text">알림확인</span>
                        </li>
                    </ul>
                </div>

                <div className="fixed-menu">
                    <h3 onClick={() => toggleMenu('admin')}>관리자</h3>
                    <ul style={{display: isMenuOpen.admin ? 'block' : 'none'}}>
                        <li className='menu-item' onClick={onFriendsPopup}>
                            <span className="menu-text">설정</span>
                        </li>
                    </ul>
                </div>
            </aside>
        </>
    );
};

export default Sidebar