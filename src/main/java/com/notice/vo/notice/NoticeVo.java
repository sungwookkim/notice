package com.notice.vo.notice;

import com.notice.domain.Notice;
import com.notice.domain.NoticeFileAttach;
import com.notice.entity.mongodb.NoticeMongoDB;
import com.notice.entity.mongodb.NoticeMongoDBFileAttach;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
 *     공지사항 게시물 vo
 * </pre>
 */
public class NoticeVo {
    private final Long noticeId;
    private final String title;
    private final String contents;
    private final String regId;
    private final Integer viewCount;
    private final List<NoticeFileAttachVo> noticeFileAttachVos;
    private final LocalDateTime noticeStartDate;
    private final LocalDateTime noticeEndDate;
    private final LocalDateTime regDate;

    public NoticeVo(Long noticeId
            , String title
            , String contents
            , String regId
            , Integer viewCount
            , List<NoticeFileAttachVo> noticeFileAttachVos
            , LocalDateTime noticeStartDate
            , LocalDateTime noticeEndDate
            , LocalDateTime regDate) {

        this.noticeId = noticeId;
        this.title = title;
        this.contents = contents;
        this.regId = regId;
        this.viewCount = viewCount;
        this.noticeFileAttachVos = noticeFileAttachVos;
        this.noticeStartDate = noticeStartDate;
        this.noticeEndDate = noticeEndDate;
        this.regDate = regDate;
    }

    public Long getNoticeId() {
        return noticeId;
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

    public Integer getViewCount() {
        return viewCount;
    }

    public LocalDateTime getNoticeStartDate() {
        return noticeStartDate;
    }

    public LocalDateTime getNoticeEndDate() {
        return noticeEndDate;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public List<NoticeFileAttachVo> getNoticeFileAttachVos() {
        return noticeFileAttachVos;
    }

    /**
     * <pre>
     *     {@link Notice} 도메인 객체를 현재 객체로 생성하는 편의 메서드
     *     첨부파일은 빈 값으로 설정하여 반환.
     * </pre>
     *
     * @param notice
     * @return
     */
    public static NoticeVo newNoticeInstance(Notice notice) {
        return new NoticeVo(notice.getNoticeId()
                , notice.getTitle()
                , notice.getContents()
                , notice.getRegId()
                , notice.getViewCount()
                , new ArrayList<>()
                , notice.getNoticeStartDate()
                , notice.getNoticeEndDate()
                , notice.getRegDate());
    }

    /**
     * <pre>
     *     {@link Notice} 도메인 객체를 현재 객체로 생성하는 편의 메서드
     *     첨부파일을 설정하여 반환.
     * </pre>
     *
     * @param notice
     * @return
     */
    public static NoticeVo newNoticeAndAttachInstance(Notice notice) {

        List<NoticeFileAttachVo> noticeFileAttachVoList = new ArrayList<>();
        for(NoticeFileAttach noticeFileAttach : notice.getNoticeFileAttach()) {
            noticeFileAttachVoList.add(new NoticeFileAttachVo(noticeFileAttach.getNoticeFileAttachId()
                    , noticeFileAttach.getOriginalFileName()
                    , noticeFileAttach.getAttachFileName()
                    , noticeFileAttach.getAttachFilePath()));
        }

        return new NoticeVo(notice.getNoticeId()
                , notice.getTitle()
                , notice.getContents()
                , notice.getRegId()
                , notice.getViewCount()
                , noticeFileAttachVoList
                , notice.getNoticeStartDate()
                , notice.getNoticeEndDate()
                , notice.getRegDate());
    }

    /**
     * <pre>
     *     읽기인 {@link NoticeMongoDB} 객체를 현재 객체로 변환하는 편의 메서드
     * </pre>
     *
     * @param noticeMongoDB
     * @return
     */
    public static NoticeVo newNoticeInstance(NoticeMongoDB noticeMongoDB) {

        List<NoticeFileAttachVo> noticeFileAttachVoList = new ArrayList<>();
        for(NoticeMongoDBFileAttach noticeMongoDBFileAttach : noticeMongoDB.getNoticeMongoDBFileAttaches()) {
            noticeFileAttachVoList.add(new NoticeFileAttachVo(noticeMongoDBFileAttach.getNoticeFileAttachId()
                    , noticeMongoDBFileAttach.getOriginalFileName()
                    , noticeMongoDBFileAttach.getAttachFileName()
                    , noticeMongoDBFileAttach.getAttachFilePath()));
        }

        return new NoticeVo(noticeMongoDB.getNoticeId()
                , noticeMongoDB.getTitle()
                , noticeMongoDB.getContents()
                , noticeMongoDB.getRegId()
                , noticeMongoDB.getViewCount()
                , noticeFileAttachVoList
                , noticeMongoDB.getNoticeStartDate()
                , noticeMongoDB.getNoticeEndDate()
                , noticeMongoDB.getRegDate());
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final NoticeVo noticeVo = (NoticeVo) o;
        return this.noticeId == noticeVo.noticeId
                && this.title.equals(noticeVo.title)
                && this.contents.equals(noticeVo.contents)
                && this.regId.equals(noticeVo.regId)
                && this.viewCount == viewCount
                && this.noticeStartDate.isEqual(noticeVo.noticeStartDate)
                && this.noticeEndDate.isEqual(noticeVo.noticeEndDate)
                && this.regDate.isEqual(noticeVo.regDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.noticeId
                , this.title
                , this.contents
                , this.regId
                , this.viewCount
                , this.noticeStartDate
                , this.noticeEndDate
                , this.regDate);
    }
}
