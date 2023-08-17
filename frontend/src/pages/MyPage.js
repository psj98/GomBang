import Header from "../components/Header";
import styles from "./MyPage.module.css"
import React, { useState, useEffect } from "react";
import axios from 'axios';

const MyPage = () => {
    const [zzimdata, setZzimdata] = useState([]);
    const [likedata, setLikedata] = useState([]);
    const [username, setUsername] = useState('');
    const [userid, setUserid] = useState('');
    useEffect(() => {
        const member = JSON.parse(sessionStorage.getItem("member"));
        const usernickname = member.nickname;
        setUsername(usernickname)
        const useruuid = member.id;
        setUserid(useruuid)

        // axios.get(`${process.env.REACT_APP_API_ROOT}/mypage/${userid}`)
        // .then(response => {
        //     console.log('받아온 정보 : ', response.data);
        //     setMyData(response.data.data);
        // }).catch(error => {
        //     console.log('오류:', error);
        // });
    }, []);
    useEffect(() => {
            axios.get(`${process.env.REACT_APP_API_ROOT}/star/my-list/${userid}`)
        .then(response => {
            console.log('받아온 정보 : ', response.data);
            setZzimdata(response.data.data.starRoomDealList);
        }).catch(error => {
            console.log('오류:', error);
        });
    }, [userid]);
    useEffect(() => {
        const LikeShowRoomMyListRequestDto = {
            "memberId": userid
        }
        axios.post(`${process.env.REACT_APP_API_ROOT}/like/my-list`, LikeShowRoomMyListRequestDto
        ).then((response) => {
            console.log('좋아요한 목록 데이터', response.data.data)
            setLikedata(response.data.data.showRoomIdList)
        }).catch((error) => {
            console.log(error)
        })
    }, [userid]);
    return (

        <div className={styles.MyPage}>
            <Header />
            <div className={styles.h1}>MY 곰방</div>
            <div className={styles.h1}>{ username }</div>
            {zzimdata.map((value, index) => (
                  <div key={index}>
                    <div>내가 찜한 매물 썸네일{value.roomDeal.thumbnail}</div>
                  </div>
                ))
                }
            {likedata.map((value, index) => (
                  <div key={index}>
                    <div>내가 좋아요한 매물 썸네일{value.roomDeal.thumbnail}</div>
                  </div>
                ))
                }
        </div>
    );
};

export default MyPage;