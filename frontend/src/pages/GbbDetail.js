import axios from "axios";
import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Header from "../components/Header";
import styles from "../components/Gbb.module.css";

import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import "swiper/css/effect-coverflow";

const GbbDetail = () => {
    const navigate = useNavigate();

    const [userid, setUserid] = useState("");
    const [roomDealId, setRoomDealId] = useState("");
    const [gbbdata, setGbbdata] = useState([]);
    const { articleId } = useParams();
    // showroom/{articleId}
    useEffect(() => {
        const member = JSON.parse(sessionStorage.getItem("member"));
        const useruuid = member.id;
        setUserid(useruuid);
    }, [setUserid]);

    useEffect(() => {
        const showRoomDetailRequestDto = {
            memberId: userid,
            showRoomId: articleId,
        };
        axios
            .post(
                `${process.env.REACT_APP_API_ROOT}/showroom/detail`,
                showRoomDetailRequestDto
            )
            .then((response) => {
                console.log(response.data.data);
                setGbbdata(response.data.data);
                const data = response.data.data.showRoom;
                setRoomDealId(data.roomDeal.id);
            })
            .catch((error) => {
                console.log(error);
            });
    }, [userid, articleId]);

    function handleOnClick(id) {
        navigate(`/roomdetail/${id}`);
    }
    function handlelike() {
        const LikeShowRoomRegisterRequestDto = {
            memberId: userid,
            showRoomId: gbbdata.showRoom.id,
        };
        console.log(LikeShowRoomRegisterRequestDto);

        if (gbbdata.checkLike) {
            axios
                .delete(`${process.env.REACT_APP_API_ROOT}/like/delete`,
                LikeShowRoomRegisterRequestDto)
                .then((response) => {
                    console.log(response.data.data);
                })
                .catch((error) => {
                    console.error("API 호출 에러:", error);
                })
                .then(window.location.reload());
        } else {
            axios
                .post(`${process.env.REACT_APP_API_ROOT}/like/register`, 
                 LikeShowRoomRegisterRequestDto,
                )
                .then((response) => {
                    console.log(response.data.data);
                })
                .catch((error) => {
                    console.error("API 호출 에러:", error);
                })
                .then(window.location.reload());
        }
    }

    return (
        <div>
            <Header />
            <div className={styles.container}>
                {" "}
                {/* 본문 - margin설정 */}
                {gbbdata && gbbdata.fileUrls ? (
                    <div className={styles.gbbdetail}>
                        {/* 내용 출력 - 중앙 정렬, flex column */}

                        <div className={styles.images}>
                            <Swiper
                                effect={"coverflow"}
                                // grabCursor={true}
                                centeredSlides={true}
                                slidesPerView={2}
                                // coverflowEffect={{
                                //   rotate: 50,
                                //   stretch: 0,
                                //   //   depth: 100,
                                //   //   modifier: 1,
                                // }}
                                className="swiper"
                            >
                                {gbbdata.fileUrls.map((image, index) => (
                                    <SwiperSlide
                                        key={index}
                                        className={styles.swiperSlide}
                                    >
                                        <img
                                            src={image}
                                            className={styles.swiperImg}
                                            alt="noImage"
                                        />
                                    </SwiperSlide>
                                ))}
                            </Swiper>
                        </div>

                        <div onClick={() => handlelike()}>
                            {gbbdata.checkLike ? "♥" : "♡"}
                        </div>
                        <div className={styles.tagList}>{gbbdata.hashTag.map((value, index) => (

                                <div key={index} className={styles.tagListItem}>{ value.hashTagName }</div>

                        ))}</div>
                        <div>
                            위치 :{" "}
                            {gbbdata.showRoom.jibunAddress
                                .split(" ", 3)
                                .join(" ")}
                        </div>
                        <button onClick={() => handleOnClick(roomDealId)}>
                            매물로 이동하기
                        </button>
                    </div>
                ) : (
                    <div>로딩중</div>
                )}
            </div>
        </div>
    );
};
export default GbbDetail;
