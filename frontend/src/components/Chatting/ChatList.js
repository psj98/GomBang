import React, { useState, useEffect } from 'react';
import axios from 'axios';
// import SockJS from 'sockjs-client';
// import Stomp from 'stompjs';
// import Header from '../Header';
import { generatePath, useNavigate } from 'react-router-dom';
import styles from "./ChatList.module.css";
import Header from '../Header';


const ChatList = () => {
    const navigate = useNavigate();
    const [isAuthorized, setIsAuthorized] = useState('');
    const [userid, setUserid] = useState('');
    const [chatData, setChatData] = useState([]);
    useEffect(() => {
        setIsAuthorized(sessionStorage.getItem("isAuthorized"));
        // setUserid(JSON.parse(sessionStorage.getItem("member")).id);
        const member = JSON.parse(sessionStorage.getItem("member"));
        const useruuid = member.id;
        setUserid(useruuid)
        if (true) {

        axios.get(`${process.env.REACT_APP_API_ROOT}/chatroom/list/${useruuid}`) 
        // axios.get(`http://localhost:8080/chatroom/list/${useruuid}`) 
        .then(response => {
            console.log('받아온 정보:', response.data);
            // data : chat_room_id, grantor_id, assignee_id, room_deal_id
            setChatData(response.data.data.list); 
            console.log(response.data); 
        })
        .catch(error => {
            console.log('오류:', error);
        });
        } else {
            navigate('/login');
        }

    }, [isAuthorized, navigate]); 

    const enterChatRoom = (chat_room_id, room_deal_id) => {
        // Chatroom 컴포넌트로 전달할 작업 수행
        navigate(`/chatroom/${chat_room_id}/${room_deal_id}`);
    };

    return (
        <div>
            <Header />
            <div className={styles.chatlistpage}>
                <div className={styles.h1}>Message</div>
                <div className={styles.chatlist}>
                {chatData.length > 0 ? (
                        chatData.map((ChatRoom, index) => (
                            <div
                                className={styles.chatlistnickname}
                            >
                                {ChatRoom.grantorId !== userid ? (
                                    <label
                                        key={index}
                                        onClick={() => enterChatRoom(ChatRoom.id, ChatRoom.roomDealId)}
                                    >
                                    {ChatRoom.grantorId.nickname} 님과의 대화
                                    <div className={ styles.entering }>입장하기</div>
                                </label>
                                ) : (
                                    <label
                                        key={index}
                                        onClick={() => enterChatRoom(ChatRoom.id, ChatRoom.roomDealId)}
                                    >
                                        {ChatRoom.assignee.nickname} 님과의 대화
                                        <div className={styles.entering}>입장하기</div>
                                    </label>
                                )
                                }

                            </div>
                    ))
                ) : (
                    <div className={ styles.nochat }>채팅 목록이 없습니다.</div>    
                    )}
                    </div>
            </div>
        </div>
    )
}

export default ChatList;

/* 채팅방 ID: {ChatRoom.id}<br /> */
/* 방 매물 ID: {ChatRoom.roomDealId} */