import axios from 'axios';
import Cookies from 'js-cookie';

// Axios 인스턴스 생성
const api = axios.create({
    baseURL: 'http://localhost:30003',
    headers: {
        "content-type": "application/json;charset=UTF-8",
    }
})

// 요청 인터셉터 설정
api.interceptors.request.use(
    (request) => {
        const accessToken = Cookies.get('accessToken');
        
        if(accessToken) {
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
        return response;
    },
    async(error) => {
        const originalRequest = error.config;

        if(error.response.status == 401) {
            try {
                const URL = process.env.REACT_APP_API_URL;
                const response = await axios.post(URL + "/api/member/reissue", null, {
                    headers: { 'REFRESH_TOKEN' : Cookies.get('refreshToken') }
                })

                Cookies.set('accessToken', response.data.accessToken, { expires: new Date(Date.now() + response.data.accessTokenExpiration), path: '/' });
                originalRequest.headers.Authorization = `Bearer ${response.data.accessToken}`;

                return axios(originalRequest);
            } catch(refreshError) {
                alert('로그인 시간이 만료되었습니다.');
                window.location.href = '/login';

                return new Promise.reject(refreshError);
            }
        } else {
            return Promise.reject(error);
        }
    }
)

export default api;