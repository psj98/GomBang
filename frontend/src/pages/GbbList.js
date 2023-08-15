import React, { useState } from 'react';
import Header from '../components/Header';
import { useNavigate } from 'react-router-dom';
import styles from '../components/Gbb.module.css';

const GbbList = ({ imageList }) => {
    const navigate = useNavigate();
    const [searchText, setSearchText] = useState('');
    const [selectedTags, setSelectedTags] = useState([]);

    const [responseArticleId, setResponseArticleId] = useState(null); // 응답으로 받은 article_id
    const [responseRoomId, setResponseRoomId] = useState(null); // 응답으로 받은 room_id

    const handleSearch = async () => {
        try {
            const apiUrl = process.env.REACT_APP_API_ROOT; // 백엔드 서버의 주소

            const requestBody = {
                filter: searchText,
                // search: searchText,
                tags: selectedTags.map(tag => ({ tagname: tag }))
            };

            const response = await fetch(`${apiUrl}/showroom/list`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestBody),
            });

            const data = await response.json();

            // 응답 데이터 처리
            setResponseArticleId(data.article_id);
            setResponseRoomId(data.room_id);

        } catch (error) {
            console.error('에러:', error);
        }
    };

    return (
        <div>
            <Header />
            <div className={styles.header}>
                <div className={styles.h1}>당신의 <span className={styles.h2}>곰방</span>을 보여주세요 .</div>
                <button onClick={() => navigate("/gbbcreate")} className={styles.plusbutton}>+</button>
            </div>
            <div className={styles.inputbox}>
            <input
                className={styles.searchbox}
                value={searchText}
                onChange={(event) => setSearchText(event.target.value)}
                placeholder="보고싶은 지역을 입력하세요."
            />
            {selectedTags.map((tag, index) => (
                <span key={index}>{tag}</span>
            ))}
            <input
                className={styles.searchbox}
                value={selectedTags}
                onChange={(event) => setSelectedTags(event.target.value.split(','))}
                placeholder="해시태그를 입력하세요."
            />
            <button className={styles.searchclick} onClick={handleSearch}>검색</button>
            </div>
            

            {/* 응답으로 받은 데이터 표시 */}
            {responseArticleId !== null && (
                <p>응답으로 받은 article_id: {responseArticleId}</p>
            )}
            {responseRoomId !== null && (
                <p>응답으로 받은 room_id: {responseRoomId}</p>
            )}

            {/* 이미지 리스트 출력 부분 */}
        </div>
    );
};

export default GbbList;
