import React, { useState, useEffect, useRef } from "react";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import axios from "axios";
import styles from "./ChatRoom.module.css";

var stompClient = null;
var useruuid;
const ChatRoomComponent = (props) => {
  const [message, setMessage] = useState("");
  const [messages, setMessages] = useState([]);
  const [previousmessage, setPreviousmessage] = useState([]);
  const messageEndRef = useRef(null);

  useEffect(() => {
    const member = JSON.parse(sessionStorage.getItem("member"));
    useruuid = member.id;
    connect();
    gethistory();
  }, []);

  useEffect(() => {
    messageEndRef.current.scrollIntoView({ behavior: "smooth" });
  }, [messages, previousmessage]);

  function connect() {
    // 연결하고자하는 Socket 의 endPoint
    const socket = new SockJS(`${process.env.REACT_APP_API_ROOT}/ws-stomp`);
    stompClient = Stomp.over(socket);
    // {}는 header에 담길 내용, 뒤의 함수들은 콜백 함수
    stompClient.connect({}, onConnected, onError);
  }

  function onConnected() {
    // sub 할 url => /sub/chat/room/roomId 로 구독한다
    stompClient.subscribe("/sub/chat/room/" + props.id, onMessageReceived);

    // 서버에 유저가 들어왔다는 것을 알림
    // /pub/chat/enterUser 로 메시지를 보냄
    stompClient.send(
      "/pub/chat/enteruser",
      {},
      JSON.stringify({
        roomId: props.id,
        memberId: useruuid,
      })
    );
  }
  function onError(error) {
    // 오류 처리
  }
  function gethistory() {
    axios
      .get(`${process.env.REACT_APP_API_ROOT}/chat/history/${props.id}`)
      .then((response) => {
        console.log("받아온 정보 : ", response.data);
        setPreviousmessage(response.data.data.history);
      })
      .catch((error) => {
        console.log("오류:", error);
      });
  }
  function sendMessage(event) {
    event.preventDefault();
    const messageContent = message.trim();

    if (messageContent && stompClient) {
      const chatMessage = {
        chat: {
          messageId: "",
          roomId: props.id,
          sender: useruuid,
          message: messageContent,
          time: "",
        },
      };
      console.log(chatMessage);
      stompClient.send("/pub/chat/sendmessage", {}, JSON.stringify(chatMessage));
      setMessage("");
    }
  }
  function formatTime(timeString) {
    const date = new Date(timeString);
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const period = hours >= 12 ? "오후" : "오전";
    const formattedHours = hours % 12 === 0 ? 12 : hours % 12;
    const formattedMinutes = minutes < 10 ? `0${minutes}` : minutes;

    return `${period} ${formattedHours}:${formattedMinutes}`;
  }

  function onMessageReceived(payload) {
    const chat = JSON.parse(payload.body);
    setMessages((prevMessages) => [...prevMessages, chat]);
  }

  return (
    <div>
      <div className={styles.chatballoon}>
        {previousmessage &&
          previousmessage.map((chat) => (
            <div key={chat.sender + chat.time} className={styles.chatmessage}>
              {/* 보낸 사람이 상대방 */}
              {/* {console.log(chat)} */}
              {chat.sender !== useruuid ? (
                <div className={styles.yourchatbox}>
                  <span className={styles.yourballoon}>{chat.message}</span>
                  <span className={styles.yourballoontime}>{formatTime(chat.time)}</span>
                </div>
              ) : (
                // 보낸 사람이 나
                <div className={styles.mychatbox}>
                  <span className={styles.myballoontime}>{formatTime(chat.time)}</span>
                  <span className={styles.myballoon}>{chat.message}</span>
                </div>
              )}
            </div>
          ))}
        {messages.map((chat) => (
          <div key={chat.sender + chat.time} className={styles.chatmessage}>
            {/* 보낸 사람이 상대방 */}
            {chat.sender !== useruuid ? (
              <div className={styles.yourchatbox}>
                <span className={styles.yourballoon}>{chat.message}</span>
                <span className={styles.yourballoontime}>{formatTime(chat.time)}</span>
              </div>
            ) : (
              // 보낸 사람이 나
              <div className={styles.mychatbox}>
                <span className={styles.myballoontime}>{formatTime(chat.time)}</span>
                <span className={styles.myballoon}>{chat.message}</span>
              </div>
            )}
          </div>
        ))}
      </div>
      <div ref={messageEndRef}></div>

      {/* --------------- 메세지 입력하는 부분 ------------ */}
      <div className={styles.sendmessage}>
        <form id="messageForm">
          <input
            id="message"
            type="text"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
          />
          <button type="submit" onClick={sendMessage}>
            Send
          </button>
        </form>
      </div>
    </div>
  );
};

export default ChatRoomComponent;
