import React, { useState,useEffect } from "react";
import axios from 'axios';
import Header from "../components/Header";
import styles from "./RoomDetail.module.css";
import { Link,useParams } from "react-router-dom";


const RoomDetail = () => {
    const { roomDealid } = useParams();
    const [roomdata, setRoomData] = useState([]);
    const [roomoption, setRoomOption] = useState([]);
    // const roomid = 3
    const expectedFee = roomdata.monthlyFee + roomdata.managementFee;
    const [userid, setUserid] = useState('');
    
    useEffect(() => {
        const member = JSON.parse(sessionStorage.getItem("member"));
        const useruuid = member.id;
        setUserid(useruuid)
    }, [setUserid]);
    
    useEffect(() => {
        console.log(roomDealid)
        axios.get(`${process.env.REACT_APP_API_ROOT}/roomdeal/${roomDealid}`)
            .then(response => {
                console.log('받아온 정보 : ', response.data);
                setRoomData(response.data.data.roomDeal);
                setRoomOption(response.data.data.roomDealOption);
            }).catch(error => {
                console.log('오류:', error);
            });
    },[roomDealid]);
    const handleChat = async () => {
        try {
            // 백엔드로 보낼 데이터 생성
            const requestData = {
                grantorId: roomdata.member.id,
                assigneeId: userid,
                roomDealId:  roomDealid// your room deal id here
            };

            // 백엔드 API 호출
            const response = await axios.post(`${process.env.REACT_APP_API_ROOT}/chatroom/create`, requestData);

            if (response.status !== 200) {
                throw new Error('Failed to create chat room');
            }

            const responseData = response.data.data;
            const isGrantor = roomdata.member.id === userid ? true : false;
            // 생성된 채팅방으로 페이지 이동
            window.location.href = `/chatroom/${isGrantor}/${responseData.roomId}/${roomDealid}`; // Assuming you have a route for chat rooms

        } catch (error) {
            console.error('Error creating chat room:', error);
        }
    };


    /* HTML + CSS */
    return (
        <div>
            <Header />
            <div className={styles.content}> {/* 매물 본문 */}
                <div className={styles.images}> {/* 이미지 - 나중에 (슬라이딩 구현 어케함) */}
                    <p>이미지 넣을 거임</p>
                    <p>이미지 넣을 거임</p>
                    <p>이미지 넣을 거임</p>
                    <p>이미지 넣을 거임</p>
                    <p>이미지 넣을 거임</p>
                    <p>이미지 넣을 거임</p>
                    <p>이미지 넣을 거임</p>
                </div>
                <div className={styles.detail}> {/* 매물 상세 내용 */}
                    <div className={styles.contentTab}> {/* 매물 상세 내용 좌측 탭 */}
                        <div className={styles.contentDetail}> {/* 가격 정보 */}
                            <h1>가격 정보</h1>
                            <div className={styles.detailInfo}> {/* 월세 flex */}
                                <div className={styles.detailInfoItem}>
                                    <h2>월세</h2>
                                </div>
                                <div className={styles.detailInfoItem}>
                                    <h2>{roomdata.deposit} / {roomdata.monthlyFee}</h2>
                                </div>
                            </div>
                            <hr />
                            <div className={styles.detailInfo}> {/* 관리비 flex */}
                                <div className={styles.detailInfoItem}>
                                    <h2>관리비</h2>
                                </div>
                                <div className={styles.detailInfoItem}>
                                    <h2>매월 {roomdata.managementFee}만원</h2>
                                </div>
                            </div>
                            <hr />
                            <div className={styles.detailInfo}> {/* 한달 예상 주거 비용 flex */}
                                <div className={styles.detailInfoItem}>
                                    <h2>한달<br />예상 주거 비용</h2>
                                </div>
                                <div className={styles.detailInfoItem}>
                                    <h2><span className={styles.expectedFeeFont}>{expectedFee}만원</span><br />월세 + 관리비</h2>
                                </div>
                            </div>
                        </div>
                        <div className={styles.contentDetail} > {/* 상세 정보 */}
                            <h1> 상세 정보</h1>
                            <div className={styles.detailInfo}> {/* 방 종류 flex */}
                                <div className={styles.detailInfoItem}>
                                    <h2>방종류</h2>
                                </div>
                                <div className={styles.detailInfoItem}>
                                    <h2>{roomdata.roomType}</h2>
                                </div>
                            </div>
                            <hr />
                            <div className={styles.detailInfo}> {/* 해당 층 / 건물 층 flex */}
                                <div className={styles.detailInfoItem}>
                                    <h2>해당층 / 건물층</h2>
                                </div>
                                <div className={styles.detailInfoItem}>
                                    <h2>{roomdata.floor}층 / {roomdata.totalFloor}층</h2>
                                </div>
                            </div>
                            <hr />
                            <div className={styles.detailInfo}> {/* 방 수 / 욕실 수 flex */}
                                <div className={styles.detailInfoItem}>
                                    <h2>방 수 / 욕실 수</h2>
                                </div>
                                <div className={styles.detailInfoItem}>
                                    <h2>{roomdata.roomCount}개 / {roomdata.bathroomCount}개</h2>
                                </div>
                            </div>
                            <hr />
                            <div className={styles.detailInfo}> {/* 주차 flex */}
                                <div className={styles.detailInfoItem}>
                                    <h2>주차</h2>
                                </div>
                                <div className={styles.detailInfoItem}>
                                    <h2>{roomoption.parkingLot ? "가능" : "불가능"}</h2>
                                </div>
                            </div>
                            <hr />
                            <div className={styles.detailInfo}> {/* 사용 승인일 flex */}
                                <div className={styles.detailInfoItem}>
                                    <h2>사용 승인일</h2>
                                </div>
                                <div className={styles.detailInfoItem}>
                                    <h2>{roomdata.usageDate}</h2>
                                </div>
                            </div>
                            <hr />
                            <div className={styles.detailInfo}> {/* 입주 가능일 flex */}
                                <div className={styles.detailInfoItem}>
                                    <h2>입주 가능일</h2>
                                </div>
                                <div className={styles.detailInfoItem}>
                                    <h2>{roomdata.moveInDate}</h2>
                                </div>
                            </div>
                            <hr />
                            <div className={styles.detailInfo}> {/* 계약 만료일 flex */}
                                <div className={styles.detailInfoItem}>
                                    <h2>계약 만료일</h2>
                                </div>
                                <div className={styles.detailInfoItem}>
                                    <h2>{roomdata.expirationDate}</h2>
                                </div>
                            </div>
                        </div>
                        <div className={styles.contentDetail}> {/* 옵션 - 이미지 10개 + 이미지 Name  => grid */}
                            <h1>옵션</h1>
                            <div className={styles.detailInfoGrid}>
                                {
                                    roomoption.airConditioner
                                        ? (<><div className={styles.detailInfoGridItem}><img src={`${process.env.PUBLIC_URL}/assets/roomoption/airConditioner.png`} /><h3>에어컨</h3></div></>)
                                        : (null)

                                }
                                {
                                    roomoption.refrigerator
                                        ? (<><div className={styles.detailInfoGridItem}><img src={`${process.env.PUBLIC_URL}/assets/roomoption/refrigerator.png`} /><h3>냉장고</h3></div></>)
                                        : (null)

                                }
                                {
                                    roomoption.washer
                                        ? (<><div className={styles.detailInfoGridItem}><img src={`${process.env.PUBLIC_URL}/assets/roomoption/washer.png`} /><h3>세탁기</h3></div></>)
                                        : (null)

                                }
                                {
                                    roomoption.dryer
                                        ? (<><div className={styles.detailInfoGridItem}><img src={`${process.env.PUBLIC_URL}/assets/roomoption/dryer.png`} /><h3>건조기</h3></div></>)
                                        : (null)

                                }
                                {
                                    roomoption.sink
                                        ? (<><div className={styles.detailInfoGridItem}><img src={`${process.env.PUBLIC_URL}/assets/roomoption/sink.png`} /><h3>싱크대</h3></div></>)
                                        : (null)

                                }
                                {
                                    roomoption.gasRange
                                        ? (<><div className={styles.detailInfoGridItem}><img src={`${process.env.PUBLIC_URL}/assets/roomoption/gasRange.png`} /><h3>가스레인지</h3></div></>)
                                        : (null)

                                }
                                {
                                    roomoption.closet
                                        ? (<><div className={styles.detailInfoGridItem}><img src={`${process.env.PUBLIC_URL}/assets/roomoption/closet.png`} /><h3>옷장</h3></div></>)
                                        : (null)

                                }
                                {
                                    roomoption.shoeCloset
                                        ? (<><div className={styles.detailInfoGridItem}><img src={`${process.env.PUBLIC_URL}/assets/roomoption/shoeCloset.png`} /><h3>신발장</h3></div></>)
                                        : (null)

                                }
                                {
                                    roomoption.fireAlarm
                                        ? (<><div className={styles.detailInfoGridItem}><img src={`${process.env.PUBLIC_URL}/assets/roomoption/fireAlarm.png`} /><h3>화재경보기</h3></div></>)
                                        : (null)

                                }
                                {
                                    roomoption.elevator
                                        ? (<><div className={styles.detailInfoGridItem}><img src={`${process.env.PUBLIC_URL}/assets/roomoption/elevator.png`} /><h3>엘레베이터</h3></div></>)
                                        : (null)

                                }
                            </div>
                        </div>
                        <div className={styles.contentDetail}> {/* 위치 및 주변 시설 */}
                            <h1>위치 및 주변 시설</h1>
                            <h3 className={styles.contentDetailRoadAddress}>{roomdata.roadAddress}</h3>
                            {/* ----------------- 지도 + 마커 + 주변 원 (25m ??) ----------------- */}
                        </div>
                    </div>
                    <div className={styles.contentTab}> {/* 매물 상세 우측 탭 */}
                        <div className={styles.fixedTab}> {/* 우측 채팅 탭 -> fixed */}
                            <div className={styles.fixedInfo}> {/* 상위 탭 */}
                                <div className={styles.fixedTopInfoLeft}> {/* 왼쪽 */}
                                    <h3>{roomdata.jibunAddress}</h3>
                                    <h2>월세 {roomdata.deposit}/{roomdata.monthlyFee}</h2>
                                    <h4>관리비 {roomdata.managementFee}만원</h4>
                                </div>
                                <div className={styles.fixedTopInfoRight}> {/* 오른쪽 - flex justify-content 멀리 */}
                                    <h5>몇일전</h5>
                                    <h4><Link to='/gbblist' className={styles.b1}>더보기</Link></h4> {/* --------- 이동 링크 달아야 함 --------- */}
                                </div>
                            </div>
                            <hr />
                            <div className={styles.fixedInfoNotFlex}> {/* 중간 탭 */}
                                <div className={styles.fixedInfoGrid}>
                                    {/* 면적 + 원룸 유형 - flex space-between */}
                                    <div className={styles.fixedInfoGridItem}>
                                        <h3>전용 {roomdata.roomSize}m^3</h3>
                                    </div>
                                    <div className={styles.fixedInfoGridItem}>
                                        <h3>{roomdata.oneroomType}</h3>
                                    </div>
                                    {/* 층 / 층 + 주차 여부 - flex space-between */}
                                    <div className={styles.fixedInfoGridItem}>
                                        <h3>{roomdata.floor}층 / {roomdata.totalFloor}층</h3>
                                    </div>
                                    <div className={styles.fixedInfoGridItem}>
                                        <h3>주차 {roomoption.parkingLot ? "가능" : "불가능"}</h3>
                                    </div>
                                </div>
                            </div>
                            <hr />
                            <div className={styles.fixedInfoBottomTab}> {/* 양도자와 채팅 버튼 */}
                                <button onClick={handleChat} className={styles.chatButton}>
                                    <h3>양도자와 채팅하기</h3>
                                </button> {/* 중앙 배치 - 가로 세로 둘 다 */}
                            </div>
                        </div>
                    </div>
                </div>
            </div >
        </div >
    );
};

export default RoomDetail;

/* <div>{roomdata.roomType}</div>
<div>{roomdata.roomSize}</div>
<div>{roomdata.roomCount}</div>
<div>{roomdata.oneroomType}</div>
<div>{roomdata.bathroomCount}</div>
<div>{roomdata.roadAddress}</div>
<div>{roomdata.jibunAddress}</div>
<div>{roomdata.monthlyFee}</div>
<div>{roomdata.deposit}</div>
<div>{roomdata.managementFee}</div>
<div>{roomdata.usageDate}</div>
<div>{roomdata.moveInDate}</div>
<div>{roomdata.expirationDate}</div>
<div>{roomdata.floor}</div>
<div>{roomdata.totalFloor}</div> */