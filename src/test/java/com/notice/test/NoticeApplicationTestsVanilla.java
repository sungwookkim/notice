package com.notice.test;

import com.notice.common.file.FileUpload;
import com.notice.common.file.entity.UploadInfo;
import com.notice.domain.Notice;
import com.notice.domain.NoticeFileAttach;
import com.notice.dto.notice.req.ReqNoticeSaveDto;
import com.notice.dto.notice.req.ReqNoticeUpdateDto;
import com.notice.exception.ProcessException;
import com.notice.repository.mongodb.notice.NoticeMongoDBRepository;
import com.notice.repository.rdb.notice.NoticeRepository;
import com.notice.service.notice.NoticeService;
import com.notice.service.notice.NoticeServiceDecorator;
import com.notice.test.common.file.TestLocalFileUploadImpl;
import com.notice.test.repository.mongodb.notice.TestNoticeMongoDBRepositoryImpl;
import com.notice.test.repository.rdb.TestNoticeRepositoryImpl;
import com.notice.test.service.TestNoticeServiceDecoratorImpl;
import com.notice.test.service.TestNoticeServiceMongoDBImpl;
import com.notice.test.service.TestNoticeServicePgImpl;
import com.notice.vo.notice.NoticeVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     DB를 활용하지 않고 테스트 구현체들을 활용한 테스트
 *
 *     순수 자바로만 테스트를 하기에 DB 활성화등이 필요 없음.
 * </pre>
 */
@ActiveProfiles("vanilla")
@SpringBootTest
class NoticeApplicationTestsVanilla {
	private final static Logger logger = LoggerFactory.getLogger(NoticeApplicationTestsVanilla.class);

	NoticeRepository testNoticeRepositoryImpl = new TestNoticeRepositoryImpl();
	NoticeMongoDBRepository testNoticeMongoDBRepositoryImpl = new TestNoticeMongoDBRepositoryImpl();

	NoticeService testNoticeServicePgImpl = new TestNoticeServicePgImpl(testNoticeRepositoryImpl);
	NoticeService testNoticeServiceMongoDBImpl = new TestNoticeServiceMongoDBImpl(testNoticeMongoDBRepositoryImpl);
	NoticeServiceDecorator testNoticeServiceDecoratorImpl = new TestNoticeServiceDecoratorImpl(testNoticeServicePgImpl
			, testNoticeServiceMongoDBImpl
			, testNoticeRepositoryImpl
			, testNoticeMongoDBRepositoryImpl);

	@Test
	@DisplayName("ReqNoticeSaveDto 객체에서 Notice 객체 반환 검증")
	void testCase1() {
		ReqNoticeSaveDto reqNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now());
		Notice notice = reqNotice.newNoticeInstance();

		logger.info("saveNotice Title : {}, findNotice Title : {}", reqNotice.getTitle(), notice.getTitle());
		Assertions.assertEquals(reqNotice.getTitle(), notice.getTitle());
	}

	@Test
	@DisplayName("ReqNoticeSaveDto 객체 필수 값 예외 검증")
	void testCase2() {
		logger.info("제목 빈값 예외 검증 시작");
		Assertions.assertThrowsExactly(ProcessException.class, () -> {
			new ReqNoticeSaveDto(""
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();
		});

		logger.info("내용 빈값 예외 검증 시작");
		Assertions.assertThrowsExactly(ProcessException.class, () -> {
			new ReqNoticeSaveDto("title"
				, ""
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now()
			)
			.newNoticeInstance();
		});

		logger.info("작성자 빈값 예외 검증 시작");
		Assertions.assertThrowsExactly(ProcessException.class, () -> {
			new ReqNoticeSaveDto("title"
				, "contents"
				, ""
				, LocalDateTime.now()
				, LocalDateTime.now()
			)
			.newNoticeInstance();
		});

		logger.info("공지 시작일시 빈값 예외 검증 시작");
		Assertions.assertThrowsExactly(ProcessException.class, () -> {
			new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, null
				, LocalDateTime.now()
			)
			.newNoticeInstance();
		});

		logger.info("공지 종료일시 빈값 예외 검증 시작");
		Assertions.assertThrowsExactly(ProcessException.class, () -> {
			new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, null
			)
			.newNoticeInstance();
		});
	}

	@Test
	@DisplayName("[PG] Notice 정상 저장 검증")
	void testCase3() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		this.testNoticeServicePgImpl.save(saveNotice);

		Optional<NoticeVo> findNotice = this.testNoticeServicePgImpl.findById(saveNotice.getNoticeId());

		logger.info("findNotice seq : {}, saveNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, saveNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertEquals(saveNotice.getTitle(), findNotice.get().getTitle());
	}

	@Test
	@DisplayName("[MongoDB] Notice 정상 저장 검증")
	void testCase4() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		this.testNoticeServiceMongoDBImpl.save(saveNotice);

		Optional<NoticeVo> findNotice = this.testNoticeServiceMongoDBImpl.findById(saveNotice.getNoticeId());

		logger.info("findNotice seq : {}, saveNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, saveNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertEquals(saveNotice.getTitle(), findNotice.get().getTitle());
	}

	@Test
	@DisplayName("[Decorator] Notice 정상 저장 검증")
	void testCase5() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		this.testNoticeServiceDecoratorImpl.save(saveNotice);

		Optional<NoticeVo> findNotice = this.testNoticeServiceDecoratorImpl.findById(saveNotice.getNoticeId());

		logger.info("findNotice seq : {}, saveNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, saveNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertEquals(saveNotice.getTitle(), findNotice.get().getTitle());
	}

	@Test
	@DisplayName("[PG] Notice 정상 수정 검증")
	void testCase6() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		this.testNoticeServicePgImpl.save(saveNotice);

		ReqNoticeUpdateDto reqNoticeUpdateDto = new ReqNoticeUpdateDto(saveNotice.getNoticeId()
				, "title_update"
				, "contents_update"
				, LocalDateTime.now()
				, LocalDateTime.now());
		Notice updateNotice = reqNoticeUpdateDto.newNoticeInstance();
		Long seq = saveNotice.getNoticeId();

		this.testNoticeServicePgImpl.update(seq, updateNotice);

		Optional<NoticeVo> findNotice = this.testNoticeServicePgImpl.findById(seq);

		logger.info("findNotice seq : {}, updateNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, updateNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertEquals(updateNotice.getTitle(), findNotice.get().getTitle());
	}

	@Test
	@DisplayName("[MongoDB] Notice 정상 수정 검증")
	void testCase7() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		this.testNoticeServiceMongoDBImpl.save(saveNotice);

		ReqNoticeUpdateDto reqNoticeUpdateDto = new ReqNoticeUpdateDto(saveNotice.getNoticeId()
				, "title_update"
				, "contents_update"
				, LocalDateTime.now()
				, LocalDateTime.now());
		Notice updateNotice = reqNoticeUpdateDto.newNoticeInstance();
		Long seq = saveNotice.getNoticeId();

		this.testNoticeServiceMongoDBImpl.update(seq, updateNotice);

		Optional<NoticeVo> findNotice = this.testNoticeServiceMongoDBImpl.findById(seq);

		logger.info("findNotice seq : {}, updateNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, updateNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertEquals(updateNotice.getTitle(), findNotice.get().getTitle());
	}

	@Test
	@DisplayName("[Decorator] Notice 정상 수정 검증")
	void testCase8() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		this.testNoticeServiceDecoratorImpl.save(saveNotice);

		ReqNoticeUpdateDto reqNoticeUpdateDto = new ReqNoticeUpdateDto(saveNotice.getNoticeId()
				, "title_update"
				, "contents_update"
				, LocalDateTime.now()
				, LocalDateTime.now());
		Notice updateNotice = reqNoticeUpdateDto.newNoticeInstance();
		Long seq = saveNotice.getNoticeId();

		this.testNoticeServiceDecoratorImpl.update(seq, updateNotice);

		Optional<NoticeVo> findNoticePg = this.testNoticeServicePgImpl.findById(seq);
		Optional<NoticeVo> findNoticeMongoDB = this.testNoticeServiceMongoDBImpl.findById(seq);

		logger.info("seq : {}, updateNotice Title : {} [PG] findNotice Title : {}  [MongoDB] findNotice Title :{}"
				, seq
				, updateNotice.getTitle()
				, findNoticePg.get().getTitle()
				, findNoticeMongoDB.get().getTitle());

		Assertions.assertTrue(updateNotice.getTitle().equals(findNoticePg.get().getTitle())
				&& updateNotice.getTitle().equals(findNoticeMongoDB.get().getTitle()));
	}

	@Test
	@DisplayName("[PG] Notice 정상 삭제 검증")
	void testCase9() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		this.testNoticeServicePgImpl.save(saveNotice);
		this.testNoticeServicePgImpl.delete(saveNotice.getNoticeId());

		Optional<NoticeVo> findNoticePg = this.testNoticeServicePgImpl.findById(saveNotice.getNoticeId());

		Assertions.assertTrue(findNoticePg.isEmpty());
	}

	@Test
	@DisplayName("[MongoDB] Notice 정상 삭제 검증")
	void testCase10() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		this.testNoticeServiceMongoDBImpl.save(saveNotice);
		this.testNoticeServiceMongoDBImpl.delete(saveNotice.getNoticeId());

		Optional<NoticeVo> findNoticeMongoDB = this.testNoticeServiceMongoDBImpl.findById(saveNotice.getNoticeId());

		Assertions.assertTrue(findNoticeMongoDB.isEmpty());
	}

	@Test
	@DisplayName("[Decorator] Notice 정상 삭제 검증")
	void testCase11() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		this.testNoticeServiceDecoratorImpl.save(saveNotice);
		this.testNoticeServiceDecoratorImpl.delete(saveNotice.getNoticeId());

		Optional<NoticeVo> findNoticePg = this.testNoticeServiceMongoDBImpl.findById(saveNotice.getNoticeId());
		Optional<NoticeVo> findNoticeMongoDB = this.testNoticeServiceMongoDBImpl.findById(saveNotice.getNoticeId());

		Assertions.assertTrue(findNoticePg.isEmpty()
				&& findNoticeMongoDB.isEmpty());
	}

	@Test
	@DisplayName("[Decorator] Notice 파일 업로드 정상 저장 검증")
	void testCase12() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		List<File> files = Arrays.asList(new File("src/test/resources/file/upload_1.txt")
				, new File("src/test/resources/file/upload_2.txt"));

		List<FileUpload> fileUploads = new ArrayList<>();
		for(File file : files) {
			fileUploads.add(new TestLocalFileUploadImpl(file));
		}

		this.testNoticeServiceDecoratorImpl.save(saveNotice, fileUploads);

		Optional<NoticeVo> findNotice = this.testNoticeServiceDecoratorImpl.findById(saveNotice.getNoticeId());

		logger.info("findNotice seq : {}, saveNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, saveNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertEquals(saveNotice.getTitle(), findNotice.get().getTitle());
	}

	@Test
	@DisplayName("[Decorator] Notice 파일 업로드 없음 정상 저장 검증")
	void testCase12_1() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
				.newNoticeInstance();

		List<File> files = new ArrayList<>();
		List<FileUpload> fileUploads = new ArrayList<>();
		for(File file : files) {
			fileUploads.add(new TestLocalFileUploadImpl(file));
		}

		this.testNoticeServiceDecoratorImpl.save(saveNotice, fileUploads);

		Optional<NoticeVo> findNotice = this.testNoticeServiceDecoratorImpl.findById(saveNotice.getNoticeId());

		logger.info("findNotice seq : {}, saveNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, saveNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertEquals(saveNotice.getTitle(), findNotice.get().getTitle());
	}

	@Test
	@DisplayName("[Decorator] Notice 파일 업로드 정상 삭제 검증")
	void testCase13() throws IOException {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		List<File> files = Arrays.asList(new File("src/test/resources/file/upload_1.txt")
				, new File("src/test/resources/file/upload_2.txt"));

		for(File file : files) {
			UploadInfo uploadInfo = new TestLocalFileUploadImpl(file).upload();

			saveNotice.addNoticeFileAttach(new NoticeFileAttach(uploadInfo.originalFileName()
					, uploadInfo.uploadFileName()
					, uploadInfo.uploadPath()));
		}

		this.testNoticeServiceDecoratorImpl.save(saveNotice);
		this.testNoticeServiceDecoratorImpl.delete(saveNotice.getNoticeId());

		Optional<NoticeVo> findNoticePg = this.testNoticeServiceMongoDBImpl.findById(saveNotice.getNoticeId());
		Optional<NoticeVo> findNoticeMongoDB = this.testNoticeServiceMongoDBImpl.findById(saveNotice.getNoticeId());

		Assertions.assertTrue(findNoticePg.isEmpty()
				&& findNoticeMongoDB.isEmpty());
	}

	@Test
	@DisplayName("[Decorator] Notice 파일 업로드 0개이상 -> 0개이상 정상 수정 검증")
	void testCase14() throws IOException {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		List<File> saveFiles = Arrays.asList(new File("src/test/resources/file/upload_1.txt"));

		for(File file : saveFiles) {
			UploadInfo uploadInfo = new TestLocalFileUploadImpl(file).upload();

			saveNotice.addNoticeFileAttach(new NoticeFileAttach(uploadInfo.originalFileName()
					, uploadInfo.uploadFileName()
					, uploadInfo.uploadPath()));
		}

		this.testNoticeServiceDecoratorImpl.save(saveNotice);

		Notice updateNotice = new ReqNoticeSaveDto("title_update"
				, "contents_update"
				, "regId_update"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		List<File> updateFiles = Arrays.asList(new File("src/test/resources/file/upload_1.txt")
				, new File("src/test/resources/file/upload_2.txt"));

		List<FileUpload> fileUploads = new ArrayList<>();
		for(File file : updateFiles) {
			fileUploads.add(new TestLocalFileUploadImpl(file));
		}

		this.testNoticeServiceDecoratorImpl.update(saveNotice.getNoticeId()
				, updateNotice
				, fileUploads
				, TestLocalFileUploadImpl.fileDelete);

		Optional<NoticeVo> findNotice = this.testNoticeServiceDecoratorImpl.findById(saveNotice.getNoticeId());

		logger.info("findNotice seq : {}, saveNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, saveNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertEquals(findNotice.get().getNoticeFileAttachVos().size(), updateFiles.size());
	}

	@Test
	@DisplayName("[Decorator] Notice 파일 업로드 0개 -> 0개 이상 정상 수정 검증")
	void testCase15() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
				.newNoticeInstance();

		this.testNoticeServiceDecoratorImpl.save(saveNotice);

		Notice updateNotice = new ReqNoticeSaveDto("title_update"
				, "contents_update"
				, "regId_update"
				, LocalDateTime.now()
				, LocalDateTime.now())
				.newNoticeInstance();

		List<File> updateFiles = Arrays.asList(new File("src/test/resources/file/upload_1.txt")
				, new File("src/test/resources/file/upload_2.txt"));

		List<FileUpload> fileUploads = new ArrayList<>();
		for(File file : updateFiles) {
			fileUploads.add(new TestLocalFileUploadImpl(file));
		}

		this.testNoticeServiceDecoratorImpl.update(saveNotice.getNoticeId()
				, updateNotice
				, fileUploads
				, TestLocalFileUploadImpl.fileDelete);

		Optional<NoticeVo> findNotice = this.testNoticeServiceDecoratorImpl.findById(saveNotice.getNoticeId());

		logger.info("findNotice seq : {}, saveNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, saveNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertTrue(findNotice.get().getNoticeFileAttachVos().size() > 0);
	}

	@Test
	@DisplayName("[Decorator] Notice 파일 업로드 1개 이상-> 0개 정상 수정 검증")
	void testCase16() throws IOException {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		List<File> saveFiles = Arrays.asList(new File("src/test/resources/file/upload_1.txt"));

		for(File file : saveFiles) {
			UploadInfo uploadInfo = new TestLocalFileUploadImpl(file).upload();

			saveNotice.addNoticeFileAttach(new NoticeFileAttach(uploadInfo.originalFileName()
					, uploadInfo.uploadFileName()
					, uploadInfo.uploadPath()));
		}

		this.testNoticeServiceDecoratorImpl.save(saveNotice);

		Notice updateNotice = new ReqNoticeSaveDto("title_update"
				, "contents_update"
				, "regId_update"
				, LocalDateTime.now()
				, LocalDateTime.now())
				.newNoticeInstance();

		this.testNoticeServiceDecoratorImpl.update(saveNotice.getNoticeId()
				, updateNotice
				, new ArrayList<>()
				, TestLocalFileUploadImpl.fileDelete);

		Optional<NoticeVo> findNotice = this.testNoticeServiceDecoratorImpl.findById(saveNotice.getNoticeId());

		logger.info("findNotice seq : {}, saveNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, saveNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertTrue(findNotice.get().getNoticeFileAttachVos().size() == 0);
	}
}
