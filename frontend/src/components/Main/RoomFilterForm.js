// import "bootstrap/dist/css/bootstrap.min.css";
// import { Form } from "react-bootstrap";
import { useState } from "react";
import styles from "./RoomFilterForm.module.css";
const RoomFilterForm = () => {
  const [selectedOpt,setselectedOpt] = useState('전체')
  const optionList = ['전체','원룸','오피스텔','빌라','아파트']
  const handleOptionClick = (option) => {
    setselectedOpt(option)
  }

  return (
    <div className={styles.bbody}>
      <div className={styles.parent}>
        <div className={styles.head}>어떤 방을 찾으시나?</div>
        <div className={styles.group}>
          {optionList.map((value)=>(
            <div key={value} className={`${styles.option} ${selectedOpt === value ? styles.selected : ''}`} onClick={()=>handleOptionClick(value)}>
              {value}
            </div>
          ))}
        </div>
        <input className={styles.myinput} type="text" placeholder="지역을 입력하세요"/>
      </div>
    </div>
  );
};

export default RoomFilterForm;
