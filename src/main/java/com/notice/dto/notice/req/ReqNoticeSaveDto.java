package com.notice.dto.notice.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.notice.common.http.ProcessCode;
import com.notice.domain.Notice;
import com.notice.exception.ProcessException;
import com.notice.spring.annotation.JsonLocalDateTime;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <pre>
 *     공지사항 저장 요청 DTO
 * </pre>
 */
public class ReqNoticeSaveDto {
    private final String title;
    private final String contents;
    private final String regId;

    @JsonLocalDateTime
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private final LocalDateTime noticeStartDate;

    @JsonLocalDateTime
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private final LocalDateTime noticeEndDate;

    public ReqNoticeSaveDto(@JsonProperty("title") String title
                , @JsonProperty("contents") String contents
                , @JsonProperty("regId") String regId
                , @JsonProperty("noticeStartDate") LocalDateTime noticeStartDate
                , @JsonProperty("noticeEndDate") LocalDateTime noticeEndDate) {

        this.title = title;
        this.contents = contents;
        this.regId = regId;
        this.noticeStartDate = noticeStartDate;
        this.noticeEndDate = noticeEndDate;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getRegId() {
        return regId;
    }

    public LocalDateTime getNoticeStartDate() {
        return noticeStartDate;
    }

    public LocalDateTime getNoticeEndDate() {
        return noticeEndDate;
    }

    /**
     * <pre>
     *    공지사항 저장 DTO -> {@link Notice} 도메인 객체를 생성 반환하는 편의 메서드
     * </pre>
     *
     * @return 공지사항 저장에 사용되는 {@link Notice} 객체
     */
    public Notice newNoticeInstance() {

        return new Notice(Optional.ofNullable(this.title)
                .filter(v -> v.replace(" ", "").length() > 0)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK))
            , Optional.ofNullable(this.contents)
                .filter(v -> v.replace(" ", "").length() > 0)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK))
            , Optional.ofNullable(this.regId)
                .filter(v -> v.replace(" ", "").length() > 0)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK))
            , Optional.ofNullable(this.noticeStartDate)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK))
            , Optional.ofNullable(this.noticeEndDate)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK)));
    }
}
