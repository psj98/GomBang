import React, { useCallback,useState,useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header";
import styles from "../components/Gbb.module.css";
import gbbCreateStyles from "../components/GbbCreate.module.css";
import axios from "axios";

const GbbCreate = ({ onImageUpload }) => {
  const [selectedImages, setSelectedImages] = useState([]);
  const [imageSrc, setImageSrc] = useState([]);
  const [hashtag, setHashtag] = useState("");
  const [selectedHashtags, setSelectedHashtags] = useState([]);
  const navigate = useNavigate();
  const [userid, setUserid] = useState("");
  const [roomid, setRoomid] = useState("");
  // 아이디 가져오는 거
  useEffect(() => {
    const member = JSON.parse(sessionStorage.getItem("member"));
    const useruuid = member.id;
    setUserid(useruuid);
  }, [setUserid]);
  // 아이디 가지고 내가 올린 매물 방 아이디 가져오는 거
  useEffect(() => {
    axios.get(`${process.env.REACT_APP_API_ROOT}/roomdeal/myroomdeal/${userid}`
    ).then((response) => {
      console.log(response.data.data)
      setRoomid(response.data.data)
    }).catch((error) => {
      console.log(error)
    })
  }, [userid]);

  const handleImageChange = (event) => {
    const files = event.target.files;

    if (files.length === 0) {
      return;
    }

    const imageList = [];
    Array.from(files).forEach((image) => {
      const previewImage = URL.createObjectURL(image);
      imageList.push(previewImage);
    });

    setImageSrc([...imageSrc, ...imageList]);
    setSelectedImages([...selectedImages, ...files]);
  };

  const handleDeleteImage = (id) => {
    setSelectedImages(selectedImages.filter((_, index) => index !== id));
    setImageSrc(imageSrc.filter((_, index) => index !== id));
  };

  const handleHashtagChange = (event) => {
    setHashtag(event.target.value);
  };

  const handleKeyDown = (event) => {
    if (event.key === "Enter") {
      const value = event.target.value.trim();
      if (hashtag.trim() !== "" && !selectedHashtags.includes("#" + value)) {
        setSelectedHashtags([...selectedHashtags, "#" + hashtag]);
        setHashtag("");
      } else {
        event.preventDefault(); // 기본 동작 방지하여 커서가 입력창에 남아있게 함
        event.target.value = ""; // 이미 있는 해시태그나 유효하지 않은 입력일 경우 입력창 비우기
      }
    }
  };

  const handleUpload = useCallback(
    async (e) => {
      e.preventDefault();
    const formData = new FormData();
    selectedImages.forEach((image) => {
      formData.append("files", image);
      // console.log("중간점검", formData.file);
      console.log("중간점검", image);
    });
    const ShowRoomHashTagRequestDto = {
      'showRoomRegisterRequestDto': {
        'roomDealId': roomid[0].id,
        'memberId': userid,
      },
      'hashTagRegisterRequestDto': {
        'hashTagNames':selectedHashtags
      }
      }
      console.log(ShowRoomHashTagRequestDto)
    const blob = new Blob([JSON.stringify(ShowRoomHashTagRequestDto)], {
      type: "application/json",
    });
    formData.append("showRoomHashTagRequestDto", blob);
    axios.post(`${process.env.REACT_APP_API_ROOT}/showroom/register`,
      formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
        // charset : 'utf-8'// Content-Type을 반드시 이렇게 하여야 합니다.
      },
    }
      ).then((response) => {
        console.log(response)
        navigate('/gbblist')
    }).catch((error) => {
      console.log(error)
    })

    
  },[navigate,roomid,selectedHashtags,selectedImages,userid]);

  return (
    <div>
      <Header /> {/* 헤더 */}
      <div>
        {/* 곰방봐 Create Form div */}
        <div className={gbbCreateStyles.gbbCreateDiv}>
          {/* 곰방봐 올리기 div */}
          <div className={gbbCreateStyles.gbbCreateH1}>
            <h1>곰방봐 올리기</h1>
          </div>
          <div></div>
          {/* 사진 업로드 */}
          <div className={gbbCreateStyles.gbbCreateContent}>
            <h2 className={gbbCreateStyles.gbbCreateImageH2}>곰방 사진</h2>
            <div className={styles.inputbox}>
              <div className={styles.imageinput}>
                <button onClick={() => document.getElementById("image-upload").click()}>
                  이미지 업로드
                </button>
              </div>
              <div>
                <input
                  id="image-upload"
                  type="file"
                  accept="image/*"
                  multiple
                  onChange={handleImageChange}
                  style={{ display: "none" }}
                />
                <div className={gbbCreateStyles.imageContainer}>
                  {imageSrc.map((image, id) => (
                    <div key={id} className={gbbCreateStyles.imageGridItem}>
                      <img
                        src={image}
                        alt={`${image}-${id}`}
                        width={"200px"}
                        onClick={() => handleDeleteImage(id)}
                      />
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
          {/* 해시태그 */}
          <div className={gbbCreateStyles.gbbCreateContent}>
            <h2>해시태그 입력</h2>
            <div className={gbbCreateStyles.tagInputDiv}>
              <input
                type="text"
                value={hashtag}
                onKeyDown={handleKeyDown}
                onChange={handleHashtagChange}
                placeholder="# 해시태그를 입력하세요"
                className={gbbCreateStyles.tagInput}
              />
            </div>
            <div className={gbbCreateStyles.tagList}>
              {selectedHashtags.map((tag, index) => (
                <span className={gbbCreateStyles.tagListItem} key={index}>
                  {tag}
                </span>
              ))}
            </div>
          </div>
          {/* 버튼 */}
          <div className={gbbCreateStyles.gbbCreateContent}>
            <button onClick={handleUpload} className={gbbCreateStyles.gbbCreateBtn}>
              <h2>곰방 올리기</h2>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default GbbCreate;
