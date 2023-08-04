
import "./Signuplogin.css";
const Loginpage = () => {

  const Rest_api_key='a20ef37212e1ae86b20e09630f6590ce' //REST API KEY
  const redirect_uri = 'http://localhost:3000/auth' //Redirect URI
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
