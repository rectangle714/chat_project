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
            <div className="header" style={{backgroundColor: 'black'}}>
            <div style={{ display: 'flex', alignItems: 'center', flexGrow: 1 }} >
                <h3 style={{color:'white', cursor:'pointer'}} onClick={() => navigate('/chatRoom')}>채팅 서비스</h3>
            </div>

            <h2 className='user_name' onClick={() => navigate('/chatRoom')}>test</h2>
            <button style={{marginLeft: '20px'}} onClick={onClickLogout}>로그아웃</button>
        </div>
        </header>
    )
}

export default Header