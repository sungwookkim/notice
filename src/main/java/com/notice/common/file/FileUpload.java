package com.notice.common.file;

import com.notice.common.file.entity.UploadInfo;

import java.io.IOException;

/**
 * 파일 업로드 파일 저장 인터페이스
 */
public interface FileUpload {

    /**
     * <pre>
     *     업로드된 파일을 저장 처리하는 메서드
     * </pre>
     *
     * @return 업르도된 파일 정보
     * @throws IOException
     */
    UploadInfo upload() throws IOException;
}
