import { useEffect, useState } from 'react';
import Header from '../components/Header';
import axios from 'axios';

export default function Auth() {
    const [c, setcode] = useState('')    
    const [accessToken,setaccessToken] =useState('')
    const [userinfo,setuserpp] = useState([])
    
    
    useEffect(()=>{
        
        const gettoken = async ()=>{
            const code = new URL(window.location.href).searchParams.get("code");
            setcode(code)
            const Rest_api_key='a20ef37212e1ae86b20e09630f6590ce' //REST API KEY
            const data ={
                grant_type: 'authorization_code',
                client_id :  Rest_api_key,
                redirect_uri :'http://localhost:3000/auth',
                code
            }
            const headers = {
                'Content-type' : 'application/x-www-form-urlencoded;charset=utf-8',
            }
            const queryString = Object.keys(data)
            .map(k=>encodeURIComponent(k)+'='+encodeURIComponent(data[k]))
            .join('&')
            
            try{        
                await axios.post("https://kauth.kakao.com/oauth/token",queryString
                ,headers)
                .then(function(r){
                    console.log('tokkensuccess',r.data)
                    setaccessToken(r.data.access_token)
                    getUserInfo(r.data.access_token)
                })
            }
            catch(error){
                console.error('Error fetching data:', error);
            }
        }

        const getUserInfo = async (at)=>{
            const headers = {
                'Authorization' : 'Bearer '+ at,
            }
            console.log(headers)
            // 엑세스 토큰 헤더에 담기

            // 카카오 사용자 정보 조회
            await axios.get("https://kapi.kakao.com/v2/user/me", {headers:headers})
            .then(function(r){
                console.log(r.data)
                console.log(r.data.properties)
                setuserpp(r.data)
            })
            .catch(function(error){console.error('error',error)})
        }
        gettoken()

    },[])
    
    

    return(
        <div>
            <Header/>
            <div className="auth">
                인가코드 :  {c}
            </div>
            <div>
                토큰정보 : {accessToken}
            </div>
            #properties
            {userinfo ? (
            <div>
                <div>connected: {userinfo.connected_at}</div>
                <div>channelID: {userinfo.id}</div>
                <div>
                kakao_account:
                {userinfo.kakao_account
                    ? Object.entries(userinfo.kakao_account).map(([key, value]) => (
                        <div key={key}>
                        {key}: {JSON.stringify(value)}
                        </div>
                    ))
                    : ''}
                </div>
                <div>
                properties:
                {userinfo.properties
                    ? Object.entries(userinfo.properties).map(([key, value]) => (
                        <div key={key}>
                        {key}: {JSON.stringify(value)}
                        </div>
                    ))
                    : ''}
                </div>
            </div>
            ) : (
            ''
            )}
        </div>
        
    )
}