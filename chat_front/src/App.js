import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from '@stores/authProvider';
import ChatForm from '@components/chat/ChatForm';
import LoginForm from '@components/member/LoginForm';
import ChatRoomForm from '@components/chat/ChatRoomForm';
import CreateChatRoomForm from '@components/chat/CreateChatRoomForm';

function App() {

  return (
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            {/* 로그인 */}
            <Route path="/login" element={<LoginForm />}/>

            {/* 채팅 */}
            <Route path="/chatroom" element={<ChatRoomForm />} />
            <Route path="/chatroom/:id" element={<CreateChatRoomForm />} />
            <Route path="/chatroom/create" element={<CreateChatRoomForm />} />
            <Route path="/chat/:id" element={<ChatForm /> } />

            <Route path="*" element={<LoginForm />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
  );
}

export default App;
