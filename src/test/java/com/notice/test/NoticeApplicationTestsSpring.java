package com.notice.test;

import com.notice.common.file.FileUpload;
import com.notice.common.file.LocalFileUploadImpl;
import com.notice.common.file.entity.UploadInfo;
import com.notice.domain.Notice;
import com.notice.domain.NoticeFileAttach;
import com.notice.dto.notice.req.ReqNoticeSaveDto;
import com.notice.dto.notice.req.ReqNoticeUpdateDto;
import com.notice.service.notice.NoticeService;
import com.notice.service.notice.NoticeServiceDecorator;
import com.notice.test.common.file.TestLocalFileUploadImpl;
import com.notice.vo.notice.NoticeVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     DB를 활용한 실제 서비스 테스트
 * </pre>
 */
@SpringBootTest
class NoticeApplicationTestsSpring {
	private final static Logger logger = LoggerFactory.getLogger(NoticeApplicationTestsSpring.class);

	@Autowired
	NoticeServiceDecorator noticeServiceDecoratorImpl;

	@Autowired
	NoticeService noticeServicePgImpl;

	@Autowired
	NoticeService noticeServiceMongoDBImpl;

	@Test
	@DisplayName("[Decorator] Notice 정상 저장 검증")
	void testCase1() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		this.noticeServiceDecoratorImpl.save(saveNotice, new ArrayList<>());

		Optional<NoticeVo> findNotice = this.noticeServiceDecoratorImpl.findById(saveNotice.getNoticeId());

		logger.info("findNotice seq : {}, saveNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, saveNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertEquals(saveNotice.getTitle(), findNotice.get().getTitle());
	}

	@Test
	@DisplayName("[Decorator] Notice 정상 수정 검증")
	void testCase2() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		this.noticeServiceDecoratorImpl.save(saveNotice, new ArrayList<>());

		ReqNoticeUpdateDto reqNoticeUpdateDto = new ReqNoticeUpdateDto(saveNotice.getNoticeId()
				, "title_update"
				, "contents_update"
				, LocalDateTime.now()
				, LocalDateTime.now());
		Notice updateNotice = reqNoticeUpdateDto.newNoticeInstance();
		Long noticeId = saveNotice.getNoticeId();

		this.noticeServiceDecoratorImpl.update(noticeId, updateNotice, new ArrayList<>(), TestLocalFileUploadImpl.fileDelete);

		Optional<NoticeVo> findNoticePg = this.noticeServicePgImpl.findById(noticeId);
		Optional<NoticeVo> findNoticeMongoDB = this.noticeServiceMongoDBImpl.findById(noticeId);

		logger.info("seq : {}, updateNotice Title : {} [PG] findNotice Title : {}  [MongoDB] findNotice Title :{}"
				, noticeId
				, updateNotice.getTitle()
				, findNoticePg.get().getTitle()
				, findNoticeMongoDB.get().getTitle());

		Assertions.assertTrue(updateNotice.getTitle().equals(findNoticePg.get().getTitle())
				&& updateNotice.getTitle().equals(findNoticeMongoDB.get().getTitle()));
	}

	@Test
	@DisplayName("[Decorator] Notice 정상 삭제 검증")
	void testCase3() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		this.noticeServiceDecoratorImpl.save(saveNotice, new ArrayList<>());
		this.noticeServiceDecoratorImpl.delete(saveNotice.getNoticeId());

		Optional<NoticeVo> findNoticePg = this.noticeServicePgImpl.findById(saveNotice.getNoticeId());
		Optional<NoticeVo> findNoticeMongoDB = this.noticeServiceMongoDBImpl.findById(saveNotice.getNoticeId());

		Assertions.assertTrue(findNoticePg.isEmpty()
				&& findNoticeMongoDB.isEmpty());
	}

	@Test
	@DisplayName("[Decorator] Notice 파일 업로드 정상 저장 검증")
	void testCase4() {
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

		this.noticeServiceDecoratorImpl.save(saveNotice, fileUploads);

		Optional<NoticeVo> findNotice = this.noticeServiceDecoratorImpl.findById(saveNotice.getNoticeId());

		logger.info("findNotice seq : {}, saveNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, saveNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertEquals(saveNotice.getTitle(), findNotice.get().getTitle());
	}

	@Test
	@DisplayName("[Decorator] Notice 파일 업로드 없음 정상 저장 검증")
	void testCase4_1() {
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

		this.noticeServiceDecoratorImpl.save(saveNotice, fileUploads);

		Optional<NoticeVo> findNotice = this.noticeServiceDecoratorImpl.findById(saveNotice.getNoticeId());

		logger.info("findNotice seq : {}, saveNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, saveNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertEquals(saveNotice.getTitle(), findNotice.get().getTitle());
	}

	@Test
	@DisplayName("[Decorator] Notice 파일 업로드 정상 삭제 검증")
	void testCase5() throws IOException {
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

		this.noticeServiceDecoratorImpl.save(saveNotice, fileUploads);
		this.noticeServiceDecoratorImpl.delete(saveNotice.getNoticeId());

		Optional<NoticeVo> findNoticePg = this.noticeServicePgImpl.findById(saveNotice.getNoticeId());
		Optional<NoticeVo> findNoticeMongoDB = this.noticeServiceMongoDBImpl.findById(saveNotice.getNoticeId());

		Assertions.assertTrue(findNoticePg.isEmpty()
				&& findNoticeMongoDB.isEmpty());
	}

	@Test
	@DisplayName("[Decorator] Notice 파일 업로드 0개이상 -> 0개이상 정상 수정 검증")
	void testCase6() throws IOException {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		List<File> saveFiles = Arrays.asList(new File("src/test/resources/file/upload_1.txt"));

		List<FileUpload> saveFileUploads = new ArrayList<>();
		for(File file : saveFiles) {
			saveFileUploads.add(new TestLocalFileUploadImpl(file));
		}

		this.noticeServiceDecoratorImpl.save(saveNotice, saveFileUploads);

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

		this.noticeServiceDecoratorImpl.update(saveNotice.getNoticeId()
				, updateNotice
				, fileUploads
				, TestLocalFileUploadImpl.fileDelete);

		Optional<NoticeVo> findNotice = this.noticeServiceDecoratorImpl.findById(saveNotice.getNoticeId());

		logger.info("findNotice seq : {}, saveNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, saveNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertEquals(findNotice.get().getNoticeFileAttachVos().size(), updateFiles.size());
	}

	@Test
	@DisplayName("[Decorator] Notice 파일 업로드 0개 -> 0개 이상 정상 수정 검증")
	void testCase7() {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		this.noticeServiceDecoratorImpl.save(saveNotice, new ArrayList<>());

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

		this.noticeServiceDecoratorImpl.update(saveNotice.getNoticeId()
				, updateNotice
				, fileUploads
				, TestLocalFileUploadImpl.fileDelete);

		Optional<NoticeVo> findNotice = this.noticeServiceDecoratorImpl.findById(saveNotice.getNoticeId());

		logger.info("findNotice seq : {}, saveNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, saveNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertTrue(findNotice.get().getNoticeFileAttachVos().size() > 0);
	}

	@Test
	@DisplayName("[Decorator] Notice 파일 업로드 1개 이상-> 0개 정상 수정 검증")
	void testCase8() throws IOException {
		Notice saveNotice = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		List<File> saveFiles = Arrays.asList(new File("src/test/resources/file/upload_1.txt"));

		List<FileUpload> fileUploads = new ArrayList<>();
		for(File file : saveFiles) {
			fileUploads.add(new TestLocalFileUploadImpl(file));
		}

		this.noticeServiceDecoratorImpl.save(saveNotice, fileUploads);

		Notice updateNotice = new ReqNoticeSaveDto("title_update"
				, "contents_update"
				, "regId_update"
				, LocalDateTime.now()
				, LocalDateTime.now())
			.newNoticeInstance();

		this.noticeServiceDecoratorImpl.update(saveNotice.getNoticeId()
				, updateNotice
				, new ArrayList<>()
				, TestLocalFileUploadImpl.fileDelete);

		Optional<NoticeVo> findNotice = this.noticeServiceDecoratorImpl.findById(saveNotice.getNoticeId());

		logger.info("findNotice seq : {}, saveNotice Title : {}, findNotice Title : {}, findNotice viewCount : {}"
				, findNotice.get().getNoticeId()
				, saveNotice.getTitle()
				, findNotice.get().getTitle()
				, findNotice.get().getViewCount());

		Assertions.assertTrue(findNotice.get().getNoticeFileAttachVos().size() == 0);
	}
}
