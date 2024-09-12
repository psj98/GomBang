import { useEffect, useState } from 'react';
// import Header from '../components/Header';
import axios from 'axios';
import styles from './Auth.module.css'
export const API_BASE_URL = process.env.REACT_APP_API_ROOT;
export const HOME_URL = process.env.REACT_APP_HOME_URL;
export default function Auth() {
    // const [c, setcode] = useState('')
    const [id, setid] = useState('');    
    const [name, setName] = useState('');    
    const [shownameform, setshownameform] = useState(false);    
    // const [userinfo,setuserinfo] = useState({})
    

    
    useEffect(() => {
        async function getid() {
            const code = new URL(window.location.href).searchParams.get("code");
            // setcode(code)
            // window.location.href = 'http://localhost:3000/auth'
            
            try{        
                await axios.get(`${API_BASE_URL}/member/login?code=${code}`)
                .then(function(r){
                    alert(r.data.message)
                    if(r.data.code === 2001){ // 로그인 성공 시
                        // setuserinfo(r.data.data.member);
                        sessionStorage.setItem("isAuthorized", "true")
                        sessionStorage.setItem("member",JSON.stringify(r.data.data.member))
		                sessionStorage.setItem("eventSource",new EventSource(
                            `${process.env.REACT_APP_API_ROOT}/notification/subscribe/${r.data.data.member.id}`
                        ));
                        
                        setshownameform(false)
                        window.location.href = HOME_URL
                    }

                    else if(r.data.code === 2002 || r.data.code === 2003){ // 회원가입 성공 시
                        setshownameform(true)
                        setid(r.data.data.id)
                    }
                    else if(r.data.code === 2101 || r.data.code === 2201 || r.data.code === 2202 ){
                        window.location.href = `${HOME_URL}/login`
                    }
                    else{alert('warning')}
                })
            }
            catch(error){
                console.error('Error fetching data:', error);
            }
        }
        getid()
    },[])


        const postname = async (name) => {
            const params = {
                'id':id,
                'name':name
            }  
            const config = {
                headers: {
                'Content-Type': 'application/json'
                }
            }
            try {
                const response = await axios.post(`${API_BASE_URL}/member/update`,params,config);
                if(response.data.code === 1000){
                    sessionStorage.setItem("isAuthorized", "true");
                    // setuserinfo(response.data.data.member);
                    sessionStorage.setItem("member",JSON.stringify(response.data.data.member))
                    setshownameform(false);
                    sessionStorage.setItem("eventSource",new EventSource(
                        `${process.env.REACT_APP_API_ROOT}/notification/subscribe/${response.data.data.member.id}`
                    ));
                    window.location.href = HOME_URL
                }
                else if(response.data.code === 2102){
                    alert(response.data.message)
                }
                else{alert('warning')}

            } catch (error) {
                console.error('Error posting data:', error);
            }
        }

        const handleNameChange = (event) => {
            setName(event.target.value);
        }
    
        const handleNameSubmit = () => {
            postname(name);
        }
    
    

    return(
        <div className={styles.page}>
            {/* <Header/> */}
            {/* <div className="auth">
                인가코드 :  {c}
                {id? <div>uuid : {id}</div> :''}
            </div> */}
            {shownameform &&
                <div className={styles.loginpage}>
                    <div className={styles.gombang}><b className='b5'>곰방</b> 회원가입하기</div>
                    <img className={styles.logoimg} alt="곰방로고" src='/assets/logo.png'/>
                    <div className={styles.namestyle}>이름을 입력하세요.</div>
                    <div className={styles.inputcontainer}>
                            <input 
                                type="text" 
                                // placeholder='이름을 입력하시오'
                                value={name}
                                onChange={handleNameChange}
                                className={styles.inputbox}
                            />
                    </div>
                    <div>※ 실명이 아닐 경우 추후 계약 진행에 불이익이 있을 수 있습니다.</div>
                    <div>
                        <button className={ styles.signupbutton } onClick={handleNameSubmit} >회원가입</button>
                    </div>
                    {/* {userinfo && Object.entries(userinfo).map((value,index)=>{return <div key={index}>{value[0]}: {value[1]}</div>})} */}
                </div>
            }
            {!shownameform &&
                <div>
                    <img className={styles.logoimg} alt="곰방로고" src='/assets/logo.png' />
                    <div className={styles.gombang}>로딩중...</div>
                </div>}

        </div>
        
    )
}