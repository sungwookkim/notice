package com.notice.entity.mongodb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.notice.domain.Notice;
import com.notice.domain.NoticeFileAttach;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     공지사항 게시물 엔티티
 *
 *     게시물 조회 시 RDB를 사용 하지 않고 NoSql을 기본으로 사용된다.
 * </pre>
 */
@Document(collection = "Notice")
public class NoticeMongoDB {
    private final Long noticeId;
    private final String title;
    private final String contents;
    private final String regId;
    private final Integer viewCount;
    private final List<NoticeMongoDBFileAttach> noticeMongoDBFileAttaches;
    private final LocalDateTime noticeStartDate;
    private final LocalDateTime noticeEndDate;
    private final LocalDateTime regDate;

    public NoticeMongoDB(@JsonProperty("noticeId") Long noticeId
            , @JsonProperty("title") String title
            , @JsonProperty("contents") String contents
            , @JsonProperty("regId") String regId
            , @JsonProperty("viewCount") Integer viewCount
            , @JsonProperty("noticeMongoDBFileAttaches") List<NoticeMongoDBFileAttach> noticeMongoDBFileAttaches
            , @JsonProperty("noticeStartDate") LocalDateTime noticeStartDate
            , @JsonProperty("noticeEndDate") LocalDateTime noticeEndDate
            , @JsonProperty("regDate") LocalDateTime regDate) {

        this.noticeId = noticeId;
        this.title = title;
        this.contents = contents;
        this.regId = regId;
        this.viewCount = viewCount;
        this.noticeMongoDBFileAttaches = noticeMongoDBFileAttaches;
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

    public List<NoticeMongoDBFileAttach> getNoticeMongoDBFileAttaches() {
        return noticeMongoDBFileAttaches;
    }

    /**
     * <pre>
     *     {@link Notice} 객체를 현재 객체로 생성하는 편의 메서드
     * </pre>
     *
     * @param notice 저장 및 수정 시 사용되는 객체
     * @return
     */
    public static NoticeMongoDB newInstance(Notice notice) {

        List<NoticeMongoDBFileAttach> noticeMongoDBFileAttachList = new ArrayList<>();

        for(NoticeFileAttach noticeFileAttach : notice.getNoticeFileAttach()) {
            noticeMongoDBFileAttachList.add(new NoticeMongoDBFileAttach(noticeFileAttach.getNoticeFileAttachId()
                    , noticeFileAttach.getOriginalFileName()
                    , noticeFileAttach.getAttachFileName()
                    , noticeFileAttach.getAttachFilePath()));
        }

        return new NoticeMongoDB(notice.getNoticeId()
                , notice.getTitle()
                , notice.getContents()
                , notice.getRegId()
                , notice.getViewCount()
                , noticeMongoDBFileAttachList
                , notice.getNoticeStartDate()
                , notice.getNoticeEndDate()
                , notice.getRegDate());
    }
}
