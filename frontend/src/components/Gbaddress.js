import React from 'react';
import axios from 'axios';
import { useDaumPostcodePopup }  from 'react-daum-postcode';

function Gbaddress() {
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
        document.getElementById("jibunAddress").value = data.jibunAddress;
                
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
            Authorization: `KakaoAK ${process.env.REST_API_KEY}`,
            },
        })
        .then((response) => {
            const { documents } = response.data;
            if (documents.length > 0) {
            const { x, y } = documents[0];
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

return (
    <div>
        <input type="text" id="postcode" placeholder="우편번호" disabled/>
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
            }}/><br />
        <input type="text" id="roadAddress" placeholder="도로명주소" disabled/>
        <input type="text" id="jibunAddress" placeholder="지번주소" disabled/>
        <span id="guide" style={{ color: '#999', display: 'none' }}></span>
        <input type="text" id="detailAddress" placeholder="상세주소" />
        {/* {<DaumPostcode onComplete={handleExecDaumPostcode} />} */}
    </div>
)
};
export default Gbaddress;