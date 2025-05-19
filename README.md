# StudyHub 프로젝트

## 📌 프로젝트 소개

**StudyHub**는 개발자들이 사이드 프로젝트나 스터디 팀을 쉽고 빠르게 찾고 참여할 수 있도록 돕는 기술 중심 매칭 플랫폼입니다.

### 🚀 주요 목표

- 기술 태그 기반 프로젝트 검색 및 참여
- 효율적인 팀원 지원 및 관리
- Redis 기반 동시성 제어
- WebSocket을 활용한 실시간 알림

---

## 💡 기능 요약

### 🔑 회원 기능

- 이메일 기반 회원가입 및 로그인 (JWT 인증)
- GitHub 소셜 로그인 지원 (기술 스택 자동 추출)
- 내 정보 조회 및 수정 (이름, 기술 스택, 프로필 이미지)

### 📂 프로젝트 관리

- 프로젝트 생성/수정/삭제 (작성자만 가능)
- 프로젝트 목록 조회 (기술 스택, 마감일, 상태별 필터링)
- 프로젝트 상세 보기 (모집 현황, 지원 현황 포함)

### 🙋 팀원 모집 및 관리

- 역할별 모집 인원 설정
- 팀원 지원, 승인 및 거절 기능
- Redis 분산 락을 이용한 동시 지원 제어

### 🔔 알림 기능

- WebSocket 기반 실시간 알림 (지원, 승인, 거절 상태 알림)
- 알림 읽음 처리 및 관리

### 📚 마이페이지

- 내가 등록한 프로젝트 목록
- 내가 지원한 프로젝트 및 상태 관리
- 즐겨찾기 프로젝트 관리

---

## 🛠 기술 스택

### 백엔드

- Java 21, Spring Boot 3.2+
- Spring Security, JWT 인증
- Spring Data JPA, QueryDSL
- MySQL, Redis (분산 락 및 캐싱)
- WebSocket, Redis Pub/Sub

### 프론트엔드

- React 또는 Next.js + TypeScript
- Tailwind CSS, Shadcn UI
- 실시간 통신: SockJS + STOMP

### 인프라 및 DevOps

- AWS EC2, RDS, ElastiCache, S3
- GitHub Actions (CI/CD)
- 모니터링: Spring Actuator, Prometheus, Grafana

---

## 📊 데이터베이스 구조

| 테이블명         | 설명                                        |
| ---------------- | ------------------------------------------- |
| Member           | 사용자 정보 (이메일, 이름, 프로필 등)       |
| Project          | 프로젝트 정보 (제목, 설명, 마감일, 상태 등) |
| RoleSlot         | 역할별 모집 슬롯 (인원 제한 등)             |
| Application      | 지원 정보 (지원자, 상태, 메시지 등)         |
| Notification     | 알림 관리 (수신자, 메시지, 읽음 여부 등)    |
| ProjectTechStack | 프로젝트의 기술 스택                        |
| MemberTechStack  | 사용자의 기술 스택                          |
| Bookmark         | 사용자별 프로젝트 즐겨찾기                  |
| ProjectMilestone | 프로젝트 진행 상황 (마일스톤)               |
| TeamMember       | 승인된 프로젝트 팀원                        |

---

## 🌐 API 명세

- RESTful API 구조
- JWT 기반 인증 처리
- JSON 기반 응답 및 요청

자세한 API 명세는 별도 문서를 참조하세요.

---

## ⚙️ 프로젝트 실행 방법

1. GitHub 저장소에서 프로젝트를 Clone합니다.
2. MySQL 및 Redis 환경을 설정합니다.
3. Spring Boot 어플리케이션을 실행합니다.
4. React 또는 Next.js 프론트엔드를 실행하여 접속합니다.

---

## 📈 향후 확장 계획

- 이메일 알림 시스템 (비동기 처리)
- 프로젝트 진행 상태 관리 대시보드
- 상세한 프로젝트 통계 정보 제공

---

## 📖 문서화

- Swagger 또는 Spring REST Docs로 API 문서화
- GitHub README, 기술 블로그 등으로 기술 공유
