import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from '@stores/authProvider';
import ChatForm from '@components/chat/ChatForm';
import LoginForm from '@components/member/LoginForm';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginForm />}/>
          <Route path="/chat" element={<ChatForm /> } />
          <Route path="*" element={<LoginForm />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
