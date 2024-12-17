import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@stores/AuthProvider';
import axios from 'axios';
import '@styles/member/LoginForm.css';

const LoginForm = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [validation, setValidation] = useState('');
    const { login } = useAuth();

    const handleSubmit = async(e) => {
        e.preventDefault();
        if(email == '') { 
            setValidation('이메일을 입력해주세요.');
            return;
        }
        if(password == '') {
            setValidation('패스워드를 입력해주세요.');
            return;
        }

        const URL = process.env.REACT_APP_API_URL;
        try {
            const response = await axios.post(URL+'/api/member/login', null, {
                params: {
                    email: email,
                    password: password
                }
            });
            login(response.data.accessToken, response.data.accessTokenExpiration,
                     response.data.refreshToken, response.data.refreshTokenExpiration,  response.data.email);
            navigate('/chatroom/public');
        } catch(error) {
            alert(error);
            setValidation(error.response.data.message);
        }
    };

    return (
        <>
            <div className="login-container">
                <div className="login-form">
                    <h2>로그인</h2>
                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="email">Email</label>
                            <input
                                type="text"
                                id="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="password">Password</label>
                            <input
                                type="password"
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>
                        <div className="form-group" style={{paddingLeft:'20px'}}>
                            <button type="submit" className="login-button">Login</button>
                        </div>
                        <div style={{
                            textAlign: 'center',
                            marginTop: '20px'
                        }}>
                            <span style={{color:'red', fontSize:'12px'}}>{validation}</span>
                        </div>
                    </form>
                </div>
            </div>
        </>
    )
}
export default LoginForm;