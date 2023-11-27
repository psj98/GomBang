# 인근 역 조회

HTTP 메서드: GET
API Path: roomdeal/station/{gugun}
Request: StationGetRequestDto
Response: StationGetResponseDto
기능: 인근 역 조회
도메인: RoomDeal

## Request body

```json
{
	'gugun' : String
}
```

## Response body

```json
{
	"isSuccess": boolean,
	"message": String,
	"code": int,
	"date": T
}
```