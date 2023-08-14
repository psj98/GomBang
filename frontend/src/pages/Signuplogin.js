import "./Signuplogin.css";
export const HOME_URL = process.env.REACT_APP_HOME_URL;
const Loginpage = () => {

  const Rest_api_key= process.env.REACT_APP_REST_API_MAP_KEY //REST API KEY
  const redirect_uri = `${HOME_URL}/auth` //Redirect URI to backend
  // oauth 요청 URL
  const kakaoURL = `https://kauth.kakao.com/oauth/authorize?client_id=${Rest_api_key}&redirect_uri=${redirect_uri}&response_type=code`
  const handleLogin = ()=>{
      window.location.href = kakaoURL
  }


  return (
    <div className="wrapper">
      <div className='loginpage'>
            <div>카카오톡으로 <b className='b5'>곰방</b> 시작하기</div>
            
            <img className="logoimg" alt="곰방로고" src='/assets/logo.png'/>

            <div>
                <img
                  className="kakaologo" 
                  src="/assets/kakao_login_large_wide.png" 
                  alt="카카오 로그인 버튼" 
                  onClick={handleLogin}
                />

            </div>
      </div>
  </div>  
  );
};

export default Loginpage;
