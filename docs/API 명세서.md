# API 명세서

## 📘 StudyHub API 명세서

## 요약

### 🔐 Auth API

| Method | URL | 설명 | 요청 Body | 응답 |
| --- | --- | --- | --- | --- |
| `POST` | `/api/auth/signup` | 회원가입 | `email`, `password`, `name`, `techStacks` | 201 Created |
| `POST` | `/api/auth/login` | 로그인 (JWT 발급) | `email`, `password` | accessToken + refreshToken |
| `POST` | `/api/auth/logout` | 로그아웃 | (쿠키 삭제) | 200 OK |
| `POST` | `/api/auth/refresh` | 토큰 재발급 | refreshToken (쿠키 또는 헤더) | accessToken 재발급 |

---

### 👤 Member API

| Method | URL | 설명 | 요청 / 응답 |
| --- | --- | --- | --- |
| `GET` | `/api/members/me` | 내 정보 조회 | Header: Authorization |
| `PUT` | `/api/members/me` | 내 정보 수정 | `name`, `bio`, `techStacks`, `profileImageUrl` |
| `GET` | `/api/members/me/projects` | 내가 등록한 프로젝트 목록 |  |
| `GET` | `/api/members/me/applications` | 내가 지원한 프로젝트 목록 |  |
| `GET` | `/api/members/me/notifications` | 알림 목록 |  |
| `PUT` | `/api/members/me/notifications/read` | 전체 알림 읽음 처리 |  |

---

### 📂 Project API

| Method | URL | 설명 | 요청 / 응답 |
| --- | --- | --- | --- |
| `POST` | `/api/projects` | 프로젝트 등록 | 제목, 설명, 기술스택, 마감일, 역할 설정 |
| `GET` | `/api/projects` | 프로젝트 목록 조회 | 쿼리파라미터: `techStack`, `status`, `deadline` 등 |
| `GET` | `/api/projects/{id}` | 프로젝트 상세 조회 | 모집현황 포함 |
| `PUT` | `/api/projects/{id}` | 프로젝트 수정 | 작성자만 가능 |
| `DELETE` | `/api/projects/{id}` | 프로젝트 삭제 | 작성자만 가능 |
| `PATCH` | `/api/projects/{id}/status` | 프로젝트 상태 변경 | `status`: `RECRUITING`, `IN_PROGRESS`, `COMPLETED` |

---

### 🎯 RoleSlot API

(역할별 인원 설정은 프로젝트 등록/수정 시 함께 처리)

---

### 🙋 Application (지원) API

| Method | URL | 설명 | 요청 / 응답 |
| --- | --- | --- | --- |
| `POST` | `/api/projects/{projectId}/apply` | 팀원 지원 | `roleSlotId`, `message` |
| `GET` | `/api/projects/{projectId}/applicants` | 지원자 목록 (팀장만) |  |
| `POST` | `/api/applications/{id}/approve` | 팀원 승인 |  |
| `POST` | `/api/applications/{id}/reject` | 팀원 거절 |  |

---

### 🔔 Notification API

| Method | URL | 설명 |
| --- | --- | --- |
| `GET` | `/api/notifications` | 내 알림 목록 조회 |
| `PUT` | `/api/notifications/{id}/read` | 알림 읽음 처리 |

---

### ⭐ Bookmark API

| Method | URL | 설명 |
| --- | --- | --- |
| `POST` | `/api/projects/{id}/bookmark` | 프로젝트 즐겨찾기 추가 |
| `DELETE` | `/api/projects/{id}/bookmark` | 즐겨찾기 제거 |

---

### 📊 Milestone API (선택 기능)

| Method | URL | 설명 |  |
| --- | --- | --- | --- |
| `GET` | `/api/projects/{projectId}/milestones` | 마일스톤 목록 |  |
| `POST` | `/api/projects/{projectId}/milestones` | 마일스톤 등록 |  |
| `PUT` | `/api/milestones/{id}` | 마일스톤 수정 |  |
| `DELETE` | `/api/milestones/{id}` | 마일스톤 삭제 |  |

## 개요

StudyHub API는 개발자들이 사이드 프로젝트 또는 스터디 팀을 모집하고 참여할 수 있는 매칭 플랫폼을 위한 백엔드 API입니다. 본 명세서는 RESTful 원칙을 따라며 JSON 형식으로 통신합니다.

- **기본 URL**: `https://api.studyhub.com` (uc608시)
- **uc778증 방식**: JWT 토큰 (Bearer Authentication)
- **Content-Type**: `application/json`

## 탐색 형식 (Response Format)

성공시:

```json
{
  "code": "200",
  "message": "성공 메시지",
  "data": {
    // 응답 데이터
  }
}
```

실패시:

```json
{
  "code": "401-1",
  "message": "인증에 실패했습니다.",
  "data": {}
}
```

## 개요 사이드에 추가될 필요가 있는 항목

| 키 | 값 | 설명 |
| --- | --- | --- |
| `code` | `"401-1"` 등 | HTTP 상태코드 + 세부 특징자 (Conflict: 409-2, Not Found: 404-1) |
| `message` | 문서 | 사용자와 개발자가 다 이해 가능한 메시지 |
| `data` | object | 성공시에도 그래트에 해당하는 데이터, 실패시에도 `{}` 형식 복귀를 위해 고정됨 |

### 예제 응답 (가장 많이 보고한 401, 403, 409)

### 인증 실패 (401-1)

```
{
  "code": "401-1",
  "message": "인증에 실패했습니다.",
  "data": {}
}
```

### 권한 없음 (403-1)

```
{
  "code": "403-1",
  "message": "접근 권한이 없습니다.",
  "data": {}
}
```

### 중복 지원 (409-1)

```
{
  "code": "409-1",
  "message": "이미 지원한 프로젝트입니다.",
  "data": {}
}
```

### 모집 인원 초과 (409-2)

```
{
  "code": "409-2",
  "message": "모집 인원을 초과했습니다.",
  "data": {}
}
```

### 서버 오류 (500-1)

```
{
  "code": "500-1",
  "message": "서버 내부 오류가 발생했습니다.",
  "data": {}
}
```

---

## 🔐 Auth API

### 회원가입

**요청**

- **Method**: `POST`
- **URL**: `/api/auth/signup`
- **Description**: 신규 회원 등록
- **Body**:
    
    ```json
    {  "email": "member@example.com",  "password": "securePassword123",  "name": "홍길동",  "techStacks": ["Java", "Spring Boot", "React"]}
    
    ```
    

**응답**

- **Status**: `201 Created`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "id": 1,    "email": "member@example.com",    "name": "홍길동",    "techStacks": ["Java", "Spring Boot", "React"]  },  "message": "회원가입이 완료되었습니다."}
    
    ```
    

### 로그인

**요청**

- **Method**: `POST`
- **URL**: `/api/auth/login`
- **Description**: 로그인 및 JWT 토큰 발급
- **Body**:
    
    ```json
    {  "email": "member@example.com",  "password": "securePassword123"}
    
    ```
    

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",    "expiresIn": 3600  },  "message": "로그인 성공"}
    
    ```
    
- **Cookies**: `refreshToken` (HttpOnly, Secure)

### 로그아웃

**요청**

- **Method**: `POST`
- **URL**: `/api/auth/logout`
- **Description**: 로그아웃 (리프레시 토큰 무효화 및 쿠키 삭제)
- **Headers**:
    - `Authorization: Bearer {accessToken}`

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "message": "로그아웃 되었습니다."}
    
    ```
    

### 토큰 재발급

**요청**

- **Method**: `POST`
- **URL**: `/api/auth/refresh`
- **Description**: 액세스 토큰 재발급
- **Cookies**: `refreshToken` (자동 전송)
- **Headers**: (쿠키 없을 경우)
    - `X-Refresh-Token: {refreshToken}`

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",    "expiresIn": 3600  },  "message": "토큰이 갱신되었습니다."}
    
    ```
    

## 👤 Member API

### 내 정보 조회

**요청**

- **Method**: `GET`
- **URL**: `/api/members/me`
- **Description**: 로그인한 사용자 정보 조회
- **Headers**:
    - `Authorization: Bearer {accessToken}`

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "id": 1,    "email": "member@example.com",    "name": "홍길동",    "bio": "백엔드 개발자입니다.",    "profileImageUrl": "https://storage.studyhub.com/profiles/member1.jpg",    "techStacks": ["Java", "Spring Boot", "React"],    "createdAt": "2025-03-15T09:30:00Z"  }}
    
    ```
    

### 내 정보 수정

**요청**

- **Method**: `PUT`
- **URL**: `/api/members/me`
- **Description**: 사용자 정보 수정
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Body**:
    
    ```json
    {  "name": "홍길동 (수정)",  "bio": "백엔드 개발자 & 데브옵스 엔지니어",  "techStacks": ["Java", "Spring Boot", "React", "Docker"],  "profileImageUrl": "https://storage.studyhub.com/profiles/member1_new.jpg"}
    
    ```
    

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "id": 1,    "name": "홍길동 (수정)",    "bio": "백엔드 개발자 & 데브옵스 엔지니어",    "techStacks": ["Java", "Spring Boot", "React", "Docker"],    "profileImageUrl": "https://storage.studyhub.com/profiles/member1_new.jpg",    "updatedAt": "2025-03-20T14:25:00Z"  },  "message": "프로필이 업데이트되었습니다."}
    
    ```
    

### 내가 등록한 프로젝트 목록

**요청**

- **Method**: `GET`
- **URL**: `/api/members/me/projects`
- **Description**: 내가 생성한 프로젝트 목록 조회
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Query Parameters**:
    - `page`: 페이지 번호 (기본값: 0)
    - `size`: 페이지 크기 (기본값: 10)
    - `status`: 프로젝트 상태 필터 (RECRUITING, IN_PROGRESS, COMPLETED)

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "content": [      {        "id": 1,        "title": "Spring Boot 프로젝트",        "status": "RECRUITING",        "deadline": "2025-06-30",        "techStacks": ["Java", "Spring Boot", "MySQL"],        "currentApplicants": 5,        "createdAt": "2025-03-10T10:00:00Z"      },      {        "id": 2,        "title": "React Native 애플리케이션",        "status": "IN_PROGRESS",        "deadline": "2025-05-15",        "techStacks": ["React Native", "Firebase"],        "currentApplicants": 3,        "createdAt": "2025-02-20T15:30:00Z"      }    ],    "pageable": {      "pageNumber": 0,      "pageSize": 10,      "totalElements": 2,      "totalPages": 1    }  }}
    
    ```
    

### 내가 지원한 프로젝트 목록

**요청**

- **Method**: `GET`
- **URL**: `/api/members/me/applications`
- **Description**: 내가 지원한 프로젝트 목록 및 지원 상태 조회
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Query Parameters**:
    - `page`: 페이지 번호 (기본값: 0)
    - `size`: 페이지 크기 (기본값: 10)
    - `status`: 지원 상태 필터 (PENDING, APPROVED, REJECTED)

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "content": [      {        "id": 1,        "projectId": 3,        "projectTitle": "MSA 아키텍처 스터디",        "roleSlot": {          "id": 5,          "roleName": "백엔드"        },        "status": "APPROVED",        "appliedAt": "2025-03-15T11:20:00Z"      },      {        "id": 2,        "projectId": 4,        "projectTitle": "Flutter 쇼핑몰 앱",        "roleSlot": {          "id": 8,          "roleName": "프론트엔드"        },        "status": "PENDING",        "appliedAt": "2025-03-18T09:45:00Z"      }    ],    "pageable": {      "pageNumber": 0,      "pageSize": 10,      "totalElements": 2,      "totalPages": 1    }  }}
    
    ```
    

### 내 알림 목록

**요청**

- **Method**: `GET`
- **URL**: `/api/members/me/notifications`
- **Description**: 사용자 알림 목록 조회
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Query Parameters**:
    - `page`: 페이지 번호 (기본값: 0)
    - `size`: 페이지 크기 (기본값: 20)
    - `isRead`: 읽음 상태 필터 (true/false, 기본값: null - 모두)

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "content": [      {        "id": 1,        "message": "MSA 아키텍처 스터디에 지원이 승인되었습니다.",        "type": "APPROVE",        "resourceId": 3,        "isRead": false,        "createdAt": "2025-03-16T14:30:00Z"      },      {        "id": 2,        "message": "홍길동님이 Spring Boot 프로젝트에 지원했습니다.",        "type": "APPLY",        "resourceId": 1,        "isRead": true,        "createdAt": "2025-03-15T11:20:00Z"      }    ],    "pageable": {      "pageNumber": 0,      "pageSize": 20,      "totalElements": 2,      "totalPages": 1    },    "unreadCount": 1  }}
    
    ```
    

### 전체 알림 읽음 처리

**요청**

- **Method**: `PUT`
- **URL**: `/api/members/me/notifications/read`
- **Description**: 모든 알림을 읽음 상태로 변경
- **Headers**:
    - `Authorization: Bearer {accessToken}`

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "updatedCount": 5  },  "message": "모든 알림이 읽음 처리되었습니다."}
    
    ```
    

## 📂 Project API

### 프로젝트 등록

**요청**

- **Method**: `POST`
- **URL**: `/api/projects`
- **Description**: 새 프로젝트 등록
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Body**:
    
    ```json
    {  "title": "Spring Security 인증 모듈 개발",  "description": "OAuth2.0과 JWT를 활용한 인증 모듈 개발 프로젝트입니다.",  "techStacks": ["Java", "Spring Boot", "Spring Security", "JWT"],  "deadline": "2025-07-15",  "roleSlots": [    {      "roleName": "백엔드",      "maxCount": 2    },    {      "roleName": "프론트엔드",      "maxCount": 1    },    {      "roleName": "DevOps",      "maxCount": 1    }  ]}
    
    ```
    

**응답**

- **Status**: `201 Created`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "id": 5,    "title": "Spring Security 인증 모듈 개발",    "description": "OAuth2.0과 JWT를 활용한 인증 모듈 개발 프로젝트입니다.",    "techStacks": ["Java", "Spring Boot", "Spring Security", "JWT"],    "deadline": "2025-07-15",    "status": "RECRUITING",    "roleSlots": [      {        "id": 9,        "roleName": "백엔드",        "maxCount": 2,        "currentCount": 0      },      {        "id": 10,        "roleName": "프론트엔드",        "maxCount": 1,        "currentCount": 0      },      {        "id": 11,        "roleName": "DevOps",        "maxCount": 1,        "currentCount": 0      }    ],    "owner": {      "id": 1,      "name": "홍길동"    },    "createdAt": "2025-05-10T13:45:00Z"  },  "message": "프로젝트가 등록되었습니다."}
    
    ```
    

### 프로젝트 목록 조회

**요청**

- **Method**: `GET`
- **URL**: `/api/projects`
- **Description**: 프로젝트 목록 조회 (필터링, 정렬, 페이징 지원)
- **Query Parameters**:
    - `keyword`: 검색 키워드 (제목, 설명 포함)
    - `techStack`: 기술 스택 (복수 가능: `techStack=Java&techStack=React`)
    - `status`: 프로젝트 상태 (RECRUITING, IN_PROGRESS, COMPLETED)
    - `deadline`: 마감일 기준 필터 (after:YYYY-MM-DD, before:YYYY-MM-DD)
    - `sort`: 정렬 기준 (newest, deadline, popular)
    - `page`: 페이지 번호 (기본값: 0)
    - `size`: 페이지 크기 (기본값: 10)

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "content": [      {        "id": 5,        "title": "Spring Security 인증 모듈 개발",        "description": "OAuth2.0과 JWT를 활용한 인증 모듈 개발 프로젝트입니다.",        "techStacks": ["Java", "Spring Boot", "Spring Security", "JWT"],        "deadline": "2025-07-15",        "status": "RECRUITING",        "owner": {          "id": 1,          "name": "홍길동"        },        "roleSlots": [          {            "roleName": "백엔드",            "maxCount": 2,            "currentCount": 0          },          {            "roleName": "프론트엔드",            "maxCount": 1,            "currentCount": 0          },          {            "roleName": "DevOps",            "maxCount": 1,            "currentCount": 0          }        ],        "applicantsCount": 0,        "bookmarksCount": 3,        "createdAt": "2025-05-10T13:45:00Z"      },      // 추가 프로젝트들...    ],    "pageable": {      "pageNumber": 0,      "pageSize": 10,      "totalElements": 35,      "totalPages": 4    }  }}
    
    ```
    

### 프로젝트 상세 조회

**요청**

- **Method**: `GET`
- **URL**: `/api/projects/{id}`
- **Description**: 프로젝트 상세 정보 조회
- **Path Parameters**:
    - `id`: 프로젝트 ID

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "id": 5,    "title": "Spring Security 인증 모듈 개발",    "description": "OAuth2.0과 JWT를 활용한 인증 모듈 개발 프로젝트입니다.\n\n## 주요 기능\n- OAuth2.0 소셜 로그인\n- JWT 토큰 기반 인증\n- 권한 관리 기능",    "techStacks": ["Java", "Spring Boot", "Spring Security", "JWT"],    "deadline": "2025-07-15",    "status": "RECRUITING",    "owner": {      "id": 1,      "name": "홍길동",      "profileImageUrl": "https://storage.studyhub.com/profiles/user1.jpg"    },    "roleSlots": [      {        "id": 9,        "roleName": "백엔드",        "maxCount": 2,        "currentCount": 0,        "isAvailable": true      },      {        "id": 10,        "roleName": "프론트엔드",        "maxCount": 1,        "currentCount": 0,        "isAvailable": true      },      {        "id": 11,        "roleName": "DevOps",        "maxCount": 1,        "currentCount": 0,        "isAvailable": true      }    ],    "teamMembers": [],    "bookmarksCount": 3,    "isBookmarked": false,    "hasApplied": false,    "createdAt": "2025-05-10T13:45:00Z",    "updatedAt": "2025-05-10T13:45:00Z"  }}
    
    ```
    

### 프로젝트 수정

**요청**

- **Method**: `PUT`
- **URL**: `/api/projects/{id}`
- **Description**: 프로젝트 정보 수정 (작성자만 가능)
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Path Parameters**:
    - `id`: 프로젝트 ID
- **Body**:
    
    ```json
    {  "title": "Spring Security 인증 모듈 개발 (수정)",  "description": "OAuth2.0과 JWT를 활용한 인증 모듈 개발 프로젝트입니다. 성능 최적화에 관심 있는 분을 모십니다.",  "techStacks": ["Java", "Spring Boot", "Spring Security", "JWT", "Redis"],  "deadline": "2025-08-01",  "roleSlots": [    {      "id": 9,      "roleName": "백엔드",      "maxCount": 3    },    {      "id": 10,      "roleName": "프론트엔드",      "maxCount": 1    },    {      "id": 11,      "roleName": "DevOps",      "maxCount": 1    }  ]}
    
    ```
    

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "id": 5,    "title": "Spring Security 인증 모듈 개발 (수정)",    "description": "OAuth2.0과 JWT를 활용한 인증 모듈 개발 프로젝트입니다. 성능 최적화에 관심 있는 분을 모십니다.",    "techStacks": ["Java", "Spring Boot", "Spring Security", "JWT", "Redis"],    "deadline": "2025-08-01",    "roleSlots": [      {        "id": 9,        "roleName": "백엔드",        "maxCount": 3,        "currentCount": 0      },      {        "id": 10,        "roleName": "프론트엔드",        "maxCount": 1,        "currentCount": 0      },      {        "id": 11,        "roleName": "DevOps",        "maxCount": 1,        "currentCount": 0      }    ],    "updatedAt": "2025-05-11T09:20:00Z"  },  "message": "프로젝트가 수정되었습니다."}
    
    ```
    

### 프로젝트 삭제

**요청**

- **Method**: `DELETE`
- **URL**: `/api/projects/{id}`
- **Description**: 프로젝트 삭제 (작성자만 가능)
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Path Parameters**:
    - `id`: 프로젝트 ID

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "message": "프로젝트가 삭제되었습니다."}
    
    ```
    

### 프로젝트 상태 변경

**요청**

- **Method**: `PATCH`
- **URL**: `/api/projects/{id}/status`
- **Description**: 프로젝트 상태 변경 (작성자만 가능)
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Path Parameters**:
    - `id`: 프로젝트 ID
- **Body**:
    
    ```json
    {  "status": "IN_PROGRESS"}
    
    ```
    

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "id": 5,    "status": "IN_PROGRESS",    "updatedAt": "2025-05-20T10:15:00Z"  },  "message": "프로젝트 상태가 변경되었습니다."}
    
    ```
    

## 🙋 Application (지원) API

### 팀원 지원

**요청**

- **Method**: `POST`
- **URL**: `/api/projects/{projectId}/apply`
- **Description**: 프로젝트에 팀원으로 지원
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Path Parameters**:
    - `projectId`: 프로젝트 ID
- **Body**:
    
    ```json
    {  "roleSlotId": 9,  "message": "Spring Security 관련 프로젝트 경험이 있으며, 인증 모듈 개발에 참여하고 싶습니다."}
    
    ```
    

**응답**

- **Status**: `201 Created`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "id": 15,    "projectId": 5,    "projectTitle": "Spring Security 인증 모듈 개발 (수정)",    "roleSlot": {      "id": 9,      "roleName": "백엔드"    },    "status": "PENDING",    "message": "Spring Security 관련 프로젝트 경험이 있으며, 인증 모듈 개발에 참여하고 싶습니다.",    "appliedAt": "2025-05-11T11:30:00Z"  },  "message": "지원이 완료되었습니다."}
    
    ```
    

### 지원자 목록 조회

**요청**

- **Method**: `GET`
- **URL**: `/api/projects/{projectId}/applicants`
- **Description**: 프로젝트에 지원한 사용자 목록 조회 (프로젝트 작성자만 접근 가능)
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Path Parameters**:
    - `projectId`: 프로젝트 ID
- **Query Parameters**:
    - `roleSlotId`: 특정 역할에 지원한 사용자만 조회 (선택)
    - `status`: 지원 상태 필터 (PENDING, APPROVED, REJECTED)
    - `page`: 페이지 번호 (기본값: 0)
    - `size`: 페이지 크기 (기본값: 10)

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "content": [      {        "id": 15,        "member": {          "id": 2,          "name": "김철수",          "profileImageUrl": "https://storage.studyhub.com/profiles/member2.jpg",          "techStacks": ["Java", "Spring Boot", "Spring Security"]        },        "roleSlot": {          "id": 9,          "roleName": "백엔드"        },        "message": "Spring Security 관련 프로젝트 경험이 있으며, 인증 모듈 개발에 참여하고 싶습니다.",        "status": "PENDING",        "appliedAt": "2025-05-11T11:30:00Z"      },      {        "id": 16,        "member": {          "id": 3,          "name": "이영희",          "profileImageUrl": "https://storage.studyhub.com/profiles/member3.jpg",          "techStacks": ["React", "TypeScript", "Tailwind CSS"]        },        "roleSlot": {          "id": 10,          "roleName": "프론트엔드"        },        "message": "인증 UI 개발 경험이 있습니다.",        "status": "PENDING",        "appliedAt": "2025-05-11T14:20:00Z"      }    ],    "pageable": {      "pageNumber": 0,      "pageSize": 10,      "totalElements": 2,      "totalPages": 1    }  }}
    
    ```
    

### 팀원 승인

**요청**

- **Method**: `POST`
- **URL**: `/api/applications/{id}/approve`
- **Description**: 지원자 승인 (프로젝트 작성자만 가능)
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Path Parameters**:
    - `id`: 지원서 ID

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "id": 15,    "status": "APPROVED",    "projectId": 5,    "projectTitle": "Spring Security 인증 모듈 개발 (수정)",    "roleSlot": {      "id": 9,      "roleName": "백엔드",      "currentCount": 1,      "maxCount": 3    },    "updatedAt": "2025-05-12T09:15:00Z"  },  "message": "지원자가 승인되었습니다."}
    
    ```
    

### 팀원 거절

**요청**

- **Method**: `POST`
- **URL**: `/api/applications/{id}/reject`
- **Description**: 지원자 거절 (프로젝트 작성자만 가능)
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Path Parameters**:
    - `id`: 지원서 ID
- **Body** (선택):
    
    ```json
    {  "rejectReason": "현재 백엔드 포지션은 Spring Security 고급 지식이 필요합니다."}
    
    ```
    

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "id": 16,    "status": "REJECTED",    "projectId": 5,    "projectTitle": "Spring Security 인증 모듈 개발 (수정)",    "roleSlot": {      "id": 10,      "roleName": "프론트엔드"    },    "rejectReason": "현재 백엔드 포지션은 Spring Security 고급 지식이 필요합니다.",    "updatedAt": "2025-05-12T09:20:00Z"  },  "message": "지원자가 거절되었습니다."}
    
    ```
    

## 🔔 Notification API

### 알림 목록 조회

**요청**

- **Method**: `GET`
- **URL**: `/api/notifications`
- **Description**: 사용자 알림 목록 조회 (Member API의 `/api/members/me/notifications`와 동일)
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Query Parameters**:
    - `page`: 페이지 번호 (기본값: 0)
    - `size`: 페이지 크기 (기본값: 20)
    - `isRead`: 읽음 상태 필터 (true/false, 기본값: null - 모두)

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "content": [      {        "id": 1,        "message": "MSA 아키텍처 스터디에 지원이 승인되었습니다.",        "type": "APPROVE",        "resourceId": 3,        "isRead": false,        "createdAt": "2025-03-16T14:30:00Z"      },      {        "id": 2,        "message": "홍길동님이 Spring Boot 프로젝트에 지원했습니다.",        "type": "APPLY",        "resourceId": 1,        "isRead": true,        "createdAt": "2025-03-15T11:20:00Z"      }    ],    "pageable": {      "pageNumber": 0,      "pageSize": 20,      "totalElements": 2,      "totalPages": 1    },    "unreadCount": 1  }}
    
    ```
    

### 알림 읽음 처리

**요청**

- **Method**: `PUT`
- **URL**: `/api/notifications/{id}/read`
- **Description**: 특정 알림 읽음 처리
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Path Parameters**:
    - `id`: 알림 ID

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "id": 1,    "isRead": true,    "updatedAt": "2025-05-12T10:15:00Z"  },  "message": "알림이 읽음 처리되었습니다."}
    
    ```
    

## ⭐ Bookmark API

### 즐겨찾기 추가

**요청**

- **Method**: `POST`
- **URL**: `/api/projects/{id}/bookmark`
- **Description**: 프로젝트 즐겨찾기에 추가
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Path Parameters**:
    - `id`: 프로젝트 ID

**응답**

- **Status**: `201 Created`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "id": 7,    "projectId": 5,    "projectTitle": "Spring Security 인증 모듈 개발 (수정)",    "bookmarkedAt": "2025-05-12T11:30:00Z"  },  "message": "프로젝트가 즐겨찾기에 추가되었습니다."}
    
    ```
    

### 즐겨찾기 제거

**요청**

- **Method**: `DELETE`
- **URL**: `/api/projects/{id}/bookmark`
- **Description**: 프로젝트 즐겨찾기에서 제거
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Path Parameters**:
    - `id`: 프로젝트 ID

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "message": "프로젝트가 즐겨찾기에서 제거되었습니다."}
    
    ```
    

## 📊 Milestone API (선택 기능)

### 마일스톤 목록 조회

**요청**

- **Method**: `GET`
- **URL**: `/api/projects/{projectId}/milestones`
- **Description**: 프로젝트의 마일스톤 목록 조회
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Path Parameters**:
    - `projectId`: 프로젝트 ID
- **Query Parameters**:
    - `status`: 마일스톤 상태 필터 (TODO, IN_PROGRESS, DONE)

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": [    {      "id": 1,      "title": "인증 모듈 설계",      "description": "Spring Security와 JWT를 활용한 인증 모듈 아키텍처 설계",      "dueDate": "2025-06-01",      "status": "DONE",      "createdBy": {        "id": 1,        "name": "홍길동"      },      "createdAt": "2025-05-15T09:00:00Z",      "updatedAt": "2025-05-25T14:30:00Z"    },    {      "id": 2,      "title": "백엔드 개발",      "description": "OAuth2.0 및 JWT 인증 구현",      "dueDate": "2025-07-01",      "status": "IN_PROGRESS",      "createdBy": {        "id": 1,        "name": "홍길동"      },      "createdAt": "2025-05-15T09:05:00Z"    },    {      "id": 3,      "title": "프론트엔드 구현",      "description": "로그인 UI 및 인증 플로우 구현",      "dueDate": "2025-07-15",      "status": "TODO",      "createdBy": {        "id": 1,        "name": "홍길동"      },      "createdAt": "2025-05-15T09:10:00Z"    }  ]}
    
    ```
    

### 마일스톤 등록

**요청**

- **Method**: `POST`
- **URL**: `/api/projects/{projectId}/milestones`
- **Description**: 새 마일스톤 등록 (프로젝트 팀원만 가능)
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Path Parameters**:
    - `projectId`: 프로젝트 ID
- **Body**:
    
    ```json
    {  "title": "서버 배포",  "description": "AWS EC2 인스턴스에 서버 배포 및 CI/CD 파이프라인 구축",  "dueDate": "2025-07-30",  "status": "TODO"}
    
    ```
    

**응답**

- **Status**: `201 Created`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "id": 4,    "title": "서버 배포",    "description": "AWS EC2 인스턴스에 서버 배포 및 CI/CD 파이프라인 구축",    "dueDate": "2025-07-30",    "status": "TODO",    "createdBy": {      "id": 1,      "name": "홍길동"    },    "createdAt": "2025-05-15T15:20:00Z"  },  "message": "마일스톤이 등록되었습니다."}
    
    ```
    

### 마일스톤 수정

**요청**

- **Method**: `PUT`
- **URL**: `/api/milestones/{id}`
- **Description**: 마일스톤 수정 (프로젝트 팀원만 가능)
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Path Parameters**:
    - `id`: 마일스톤 ID
- **Body**:
    
    ```json
    {  "title": "서버 배포 및 모니터링 설정",  "description": "AWS EC2 인스턴스에 서버 배포 및 CI/CD 파이프라인 구축, Prometheus/Grafana 모니터링 설정",  "dueDate": "2025-08-10",  "status": "TODO"}
    
    ```
    

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "data": {    "id": 4,    "title": "서버 배포 및 모니터링 설정",    "description": "AWS EC2 인스턴스에 서버 배포 및 CI/CD 파이프라인 구축, Prometheus/Grafana 모니터링 설정",    "dueDate": "2025-08-10",    "status": "TODO",    "updatedAt": "2025-05-15T16:45:00Z"  },  "message": "마일스톤이 수정되었습니다."}
    
    ```
    

### 마일스톤 삭제

**요청**

- **Method**: `DELETE`
- **URL**: `/api/milestones/{id}`
- **Description**: 마일스톤 삭제 (프로젝트 작성자 또는 마일스톤 작성자만 가능)
- **Headers**:
    - `Authorization: Bearer {accessToken}`
- **Path Parameters**:
    - `id`: 마일스톤 ID

**응답**

- **Status**: `200 OK`
- **Body**:
    
    ```json
    {  "success": true,  "message": "마일스톤이 삭제되었습니다."}
    
    ```
    

## 참고사항

### 오류 코드 및 메시지

| 코드 | 메시지 | 설명 |
| --- | --- | --- |
| `INVALID_REQUEST` | 요청이 유효하지 않습니다. | 잘못된 요청 형식 |
| `UNAUTHORIZED` | 인증이 필요합니다. | 인증 토큰 없음 |
| `TOKEN_EXPIRED` | 토큰이 만료되었습니다. | JWT 토큰 만료 |
| `FORBIDDEN` | 접근 권한이 없습니다. | 권한 없는 리소스 접근 |
| `RESOURCE_NOT_FOUND` | 리소스를 찾을 수 없습니다. | 존재하지 않는 리소스 |
| `DUPLICATE_RESOURCE` | 이미 존재하는 리소스입니다. | 중복 리소스 생성 시도 |
| `VALIDATION_ERROR` | 입력값이 유효하지 않습니다. | 유효성 검사 실패 |
| `PROJECT_CLOSED` | 모집이 마감된 프로젝트입니다. | 마감된 프로젝트 지원 시도 |
| `ROLE_FULL` | 해당 역할의 모집이 마감되었습니다. | 역할 정원 초과 |
| `ALREADY_APPLIED` | 이미 지원한 프로젝트입니다. | 중복 지원 시도 |
| `CONCURRENT_MODIFICATION` | 동시 수정 충돌이 발생했습니다. | 동시성 제어 예외 |
| `INTERNAL_SERVER_ERROR` | 서버 오류가 발생했습니다. | 서버 내부 오류 |

### 실시간 알림 (WebSocket)

**WebSocket 연결**

- **URL**: `/ws/notifications`
- **프로토콜**: STOMP over SockJS
- **인증**: 연결 시 JWT 토큰을 헤더에 포함

**구독 예시**

- **Topic**: `/member/queue/notifications`
- **메시지 형식**:
    
    ```json
    {  "id": 20,  "message": "Spring Security 인증 모듈 개발 프로젝트에 지원이 승인되었습니다.",  "type": "APPROVE",  "resourceId": 5,  "isRead": false,  "createdAt": "2025-05-12T14:30:00Z"}
    
    ```
    

### 동시성 제어 메커니즘

- 잔여 인원에 대한 동시 지원 처리는 Redis 분산 락을 사용하여 제어합니다.
- 락 획득 실패 시 `CONCURRENT_MODIFICATION` 오류를 반환합니다.
- 지원 프로세스에서는 다음과 같은 락 키 패턴을 사용합니다:
    - `project:{projectId}:roleSlot:{roleSlotId}:lock`