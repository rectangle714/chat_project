import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '@styles/layout/SideBar.css';



const Sidebar = ({ onOpenPopup }) => {
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
                <div className="fixed-menu" onClick={() => toggleMenu('chat')}>
                    <h3>채팅</h3>
                    <ul style={{display: isMenuOpen.chat ? 'none' : 'block'}}>
                        <li className='menu-item' onClick={onOpenPopup}>
                            <span className="menu-text">방생성</span>
                        </li>
                        <li className="menu-item" onClick={() => navigate('/chatRoom')}>
                            <span className="menu-text">목록</span>
                        </li>
                    </ul>
                </div>

                <div className="fixed-menu" onClick={() => toggleMenu('friends')}>
                    <h3>멤버</h3>
                    <ul style={{display: isMenuOpen.friends ? 'none' : 'block'}}>
                        <li className='menu-item' onClick={onOpenPopup}>
                            <span className="menu-text">멤버추가</span>
                        </li>
                    </ul>
                </div>
            </aside>
        </>
    );
};

export default Sidebar