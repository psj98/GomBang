import React, { useState, useEffect } from 'react';
import styles from './RoomList.module.css';
import InfiniteScroll from 'react-infinite-scroll-component';
import RoomListItem from './RoomListItem';
import axios from 'axios';



const RoomList = (props) => {
    const [hasMore, sethasMore] = useState(true);
    const [roomids, setRoomids] = useState([]);
    useEffect(() => {
        if(!props.lat || !props.lon || !props.word) return
        const SearchByAddressRequestDto = {
            "address": props.word,
            "content": '',
        };
        const SearchByStationUnivRequestDto = {
            "lat": props.lat,
            "lon": props.lon,
            "content": '',
        };
        if (props.lat === 'lat') {
            axios.post(`${process.env.REACT_APP_API_ROOT}/roomdeal/search-address`, SearchByAddressRequestDto
            ).then((response) => {
                console.log("주소주소", response.data)
                const temp = []
                temp.concat()
                response.data.data.map((value,index)=>(roomids = roomids.concat(value.roomDealId)))
                console.log('roomids',roomids)
            }).catch((error) => {
                console.error('API 호출 에러:', error);
            })
        }
        else {
            axios.post(`${process.env.REACT_APP_API_ROOT}/roomdeal/search-station-univ`, SearchByStationUnivRequestDto
            ).then((response) => {
                console.log("역역", response.data)
                response.data.data.map((value,index)=>(roomids = roomids.concat(value.roomId)))
                console.log('roomids',roomids)
            }).catch((error) => {
                console.error('API 호출 에러:', error);
            })
        }
    }, [props.lat,props.lon,props.word]);

    const fetchMoreData = () =>{
        if (roomids.length >= 80) {
            sethasMore(false)
            return
        } 

        setTimeout(()=>{
            setRoomids(prevItems => prevItems.concat(roomids))
        },1500)
    }

    return (
        <div className={styles.Frame}>
            <div className={styles.btnparent}>
            </div>
            <div id="scrollableDiv" className={styles.scrollableDiv}>
                <InfiniteScroll
                    dataLength={roomids.length}
                    next={fetchMoreData}
                    hasMore={hasMore}
                    loader={<h4>불러오는 중이곰...</h4>}
                    scrollableTarget="scrollableDiv"
                >
                {roomids.map((value, index) => (
                    <RoomListItem key={index} roomid={value}/>
                ))}
                </InfiniteScroll>
            </div>
        </div>
    )
}

export default RoomList;