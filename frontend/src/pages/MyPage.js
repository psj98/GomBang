import Header from "../components/Header";
import styles from "./Mypage.module.css"
import React, { useState, useEffect } from "react";
import axios from 'axios';

const MyPage = () => {
    const [mydata, setMyData] = useState([]);
    // const [userid, setUserid] = useState('');
    useEffect(() => {
        const member = JSON.parse(sessionStorage.getItem("member"));
        const userid = member.id;
        axios.get(`${process.env.REACT_APP_API_ROOT}/mypage/${userid}`)
        .then(response => {
            console.log('받아온 정보 : ', response.data);
            setMyData(response.data.data);
        }).catch(error => {
            console.log('오류:', error);
        });
    });

    return (

        <div className={styles.MyPage}>
            <Header />
            <div className={styles.h1}>MY 곰방</div>
            <div className={styles.h1}>{ mydata.id }</div>
            <div className={styles.h1}>{ mydata.nickname }</div>

        </div>
    );
};

export default MyPage;