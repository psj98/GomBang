import React, { useState } from "react";
import Map from "./pages/MapPage";
import Zzimlist from "./pages/Zimlist";
import Roomout from "./pages/Roomout";
import Signuplogin from "./pages/Signuplogin";
import GbbList from "./pages/GbbList";
import GbbCreate from "./pages/GbbCreate";
import ChatRoom from "./components/Chatting/ChatRoom";
import ChatList from "./components/Chatting/ChatList";
import AssigneeRtcRoom from "./components/Rtc/AssigneeRtcRoom";
import GrantorRtcRoom from "./components/Rtc/GrantorRtcRoom";
import Main from "./pages/Main";
import Auth from "./pages/Auth";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import RoomDetail from "./pages/RoomDetail";
import RoomList from "./components/Maps/RoomList";
import GbbDetail from "./pages/GbbDetail";
import MyPage from "./pages/MyPage";

const App = () => {
  const [imageList, setImageList] = useState([]);

  const handleImageUpload = (images) => {
    setImageList([...imageList, images]);
  };
  return (
    <BrowserRouter>
      <div className="App">
        <Routes>
          <Route path="/" element={<Main />} />
          <Route path="/map" element={<Map />} />
          <Route path="/map/:word/:lat/:lon" element={<Map />} />
          <Route path="/gbblist" element={<GbbList imageList={imageList} />} />
          <Route path="/gbblist/:articleId" element={<GbbDetail/>} />
          <Route path="/gbbcreate" element={<GbbCreate onImageUpload={handleImageUpload} />} />
          <Route path="/zzim" element={<Zzimlist />} />
          <Route path="/roomout" element={<Roomout />} />
          <Route path="/mypage" element={<MyPage />} />
          <Route path="/roomdetail/:roomDealid" element={<RoomDetail />} />
          <Route path="/login" element={<Signuplogin />} />
          <Route path="/auth" element={<Auth />} />
          <Route path="/chatroom/:isGrantor/:id/:roomDealId" element={<ChatRoom />} />
          <Route path="/chatlist" element={<ChatList />} />
          <Route path="/rtcroom/grantor/:id/:roomDealId" element={<GrantorRtcRoom />} />
          <Route path="/rtcroom/assignee/:id/:roomDealId" element={<AssigneeRtcRoom />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
};

export default App;
