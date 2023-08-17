import React from "react";
import styles from "./ChatList.module.css";
import Header from "../Header";
import ChatListComponent from "./ChatListComponent";

const ChatList = () => {
  return (
    <div>
      <Header />
      <div className={styles.h1}>Message</div>
      <ChatListComponent />
    </div>
  );
};

export default ChatList;
