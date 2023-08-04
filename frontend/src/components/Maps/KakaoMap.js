import React, { useEffect } from 'react';
import axios from 'axios';

const KakaoMap = () => {
  useEffect(() => {
    const fetchData = async () => {
      try {
        // 1. Axios를 사용하여 데이터 가져오기
        // const response = await axios.get('https://apis.map.kakao.com/download/web/data/chicken.json');
        
        // 2. 미리 받아놓은 json파일에서 불러오기
        const response = await axios.get('/chicken.json');
        
        
        const data = response.data;

        const container = document.getElementById('map');
        const map = new window.kakao.maps.Map(container, {
          center: new window.kakao.maps.LatLng(37.5019, 127.0397),
          level: 1,
        });
        map.setMinLevel(1);
        const clusterer = new window.kakao.maps.MarkerClusterer({
          map: map,
          averageCenter: true,
          minLevel: 1,
          minClusterSize: 1,
        });

        const markers = data.positions.map((position) => {
          return new window.kakao.maps.Marker({
            position: new window.kakao.maps.LatLng(position.lat, position.lng),
          });
        });

        clusterer.addMarkers(markers);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  return (
        <div id="map" style={{margin : '0', width : '100%', height : '720px'}} ></div>
  );
};

export default KakaoMap;
