import React, { useEffect, useState } from 'react';
import styles from './FilterNav.module.css';
import ModalPortal from '../ModalPortal'
import FilterAdjust from './FilterAdjust'
const FilterNav = (props) => {
  const [showmodal,setshowmodal] = useState(false)
  const [location,setlocation] = useState(localStorage.getItem('searchloc')?localStorage.getItem('searchloc'):'')
  const [content,setcontent] = useState(localStorage.getItem('searchcon')?localStorage.getItem('searchcon'):'')
  

  useEffect(()=>{
    if(props.word){
      setlocation(props.word)
      const locsearch = document.querySelector("#locsearch")
      locsearch.disabled = true
    }
  },[props.word])


  const handleOpen = (event) =>{
    setkeywordTostorage()
    setshowmodal(true)
  }

  const handleClose = (event) =>{
    event.preventDefault();
    if(event.target === event.currentTarget)
    {
      //필터검색 fetch 
      setshowmodal(false)
    }
  }

  const handleKeyPress = (event) =>{
    if(event.key.code === 13 || event.key === 'Enter'){
      setkeywordTostorage()
    }
  }


  function setkeywordTostorage(){
    localStorage.setItem('searchloc',location)
    localStorage.setItem('searchcon',content)
    // ES 검색 fetch보내기
  }

  return (
    <div className={styles.Frame}>
      <input id='locsearch' className={styles.LocSearch} onChange={(e)=>{setlocation(e.target.value)}} type="text" value={location} placeholder='지역, 학교, 역으로 검색'/>
      <input className={styles.ContSearch} onChange={(e)=>{setcontent(e.target.value)}} type="text" value={content} placeholder='검색어를 입력하세요' onKeyDown={handleKeyPress}/>
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