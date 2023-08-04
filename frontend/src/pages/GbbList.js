import React from 'react';
import Header from '../components/Header';
import { useNavigate } from 'react-router-dom';

const List = ({ imageList }) => {
    const navigate = useNavigate();

    if (!imageList || imageList.length === 0) {
        return (
            <div>
                <Header />
            <h3>아직 아무 게시글이 없습니다.</h3>
            <button onClick={() => navigate("/gbbcreate")}>+</button>
        </div>
        );
    }
    else {
        return (
            <div>
                <Header />
                <button onClick={() => navigate("/gbbcreate")}>+</button>
            {imageList.map((imageGroup, index) => (
            <div key={index}>
                <h3>게시글 {index + 1}</h3>
                {Array.isArray(imageGroup.images) ? (
                imageGroup.images.map((image, idx) => (
                    <img
                    key={idx}
                    src={URL.createObjectURL(image)}
                    alt={`게시글 ${index + 1} 이미지 ${idx + 1}`}
                    width='100px'
                    />
                ))
                ) : (
                <img
                    src={URL.createObjectURL(imageGroup.images)}
                    alt={`${imageGroup.hashtag} 이미지 1`}
                    width="100px"
                />
                )}
                <p>{imageGroup.hashtag}</p>
                
            </div>
            ))}
            
        </div>
        );
    }
    };

export default List;
