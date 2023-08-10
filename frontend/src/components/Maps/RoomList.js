import React, { useState } from 'react';
import styles from './RoomList.module.css';
import InfiniteScroll from 'react-infinite-scroll-component';


const RoomList = () => {
    const [items, setItems] = useState(Array.from({length:20}))
    const [hasMore, sethasMore] = useState(true)

    const fetchMoreData = () =>{
        if (items.length >= 80) {
            sethasMore(false)
            return
        } 

        setTimeout(()=>{
            setItems(prevItems => prevItems.concat(Array.from({ length: 20 })))
        },1500)
    }

    return (
        <div className={styles.Frame}>
            <div className={styles.btnparent}>
                <button className={styles.mybtn}>입주순</button>  
                <button className={styles.mybtn}>가격순</button>  
                <button className={styles.mybtn}>등록순</button>
            </div>
            <div id="scrollableDiv" className={styles.scrollableDiv}>
                <InfiniteScroll
                    dataLength={items.length}
                    next={fetchMoreData}
                    hasMore={hasMore}
                    loader={<h4>불러오는 중이곰...</h4>}
                    scrollableTarget="scrollableDiv"
                >
                {items.map((i, index) => (
                    <div className={styles.ListItems} key={index}>
                        room - #{index}
                    </div>
                ))}
                </InfiniteScroll>
            </div>
        </div>
    )
}

export default RoomList;