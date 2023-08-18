import Header from '../components/Header';
import KakaoMap from'../components/Maps/KakaoMap'
import FilterNav from '../components/Maps/FilterNav';
import styles from './MapPage.module.css'
import RoomList from '../components/Maps/RoomList';
import { useParams } from "react-router-dom";

const MapPage = () => {

  const { word, lat, lon } = useParams();

  return (
    <div>
      <Header/>
      <div className={styles.body}>
        <div className={styles.background}><KakaoMap/></div>
        <div className={styles.nav}><FilterNav word={word}/></div>
        <div className={styles.rlist}><RoomList word={word} lat={lat} lon={lon}/></div>
      </div>
    </div>
  );
};

export default MapPage;
