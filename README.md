# 🐻 GomBang 🐻

<br/>

## 🏷 주제

화상 매물 양도 서비스

<br/>

## 📑 목적
- 월세로 집에 사는 도중 중간에 나가야 하는 경우, 남은 기간 동안 월세를 내야하는 단점이 있다.
- 만약 집을 중간에 양도해야 하는 경우, 복비를 두 번 내야하기 때문에 많은 비용을 소비하게 된다.
- 또한 양도자의 경우, 본인의 집을 보러올 때마다 집을 청소해야 하고 집을 보고 간 후에도 청소를 해야하는 번거로움이 있다.
- 양수자의 경우, 해당 집을 직접 보러가야 하는 번거로움이 있다.
- 이를 해결하기 위해 화상 채팅을 접목하여 쉽게 매물을 양도할 수 있는 서비스를 제작하였다.

<br/>

## 📚 기술 스택

### Back-End
![Java](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![JPA](https://img.shields.io/badge/jpa-20336B?style=for-the-badge&logo=JPA&logoColor=white)
<br/>
![ElasticSearch](https://img.shields.io/badge/elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white)
![Kibana](https://img.shields.io/badge/kibana-005571?style=for-the-badge&logo=kibana&logoColor=white)
<br/>
![MySQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![MongoDB](https://img.shields.io/badge/mongodb-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
<br/>
![Amazon EC2](https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)
![Docker](https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Jenkins](https://img.shields.io/badge/jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white)
![Amazon S3](https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)
<br/>
![WebRTC](https://img.shields.io/badge/webrtc-333333?style=for-the-badge&logo=webrtc&logoColor=white)

### Front-End
![React](https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=white)
![Css](https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css&logoColor=white)

<br/>

## 📋 개발 담당

### Elastic Search
- 매물 검색
- 관련 검색어
- 해시태그 검색
- 본문 검색
- 가까운 역, 대학교 검색

### SpringBoot, JPA
- 매물 피드 구현
- ElasticSearch 쿼리 구현 및 연동

### CI / CD
- Docker
  - ElasticSearch, Kibana 이미지 생성 및 실행
  - - DockerHub 백업

### CSS
- Figma를 기반으로 CSS 적용

<br/>

## 📌 규칙

### Git Commit Convention
- [Convention 참고](https://velog.io/@archivvonjang/Git-Commit-Message-Convention)

### Git Branch
- develop-back과 develop-front로 나눠서 개발
- 각 develop에서 feature branch를 생성해서 기능 별로 개발
  - 단, 기능 별로 branch를 생성할 것 (ex. 로그인, 로그아웃 등)
- 각 develop으로 merge하기 전 기능이 잘 작동하는지와 diff 확인할 것

<br/>

## ✅ 참고 자료
- [Notion](https://checkered-bobolink-2c4.notion.site/468a7f0375984bd6afbf2cd9514bb159?pvs=4)
