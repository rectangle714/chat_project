<h1 align="center">ChatWave</h1>

**실시간 커뮤니케이션이 가능한 채팅 서비스**로, 1:1 및 그룹 채팅을 지원합니다.  
JWT 기반 인증, WebSocket을 활용한 메시지 송수신, Redis 기반 메시지 브로커를 통해 안정성과 확장성을 고려한 구조로 설계되었습니다.

## 🔧 Tech Stack

- **Backend:** Java 17, Spring Boot, Spring Security, JPA, Querydsl
- **Frontend:** React, JavaScript
- **DB:** MariaDB, Redis
- **Message Broker:** Redis Pub/Sub
- **Build & Deploy:** Gradle, Docker, Jenkins, AWS EC2
- **Version Control:** GitHub

## 🧩 주요 기능

### ✅ 인증 및 사용자 관리
- JWT 기반 로그인/회원가입
- Spring Security 기반 Role 인증 처리
- 비밀번호 암호화 및 인증 예외 처리

### ✅ 실시간 채팅 기능
- WebSocket + STOMP 기반 실시간 채팅
- 파일 전송 (멀티파트 업로드, 대용량 대응)
- 공개 채팅방 및 친구 간 1:1 채팅 지원

### ✅ 채팅방 관리
- 채팅방 생성/수정/삭제
- 최대 인원 수 제한 설정
- 참여자 목록 및 최근 메시지 캐싱

### ✅ 친구 관리
- 친구 요청 / 수락 / 거절 및 친구 목록 관리
- 실시간 알림 처리
