// import Gbaddress from "../components/Gbaddress";
import Header from "../components/Header";
import React, { useCallback, useState, useEffect } from "react";
import styles from "./Roomout.module.css";
import DatePicker from "react-datepicker";
import 'react-datepicker/dist/react-datepicker.css'
import { ko } from 'date-fns/esm/locale';
import axios from 'axios';
import { useDaumPostcodePopup }  from 'react-daum-postcode';

export default function Roomout() {
    const [userid, setUserid] = useState('');
    useEffect(() => {
        const member = JSON.parse(sessionStorage.getItem("member"));
        const useruuid = member.id;
        setUserid(useruuid)
    },[setUserid]);
    const [info, setInfo] = useState([]);
    const [struc, setStruc] = useState([]);
    const [nearstation, setNearstation] = useState('');
    const [nearschool, setNearschool] = useState('');
    const [doroaddress, setDoroaddress] = useState('');
    const [jibunaddress, setJibunaddress] = useState('');
    const [lat, setLat] = useState('');
    const [lon, setLon] = useState('');
    const [deposit, setDeposit] = useState(0);
    const [monthlyRent, setMonthlyRent] = useState(0);
    const [managementFee, setManagementFee] = useState(0);
    const [pyeong, setPyeong] = useState(0);
    const [roomCount, setRoomCount] = useState(0);
    const [bathroomCount, setBathroomCount] = useState(0);
    const [totalFloor, setTotalFloor] = useState(0);
    const [content, setContent] = useState('');
    const [elevator, setElevator] = useState("elevatoryes");
    const [parking, setParking] = useState("parkingyes");
    const handleClickInfoButton = (e) => {
        setInfo(e.target.value)
    };
    const handleClickStrucButton = (e) => {
        setStruc(e.target.value)
    };
    const handleTotalFloor = (e) => {
        setTotalFloor(e.target.value)
    };
    const handleContent = (e) => {
        setContent(e.target.value)
    };
    const handleClickElevatorButton = (e) => {
        setElevator(e.target.value)
    };
    const handleClickParkingButton = (e) => {
        setParking(e.target.value)
    };
    const handleDepositChange = (event) => {
        const value = parseInt(event.target.value);
        setDeposit(value);
    };

    const handleMonthlyRentChange = (event) => {
        const value = parseInt(event.target.value);
        setMonthlyRent(value);
    };

    const handleManagementFeeChange = (event) => {
        const value = parseInt(event.target.value);
        setManagementFee(value);
    };
    const handlePyeong = (event) => {
        const value = parseInt(event.target.value);
        setPyeong(value);
    };
    const handleBathroomCount = (event) => {
        const value = parseInt(event.target.value);
        setBathroomCount(value);
    };
    const handleRoomCount = (event) => {
        const value = parseInt(event.target.value);
        setRoomCount(value);
    };

    const [selectedImages, setSelectedImages] = useState([]);
    const handleImageChange = (event) => {
        const files = event.target.files;
        setSelectedImages([...selectedImages, ...files]);
        console.log(selectedImages)
    };
    const checkBoxList = ['에어컨', '냉장고', '세탁기', '건조기', '싱크대', '가스레인지', '장롱', '신발장', '화재경보기'];
    const [checkedList, setCheckedList] = useState([]);

    const checkHandler = (value) => {
        setCheckedList((prev) => {
            if (prev.includes(value)) {
            return prev.filter((item) => item !== value);
            } else {
            return [...prev, value];
            }
        });
        };
    
        const onSubmit = useCallback(
        (e) => {
            e.preventDefault();
            console.log('checkedList:', checkedList);
        },
        [checkedList]
    );
    const [agreement, setAgreement] = useState(false);
    const agreeBtnEvent =()=>{
        if(agreement === false) {
            setAgreement(true)
        }else {
            setAgreement(false)
        }
    };
    // const [date, setDate] = useState(new Date());
    const [startDate, setStartDate] = useState(new Date());
    const [endDate, setEndDate] = useState(new Date());
    const [approveDate, setApproveDate] = useState(new Date());
    const handleCalendarClose = () => console.log("Calendar closed");
    const handleCalendarOpen = () => console.log("Calendar opened");
        // 날짜를 "YYYY-MM-DD" 형식의 문자열로 변환하는 함수
    const formatDate = (date) => {
        if (date) {
            const year = date.getFullYear();
            const month = (date.getMonth() + 1).toString().padStart(2, '0');
            const day = date.getDate().toString().padStart(2, '0');
            return `${year}-${month}-${day}`;
        }
        return ''; // 날짜가 선택되지 않았을 때 처리
    };
    const formattedStartDate = formatDate(startDate);
    const formattedEndDate = formatDate(endDate);
    const formattedApproveDate = formatDate(approveDate);

    const [floor, setFloor] = useState('');
    const handleFloorChange = (event) => {
    const value = parseInt(event.target.value);
    setFloor(value);
};

const onRealSubmit = useCallback(async (e) => {
    e.preventDefault();
    const formData = new FormData();
    // console.log(selectedImages);
    // formData.append("checkFiles", selectedImages);
    // console.log(formData);
    selectedImages.forEach(image => {
        formData.append('files', image);
        console.log('중간점검',formData.file)
    });
    // formData.append('file', selectedImages);


    for (let file of formData.getAll('files')) {
        console.log('File Name:', file['name']);
        console.log('File Type:', file.type);
        console.log('File Size:', file.size);
        // You can log other properties as needed
    }
    const roomDealRegisterRequestDto = {
            "roomDealRegisterDefaultDto": {
                'id': userid,
                "roomType": info,
                "roomSize": pyeong,
                "roomCount": roomCount,
                "oneroomType": struc,
                "bathroomCount": bathroomCount,
                "roadAddress": doroaddress,
                "jibunAddress": jibunaddress,
                "monthlyFee": monthlyRent,
                "deposit": deposit,
                "managementFee": managementFee,
                "usageDate": formattedApproveDate,
                "moveInDate": formattedStartDate,
                "expirationDate": formattedEndDate,
                "floor": floor,
                "totalFloor": totalFloor,
                "lat": lat,
                "lon": lon,
                "station": nearstation,
                "univ": nearschool,
                "content": content,
                // 다른 속성들도 유사하게 추가해주세요
            },
            "roomDealRegisterOptionDto": {
                "airConditioner": checkedList.includes("에어컨"),
                "refrigerator": checkedList.includes("냉장고"),
                "washer": checkedList.includes("세탁기"),
                "dryer": checkedList.includes("건조기"),
                "sink": checkedList.includes("싱크대"),
                "gasRange": checkedList.includes("가스레인지"),
                "closet": checkedList.includes("장롱"),
                "shoeCloset": checkedList.includes("신발장"),
                "fireAlarm": checkedList.includes("화재경보기"),
                "elevator": elevator === "elevatoryes",
                "parkingLot": parking === "parkingyes"
            }
        }
;
    // console.log(value)
    const jh = new Blob([JSON.stringify(roomDealRegisterRequestDto)], { type: "application/json" });
    formData.append("roomDealRegisterRequestDto", jh);
    // const formData = {
    //     "files": selectedImages,
    //     "roomDealRegisterRequestDto": {
    //         "roomDealRegisterDefaultDto": {
    //             'id': userid,
    //             "roomType": info,
    //             "roomSize": pyeong,
    //             "roomCount": roomCount,
    //             "oneroomType": struc,
    //             "bathroomCount": bathroomCount,
    //             "roadAddress": doroaddress,
    //             "jibunAddress": jibunaddress,
    //             "monthlyFee": monthlyRent,
    //             "deposit": deposit,
    //             "managementFee": managementFee,
    //             "usageDate": approveDate,
    //             "moveInDate": startDate,
    //             "expirationDate": endDate,
    //             "floor": floor,
    //             "totalFloor": totalFloor,
    //             "lat": lat,
    //             "lon": lon,
    //             "station": nearstation,
    //             "univ": nearschool,
    //             "content": content,
    //             // 다른 속성들도 유사하게 추가해주세요
    //         },
    //         "roomDealRegisterOptionDto": {
    //             "airConditioner": checkedList.includes("에어컨"),
    //             "refrigerator": checkedList.includes("냉장고"),
    //             "washer": checkedList.includes("세탁기"),
    //             "dryer": checkedList.includes("건조기"),
    //             "sink": checkedList.includes("싱크대"),
    //             "gasRange": checkedList.includes("가스레인지"),
    //             "closet": checkedList.includes("장롱"),
    //             "shoeCloset": checkedList.includes("신발장"),
    //             "fireAlarm": checkedList.includes("화재경보기"),
    //             "elevator": elevator === "elevatoryes",
    //             "parkingLot": parking === "parkingyes"
    //         }
    //     }
    // };
    // console.log(formData)
    try {
        // for (let value of formData.values()) {
        //     console.log(value);
        //   }
        const response = await axios.post(
            `${process.env.REACT_APP_API_ROOT}/roomdeal/register`,
            formData,
            {
                headers: {
                    "Content-Type" : "multipart/form-data", 
                    // charset : 'utf-8'// Content-Type을 반드시 이렇게 하여야 합니다.
                },
            }
        );
        console.log(formData)
        console.log("데이터 전송 성공:", response.data);

        // 선택적으로 성공을 처리하거나 사용자에게 성공 메시지를 보여줄 수 있습니다
    } catch (error) {
        console.error("데이터 전송 오류:", error);
        // 선택적으로 오류를 처리하거나 사용자에게 오류 메시지를 보여줄 수 있습니다
    }
}, [info,struc,formattedStartDate, nearstation,roomCount,deposit,pyeong, elevator, formattedEndDate, floor, managementFee, monthlyRent, parking,formattedApproveDate,bathroomCount, checkedList,content,totalFloor,lat,lon,nearschool,userid,doroaddress,jibunaddress,selectedImages]);

    // 여기는 주소 입력창 팝업 열리는 부분 ---------------------------------------------------------------------------------
    const scriptUrl="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"
    const open = useDaumPostcodePopup(scriptUrl);
    const handleClick = () => {
        open({ onComplete: handleExecDaumPostcode });
    };

    const handleExecDaumPostcode = (data) => {

        // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

        // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
        // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
        var roadAddr = data.roadAddress; // 도로명 주소 변수
        var extraRoadAddr = ''; // 참고 항목 변수

        // 법정동명이 있을 경우 추가한다. (법정리는 제외)
        // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
        if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
            extraRoadAddr += data.bname;
        }
        // 건물명이 있고, 공동주택일 경우 추가한다.
        if (data.buildingName !== '' && data.apartment === 'Y') {
            extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
        }
        // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
        if (extraRoadAddr !== '') {
            extraRoadAddr = ' (' + extraRoadAddr + ')';
        }

        // 우편번호와 주소 정보를 해당 필드에 넣는다.
        document.getElementById('postcode').value = data.zonecode;
        document.getElementById("roadAddress").value = roadAddr;
        setDoroaddress(roadAddr)
        document.getElementById("jibunAddress").value = data.jibunAddress;
        setJibunaddress(data.jibunAddress)
        // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
        // if (roadAddr !== '') {
        //     document.getElementById("extraAddress").value = extraRoadAddr;
        // } else {
        //     document.getElementById("extraAddress").value = '';
        // }

        var guideTextBox = document.getElementById("guide");
        // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
        if (data.autoRoadAddress) {
            var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
            guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
            guideTextBox.style.display = 'block';

        } else if (data.autoJibunAddress) {
            var expJibunAddr = data.autoJibunAddress;
            guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
            guideTextBox.style.display = 'block';
        } else {
            guideTextBox.innerHTML = '';
            guideTextBox.style.display = 'none';
        }
        const apiUrl = `https://dapi.kakao.com/v2/local/search/address.json?query=${data.roadAddress}`;

        axios
            .get(apiUrl, {
                headers: {
                    Authorization: `KakaoAK ${process.env.REACT_APP_REST_API_MAP_KEY}`,
            },
        }).then((response) => {
            const { documents } = response.data;
            if (documents.length > 0) {
                const { x, y } = documents[0];
                setLat(y.toString());
                setLon(x.toString());
                const addressData = {
                    "lat" : y.toString(),
                    "lon": x.toString(),
                    "content" : ''
                }
                // const RealaddressData = JSON.stringify(addressData)
                // console.log(addressData)
                axios.post(`${process.env.REACT_APP_API_ROOT}/roomdeal/search-nearest`,addressData
                    ).then((response) => {
                        console.log(response)
                        console.log(response.data.data.stationName)
                        setNearstation(response.data.data.stationName)
                        setNearschool(response.data.data.univName)

                    }).catch((error) => {
                        console.error('Error fetching data:', error);
                    });
            console.log('Longitude:', x);
            console.log('Latitude:', y);
            } else {
            console.log('No results found.');
            }
        })
        .catch((error) => {
            console.error('Error fetching data:', error);
        });
    }
    //-------------------------------------------------------------------------------------------------------------------
    
    
    return (
        <div>
            <Header />
        <div className={styles.roomout}>
            <div className={styles.roomoutform}>
                <h1 className={styles.h1_title}>곰방 내놓기</h1>
                <div className={styles.gbinfo}>
                    <h2 className={styles.h2_title}>곰방 정보</h2>
                    <h3 className={styles.h3_title}>곰방 유형</h3>
                    <div className={styles.gbtype}>
                        <label className={styles.oneroom}>
                            <input type="radio" value="원룸" checked={info === "원룸"} onChange={handleClickInfoButton} />
                            <p>원룸</p>
                        </label>
                        <label className={styles.officetell}>
                            <input type="radio" value="오피스텔" checked={info === "오피스텔"} onChange={handleClickInfoButton} />
                            <p>오피스텔</p>
                        </label>
                        <label className={styles.villa}>
                            <input type="radio" value="빌라" checked={info === "빌라"} onChange={handleClickInfoButton} />
                            <p>빌라</p>
                        </label>
                        <label className={styles.apartment}>
                            <input type="radio" value="아파트" checked={info === "아파트"} onChange={handleClickInfoButton} />
                            <p>아파트</p>
                        </label>
                    </div>
                    <h3 className={styles.h3_title}>곰방 구조</h3>
                    <div className={styles.gbstructure}>
                        <label>
                            <input type="radio" value="오픈형" checked={struc === "오픈형"} onChange={handleClickStrucButton} />
                            <p>오픈형</p>
                        </label>
                        <label>
                            <input type="radio" value="분리형" checked={struc === "분리형"} onChange={handleClickStrucButton} />
                            <p>분리형</p>
                        </label>
                        <label>
                            <input type="radio" value="복층형" checked={struc === "복층형"} onChange={handleClickStrucButton} />
                            <p>복층형</p>
                        </label>
                        {/* <label>
                            <input type="radio" value="tworoom" checked={struc === "tworoom"} onChange={handleClickStrucButton} />
                            투룸
                        </label>
                        <label>
                            <input type="radio" value="threenmore" checked={struc === "threenmore"} onChange={handleClickStrucButton} />
                            쓰리룸 이상
                        </label> */}
                    </div>
                    <h3 className={styles.h3_title}>주소</h3>
                    <div className={styles.gbaddress}>
                        <div className={styles.addresstoparea}>
                            <input className={styles.postcode} type="text" id="postcode" placeholder="우편번호" disabled/>
                            <input
                                type="button"
                                onClick={handleClick}
                                value="우편번호 찾기"
                                style={{
                                    padding: '8px 16px',
                                    fontSize: '16px',
                                    // fontWeight: 'bold',
                                    color: 'white',
                                    backgroundColor: '#C7AD92',
                                    border: 'none',
                                    borderRadius: '4px',
                                    cursor: 'pointer',
                                    // marginTop: '10px',
                                }}/>
                        </div>
                        <div className={styles.addressbuttomarea}>
                            <input type="text" id="roadAddress" placeholder="도로명주소" disabled/>
                            <input type="text" id="jibunAddress" placeholder="지번주소" disabled/>
                            <span id="guide" style={{ color: '#999', display: 'none' }}></span>
                            <input className={styles.detailaddress} type="text" id="detailAddress" placeholder="상세주소" />
                        </div>
                        {/* {<DaumPostcode onComplete={handleExecDaumPostcode} />} */}
                    </div>
                        <h3 className={styles.h3_title}>근처 역/학교</h3>
                        <div className={styles.stationuniv}>
                            {nearstation ? (
                                <div className={styles.nearstation} ><div>{nearstation}</div></div>) : (
                                <div className={styles.nearstation} >가까운 역이 없습니다. </div>)
                                
                            }
                            {nearschool ? (
                                <div className={styles.nearstation} ><div>{nearschool}</div></div>) : (
                                <div className={styles.nearstation} >가까운 대학교가 없습니다. </div>)
                                
                            }
                        </div>
                    <div className={styles.moneybox}>
                        <div>
                            <h3 className={styles.h3_title}>보증금</h3>
                            <p className={ styles.detailtext }>.</p>
                            <div className={ styles.money }><input type="number" value={deposit} onChange={handleDepositChange} step="10000" min="0" placeholder="보증금"/>원</div>
                        </div>
                        <div>
                            <h3 className={styles.h3_title}>월세</h3>
                            <p className={ styles.detailtext }>전세 일 경우 0을 입력하세요.</p>
                            <div className={ styles.money }><input type="number" value={monthlyRent} onChange={handleMonthlyRentChange} step="10000" min="0" placeholder="월세"/>원</div>
                        </div>
                        <div>
                            <h3 className={styles.h3_title}>관리비</h3><p className={ styles.detailtext }>없을 경우 0을 입력하세요.</p>
                            <div className={ styles.money }><input type="number" value={managementFee} onChange={handleManagementFeeChange} step="10000" min="0" placeholder="관리비 (없을 경우 0 입력)" />원</div>
                        </div>
                    </div>
                    <div className={styles.datebox}>
                        <div>
                            <h3 className={styles.h3_title}>입주 가능 일자</h3>
                                <DatePicker
                                    showIcon
                                    selected={startDate}
                                    onChange={(date) => setStartDate(date)}
                                    isClearable
                                    locale={ko}
                                    onCalendarClose={handleCalendarClose}
                                    onCalendarOpen={handleCalendarOpen}
                                    dateFormat="yyyy년 MM월 dd일"
                                    minDate={new Date()}
                                    />일
                        </div>
                        <div>
                            <h3 className={styles.h3_title}>계약 만료 일자</h3>
                            <DatePicker
                                    showIcon
                                    selected={endDate}
                                    onChange={(date) => setEndDate(date)}
                                    isClearable
                                    locale={ko}
                                    onCalendarClose={handleCalendarClose}
                                    onCalendarOpen={handleCalendarOpen}
                                    dateFormat="yyyy년 MM월 dd일"
                                    minDate={startDate}
                                        />일
                        </div>
                        <div>
                            <h3 className={styles.h3_title}>사용 승인일</h3>
                            <DatePicker
                                showIcon
                                selected={approveDate}
                                onChange={(date) => setApproveDate(date)}
                                isClearable
                                locale={ko}
                                onCalendarClose={handleCalendarClose}
                                onCalendarOpen={handleCalendarOpen}
                                dateFormat={"yyyy년 MM월 dd일"}
                                dateFormatCalendar={"yyyy년 MM월"}
                                // dropdownMode="select"
                                showYearDropdown
                                showMonthDropdown
                                        />일
                        </div>
                    </div>
                    <div className={styles.cmd}>
                        <div>
                            <h3 className={styles.h3_title}>층 수</h3>
                            <div>
                                <input type="number" value={floor} onChange={handleFloorChange} placeholder="현재 층 수" />층 /
                                <input type="number" value={totalFloor} onChange={handleTotalFloor} placeholder="건물 전체 층 수" />층
                            </div>
                        </div>
                        <div>
                            <h3 className={styles.h3_title}>엘리베이터</h3>
                            <div className={styles.elevator}>
                                <label>
                                    <input type="radio" value="elevatoryes" checked={elevator === "elevatoryes"} onChange={handleClickElevatorButton} />
                                    있음
                                </label>
                                <label>
                                    <input type="radio" value="elevatorno" checked={elevator === "elevatorno"} onChange={handleClickElevatorButton} />
                                    없음
                                </label>
                            </div>
                        </div>
                        <div>
                            <h3 className={styles.h3_title}>주차 가능 여부</h3>
                            <div className={styles.elevator}>
                                <label>
                                    <input type="radio" value="parkingyes" checked={parking === "parkingyes"} onChange={handleClickParkingButton} />
                                    가능
                                </label>
                                <label>
                                    <input type="radio" value="parkingno" checked={parking === "parkingno"} onChange={handleClickParkingButton} />
                                    불가능
                                </label>
                            </div>
                        </div>
                    </div>
                    <div className={styles.roomoption}>
                        <div>
                            <h3 className={styles.h3_title}>평 수</h3>
                            <div><input type="number"  value={pyeong} onChange={handlePyeong} placeholder="평 수"/>평</div>
                        </div>
                        <div>
                            <h3 className={styles.h3_title}>방 수</h3>
                            <div><input type="number"  value={roomCount} onChange={handleRoomCount} placeholder="방 수"/>개</div>
                        </div>
                        <div>
                            <h3 className={styles.h3_title}>욕실 수</h3>
                            <div><input type="number"  value={bathroomCount} onChange={handleBathroomCount} placeholder="욕실 수"/>개</div>
                        </div>
                    </div>
                    
                
                    <h3 className={styles.h3_title}>추가 옵션</h3>
                    <form className={styles.option} onSubmit={onSubmit}>
                        <div className={styles.checkboxgroup}>
                            {checkBoxList.map((item, idx) => (
                                <div className={styles.checkbox} key={idx}>
                                    <input
                                        type='checkbox'
                                        id={item}
                                        checked={checkedList.includes(item)}
                                        onChange={() => checkHandler(item)}
                                        />
                                    <label htmlFor={item}>{item}</label>
                                </div>
                            ))}
                        </div>

                        {/* <button type='submit'>저장</button> */}
                    </form>
                    
                    <h3 className={styles.h3_title}>매물 사진</h3>
                    <div>
                        <input
                            type="file"
                            accept="image/*"
                            multiple
                            onChange={handleImageChange} />
                    </div>
                    <h3 className={styles.h3_title}>상세 설명</h3>
                    <div className={styles.textarea}>
                        <textarea value={content} onChange={handleContent} rows="5" cols="50"/></div>
                    </div>
                    <div className={styles.check}>
                        <input type="checkbox" id="check1" checked={agreement} onChange={agreeBtnEvent}/>
                        <label htmlFor="check1">매물관리규정을 확인하였으며, 입력한 정보는 실제 매물과 다름이 없습니다. <span className='pilsoo'>(필수)</span></label>
                    </div>
            <button onClick={onRealSubmit}>곰방 내놓기</button>
            </div>
            </div>
            </div>
    );      
    
}