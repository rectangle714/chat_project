import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './store/AuthProvider';
import ChatForm from './components/chat/ChatForm';
import LoginForm from './components/member/LoginForm';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginForm/>}></Route>
          <Route path="/chat" element={<ChatForm/>}></Route>
          <Route path="*" element={<LoginForm/>}></Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
