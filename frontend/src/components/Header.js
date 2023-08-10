import React from 'react';
import {Link} from "react-router-dom";
import './Header.css';
import { useState, useEffect } from "react"

const Header = () => {
  const [isAuthorized, setIsAuthorized] = useState('');
  const [userinfo,setUserinfo] = useState({})
  useEffect(() => {
      setIsAuthorized(sessionStorage.getItem("isAuthorized"));
      setUserinfo(JSON.parse(sessionStorage.getItem('member')))
  }, []); // This effect runs once when the component mounts
  
  function handleLogout(){
    setIsAuthorized(false)
    sessionStorage.removeItem("isAuthorized")
    window.location.href = `https://kauth.kakao.com/oauth/logout?client_id=a20ef37212e1ae86b20e09630f6590ce&logout_redirect_uri=http://localhost:3000/`
  }
  
  return (
    <div>
      {isAuthorized ? (
        <div className='header'>
          <div className='parent'>
            <div className='logoparents'>
              <Link to="/">
                <img className='logoIcon' alt="" src='/assets/logo.png' />
              </Link>
            </div>
            <Link to="/map" className='b1'>지도</Link>
            <Link to="/gbblist" className='b1'>곰방봐</Link>
            <Link to="/zzim" className='b1'>찜 목록</Link>
            <Link to="/roomout" className='b1'>방 내놓기</Link>
          </div>
          <Link to="/chatlist" className='b2'>채팅</Link>
          <div>반가워요, {userinfo?.name}!</div>
          <button onClick={handleLogout}>Logout</button>
        </div>
      ) : (
        <div className='header'>
          <div className='parent'>
            <div className='logoparents'>
              <Link to = "/">
                <img className='logoIcon' alt="" src='/assets/logo.png'/>
              </Link>
            </div>
            <Link to = "/map" className='b1'>지도</Link>
            <Link to = "/gbblist" className='b1'>곰방봐</Link>
            <Link to = "/zzim" className='b1'>찜 목록</Link>
            <Link to="/roomout" className='b1'>방 내놓기</Link>
          </div>
          {/* <Link to="/chatroom" className='b2'>채팅</Link> */}
          <Link to="/login" className='b2'>회원가입/로그인</Link>
        </div>
      )}
    </div>
  )
}

export default Header;