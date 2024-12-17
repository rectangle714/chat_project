import { Fragment, useState} from 'react';
import ReactDOM from 'react-dom';
import { useNavigate } from 'react-router-dom';
import '@styles/layout/SideBar.css';
import FriendsListForm from '../friends/FriendsListForm';

const Sidebar = ({ onChatPopup, onFriendsPopup }) => {
    const navigate = useNavigate();
    const [isMenuOpen, setIsMenuOpen] = useState({chat: false, friends: false, notifications: false, admin: false});
    const [isFriendsPopupOpen, setIsFriendsPopupOpen] = useState(false);
    
    const toggleMenu = (menu) => {
        setIsMenuOpen(preState => ({
            ...preState,
            [menu]: !preState[menu]
        }));
    }

    const openFriendsPopup = () => setIsFriendsPopupOpen(true);
    const closeFriendsPopup = () => setIsFriendsPopupOpen(false);

    return (
        <>
            <aside className='sidebar'>
                <div className="fixed-menu">
                    <h3 onClick={() => toggleMenu('chat')}>채팅</h3>
                    <ul style={{display: isMenuOpen.chat ? 'block' : 'none'}}>
                        <li className="menu-item" onClick={() => { navigate('/chatRoom/public') }}>
                            <span className="menu-text">오픈채팅</span>
                        </li>
                    </ul>
                </div>

                <div className="fixed-menu">
                    <h3 onClick={openFriendsPopup}>친구</h3>
                    <ul style={{display: isMenuOpen.friends ? 'block' : 'none'}}>
                        <li className="menu-item" onClick={() => {navigate('/friends')}}>
                            <span className="menu-text">목록</span>
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

            {isFriendsPopupOpen &&
                ReactDOM.createPortal(
                    <div className="popup-overlay" onClick={closeFriendsPopup}>
                    <div className="window-controls" onClick={(e) => e.stopPropagation()}>
                        <div style={{textAlign:'right', paddingBottom: '5px', paddingTop: '5px'}}>
                            <span className="control close" onClick={closeFriendsPopup}>&times;</span>
                        </div>
                        <FriendsListForm />
                    </div>
                  </div>,
                  document.getElementById('portal-root') // Portal이 렌더링될 DOM 위치
                )
            }
        </>
    );
};

export default Sidebar