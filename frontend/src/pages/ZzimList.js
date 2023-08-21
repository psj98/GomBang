import Header from "../components/Header";
import { useEffect, useState } from "react";
import axios from "axios";
import styles from "./Zimlist.module.css";

export default function Zimlist() {
	const [userid, setUserid] = useState("");
	const [starroomDeal, SetStarroomDeal] = useState([]);

	useEffect(() => {
		const member = JSON.parse(sessionStorage.getItem("member"));
		const useruuid = member.id;
		setUserid(useruuid);
	}, []);
	useEffect(() => {
		if (userid) {
			axios
				.get(`${process.env.REACT_APP_API_ROOT}/star/my-list/${userid}`)
				.then((response) => {
					console.log(response.data);
					SetStarroomDeal(response.data.data.starRoomDealList);
				})
				.catch((error) => {
					console.error("API 호출 에러:", error);
				});
		}
	}, [userid]);

	function handleonclick(id) {
		window.location.href = `/roomdetail/${id}`;
	}
	function handleStar(roomDealid) {
		const StarRoomDealDeleteRequestDto = {
			memberId: userid,
			roomDealId: roomDealid,
		};
		console.log(StarRoomDealDeleteRequestDto.memberId);
		console.log(StarRoomDealDeleteRequestDto.roomDealId);

		console.log(StarRoomDealDeleteRequestDto);
		axios
			.delete(`${process.env.REACT_APP_API_ROOT}/star/delete`, {
				data: StarRoomDealDeleteRequestDto,
			})
			.then((response) => {
				console.log(response.data.data);
			})
			.catch((error) => {
				console.error("API 호출 에러:", error);
			})
			.then(window.location.reload());
	}

	return (
		<div>
			<Header />
			<div className={styles.container}>
				<div>
					<h1>찜 목록</h1>
				</div>
				{/* 본문 */}

				{starroomDeal.length > 0 ? (
					<div className={styles.content}>
						{starroomDeal.map((data, index) => (
							<div key={index} className={styles.frame}>
								{/* 카드 */}
								<img
									className={styles.roomimage}
									onClick={() => handleonclick(data.roomDeal.id)}
									src={data.roomDeal.thumbnail}
									alt="noImage"
								/>
								<div
									className={styles.heartbutton}
									onClick={() => handleStar(data.roomDeal.id)}
								>
									♥
								</div>
							</div>
						))}
					</div>
				) : (
					<div className={styles.content}>찜 목록이 없습니다. </div>
				)}
			</div>
		</div>
	);
}
