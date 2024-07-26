import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from '@stores/authProvider';
import ChatForm from '@components/chat/ChatForm';
import LoginForm from '@components/member/LoginForm';
import ChatRoomForm from '@components/chat/ChatRoomForm';

function App() {

  return (
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route path="/login" element={<LoginForm />}/>
            <Route path="/chatroom" element={<ChatRoomForm />} />
            <Route path="/chat" element={<ChatForm /> } />
            <Route path="*" element={<LoginForm />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
  );
}

export default App;
