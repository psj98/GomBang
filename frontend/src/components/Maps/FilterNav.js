import React, { useState } from 'react';
import styles from './FilterNav.module.css';
import ModalPortal from '../ModalPortal'
import FilterAdjust from './FilterAdjust'
const FilterNav = () => {
  const [showmodal,setshowmodal] = useState(false)
  const handleOpen = (event) =>{
    setshowmodal(true)
  }
  const handleClose = (event) =>{
    event.preventDefault();
    if(event.target === event.currentTarget)
    {
      setshowmodal(false)
    }
  }

  return (
    <div className={styles.Frame}>
      <input className={styles.LocSearch} type="text" placeholder='지역, 학교, 역으로 검색'/>
      <input className={styles.ContSearch} type="text" placeholder='검색어를 입력하세요'/>
      <div className={styles.icon_parents} >
        <span onClick={handleOpen}>상세 필터</span>
        <img className={styles.icon_detail}  src="/assets/detail.svg" alt="" onClick={handleOpen}/>
        <img className={styles.icon_detail}  src="/assets/filterremove.svg" alt="" />
      </div>
      {showmodal && (
        <ModalPortal closePortal={handleClose}>
          <FilterAdjust />
        </ModalPortal>
      )}
    </div>
  )
}

export default FilterNav;