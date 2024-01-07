package com.notice.common.file;

import com.notice.common.file.entity.UploadInfo;

import java.io.IOException;

/**
 * 파일 업로드 삭제 인터페이스
 */
public interface FileDelete {

    /**
     * <pre>
     *     업로드된 파일을 삭체 처리 하는 메서드
     * </pre>
     *
     * @param uploadInfo 삭제 처리에 필요한 파일 정보
     * @throws IOException
     */
    void delete(UploadInfo uploadInfo) throws IOException;
}
