import React from 'react';
import styles from './FilterNav.module.css';

const FilterNav = () => {
  return (
    <div className={styles.Frame}>
      <input className={styles.LocSearch} type="text" placeholder='지역, 학교, 역으로 검색'/>
      {/* <hr className={styles.verticalLine} /> */}
      <input className={styles.ContSearch} type="text" placeholder='검색어를 입력하세요'/>
      <div className={styles.icon_parents}>
        상세 필터
        <img className={styles.icon_detail}  src="/assets/detail.svg" alt="" />
        {/* <img className={styles.icon_detail}  src="/assets/filterremove.svg" alt="" /> */}
      </div>
    </div>
  )
}

export default FilterNav;