package com.notice.entity.mongodb;

/**
 * <pre>
 *     공지사항 게시물 파일첨부 엔티티
 * </pre>
 */
public class NoticeMongoDBFileAttach {
    private final Long noticeFileAttachId;
    private final String originalFileName;
    private final String attachFileName;
    private final String attachFilePath;

    public NoticeMongoDBFileAttach(Long noticeFileAttachId
            , String originalFileName
            , String attachFileName
            , String attachFilePath) {

        this.noticeFileAttachId = noticeFileAttachId;
        this.originalFileName = originalFileName;
        this.attachFileName = attachFileName;
        this.attachFilePath = attachFilePath;
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
}
