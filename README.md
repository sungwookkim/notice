# README

# 개발환경

- Java 17
- Spring Boot 3.1.7
    - spring-boot-starter-data-jpa
    - spring-boot-starter-data-mongodb
    - spring-boot-starter-web
- DB
    - Postgresql 15-alpine
    - MongoDB 4.4.26
- Docker-compose

# 패키지 및 클래스

- `common` 전역으로 사용하는 기능 패키지
    - `file` 파일 업로드 관련 패키지
        - `entity` 파일 업로드 처리 관련 엔티티 객체 패키지
            - **UploadInfo.java** *파일 업로드 프로세스 처리 관련 레코드*
        - **FileDelete.java** 파일 업로드 파일 삭제 인터페이스
        - **FileUpload.java** 파일 업로드 파일 저장 인터페이스
        - **LocalFileUploadImpl.java** 실행 중이 환경에 파일을 저장하는 구현체. OS 환경의 임시폴더에 저장.
    - `http` Http 관련 패키지
        - **ProcessCode.java** Rest 응답코드를 관리하는 인터페이스
        - **RestResponseResult.java** Rest 응답을 제어하는 클래
- `controller` 컨트롤러 패키지
    - `abs` 컨트롤러 추상 클래스를 관리하는 패키지
        - **NoticeControllerAbs.java** 공지사항 API 주요 프로세스를 관리하는 추상 클래스
    - `v1` v1 버전 API를 관리하는 패키지. 일반적으로 abs 추상 클래스들의 구현체
        - **NoticeController.java** *공지사항 v1 API 컨트롤러*
- `domain` 도메인 관련 패키지
    - **Notice.java** *공지사항 도메인*
    - **NoticeFileAttach.java** *공지사항 파일첨부 도메인*
- `dto` DTO 패키지
    - `notice` 공지사항 DTO 패키지
        - `req` 공지사항 요청 DTO 패키지
            - **ReqNoticeSaveDto.java** 공지사항 저장 요청 DTO
            - **ReqNoticeUpdateDto.java** 공지사항 수정 요청 DTO
        - `resp` 공지사항 응답 DTO 패키지
            - **RespNoticeDto.java** 공지사항 조회 API 응답에 사용되는 DTO
- `entity` 엔티티 관련 패키지
    - `mongodb` MongoDB 엔티티를 관리하는 패키지
        - **NoticeMongoDB.java** 공지사항 게시물 엔티티
        - **NoticeMongoDBFileAttach.java** 공지사항 게시물 파일첨부 엔티티
- `exception` 예외처리를 관리 패키지
    - **ProcessException** 프로세스 처리 전용으로 사용하는 예외
- `helper` 프로세스 처리에 유틸/편의 기능을 관리하는 패키지
    - **JsonHelper.java** JSON 관련 편의 클래스
- `repository` 저장소 관리 패키지
    - `mongodb` MongoDB 저장소를 관리하는 패키지
        - `notice` 공지사항 게시물 패키지
            - `impl` MongoDB 저장소 구현체 패키지
                - **NoticeMongoDBRepositoryImpl.java** 공지사항 게시물  구현체
            - **NoticeMongoDBRepository.java** *공지사항 게시물 저장소 인터페이스*
    - `rdb` RDB 저장소를 관리하는 패키지
        - `notice` 공지사항 게시물 패키지
            - **NoticeRepository.java** 공지사항 게시물 JPA 인터페이스
            - **NoticeFileAttachRepository.java** 공지사항 게시물 첨부파일 JPA 인터페이스
- `service` 프로세스 로직을 담당하는 service를 관리하는 패키지
    - `notice` 공지사항 게시물 패키지
        - `impl` service 구현체 관리 패키지
            - **NoticeServiceDecoratorImpl.java** 게시물 프로세스의 쓰기(RDB)/읽기(Nosql) 구현체들을 활용하여 전체 프로세스를 관리하는 구현체
            - **NoticeServiceMongoDBImpl.java** MongoDB 게시물 프로세스만 처리하는 구현체
            - **NoticeServicePgImpl.java** RDB(Postgresql) 게시물 프로세스만 처리하는 구현체
        - **NoticeService.java** 게시물 서비스 인터페이스
        - **NoticeServiceDecorator.java** 게시물 데코레이터 패턴 서비스 인터페이스
- `spring` 스프링 확장 및 설정를 관리하는 패키지
    - `annotation` 커스텀 어노테이션를 관리하는 패키지
        - **JsonLocalDateTime.java** LocalDateTime 타입의Serialize/Deserialize를 처리하기 위 ObjectMapper용 커스텀 어노테이션
    - `config` 스프링 Config를 관리하는 패키지
        - `db` Database Config를 관리하는 패키지
            - `mongodb` MongoDB 설정 패키지
                - **MongoDBConfig.java** MongoDB 설정
            - `pg` Postgresql 설정 패키지
                - **PostgresqlConfig.java** Postgresql 설정
        - `exception` Spring 관련 전체적인 예외를 관리 하는 패키지
            - **RestExceptionHandler.java** RestControllerAdvice 어노테이션이 선언된 클래스로 Rest와 관련된 모든 예외를 전역으로 처리.
- `vo` vo 객체 관리 패키지
    - `notice` 공지사항 게시물 vo 관리 패키지
        - **NoticeVo.java** 공지사항 게시물 vo
        - **NoticeFileAttachVo.java** 공지사항 게시물의 첨부파일 vo

# 외부요소

## DB

DB를 사용하기 위해선 docker-compose를 사용해서 프로젝트 안에 docker-compose 폴더의 postgresql-mongodb.yml 파일을 사용하여 DB들을 활성화 시킨다.

CMD등을 이용해 postgresql-mongodb.yml 폴더로 진입 후 아래 명령어를 실행한다.

`docker-compose -f postgresql-mongodb.yml up -d`

# 테스트

- `NoticeApplicationTestsVanilla`

  테스트 Repository, Service 구현체들을 사용하는데 해당 구현체들은 순수 자바만 사용하기에 DB 등과 같은 외부 요소가 없이 테스트가 가능한 클래스

- `NoticeApplicationTestsSpring`
  DB등과 같은 외부 요소를 사용하는 Service 테스트 클래스
- `NoticeApplicationTestsSpringRest`
  DB등과 같은 외부 요소를 사용하는 Controller 테스트 클래스

# 기타

- 대용량 트래픽 처리를 위해 조회 시 RDB가 아닌 Nosql를 사용함으로서 Read/Write 부하를 하나의 DB에 집중적으로 발생되는 부분을 분산.
    - `NoticeServiceDecoratorImpl` 클래스 참고.
- DB 필수 테스트, DB 비필수 테스트(순수 자바로만 구현)등으로 나눔으로서 테스트 시 DB와 같이 외부 요소 없이 비지니스 로직에만 집중해서 테스트 코드를 작성.
- 파일첨부 관련하여 게시물 저장/삭제/수정 메서드에서 전략 패턴을 활용하여 게시물을 처리하는 DB 프로세스와 파일첨부 프로세스를 분리하여 결합도를 낮춤.

# API

## 공통 응답전문

```json
{
	// Http Status 값
	"httpStatus": {
		"code": ...,
		"reasonPhrase": ...
	},
	// 프로세스 응답 코드
	"processCode": ...,
	// 프로세스 응답 값
	"processValue": ...
}
```

## 공통 processCode 값

- `1000` 성공
- `-9999` 서버 에러

# 공지사항

## 게시물 저장

`Method` POST

`URI` v1/notice

`요청값`

**noticeInfo**

- 필수여부 : Y

```json
{
	// 게시물 제목
	"title": "title",
	// 게시물 내용
	"contents": "contents",
	// 게시물 등록 ID
	"regId": "regId",
	// 게시물 공지 시작일
	"noticeStartDate": "2024-01-07 21:00:02.437",
	// 게시물 공지 종료일
	"noticeEndDate": "2024-01-07 21:00:02.437"
}
```

**fileAttach**

- 필수여부 : N
- 파일첨부할 파일

**method**

- 필수여부 : Y
- post 문자열

`요청 예제`

```bash
curl --location 'http://localhost:8080/v1/notice' \
--form 'noticeInfo="{\"title\":\"title\",\"contents\":\"contents\",\"regId\":\"regId\",\"noticeStartDate\":\"2024-01-07 21:00:02.437\",\"noticeEndDate\":\"2024-01-07 21:00:02.437\"}";type=application/json' \
--form 'fileAttach=@"/파일경로"' \
--form 'fileAttach=@"/파일경로"' \
--form 'method="post";type=text/plain'
```

`응답`

```json
{
    "httpStatus": {
        "code": "200",
        "reasonPhrase": "OK"
    },
    "processCode": "1000",
    "processValue": ""
}
```

**processCode**

성공

- 1000

실패

- 공통 processCode 값
- -1000 빈 값 존재
- -1002 파일첨부 삭제 중 에러 발생
- -1003 파일첨부 저장 중 에러 발생

**processValue**

빈 문자열

## 게시물 수정

`Method` POST

`URI` v1/notice

`요청값`

**noticeInfo**

- 필수여부 : Y

```json
{
	// 수정할 게시물 번호
	"noticeId": 3,
	// 게시물 제목
	"title": "title_update",
	// 게시물 내용
	"contents": "contents_update",
	// 게시물 공지 시작일
	"noticeStartDate": "2024-01-07 21:09:14.257",
	// 게시물 공지 종료일
	"noticeEndDate": "2024-01-08 21:09:14.257"
}
```

**fileAttach**

- 필수여부 : N
- 파일첨부할 파일

**method**

- 필수여부 : Y
- put 문자열

`요청 예제`

```bash
curl --location 'http://localhost:8080/v1/notice' \
--form 'noticeInfo="{\"noticeId\":3,\"title\":\"title_update\",\"contents\":\"contents_update\",\"noticeStartDate\":\"2024-01-07 21:09:14.257\",\"noticeEndDate\":\"2024-01-08 21:09:14.257\"}";type=application/json' \
--form 'fileAttach=@"/파일경로"' \
--form 'fileAttach=@"/파일경로"' \
--form 'method="put";type=text/plain'
```

`응답`

```json
{
    "httpStatus": {
        "code": "200",
        "reasonPhrase": "OK"
    },
    "processCode": "1000",
    "processValue": ""
}
```

**processCode**

성공

- 1000

실패

- 공통 processCode 값
- -1000 빈 값 존재
- -1002 파일첨부 삭제 중 에러 발생
- -1003 파일첨부 저장 중 에러 발생

**processValue**

빈 문자열

## 게시물 조회

`Method` GET

`URI` v1/notice/{게시물 번호}

`요청값`

**{게시물 번호}**

- 필수여부 : Y
- PathVariable 조회할 게시물 번호

`요청 예제`

```bash
curl --location 'http://localhost:8080/v1/notice/3'
```

`응답`

```json
{
    "httpStatus": {
        "code": "200",
        "reasonPhrase": "OK"
    },
    "processCode": "1000",
    "processValue": {
        "title": "title_update",
        "contents": "contents_update",
        "regDate": "2024-01-07 21:06:52.213",
        "viewCount": 2,
        "regId": "regId"
    }
}
```

**processCode**

성공

- 1000

실패

- 공통 processCode 값
- -1001 게시물이 미 존재

**processValue**

```json
{
	// 게시물 제목
	"title": "title_update",
	// 게시물 내용
	"contents": "contents_update",
	// 게시물 등록 날짜
	"regDate": "2024-01-07 21:06:52.213",
	// 게시물 조회 수
	"viewCount": 2,
	// 게시물 등록 ID
	"regId": "regId"
}
```

## 게시물 삭제

`Method` DELETE

`URI` v1/notice/{게시물 번호}

`요청값`

**{게시물 번호}**

- 필수여부 : Y
- PathVariable 삭제 게시물 번호

`요청 예제`

```bash
curl --location --request DELETE 'http://localhost:8080/v1/notice/3'
```

`응답`

```json
{
    "httpStatus": {
        "code": "200",
        "reasonPhrase": "OK"
    },
    "processCode": "1000",
    "processValue": ""
}
```

**processCode**

성공

- 1000

실패

- 공통 processCode 값

**processValue**

빈 문자열