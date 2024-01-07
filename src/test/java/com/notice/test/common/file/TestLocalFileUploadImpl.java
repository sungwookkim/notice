package com.notice.test.common.file;

import com.notice.common.file.FileDelete;
import com.notice.common.file.FileUpload;
import com.notice.common.file.entity.UploadInfo;
import com.notice.common.http.ProcessCode;
import com.notice.exception.ProcessException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

/**
 * <pre>
 *     테스트용 첨부파일 클래스
 * </pre>
 */
public class TestLocalFileUploadImpl implements FileUpload {
    public final static FileDelete fileDelete = uploadInfo -> {

        File deleteFile = new File(uploadInfo.uploadPath() + uploadInfo.uploadFileName());

        if(deleteFile.exists() && !deleteFile.delete()) {
            throw new IOException("Delete File Exception");
        }
    };

    private final File file;

    public TestLocalFileUploadImpl(File file) {
        this.file = file;
    }

    /**
     * <pre>
     *     테스트 첨부파일은 src/test/resources/upload/ 폴더에 저장.
     * </pre>
     *
     * @return
     * @throws IOException
     */
    @Override
    public UploadInfo upload() throws IOException {

        String originalFileName = Optional.ofNullable(file.getName())
                .filter(v -> v.replace(" ", "").length() > 0)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK));
        String uploadFileName = UUID.randomUUID().toString().replace("-", "")
                + "_" + originalFileName;
        String uploadFilePath = "src/test/resources/upload/";

        try {
            String content = Files.readString(file.toPath());

            Files.write(Paths.get(uploadFilePath + uploadFileName), content.getBytes());

        } catch (IOException e) {
            throw e;
        }

        return new UploadInfo(originalFileName, uploadFileName, uploadFilePath);
    }
}
