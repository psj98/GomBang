import { useEffect, useState } from 'react';
import Header from '../components/Header';
import axios from 'axios';

export default function Auth() {
    const [c, setcode] = useState('')
    const [id, setid] = useState('')    
    const [shownameform, setshownameform] = useState(false)    
    const [userinfo,setuserinfo] = useState({})
    

    
    useEffect(() => {
        async function getid() {
            const code = new URL(window.location.href).searchParams.get("code");
            setcode(code)
            // window.location.href = 'http://localhost:3000/auth'
            
            try{        
                await axios.get(`http://localhost:8080/member/login?code=${code}`)
                .then(function(r){
                    alert(r.data.message)
                    if(r.data.code === 2001){ // 로그인 성공 시
                        setuserinfo(r.data.data.member);
                        sessionStorage.setItem("isAuthorized", "true")
                        sessionStorage.setItem("member",JSON.stringify(r.data.data.member))
                        setshownameform(false)
                        window.location.href = 'http://localhost:3000/'
                    }

                    else if(r.data.code === 2002 || r.data.code === 2003){ // 회원가입 성공 시
                        setshownameform(true)
                        setid(r.data.data.id)
                    }
                    else if(r.data.code === 2101 || r.data.code === 2201 || r.data.code === 2202 ){
                        window.location.href = 'http://localhost:3000/login'
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
                const response = await axios.post('http://localhost:8080/member/update',params,config);
                if(response.data.code === 1000){
                    sessionStorage.setItem("isAuthorized", "true");
                    setuserinfo(response.data.data.member);
                    sessionStorage.setItem("member",JSON.stringify(response.data.data.member))
                    setshownameform(false);
                    window.location.href = 'http://localhost:3000/'
                }
                else if(response.data.code === 2102){
                    alert(response.data.message)
                }
                else{alert('warning')}

            } catch (error) {
                console.error('Error posting data:', error);
            }
        }

        function handleKeyPress(event){
            if (event.key === "Enter" || event.keyCode === 13){
                postname(event.target.value)
            }
        }
    
    

    return(
        <div>
            <Header/>
            <div className="auth">
                인가코드 :  {c}
                {id? <div>uuid : {id}</div> :''}
            </div>
                {shownameform&&
                <input 
                type="text" 
                placeholder='이름을 입력하시오' 
                onKeyDown={handleKeyPress} 
                />}
            <div>
                {userinfo && Object.entries(userinfo).map((value,index)=>{return <div key={index}>{value[0]}: {value[1]}</div>})}
            </div>
            <div>
            </div>
        </div>
        
    )
}