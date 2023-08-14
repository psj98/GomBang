import Header from "../components/Header";
import React, { useState } from "react";
import axios from 'axios';

const RoomDetail = () => {
    const [roomdata, setRoomData] = useState([]);
    const [roomoption, setRoomOption] = useState([]);
    const roomid=''
    axios.get(`${process.env.REACT_APP_API_ROOT}/roomdeal/${roomid}`)
    .then(response => {
        console.log('받아온 정보 : ', response.data);
        setRoomData(response.data.data.roomDeal);
        setRoomOption(response.data.data.roomDealOption);
    }).catch(error => {
        console.log('오류:', error);
    });




    return (
        <div>
            <Header />
            <div>{ roomdata.id }</div>
            <div>{ roomdata.member.id }</div>
            <div>{ roomdata.roomType }</div>
            <div>{ roomdata.roomSize }</div>
            <div>{ roomdata.roomCount }</div>
            <div>{ roomdata.oneroomType }</div>
            <div>{ roomdata.bathroomCount }</div>
            <div>{ roomdata.roadAddress }</div>
            <div>{ roomdata.jibunAddress }</div>
            <div>{ roomdata.monthlyFee }</div>
            <div>{roomdata.deposit}</div>
            <div>{roomdata.managementFee}</div>
            <div>{roomdata.usageDate}</div>
            <div>{roomdata.moveInDate}</div>
            <div>{roomdata.expirationDate}</div>
            <div>{roomdata.floor}</div>
            <div>{roomdata.totalFloor}</div>
            <div>{roomoption.airConditioner}</div>

        </div>
    );
};

export default RoomDetail;

/* 
"airConditioner": boolean,
"refrigerator": boolean,
"washer": boolean,
"dryer": boolean,
"sink": boolean,
"gasRange": boolean,
"closet": boolean,
"shoeCloset": boolean,
"fireAlarm": boolean,
"elevator": boolean,
"parkingLot": boolean
*/