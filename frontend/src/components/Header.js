import React from 'react';
import {Link} from "react-router-dom";
import './Header.css';

const Header = () => {
    return (
        <div>
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
            <Link to="/login" className='b2'>회원가입/로그인</Link>
        </div>
        </div>
    )
}

export default Header;