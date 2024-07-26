import axios from 'axios';
import Cookies from 'js-cookie';
import { useAuth } from './authProvider';

// Axios 인스턴스 생성
const api = axios.create({
    baseURL: 'http://localhost:30001',
    headers: {
        "content-type": "application/json;charset=UTF-8",
    }
})

// 요청 인터셉터 설정
api.interceptors.request.use(
    (request) => {
        const { reissue } = useAuth();
        let accessToken = Cookies.get('accessToken');
        
        if(accessToken) {
            request.headers['Authorization'] = `Bearer ${accessToken}`;
        } else {
            reissue();
            accessToken = Cookies.get('accessToken');
            request.headers['Authorization'] = `Bearer ${accessToken}`;
        }

        return request;
    },
    (error) => {
        return Promise.reject(error);
    }
)

api.interceptors.response.use(
    (response) => {
        if(response.status === 404) {
            console.log('404 에러');
        }

        return response;
    },
    async(error) => {
        if(error.status === 401) {
            console.log('401 에러');
        }
        return Promise.reject(error);
    }
)

export default api;