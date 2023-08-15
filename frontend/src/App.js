import React, { useState } from 'react';
import Map from './pages/MapPage';
import ZzimList from './pages/ZzimList';
import RoomOut from './pages/RoomOut';
import Signuplogin from './pages/Signuplogin';
import GbbList from './pages/GbbList';
import GbbCreate from './pages/GbbCreate';
import Main from './pages/Main'
import Auth from './pages/Auth';
import ChatRoom from './components/Chatting/ChatRoom';
import ChatList from './components/Chatting/ChatList';

import { BrowserRouter, Route, Routes } from "react-router-dom";
import './App.css';

const App = () => {
  const [imageList, setImageList] = useState([]);

  const handleImageUpload = (images) => {
    setImageList([...imageList, images]);
  };
  return (
    <BrowserRouter>
      <div className='App'>
        <Routes>
          <Route path="/" element={<Main />} />
          <Route path="/map" element={<Map />} />
          <Route path="/gbblist" element={<GbbList imageList={imageList} />} />
          <Route path="/gbbcreate" element={<GbbCreate onImageUpload={handleImageUpload} />} />
          <Route path="/zzim" element={<ZzimList />} />
          <Route path="/roomout" element={<RoomOut />} />
          <Route path="/login" element={<Signuplogin />} />
          <Route path="/auth" element={<Auth />} />
          <Route path="/chatroom/:id/:roomDealId" element={<ChatRoom />} />
          <Route path="/chatlist" element={<ChatList />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
};

export default App;


