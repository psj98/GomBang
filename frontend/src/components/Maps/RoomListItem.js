import axios from 'axios';
import { useEffect } from 'react';
import styles from './RoomListItem.module.css';
import { useState } from 'react';


const RoomListItem = (props) => {
    const [roominfo,setroominfo] = useState({})
    const [roomuptime,setroomuptime] = useState('')
    const [roomlocate,setroomlocate] = useState('')
    useEffect(() => {
        axios.get(`https://i9a804.p.ssafy.io/api/v1/roomdeal/${props.roomid}`)
        .then((r)=>{
            if(r.data.code===1000){
                console.log(r.data)
                setroominfo(r.data.data)
            }
        })
        .catch((e)=>{console.log(e)})
    }, []);

    useEffect(()=>{
        if(roominfo){
            setroomuptime(roominfo?.roomDeal?.registerTime)
            setroomlocate(roominfo?.roomDeal?.jibunAddress)
        }
    },[roominfo])
    
    // function getdatesyrializer(dateString) {
    //     const inputDate = new Date(dateString);
    //     const currentDate = new Date();
    
    //     const timeDifference = currentDate - inputDate;
    //     const daysDifference = Math.floor(timeDifference / (1000 * 60 * 60 * 24));
        
    //     if (daysDifference === 0) {
    //         return '오늘';
    //     } else if (daysDifference === 1) {
    //         return '하루전';
    //     } else if (daysDifference === 2) {
    //         return '이틀전';
    //     } else {
    //         return `${daysDifference}일전`;
    //     }
    // }

    function getlocationserializer(fulllocation) {
        if (typeof(fulllocation)==='string'){ 
            const words = fulllocation.split(' ');
        
            if (words.length >= 3) {
                return words[1] + ' ' + words[2];
            } else if(words.length >= 2) {
                return words[0] + ' ' + words[1];
            } else{
                return fulllocation
            }
        }
        return ''
    }

    function handleonclick(){
        window.location.href = `/roomdetail/${props.roomid}`
    }


    return (
        <div>
        {roominfo?.roomDeal? 
            <div className={styles.Frame} onClick={handleonclick}>
                <img className={styles.roomimage} src={roominfo.roomDeal.thumbnail} alt='image'>
                </img>
                <div className={styles.roombody}>
                    <span className={styles.roomtype}>{roominfo.roomDeal.oneroomType} {roominfo.roomDeal.roomType}</span>
                    <span className={styles.roomprice}>월세 {roominfo.roomDeal.deposit}/{roominfo.roomDeal.monthlyFee}</span>
                    <span className={styles.roomsize}>{(roominfo.roomDeal.roomSize)*3.306}m<sup>2</sup>({roominfo.roomDeal.roomSize} 평) {roominfo.roomDeal.floor}층</span>
                    <span className={styles.roomlocate}>{getlocationserializer(roomlocate)}</span>
                    {/* <span className={styles.roomdescription}>{roominfo.roomDeal.content}</span> */}
                    {/* <span className={styles.roomdate}>{getdatesyrializer(roomuptime)}</span> */}
                </div>
            </div>
        : 
        ''
        }               
        </div>
    )
}

export default RoomListItem;