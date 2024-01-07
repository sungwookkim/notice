package com.notice.domain;

import jakarta.persistence.*;

import java.util.Optional;

/**
 * <pre>
 *     공지사항 파일첨부 도메인
 * </pre>
 */
@Entity
@Table(name = "notice_file_attach")
public class NoticeFileAttach {
    @Id
    @Column(name = "notice_file_attach_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeFileAttachId;

    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;

    @Column(name = "attach_file_name", nullable = false)
    private String attachFileName;

    @Column(name = "attach_file_path", nullable = false)
    private String attachFilePath;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    public NoticeFileAttach() {}

    public NoticeFileAttach(String originalFileName
            , String attachFileName
            , String attachFilePath) {

        this.originalFileName = originalFileName;
        this.attachFileName = attachFileName;
        this.attachFilePath = attachFilePath;
    }

    public NoticeFileAttach(String originalFileName
            , String attachFileName
            , String attachFilePath
            , Notice notice) {

        this.originalFileName = originalFileName;
        this.attachFileName = attachFileName;
        this.attachFilePath = attachFilePath;
        this.notice = notice;
    }

    public Long getNoticeFileAttachId() {
        return noticeFileAttachId;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public String getAttachFilePath() {
        return attachFilePath;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {

        Optional.ofNullable(this.notice)
                .ifPresent(v -> {
                    v.getNoticeFileAttach().remove(this);
                });

        this.notice = notice;

        if(!notice.getNoticeFileAttach().contains(this)) {
            notice.getNoticeFileAttach().add(this);
        }
    }
}
