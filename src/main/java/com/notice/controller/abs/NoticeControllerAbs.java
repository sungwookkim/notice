package com.notice.controller.abs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.notice.common.file.FileUpload;
import com.notice.common.file.LocalFileUploadImpl;
import com.notice.common.http.ProcessCode;
import com.notice.common.http.RestResponseResult;
import com.notice.domain.Notice;
import com.notice.dto.notice.req.ReqNoticeSaveDto;
import com.notice.dto.notice.req.ReqNoticeUpdateDto;
import com.notice.dto.notice.resp.RespNoticeDto;
import com.notice.exception.ProcessException;
import com.notice.helper.JsonHelper;
import com.notice.service.notice.NoticeServiceDecorator;
import com.notice.vo.notice.NoticeVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     공지사항 API의 프로세스를 제어하는 추상 클래스.
 *
 *     해당 추상 클래스에선 {@link GetMapping}와 같이 API 기능들을 구현한다.
 *     그 후 구현체에선
 *
 *      @RestController
 *      @RequestMapping("v1/notice")
 *      public class NoticeController extends NoticeControllerAbs {
 *
 *      public NoticeController(NoticeServiceDecorator noticeServiceDecoratorImpl) {
 *          ...
 *      }
 *
 *      와 같이 {@link RequestMapping}를 사용하여 버전링을 한다. 이후 새로운 버전이 필요한 경우엔
 *      추상 클래스를 상속 받고 재 구현이 필요한 기능의 메서드만 오버라이드를 통해 기능을 변경하거나
 *      해당 구현체에 현재 버전의 기능을 구현하여 기존 버전의 API와 격리를 하도록 한다.
 * }
 * </pre>
 */
public abstract class NoticeControllerAbs {
    private final NoticeServiceDecorator noticeServiceDecoratorImpl;

    public NoticeControllerAbs(NoticeServiceDecorator noticeServiceDecoratorImpl) {

        this.noticeServiceDecoratorImpl = noticeServiceDecoratorImpl;
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<RestResponseResult<RespNoticeDto>> findNotice(@PathVariable("noticeId") Long noticeId) {

        NoticeVo noticeVo = this.noticeServiceDecoratorImpl.findById(noticeId)
                .orElseThrow(() -> new ProcessException(ProcessCode.NoticeCode.NOTICE_DOES_NOT_EXIST));
        RespNoticeDto respNoticeDto = RespNoticeDto.newInstance(noticeVo);

        return new ResponseEntity<>(new RestResponseResult<>(respNoticeDto), HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<RestResponseResult<String>> upsert(@RequestPart(name = "method") String method
            , @RequestPart(name = "noticeInfo") String noticeInfo
            , @RequestPart(name = "fileAttach", required = false) List<MultipartFile> multipartFiles) throws JsonProcessingException {

        List<MultipartFile> noticeAttachFiles = Optional.ofNullable(multipartFiles).orElseGet(ArrayList::new);
        List<FileUpload> fileUploads = new ArrayList<>();
        for(MultipartFile noticeAttachFile : noticeAttachFiles) {
            fileUploads.add(new LocalFileUploadImpl(noticeAttachFile));
        }

        if("POST".equals(method.toUpperCase())) {
            Notice notice = JsonHelper.Singleton.getInstance().getObjectMapper().readValue(noticeInfo, ReqNoticeSaveDto.class)
                    .newNoticeInstance();

            this.noticeServiceDecoratorImpl.save(notice, fileUploads);
        } else if("PUT".equals(method.toUpperCase())) {
            ReqNoticeUpdateDto reqNoticeUpdateDto = JsonHelper.Singleton.getInstance().getObjectMapper().readValue(noticeInfo, ReqNoticeUpdateDto.class);
            Notice notice = reqNoticeUpdateDto.newNoticeInstance();

            this.noticeServiceDecoratorImpl.update(reqNoticeUpdateDto.getNoticeId()
                    , notice
                    , fileUploads
                    , LocalFileUploadImpl.fileDelete);
        }

        return new ResponseEntity<>(RestResponseResult.success(), HttpStatus.OK);
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<RestResponseResult<String>> delete(@PathVariable("noticeId") Long noticeId) {

        this.noticeServiceDecoratorImpl.delete(noticeId);

        return new ResponseEntity<>(RestResponseResult.success(), HttpStatus.OK);
    }
}
