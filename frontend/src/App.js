import React, { useState } from "react";
import Map from "./pages/MapPage";
import Zzimlist from "./pages/Zimlist";
import Roomout from "./pages/Roomout";
import Signuplogin from "./pages/Signuplogin";
import GbbList from "./pages/List";
import GbbCreate from "./pages/Create";
// import TestApp from "./components/TestApp";
import ChatRoom from "./components/Chatting/ChatRoom";
import ChatList from "./components/Chatting/ChatList";
import AssigneeRtcRoom from "./components/Rtc/AssigneeRtcRoom";
import RtcRoom from "./components/Rtc/RtcRoom";
import GrantorRtcRoom from "./components/Rtc/GrantorRtcRoom";
import Main from "./pages/Main";
import Auth from "./pages/Auth";
import RtcRoomQR from "./components/Rtc/RtcRoomQR";

import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";

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
          <Route path="/gbblist" element={<GbbList imageList={imageList} />} />
          <Route path="/gbbcreate" element={<GbbCreate onImageUpload={handleImageUpload} />} />
          <Route path="/zzim" element={<Zzimlist />} />
          <Route path="/roomout" element={<Roomout />} />
          <Route path="/login" element={<Signuplogin />} />
          <Route path="/auth" element={<Auth />} />
          {/* <Route path="/test" element={<TestApp />} /> */}
          <Route path="/chatroom/:isGrantor/:id/:roomDealId" element={<ChatRoom />} />
          <Route path="/chatlist" element={<ChatList />} />
          <Route path="/rtcroom/:id/:roomDealId" element={<RtcRoom />} />
          <Route path="/rtcroom/grantor/:id/:roomDealId" element={<GrantorRtcRoom />} />
          <Route path="/rtcroom/assignee/:id/:roomDealId" element={<AssigneeRtcRoom />} />
          <Route path="/rtcroom/qr/:id/:roomDealId" element={<RtcRoomQR />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
};

export default App;
