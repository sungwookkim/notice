package com.notice.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     공지사항 도메인
 * </pre>
 */
@Entity
@Table(name = "notice")
public class Notice {
    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "reg_id", nullable = false)
    private String regId;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Column(name = "notice_start_date", nullable = false)
    private LocalDateTime noticeStartDate;

    @Column(name = "notice_end_date", nullable = false)
    private LocalDateTime noticeEndDate;

    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate;

    @OneToMany(mappedBy = "notice", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<NoticeFileAttach> noticeFileAttach = new ArrayList<>();

    public Notice() {}

    public Notice(String title
            , String contents
            , String regId
            , LocalDateTime noticeStartDate
            , LocalDateTime noticeEndDate) {

        this.title = title;
        this.contents = contents;
        this.regId = regId;
        this.noticeStartDate = noticeStartDate;
        this.noticeEndDate = noticeEndDate;
        this.regDate = LocalDateTime.now();
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

    public List<NoticeFileAttach> getNoticeFileAttach() {
        return noticeFileAttach;
    }

    public Notice title(String title) {
        this.title = title;
        return this;
    }

    public Notice contents(String contents) {
        this.contents = contents;
        return this;
    }

    public Notice noticeStartDate(LocalDateTime noticeStartDate) {
        this.noticeStartDate = noticeStartDate;
        return this;
    }

    public Notice noticeEndDate(LocalDateTime noticeEndDate){
        this.noticeEndDate = noticeEndDate;
        return this;
    }

    public void addNoticeFileAttach(NoticeFileAttach noticeFileAttach) {

        if(!this.noticeFileAttach.contains(noticeFileAttach)) {
            this.noticeFileAttach.add(noticeFileAttach);
        }

        if(noticeFileAttach.getNotice() != this) {
            noticeFileAttach.setNotice(this);
        }
    }
}
