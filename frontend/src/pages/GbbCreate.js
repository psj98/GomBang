import React, { useState } from 'react';
import { useNavigate   } from 'react-router-dom';
import Header from '../components/Header';

const Create = ({ onImageUpload }) => {
    const [selectedImages, setSelectedImages] = useState([]);
    const [hashtag, setHashtag] = useState('');
    const [selectedHashtags, setSelectedHashtags] = useState([]);

        const navigate = useNavigate  ();
        
        const handleImageChange = (event) => {
        const files = event.target.files;
        setSelectedImages([...selectedImages, ...files]);
    };
    const handleHashtagChange = (event) => {
        setHashtag(event.target.value);
    };
    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            const value = event.target.value.trim();
            if (hashtag.trim() !== '' && !selectedHashtags.includes('#'+value)) {
                setSelectedHashtags([...selectedHashtags, '#'+hashtag]);
                setHashtag('');
            }else {
                event.preventDefault(); // 기본 동작 방지하여 커서가 입력창에 남아있게 함
                event.target.value = ''; // 이미 있는 해시태그나 유효하지 않은 입력일 경우 입력창 비우기
            }
        }
    };
    

    const handleUpload = () => {
        if (selectedImages.length > 0 || hashtag.trim() !== '') {
        const newImageGroup = {
            hashtag: selectedHashtags,
            images: selectedImages,
        };

        // 이미지들을 로컬에 저장하고 리스트에 추가하는 함수 호출
        // onImageUpload({ hashtag, images: selectedImages });
        onImageUpload(newImageGroup);
        setSelectedImages([]);
        setHashtag('');
        navigate('/gbblist');
        // navigate('/list', {setSelectedImages});
        }
    };

    return (
        <div>
            <Header />
        <div>
            <input
                type="file"
                accept="image/*"
                multiple
            onChange={handleImageChange} />
        
        </div>
        <div>
            <label>해시태그:</label>
                <input
                    type="text"
                    value={hashtag}
                    onKeyDown={handleKeyDown}
                    onChange={handleHashtagChange}
                    placeholder='# 해시태그를 입력하세요'
                />
            </div>
        <div>
            {selectedHashtags.map((tag, index) => (
                <span key={index}>{tag}</span>
            ))}
        </div>
        <button onClick={handleUpload}>Upload</button>
        </div>
    );
};

export default Create;
