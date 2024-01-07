package com.notice.common.file;

import com.notice.common.file.entity.UploadInfo;
import com.notice.common.http.ProcessCode;
import com.notice.exception.ProcessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * <pre>
 *     실행 중이 환경에 파일을 저장하는 구현체. OS 환경의 임시폴더에 저장.
 * </pre>
 */
public class LocalFileUploadImpl implements FileUpload {
    /*
    업로드된 파일을 삭제 처리 하는 편의 객체.
     */
    public final static FileDelete fileDelete = uploadInfo -> {

        File deleteFile = new File(uploadInfo.uploadPath() + uploadInfo.uploadFileName());

        if(deleteFile.exists() && !deleteFile.delete()) {
            throw new IOException("Delete File Exception");
        }
    };

    private final MultipartFile multipartFile;

    public LocalFileUploadImpl(MultipartFile multipartFile) {

        this.multipartFile = multipartFile;
    }

    /**
     * <pre>
     *     Spring의 {@link MultipartFile} 객체를 파일로 저장
     * </pre>
     *
     * @return
     * @throws IOException
     */
    @Override
    public UploadInfo upload() throws IOException {

        String originalFileName = Optional.ofNullable(multipartFile.getOriginalFilename())
                .filter(v -> v.replace(" ", "").length() > 0)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK));
        String uploadFileName = UUID.randomUUID().toString().replace("-", "")
                + "_" + originalFileName;
        String uploadFilePath = System.getProperty("java.io.tmpdir");

        File convertedFile = new File(uploadFilePath + uploadFileName);
        multipartFile.transferTo(convertedFile);

        return new UploadInfo(originalFileName, uploadFileName, uploadFilePath);
    }
}
