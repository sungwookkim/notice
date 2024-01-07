package com.notice.vo.notice;

import java.util.Objects;

public class NoticeFileAttachVo {

    private Long noticeFileAttachId;
    private String originalFileName;
    private String attachFileName;
    private String attachFilePath;

    public NoticeFileAttachVo(Long noticeFileAttachId
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

    @Override
    public boolean equals(final Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final NoticeFileAttachVo value = (NoticeFileAttachVo) o;
        return this.noticeFileAttachId == value.noticeFileAttachId
                && this.originalFileName.equals(value.originalFileName)
                && this.attachFileName.equals(value.attachFileName)
                && this.attachFilePath.equals(value.attachFilePath);
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.noticeFileAttachId
                , this.originalFileName
                , this.attachFileName
                , this.attachFilePath);
    }
}
