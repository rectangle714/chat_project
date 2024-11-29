import '@styles/layout/Header.css'
import { jwtDecode } from "jwt-decode";
import { useAuth } from '@stores/AuthProvider'
import { useNavigate } from 'react-router-dom';

const Header = ({loggedIn, handleLoginLogout}) => {
    const { logout, userEmail } = useAuth();
    const navigate = useNavigate();

    const onClickLogout = () => {
        logout();
        navigate('/login');
    }

    // JWT에서 이메일 추출
    const getUserEmailFromToken = () => {
        const token = localStorage.getItem("refreshToken");
        if (!token) return null;

        try {
        const decoded = jwtDecode(token);
        console.log('decoded ',decoded);
        return decoded.email; // JWT의 payload에서 이메일 추출
        } catch (error) {
        console.error("Token decoding failed", error);
        return null;
        }
    };

    return (
        <header>
            <div className="header" style={{backgroundColor: 'black'}}>
            <div style={{ display: 'flex', alignItems: 'center', flexGrow: 1 }} >
                <h3 style={{color:'white', cursor:'pointer'}} onClick={() => navigate('/chatRoom')}>채팅 서비스</h3>
            </div>

            <h2 className='user_name' onClick={() => navigate('/chatRoom')}>{userEmail} 님</h2>
            <button style={{marginLeft: '20px'}} onClick={onClickLogout}>로그아웃</button>
        </div>
        </header>
    )
}

export default Header