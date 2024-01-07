package com.notice.dto.notice.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.notice.spring.annotation.JsonLocalDateTime;
import com.notice.vo.notice.NoticeVo;

import java.time.LocalDateTime;

/**
 * <pre>
 *     공지사항 조회 API 응답에 사용되는 DTO
 * </pre>
 */
public class RespNoticeDto {
    private final String title;
    private final String contents;

    @JsonLocalDateTime
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private final LocalDateTime regDate;

    private final Integer viewCount;
    private final String regId;

    public RespNoticeDto(String title
            , String contents
            , LocalDateTime regDate
            , Integer viewCount
            , String regId) {

        this.title = title;
        this.contents = contents;
        this.regDate = regDate;
        this.viewCount = viewCount;
        this.regId = regId;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public String getRegId() {
        return regId;
    }

    /**
     * <pre>
     *     조회된 공지사항 정보를 현재 객체로 변환하는 편의 메서드
     * </pre>
     *
     * @param noticeVo 공지사항 조회 데이터
     * @return
     */
    public static RespNoticeDto newInstance(NoticeVo noticeVo) {

        return new RespNoticeDto(noticeVo.getTitle()
                , noticeVo.getContents()
                , noticeVo.getRegDate()
                , noticeVo.getViewCount()
                , noticeVo.getRegId());
    }
}
