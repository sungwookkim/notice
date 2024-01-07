package com.notice.test;

import com.notice.common.http.ProcessCode;
import com.notice.dto.notice.req.ReqNoticeSaveDto;
import com.notice.dto.notice.req.ReqNoticeUpdateDto;
import com.notice.helper.JsonHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <pre>
 *     {@link MockMvc}를 활용한 실제 API 테스트
 * </pre>
 */
@SpringBootTest
@AutoConfigureMockMvc
class NoticeApplicationTestsSpringRest {
	private final static Logger logger = LoggerFactory.getLogger(NoticeApplicationTestsSpringRest.class);

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("[POST] /v1/notice - 첨부파일 첨부")
	void testCase1() throws Exception {
		ReqNoticeSaveDto reqNoticeSaveDto = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now());
		MockMultipartFile file1 = new MockMultipartFile("fileAttach"
				, "upload_1.txt"
				, "multipart/form-data"
				, new FileInputStream(new File("src/test/resources/file/upload_1.txt")));
		MockMultipartFile noticeSaveDto = new MockMultipartFile("noticeInfo"
				, null
				, "plain/text"
				, JsonHelper.Singleton.getInstance().getObjectMapper().writeValueAsString(reqNoticeSaveDto).getBytes());
		MockMultipartFile method = new MockMultipartFile("method"
				, null
				, "plain/text"
				, "post".getBytes());

		mockMvc.perform(multipart("/v1/notice")
						.file(file1)
						.file(noticeSaveDto)
						.file(method))
				.andExpect(status().isOk())
				.andExpect(jsonPath("httpStatus.code").value(HttpStatus.OK.value()))
				.andExpect(jsonPath("processCode").value(ProcessCode.Common.SUCCESS.getCode()))
				.andDo(print());
	}

	@Test
	@DisplayName("[POST] /v1/notice - 첨부파일 미첨부")
	void testCase2() throws Exception {
		ReqNoticeSaveDto reqNoticeSaveDto = new ReqNoticeSaveDto("title"
				, "contents"
				, "regId"
				, LocalDateTime.now()
				, LocalDateTime.now());
		MockMultipartFile noticeSaveDto = new MockMultipartFile("noticeInfo"
				, null
				, "plain/text"
				, JsonHelper.Singleton.getInstance().getObjectMapper().writeValueAsString(reqNoticeSaveDto).getBytes());
		MockMultipartFile method = new MockMultipartFile("method"
				, null
				, "plain/text"
				, "post".getBytes());

		mockMvc.perform(multipart("/v1/notice")
						.file(noticeSaveDto)
						.file(method))
				.andExpect(status().isOk())
				.andExpect(jsonPath("httpStatus.code").value(HttpStatus.OK.value()))
				.andExpect(jsonPath("processCode").value(ProcessCode.Common.SUCCESS.getCode()))
				.andDo(print());
	}

	/*
	아래 GET 테스트를 하기 앞서 testCase1, testCase2 메서드를 상황에 맞게
	선행으로 실행 후 noticeId를 활용 해야 함.
 	*/
	@Test
	@DisplayName("[GET] /v1/notice - 조회 가능한 게시물")
	void testCase3() throws Exception {
		String noticeId = "96";

		mockMvc.perform(get("/v1/notice/" + noticeId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("httpStatus.code").value(HttpStatus.OK.value()))
				.andExpect(jsonPath("processCode").value(ProcessCode.Common.SUCCESS.getCode()))
				.andDo(print());
	}

	@Test
	@DisplayName("[GET] /v1/notice - 존재 하지 않는 게시물")
	void testCase4() throws Exception {
		String noticeId = "-1";

		mockMvc.perform(get("/v1/notice/" + noticeId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("httpStatus.code").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
				.andExpect(jsonPath("processCode").value(ProcessCode.NoticeCode.NOTICE_DOES_NOT_EXIST.getCode()))
				.andDo(print());
	}

	/*
	아래 DELETE 테스트를 하기 앞서 testCase1, testCase2 메서드를 상황에 맞게
	선행으로 실행 후 noticeId를 활용 해야 함.
	 */
	@Test
	@DisplayName("[DELETE] /v1/notice - 조회 가능한 게시물")
	void testCase5() throws Exception {
		String noticeId = "85";

		mockMvc.perform(delete("/v1/notice/" + noticeId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("httpStatus.code").value(HttpStatus.OK.value()))
				.andExpect(jsonPath("processCode").value(ProcessCode.Common.SUCCESS.getCode()))
				.andDo(print());
	}

	@Test
	@DisplayName("[DELETE] /v1/notice - 존재 하지 않는 게시물")
	void testCase6() throws Exception {
		String noticeId = "-1";

		mockMvc.perform(delete("/v1/notice/" + noticeId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("httpStatus.code").value(HttpStatus.OK.value()))
				.andExpect(jsonPath("processCode").value(ProcessCode.Common.SUCCESS.getCode()))
				.andDo(print());
	}

	@Test
	@DisplayName("[DELETE] /v1/notice - 조회 가능한 게시물(파일 첨부)")
	void testCase7() throws Exception {
		String noticeId = "9";

		mockMvc.perform(delete("/v1/notice/" + noticeId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("httpStatus.code").value(HttpStatus.OK.value()))
				.andExpect(jsonPath("processCode").value(ProcessCode.Common.SUCCESS.getCode()))
				.andDo(print());
	}

	@Test
	@DisplayName("[DELETE] /v1/notice - 조회 가능한 게시물(파일 미첨부)")
	void testCase8() throws Exception {
		String noticeId = "10";

		mockMvc.perform(delete("/v1/notice/" + noticeId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("httpStatus.code").value(HttpStatus.OK.value()))
				.andExpect(jsonPath("processCode").value(ProcessCode.Common.SUCCESS.getCode()))
				.andDo(print());
	}

	/*
	아래 PUT 테스트를 하기 앞서 testCase1, testCase2 메서드를 상황에 맞게
	선행으로 실행 후 noticeId를 활용 해야 함.
	 */
	@Test
	@DisplayName("[PUT] /v1/notice")
	void testCase9() throws Exception {
		Long noticeId = 3L;

		ReqNoticeUpdateDto reqNoticeUpdateDto = new ReqNoticeUpdateDto(noticeId
				, "title_update"
				, "contents_update"
				, LocalDateTime.now()
				, LocalDateTime.now().plusDays(1));
		MockMultipartFile file1 = new MockMultipartFile("fileAttach"
				, "upload_1.txt"
				, "multipart/form-data"
				, new FileInputStream("src/test/resources/file/upload_1.txt"));
		MockMultipartFile file2 = new MockMultipartFile("fileAttach"
				, "upload_2.txt"
				, "multipart/form-data"
				, new FileInputStream("src/test/resources/file/upload_2.txt"));
		MockMultipartFile noticeUpdateDto = new MockMultipartFile("noticeInfo"
				, null
				, "plain/text"
				, JsonHelper.Singleton.getInstance().getObjectMapper().writeValueAsString(reqNoticeUpdateDto).getBytes());
		MockMultipartFile method = new MockMultipartFile("method"
				, null
				, "plain/text"
				, "put".getBytes());

		mockMvc.perform(multipart("/v1/notice")
						.file(file1)
						.file(file2)
						.file(noticeUpdateDto)
						.file(method))
				.andExpect(status().isOk())
				.andExpect(jsonPath("httpStatus.code").value(HttpStatus.OK.value()))
				.andExpect(jsonPath("processCode").value(ProcessCode.Common.SUCCESS.getCode()))
				.andDo(print());
	}
}
