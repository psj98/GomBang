import React from "react";
import { Link } from "react-router-dom";
import styles from "./Header.module.css";
import { useState, useEffect } from "react";

const Header = () => {
	const [isAuthorized, setIsAuthorized] = useState("");
	const [userinfo, setUserinfo] = useState({});
	const [eventSource, setEventSource] = useState(() => { });
	useEffect(() => {
		setIsAuthorized(sessionStorage.getItem("isAuthorized"));
		setUserinfo(JSON.parse(sessionStorage.getItem("member")));
	}, []); // This effect runs once when the component mounts

	useEffect(() => {
		if (userinfo === undefined || userinfo === null) return;
		if (Object.keys(userinfo).length === 0) return;
		if (sessionStorage.getItem("notification_issubscribe")) {
			return;
		}
		setEventSource(
			new EventSource(`http://localhost:8081/notification/subscribe/${userinfo.id}`)
		);
		sessionStorage.setItem("notification_issubscribe", true);
	}, [userinfo]);

	useEffect(() => {
		if (eventSource === null) return;
		if (typeof eventSource === "undefined") return;
		eventSource.addEventListener("sse", function (event) {
			if (event.data.charAt(0) !== "{") return;
			const data = JSON.parse(event.data);

			(async () => {
				// 브라우저 알림
				const showNotification = () => {
					const notification = new Notification(
						data.notificationType === "LIKED" ? "찜 알림" : "추천 알림",
						{
							body: data.notificationContent,
						}
					);

					setTimeout(() => {
						notification.close();
					}, 10 * 1000);

					notification.addEventListener("click", () => {
						window.open(`roomdetail/${Number(data.relatedUrl)}`, "_blank");
					});
					// 클릭하면 읽음 표시하는 api도 요청해야댐
				};

				// 브라우저 알림 허용 권한
				let granted = false;

				if (Notification.permission === "granted") {
					granted = true;
				} else if (Notification.permission !== "denied") {
					let permission = await Notification.requestPermission();
					granted = permission === "granted";
				}

				// 알림 보여주기
				if (granted) {
					showNotification();
				}
			})();
		});
	}, [eventSource]);

	function handleLogout() {
		setIsAuthorized(false);
		sessionStorage.removeItem("isAuthorized");
		sessionStorage.removeItem("member");
		window.location.href = `https://kauth.kakao.com/oauth/logout?client_id=${process.env.REACT_APP_REST_API_MAP_KEY}&logout_redirect_uri=${process.env.REACT_APP_HOME_URL}`;
	}

	return (
		<div className={styles.headerDiv}>
			<div className={styles.leftTab}>
				<div>
					<Link to="/">
						<img className={styles.logoIcon} alt="" src="/assets/logo.png" />
					</Link>
				</div>
				<Link to="/map" className={styles.b1}>
					지도
				</Link>
				<Link to="/gbblist" className={styles.b1}>
					곰방봐
				</Link>
				<Link to="/zzim" className={styles.b1}>
					찜 목록
				</Link>
				<Link to="/roomout" className={styles.b1}>
					방 내놓기
				</Link>
			</div>
			<div className={styles.rightTab}>
				{isAuthorized ? (
					<>
						<div className={styles.rightTabRightDiv}>반가워요, {userinfo?.name}!</div>
						<div className={styles.rightTabRightDiv}>
							<Link to="/chatlist" >
								<img src={`${process.env.PUBLIC_URL}/images/chat.png`} alt="noImage" className={styles.rightTabImg} />
							</Link>
						</div>
						<div className={styles.rightTabRightDiv}>
							<Link to="/mypage" >
								<img src={`${process.env.PUBLIC_URL}/images/user.png`} alt="noImage" className={styles.rightTabImg} />
							</Link>
						</div>
						<div className={styles.rightTabRightDiv}>
							<img onClick={handleLogout} src={`${process.env.PUBLIC_URL}/images/logout.png`} alt="noImage" className={styles.rightTabImg} />
						</div>
					</>
				) : (
					<>
						<div>
							<Link to="/login" className={styles.loginBtn}>
								로그인 / 회원가입
							</Link>
						</div>
					</>
				)}
			</div>
		</div >
	);
};

export default Header;
