import React, { createContext, useState, useContext, useEffect } from 'react';
import api from './api';
import Cookies from 'js-cookie';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [accessToken, setAccessToken] = useState(Cookies.get('accessToken') || null);
    const [refreshToken, setRefreshToken] = useState(Cookies.get('refreshToken') || null);

    const login = (accessToken, accessTokenExpired, refreshToken, refreshTokenExpired) => {
        setAccessToken(accessToken);
        setRefreshToken(refreshToken);
        Cookies.set('accessToken', accessToken, { expires: accessTokenExpired, path: '/' });
        Cookies.set('refreshToken', refreshToken, { expires: refreshTokenExpired, path: '/' });
    }

    const logout = () => {
        setAccessToken(null);
        setRefreshToken(null);
        Cookies.remove('accessToken', { path: '/' });
        Cookies.remove('refreshToken', { path: '/' });
    }

    const reissue = async () => {
        try {
            const response = await api.post("/member/reissue", null, {
                headers: { 'REFRESH_TOKEN' : `Bearer ${refreshToken}` }
            })
    
            if(response.data.accessToken) {
                login(response.data.accessToken, response.data.accessTokenExpired,
                        response.data.refreshToken, response.data.refreshTokenExpired);
            } else {
                logout();
            }
        } catch(error) {
            console.log(error);
            logout();
        }
    }

    useEffect(() => {
        const accessToken = Cookies.get('accessToken');
        const refreshToken = Cookies.get('refreshToken');
        if(accessToken && refreshToken) {
            setAccessToken(accessToken);
            setRefreshToken(refreshToken);
        }
    }, []);

    return (
        <AuthContext.Provider value={{ accessToken, refreshToken, login, logout, reissue }}>
            {children}
        </AuthContext.Provider>
    )
};

export const useAuth = () => {
    return useContext(AuthContext);
}