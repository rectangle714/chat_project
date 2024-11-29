import React, { createContext, useState, useContext, useEffect } from 'react';
import Cookies from 'js-cookie';
import { useNavigate, useLocation } from 'react-router-dom/dist';
import axios from 'axios';
import { jwtDecode } from "jwt-decode";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const navigate = useNavigate();
    const location = useLocation();
    const [accessToken, setAccessToken] = useState(Cookies.get('accessToken') || null);
    const [refreshToken, setRefreshToken] = useState(Cookies.get('refreshToken') || null);
    const [userEmail, setUserEmail] = useState('');

    /* 로그인 */
    const login = (accessToken, accessTokenExpired, refreshToken, refreshTokenExpired, email) => {
        setAccessToken(accessToken);
        setRefreshToken(refreshToken);
        Cookies.set('accessToken', accessToken, { expires: new Date(Date.now() + accessTokenExpired), path: '/' });
        Cookies.set('refreshToken', refreshToken, { expires: new Date(Date.now() + refreshTokenExpired), path: '/' });
        

        setTimeout(() => {
            logout();
        }, refreshTokenExpired);
    }

    /* 로그아웃 */
    const logout = () => {
        setAccessToken(null);
        setRefreshToken(null);
        Cookies.remove('accessToken', { path: '/' });
        Cookies.remove('refreshToken', { path: '/' });
    }

    /* 토큰 재발급 */
    const reissue = async () => {
        try {
            const URL = process.env.REACT_APP_API_URL;
            const response = await axios.post(URL + "/api/member/reissue", null, {
                headers: { 'REFRESH_TOKEN' : Cookies.get('refreshToken') }
            })
    
            if(response.data.accessToken) {
                setAccessToken(response.data.accessToken);
                Cookies.set('accessToken', response.data.accessToken, { expires: new Date(Date.now() + response.data.accessTokenExpiration), path: '/' });
            }
        } catch(error) {
            logout();
            navigate('/login');
        }
    }

    // JWT에서 이메일 추출
    const getUserEmailFromToken = () => {
        const token = Cookies.get("refreshToken");
        if (!token) return null;

        try {
        const email = jwtDecode(token).sub.split(':')[0];
        return email;
        } catch (error) {
        console.error("Token decoding failed", error);
        return null;
        }
    };

    useEffect(() => {
        const accessToken = Cookies.get('accessToken');
        const refreshToken = Cookies.get('refreshToken');

        setUserEmail(getUserEmailFromToken());
        
        if(location.pathname != '/login') {
            if(accessToken) {
                setAccessToken(accessToken); 
                setRefreshToken(refreshToken);
            }
        }
    }, []);

    return (
        <AuthContext.Provider value={{ accessToken, refreshToken, login, logout, reissue, userEmail }}>
            {children}
        </AuthContext.Provider>
    )
};

export const useAuth = () => {
    return useContext(AuthContext);
}