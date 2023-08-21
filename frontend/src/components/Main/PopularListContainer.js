import { Link } from "react-router-dom";
import styles from "./PopularListContainer.module.css";
// import { useCallback } from "react";

const PopularListContainer = () => {
  // const onImageClick = useCallback(() => {
  //   // Please sync "gombangba_detail1" to the project
  // }, []);
  const imgtags = [];
  for (let index = 0; index < 4; index++) {
    imgtags.push(<img key={index} className={styles.imageIcon} src="/assets/sampleimg.png" alt="" />)
  }

  return (
    <div className={styles.frameParent}>
      <div className={styles.frame}>
        <div className={styles.textparent}>
          <div className={styles.b}>곰방에서 인기 많은 매물</div>
          <Link to='/map' className={styles.b1}>더보기</Link>
        </div>
        <div className={styles.imageParent}>
          {imgtags}
        </div>
      </div>

      <div className={styles.frame} >
        <div className={styles.textparent}>
          <div className={styles.b}>곰방봐 보러 오세요</div>
          <Link to='/gbblist' className={styles.b1}>더보기</Link>
        </div>
        <div className={styles.imageParent}>
          {imgtags}
          {/* <img
            className={styles.image14Icon}
            alt=""
            src="/1@2x.png"
            onClick={onImageClick}
            />
          <img className={styles.image14Icon} alt="" src="/frame-189@2x.png" />
          <img className={styles.image14Icon} alt="" src="/frame-190@2x.png" />
          <img className={styles.image14Icon} alt="" src="/frame-191@2x.png" /> */}
        </div>
      </div>
    </div>
  );
};

export default PopularListContainer;