import React, { useState, useEffect } from "react";
import Header from "../components/Header";
import { useNavigate } from "react-router-dom";
import gbbListStyles from "../components/GbbList.module.css";
import axios from "axios";

const GbbList = ({ imageList }) => {
    const navigate = useNavigate();
    const [searchText, setSearchText] = useState("");
    const [hashtag, setHashtag] = useState("");
    const [selectedTags, setSelectedTags] = useState([]);
    const [responseArticleId, setResponseArticleId] = useState([]); // 응답으로 받은 article_id
    const [userid, setUserid] = useState("");
    const [nearsearch, setNearsearch] = useState([]);
    const [length, setLength] = useState([]);

    useEffect(() => {
        const member = JSON.parse(sessionStorage.getItem("member"));
        if (!member || !member.id) {
            navigate("/login");
            return; 
        }
        const useruuid = member.id;
        setUserid(useruuid);
    }, [setUserid,navigate]);

    useEffect(() => {
        axios.get(`${process.env.REACT_APP_API_ROOT}/showroom`
        ).then((response) => {
            console.log(response.data.data)
            setResponseArticleId(response.data.data);
        }).catch((error) => {
            console.log(error)
        })
    }, [])

    const handleSearch = async () => {
        const ShowRoomSearchRequestDto = {
            "memberId": userid,
            "searchWord": searchText,
            "searchType": 'station',
            "hashTag": selectedTags,
            "sortType": 'desc',
            "pageOffset": 0
        }
        axios.post(`${process.env.REACT_APP_API_ROOT}/showroom/search-result`, ShowRoomSearchRequestDto
        ).then((response) => {
            console.log(response.data)
        }).catch((error) => {
            console.log(error)
        })
    }

    function handleLike(showRoomId) {
        const LikeShowRoomRegisterRequestDto = {
            memberId: userid,
            showRoomId: showRoomId,
        };

        // 좋아요 체크하고 보내야 함
        axios
            .delete(`${process.env.REACT_APP_API_ROOT}/star/delete`, {
                data: LikeShowRoomRegisterRequestDto,
            })
            .then((response) => {
                console.log(response.data.data);
            })
            .catch((error) => {
                console.error("API 호출 에러:", error);
            })
            .then(window.location.reload());
    }

    const handleSearchRelated = () => {
        const SearchRelatedListRequestDto = {
            "searchWord": searchText
        }

        console.log(searchText)

        axios.post(`${process.env.REACT_APP_API_ROOT}/roomdeal/search-related-list`, SearchRelatedListRequestDto
        ).then((response) => {
            console.log(response)
            setNearsearch(response.data.data)
            setLength(response.data.data)
        }).catch((error) => {
            console.error('API 호출 에러:', error);
        });
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter') {
            handleSearchRelated();
            e.preventDefault();
        }
    };

    function handleOnClick(word, type, lat, lon) {
        if (type === 'address') {
            localStorage.setItem('searchloc', word)
            navigate(`/map/${word}/${"lat"}/${"lon"}`)
        }
        else if (type === 'station' || type === 'univ') {
            navigate(`/map/${"word"}/${lat}/${lon}`)
        }
    };

    const handleXClick = () => {
        setSearchText("");
        setNearsearch([])
        setLength([])
    };

    const handleKeyDown = (event) => {
        if (event.key === "Enter") {
            const value = event.target.value.trim();
            if (hashtag.trim() !== "" && !selectedTags.includes("#" + value)) {
                setSelectedTags([...selectedTags, "#" + hashtag]);
                setHashtag("");
            } else {
                event.preventDefault(); // 기본 동작 방지하여 커서가 입력창에 남아있게 함
                event.target.value = ""; // 이미 있는 해시태그나 유효하지 않은 입력일 경우 입력창 비우기
            }
        }
    };

    const handleHashtagChange = (event) => {
        setHashtag(event.target.value);
    };

    const handleDeleteTag = (id) => {
        setSelectedTags(selectedTags.filter((_, index) => index !== id));
    };
    function handlegotoDetail(id) {
        navigate(`/gbblist/${id}`)
    }


    return (
        <div>
            <Header />
            <div className={gbbListStyles.gbbListBody}>
                <div className={gbbListStyles.gbbListTop}>
                    {/* 당신의 곰방을 보여달라 */}
                    <div className={gbbListStyles.showMeTheGbbList}>
                        당신의
                        <span className={gbbListStyles.showMeTheGbbListBold}> 곰방</span>을
                        보여주세요
                    </div >
                    <div>
                        <button onClick={() => navigate("/gbbcreate")}
                            className={gbbListStyles.uploadBtn}>+</button>
                    </div>
                </div>
                {/* 검색창 */}
                <div className={gbbListStyles.gbbListMid}>
                    <div className={gbbListStyles.gbbListMidItem}>
                        <input
                            className={gbbListStyles.gbbListMidInput}
                            value={searchText}
                            onChange={(event) =>
                                setSearchText(event.target.value)
                            }
                            onKeyDown={handleKeyPress}
                            placeholder="보고 싶은 지역을 입력하세요"
                        />
                        <div className={gbbListStyles.gbbXBtnDiv}>
                            <button className={gbbListStyles.gbbXBtn} onClick={() => handleXClick()}>X</button>
                        </div>
                        <div className={gbbListStyles.searchResultScroll}>
                            <div className={gbbListStyles.searchResultFlex}>
                                {length.length > 0 ? (
                                    <>
                                        {
                                            nearsearch.map((value, index) => (
                                                <div key={index} className={gbbListStyles.searchResultDiv}>
                                                    <div onClick={() => handleOnClick(value.searchWord, value.searchType, value.lat, value.lon)}>{value.searchWord}</div>
                                                </div>
                                            ))
                                        }
                                    </>
                                ) : null}
                                {length.length === 0 && searchText.length > 1 ? (
                                    <div className={gbbListStyles.searchResultDiv}>검색결과가 없습니다.</div>
                                ) : null}
                            </div>
                        </div>
                    </div>
                    <div className={gbbListStyles.gbbListMidItem}>
                        <div className={gbbListStyles.hashTagInputMargin}>
                            <input
                                type="text"
                                className={gbbListStyles.gbbListMidInput}
                                value={hashtag}
                                onKeyDown={handleKeyDown}
                                onChange={handleHashtagChange}
                                placeholder="해시태그를 입력하세요"
                            />
                        </div>
                        <div className={gbbListStyles.tagList}>
                            {selectedTags.map((tag, index) => (
                                <span className={gbbListStyles.tagListItem} key={index}
                                    onClick={() => handleDeleteTag(index)}>
                                    {tag}
                                </span>
                            ))}
                        </div>
                    </div>
                    <div className={gbbListStyles.gbbListMidItem}>
                        <button onClick={handleSearch} className={gbbListStyles.gbbSearchBtn}>검색</button>
                    </div>
                </div>
                <div className={gbbListStyles.gbbListBottom}>
                    {/* 사진 Grid 3*X */}
                    <div className={gbbListStyles.imageContainer}>
                        {responseArticleId.map((value, id) => (
                            // <div key={index}>
                            //     <div>구분용 인덱스 : {index}</div>
                            //     <div>곰방봐 id : {value.id}</div>
                            //     <div>매물번호 : {value.roomDeal.id}</div>
                            //     <div>곰방봐 썸네일 : {value.thumbnail}</div>
                            // </div>
                            <div key={id} className={gbbListStyles.imageGridItem}>
                                <img
                                    src={value.thumbnail}
                                    alt={`${value}-${id}`}
                                    className={gbbListStyles.showRoomImg}
                                    onClick={() => handlegotoDetail(value.id)}
                                />
                                {
                                    value.checkLike ?
                                        <>
                                            <div
                                                className={gbbListStyles.heartBtn}
                                                onClick={() => handleLike(value.id)}
                                            >
                                                ♥
                                            </div>
                                        </>
                                        :
                                        <>
                                            <div
                                                className={gbbListStyles.notHeartBtn}
                                                onClick={() => handleLike(value.id)}
                                            >
                                                ♥
                                            </div>
                                        </>
                                }
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default GbbList;
