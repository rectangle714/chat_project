import { BrowserRouter, Routes, Route } from 'react-router-dom';
import ChatForm from './components/chat/ChatForm';
import LoginForm from './components/member/LoginForm';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginForm/>}></Route>
        <Route path="/chat" element={<ChatForm/>}></Route>
        <Route path="*" element={<LoginForm/>}></Route>
      </Routes>
    </BrowserRouter>
    // <ChatForm/>
  );
}

export default App;
