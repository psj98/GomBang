import styles from "./FilterAdjust.module.css";
import 'rc-slider/assets/index.css';
import Slider from "rc-slider";
import { useState } from "react";
const FilterAdjust = () => {
  const roomTypelist = ['원룸','빌라','오피스텔','아파트']
  const roomstructurelist = ['오픈형','분리형','복층','투룸','쓰리룸 이상']
  const floorlist = ['1-3층','4-6층','7-9층','10층 이상','반지층','옥탑방']
  const genderlist = ['상관 없음','동성 매물만']
  const addtionaloptionlist = ['에어컨','냉장고','세탁기','싱크대','옷장','가스레인지','신발장','화재경보기','엘리베이터','주차가능','건조기']
  const [type, setType] = useState(roomTypelist);
  const [structure, setStructure] = useState(roomstructurelist)
  const [floor, setFloor] = useState(floorlist)
  const [gender, setGender] = useState(['상관 없음'])
  const [additionaloption, setAdditionaloption] = useState([])

  const handleClickTypeButton = (e) => {
    if(type.includes(e.target.id)){setType(type.filter((element)=>element!==e.target.id))}
    else{setType([...type,e.target.id])}
  };
  const handleClickGenderButton = (e) => {
    if(gender.includes(e.target.id)){setGender(gender.filter((element)=>element!==e.target.id))}
    else{setGender([...gender,e.target.id])}
  };
  const handleClickStructureButton = (e) => {
    if(structure.includes(e.target.id)){setStructure(structure.filter((element)=>element!==e.target.id))}
    else{setStructure([...structure,e.target.id])}
  };
  const handleClickFloorButton = (e) => {
    if(floor.includes(e.target.id)){setFloor(floor.filter((element)=>element!==e.target.id))}
    else{setFloor([...floor,e.target.id])}
  };
  const handleClickAdditionaloptionButton = (e) => {
    if(additionaloption.includes(e.target.id)){setAdditionaloption(additionaloption.filter((element)=>element!==e.target.id))}
    else{setAdditionaloption([...additionaloption,e.target.id])}
  };
  
  const [floorrange,setFloorrange] = useState([0,10])
  const handleFloorrangeChange = (e) => {
    setFloorrange(e)
  }
  const [depositrange,setDepositrange] = useState([0,10090])
  const handleDepositrangeChange = (e) => {
    setDepositrange(e)
  }
  const [monthlyfeerange,setMonthlyfeerange] = useState([0,121])
  const handleMonthlyfeerangeChange = (e) => {
    setMonthlyfeerange(e)
  }
  const [maintenancefeerange,setMaintenancefeerange] = useState([0,50.5])
  const handleMaintenancefeerangeChange = (e) => {
    setMaintenancefeerange(e)
  }
  const [roomsizerange,setRoomsizerange] = useState([0,200])
  const handleRoomsizerangeChange = (e) => {
    setRoomsizerange(e)
  }
  const addComma = (price) => {
    let returnString = price?.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return returnString;
}


  return (
      <div className={styles.Frame}>  
        
        {/*framechild1*/}
        <div className={styles.FrameChild}> 
          {/* 방 유형 */}    
          <div className={styles.container} style={{ borderRight: '1px solid black' }} id="roomtype">
            <div className={styles.headline}>유형</div>
            <div className={styles.subheadline}>중복선택이 가능합니다.</div>
            <div className={styles.optionbody}>
              <label className={styles.roomoption} htmlFor='roomType_total' onClick={()=>{if(type.length!==roomTypelist.length){setType([...roomTypelist])}else{setType([])}}} id='roomType_total'>
                <input 
                  type="checkbox"
                  className={styles.mycheckbox}
                  key={type.length===roomTypelist.length}
                  id='roomType_total'
                  onChange={()=>{if(type.length!==roomTypelist.length){setType([...roomTypelist])}else{setType([])}}}
                  checked={type.length===roomTypelist.length} 
                />
                전체
              </label>
              {roomTypelist.map((value,index) => (
              <label key={index} className={styles.roomoption} htmlFor={value} onClick={handleClickTypeButton} id={value}>
                <input 
                  type="checkbox"
                  className={styles.mycheckbox}
                  key={type.includes(value)}
                  id={value}
                  onChange={handleClickTypeButton}
                  checked={type.includes(value)} 
                  />
                  {value}
                </label>
                ))}
            </div>
          </div>
          
          {/* 방 구조 */}    
          <div className={styles.container} style={{ borderRight: '1px solid black' }} id="roomstructure">
            <div className={styles.headline}>방 구조</div>
            <div className={styles.subheadline}>중복선택이 가능합니다.</div>
            <div className={styles.optionbody}>
              <label className={styles.roomoption} htmlFor='roomstructure_total' onClick={()=>{if(structure.length!==roomstructurelist.length){setStructure([...roomstructurelist])}else{setStructure([])}}} id='roomstructure_total'>
                <input 
                  type="checkbox"
                  className={styles.mycheckbox}
                  key={structure.length===roomstructurelist.length}
                  id='roomstructure_total'
                  onChange={()=>{if(structure.length!==roomstructurelist.length){setStructure([...roomstructurelist])}else{setStructure([])}}}
                  checked={structure.length===roomstructurelist.length} 
                />
                전체
              </label>
              {roomstructurelist.map((value,index) => (
              <label key={index} className={styles.roomoption} htmlFor={value} onClick={handleClickStructureButton} id={value}>
                <input 
                  type="checkbox"
                  className={styles.mycheckbox}
                  key={structure.includes(value)}
                  id={value}
                  onChange={handleClickStructureButton}
                  checked={structure.includes(value)} 
                  />
                  {value}
                </label>
                ))}
            </div>
          </div>

          {/* 방 층수 */}    
          <div className={styles.container} id="roomfloor">
          <div className={styles.headline}>층 수</div>
          <div className={styles.sliderparent} style={{height:"100px"}} id="층 수">
              <div className={styles.sliderindex}><span>층 수</span><span>{floorrange[0]<1?"반지하":addComma(floorrange[0])}~{floorrange[1]>9?"옥상":addComma(floorrange[1])}{'층'}</span></div>
              <Slider
                vertical
                range
                min={0}
                max={10}
                step={1}
                trackStyle={{ backgroundColor: '#c7ad92' }}
                handleStyle={{ backgroundColor: '#c7ad92', borderColor:'#c7ad92' }}
                dotStyle={{borderColor:'#c7ad92'}}
                value={floorrange}
                onChange={handleFloorrangeChange}
                marks={{
                  0: <span style={{ whiteSpace: 'nowrap', display: 'inline-block', textAlign: 'right' }}>반지층</span>,
                  10: <span style={{ whiteSpace: 'nowrap', display: 'inline-block', textAlign: 'right' }}>옥탑방</span>
                }}
                />
            </div>
          </div>
          
          {/* <div className={styles.container} id="roomfloor">
          <div className={styles.headline}>층 수</div>
          <div className={styles.subheadline}>중복선택이 가능합니다.</div>
          <div className={styles.optionbody}>
            <label className={styles.roomoption} htmlFor='roomfloor_total' onClick={()=>{if(floor.length!==floorlist.length){setFloor([...floorlist])}else{setFloor([])}}} id='roomfloor_total'>
              <input 
                type="checkbox"
                className={styles.mycheckbox}
                key={floor.length===floorlist.length}
                id='roomfloor_total'
                onChange={()=>{if(floor.length!==floorlist.length){setFloor([...floorlist])}else{setFloor([])}}}
                checked={floor.length===floorlist.length} 
              />
              전체
            </label>
            {floorlist.map((value,index) => (
            <label key={index} className={styles.roomoption} htmlFor={value} onClick={handleClickFloorButton} id={value}>
              <input 
                type="checkbox"
                className={styles.mycheckbox}
                key={floor.includes(value)}
                id={value}
                onChange={handleClickFloorButton}
                checked={floor.includes(value)} 
                />
                {value}
              </label>
              ))}
          </div>
          </div> */}
        </div>

        {/*framechild2*/}
        <div className={styles.FrameChild}>
          {/* 가격 */}    
          <div className={styles.container} style={{ borderRight: '1px solid black' }}>
            <div className={styles.headline}>가격</div>
            <div className={styles.sliderparent} id="보증금">
              <div className={styles.sliderindex}><span>보증금</span><span>{addComma(depositrange[0])}~{depositrange[1]>10000?"":addComma(depositrange[1])}{'만원'}</span></div>
              <Slider
                range
                min={0}
                max={10090}
                step={100}
                trackStyle={{ backgroundColor: '#c7ad92' }}
                handleStyle={{ backgroundColor: '#c7ad92', borderColor:'#c7ad92' }}
                dotStyle={{borderColor:'#c7ad92'}}
                value={depositrange}
                onChange={handleDepositrangeChange}
                marks={{
                  0: <span>0</span>,
                  5000: '5,000만원',
                  10090: <span style={{ whiteSpace: 'nowrap', display: 'inline-block', textAlign: 'right' }}>무제한</span>
                }}
                />
            </div>
            <div className={styles.sliderparent} id="월세">
              <div className={styles.sliderindex}><span>월세</span><span>{addComma(monthlyfeerange[0])}~{monthlyfeerange[1]>120?"":addComma(monthlyfeerange[1])}{'만원'}</span></div>
              <Slider
                range
                min={0}
                max={121}
                step={5}
                trackStyle={{ backgroundColor: '#c7ad92' }}
                handleStyle={{ backgroundColor: '#c7ad92', borderColor:'#c7ad92' }}
                dotStyle={{borderColor:'#c7ad92'}}
                value={monthlyfeerange}
                onChange={handleMonthlyfeerangeChange}
                marks={{
                  0: <span>0</span>,
                  60: '60만원',
                  121: <span style={{ whiteSpace: 'nowrap', display: 'inline-block', textAlign: 'right' }}>무제한</span>
                }}
                />
            </div>
            <div className={styles.sliderparent} id="관리비">
              <div className={styles.sliderindex}><span>관리비</span><span>{addComma(maintenancefeerange[0])}~{maintenancefeerange[1]>50?"":addComma(maintenancefeerange[1])}{'만원'}</span></div>
              <Slider
                range
                min={0}
                max={50.5}
                step={1}
                trackStyle={{ backgroundColor: '#c7ad92' }}
                handleStyle={{ backgroundColor: '#c7ad92', borderColor:'#c7ad92' }}
                dotStyle={{borderColor:'#c7ad92'}}
                value={maintenancefeerange}
                onChange={handleMaintenancefeerangeChange}
                marks={{
                  0: <span>0</span>,
                  25: '25만원',
                  50.5: <span style={{ whiteSpace: 'nowrap', display: 'inline-block', textAlign: 'right' }}>무제한</span>
                }}
                />
            </div>
          </div>
          
          {/* 방 크기 */}    
          <div className={styles.container2}>
            <div className={styles.roomsize}>
              <div className={styles.headline}>방 크기</div>
              <div className={styles.sliderindex}><span></span><span>{addComma(roomsizerange[0])}~{roomsizerange[1]>199?"":addComma(roomsizerange[1])}m<sup>2</sup></span></div>
              <Slider
                range
                min={0}
                max={200}
                step={0.01}
                value={roomsizerange}
                trackStyle={{ backgroundColor: '#c7ad92' }}
                handleStyle={{ backgroundColor: '#c7ad92', borderColor:'#c7ad92' }}
                dotStyle={{borderColor:'#c7ad92'}}
                onChange={handleRoomsizerangeChange}
                marks={{
                  0: <span>0</span>,  
                  99.17: <span>99.17m<sup>2</sup> = 30평</span>,
                  200: <span style={{ whiteSpace: 'nowrap', display: 'inline-block', textAlign: 'right' }}>무제한</span>
                }}
                />
            </div>
            {/* <div className={styles.gender}>
              <div className={styles.headline}>성별</div>
              <div className={styles.FrameChild}>
              {genderlist.map((value,index) => (
                <label key={index} className={styles.roomoption} htmlFor={value} onClick={handleClickGenderButton} id={value}>
                <input 
                  type="checkbox"
                  className={styles.mycheckbox}
                  key={gender.includes(value)}
                  id={value}
                  onChange={handleClickGenderButton}
                  checked={gender.includes(value)} 
                  />
                  {value}
                </label>
                ))}
              </div>
            </div> */}
          
          </div>
        </div>

        {/*framechild3*/}
        <div className={styles.container}>
            <div className={styles.headline}>추가 옵션</div>
            <div className={styles.subheadline}>중복 선택이 가능합니다.</div>
            <div className={styles.buttongroup}>
              {addtionaloptionlist.map((value,index)=>(
              <div 
                key={index} 
                className={additionaloption.includes(value) ? styles.buttonbox_selected : styles.buttonbox} 
                id={value} 
                onClick={handleClickAdditionaloptionButton}
                >{value}
              </div>
                
              ))}
            </div>
            
        </div>  
      </div>
      
  );
};

export default FilterAdjust;
