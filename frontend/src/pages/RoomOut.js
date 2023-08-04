import Gbaddress from "../components/Gbaddress";
import Header from "../components/Header";
import React, { useCallback, useState } from "react";
import styles from "./Roomout.module.css";
import DatePicker from "react-datepicker";
import 'react-datepicker/dist/react-datepicker.css'
import { ko } from 'date-fns/esm/locale';

export default function Roomout() {
    const [info, setInfo] = useState([]);
    const [struc, setStruc] = useState([]);
    const [elevator, setElevator] = useState([]);
    const [parking, setParking] = useState([]);
    const handleClickInfoButton = (e) => {
        setInfo(e.target.value)
    };
    const handleClickStrucButton = (e) => {
        setStruc(e.target.value)
    };
    const handleClickElevatorButton = (e) => {
        setElevator(e.target.value)
    };
    const handleClickParkingButton = (e) => {
        setParking(e.target.value)
    };
    const [selectedImages, setSelectedImages] = useState([]);
    const handleImageChange = (event) => {
        const files = event.target.files;
        setSelectedImages([...selectedImages, ...files]);
    };
    const checkBoxList = ['에어컨', '냉장고', '세탁기', '건조기', '싱크대', '전자레인지', '가스레인지', '신발장', '화재경보기', '현관 보안', '방충망'];
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

    const handleKeyPress = (e) => {
        const charCode = e.which ? e.which : e.keyCode;
        if (charCode !== 8 && charCode !== 0 && (charCode < 48 || charCode > 57)) {
          e.preventDefault();
        }
    }

    return (
        <div>
            <Header />
        <div className={styles.roomout}>
            <div className={styles.roomoutform}>
                <h1>곰방 내놓기</h1>
                <div className={styles.gbinfo}>
                    <h2>곰방 정보</h2>
                    <h3>곰방 유형</h3>
                    <div className={styles.gbtype}>
                        <label>
                            <input type="radio" value="oneroom" checked={info === "oneroom"} onChange={handleClickInfoButton} />
                            원룸
                        </label>
                        <label>
                            <input type="radio" value="officetell" checked={info === "officetell"} onChange={handleClickInfoButton} />
                            오피스텔
                        </label>
                        <label>
                            <input type="radio" value="villa" checked={info === "villa"} onChange={handleClickInfoButton} />
                            빌라
                        </label>
                        <label>
                            <input type="radio" value="apartment" checked={info === "apartment"} onChange={handleClickInfoButton} />
                            아파트
                        </label>
                    </div>
                    <h3>곰방 구조</h3>
                    <div className={styles.gbstructure}>
                        <label>
                            <input type="radio" value="open" checked={struc === "open"} onChange={handleClickStrucButton} />
                            오픈형
                        </label>
                        <label>
                            <input type="radio" value="seperate" checked={struc === "seperate"} onChange={handleClickStrucButton} />
                            분리형
                        </label>
                        <label>
                            <input type="radio" value="multiple" checked={struc === "multiple"} onChange={handleClickStrucButton} />
                            복층
                        </label>
                        <label>
                            <input type="radio" value="tworoom" checked={struc === "tworoom"} onChange={handleClickStrucButton} />
                            투룸
                        </label>
                        <label>
                            <input type="radio" value="threenmore" checked={struc === "threenmore"} onChange={handleClickStrucButton} />
                            쓰리룸 이상
                        </label>
                    </div>
                    <h3>주소</h3>
                    <div className={styles.gbaddress}>
                        <Gbaddress />
                    </div>
                    <h3>근처 역/학교</h3>
                    <div><input type="text" /></div>
                    <h3>보증금</h3>
                    <div><input type="text" onInput={handleKeyPress} />원</div>
                    <h3>월세</h3>
                    <div><input type="text" onInput={handleKeyPress} />원</div>
                    <h3>관리비</h3>
                    <div><input type="text" onInput={handleKeyPress} />원</div>
                    <h3>입주 가능 일자</h3>
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
                        
                    <h3>계약 만료 일자</h3>
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
                    <h3>사용 승인일</h3>
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
                    <h3>층 수</h3>
                    <div><input type="text" onInput={handleKeyPress} />층 / <input type="text" onInput={handleKeyPress}/>층</div>
                    <h3>엘리베이터</h3>
                    <div><label>
                        <input type="radio" value="elevatoryes" checked={elevator === "elevatoryes"} onChange={handleClickElevatorButton} />
                        있음
                    </label>
                        <label>
                            <input type="radio" value="elevatorno" checked={elevator === "elevatorno"} onChange={handleClickElevatorButton} />
                            없음
                        </label></div>
                    <h3>욕실 수</h3>
                    <div><input type="text" onInput={handleKeyPress}/>개</div>
                    <h3>주차 가능 여부</h3>
                    <div><label>
                        <input type="radio" value="parkingyes" checked={parking === "parkingyes"} onChange={handleClickParkingButton} />
                        가능
                    </label>
                        <label>
                            <input type="radio" value="parkingno" checked={parking === "parkingno"} onChange={handleClickParkingButton} />
                            불가능
                        </label></div>
                
                    <h3>추가 옵션</h3>
                    <form onSubmit={onSubmit}>
                        <div className='checkbox-group'>
                            {checkBoxList.map((item, idx) => (
                                <div className='checkbox' key={idx}>
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

                        <button type='submit'>저장</button>
                    </form>
                    <h3>매물 사진</h3>
                    <div>
                        <input
                            type="file"
                            accept="image/*"
                            multiple
                            onChange={handleImageChange} />
                    </div>
                    <h3>제목</h3>
                    <div><input type="text" /></div>
                    <h3>상세 설명</h3>
                    <div><textarea rows="5" cols="50"/></div>
                    </div>
                    <div>
                        <input type="checkbox" id="check1" checked={agreement} onChange={agreeBtnEvent}/>
                        <label htmlFor="check1">매물관리규정을 확인하였으며, 입력한 정보는 실제 매물과 다름이 없습니다. <span className='pilsoo'>(필수)</span></label>
                    </div>
            <button>곰방 내놓기</button>
            </div>
            </div>
            </div>
    );      
    
}