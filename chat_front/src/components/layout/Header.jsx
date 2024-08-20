import '@styles/layout/Header.css'
import { useAuth } from '@stores/authProvider'
import { useNavigate } from 'react-router-dom';

const Header = ({loggedIn, handleLoginLogout}) => {
    const { accessToken, logout } = useAuth();
    const navigate = useNavigate();

    const onClickLogout = () => {
        logout();
        navigate('/login');
    }

    return (
        <header>
            <div className="header">
                <h2 style={{cursor: 'pointer'}} onClick={() => navigate('/chatRoom')}>채팅</h2>
                <button onClick={onClickLogout}>
                    {accessToken ? '로그아웃' : '로그인'}
                </button>
            </div>
        </header>
    )
}

export default Header