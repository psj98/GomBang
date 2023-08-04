import Header from '../components/Header';
import KakaoMap from'../components/Maps/KakaoMap'
import FilterNav from '../components/Maps/FilterNav';
import styles from './MapPage.module.css'
import RoomList from '../components/Maps/RoomList';

const MapPage = () => {


  return (
    <div>
      <Header/>
      <div className={styles.body}>
        <div className={styles.background}><KakaoMap/></div>
        <div className={styles.nav}><FilterNav/></div>
        <div className={styles.rlist}><RoomList/></div>
      </div>
    </div>
  );
};

export default MapPage;
