package com.notice.test.service;

import com.notice.common.file.FileDelete;
import com.notice.common.file.FileUpload;
import com.notice.common.file.entity.UploadInfo;
import com.notice.common.http.ProcessCode;
import com.notice.domain.Notice;
import com.notice.domain.NoticeFileAttach;
import com.notice.exception.ProcessException;
import com.notice.repository.mongodb.notice.NoticeMongoDBRepository;
import com.notice.repository.rdb.notice.NoticeRepository;
import com.notice.service.notice.NoticeService;
import com.notice.service.notice.NoticeServiceDecorator;
import com.notice.test.common.file.TestLocalFileUploadImpl;
import com.notice.vo.notice.NoticeFileAttachVo;
import com.notice.vo.notice.NoticeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     테스트 게시물 프로세스의 쓰기(RDB)/읽기(Nosql) 구현체들을 활용하여 전체 프로세스를 관리하는 구현체
 *
 *     테스트 구현체들을 사용.
 * </pre>
 */
public class TestNoticeServiceDecoratorImpl implements NoticeServiceDecorator {
    private final static Logger logger = LoggerFactory.getLogger(TestNoticeServiceDecoratorImpl.class);
    private final static NoticeServiceDecorator.ErrorNotification readErrorNotification = e -> logger.error("[{}] Notice Read Error!!", e.toString());
    private final static NoticeServiceDecorator.ErrorNotification updateErrorNotification = e -> logger.error("[{}] Notice Update Error!!", e.toString());

    private final NoticeService testNoticeServicePgImpl;
    private final NoticeService testNoticeServiceMongoDBImpl;

    private final NoticeRepository testNoticeRepositoryImpl;
    private final NoticeMongoDBRepository testNoticeMongoDBRepositoryImpl;

    public TestNoticeServiceDecoratorImpl(NoticeService testNoticeServicePgImpl
            , NoticeService testNoticeServiceMongoDBImpl

            , NoticeRepository testNoticeRepositoryImpl
            , NoticeMongoDBRepository testNoticeMongoDBRepositoryImpl) {

        this.testNoticeServicePgImpl = testNoticeServicePgImpl;
        this.testNoticeServiceMongoDBImpl = testNoticeServiceMongoDBImpl;

        this.testNoticeRepositoryImpl = testNoticeRepositoryImpl;
        this.testNoticeMongoDBRepositoryImpl = testNoticeMongoDBRepositoryImpl;
    }

    @Override
    public void save(Notice notice, List<FileUpload> fileUploads) {

        this.fileUpload(notice, fileUploads);
        this.save(notice);
    }

    @Override
    public void save(Notice notice) {

        this.testNoticeServicePgImpl.save(notice);
        this.testNoticeServiceMongoDBImpl.save(notice);
    }

    @Override
    public void update(Long noticeId, Notice notice, List<FileUpload> fileUploads, FileDelete fileDelete) {

        this.testNoticeServiceMongoDBImpl.findById(noticeId)
                .ifPresent(v -> {
                    for(NoticeFileAttachVo noticeFileAttachVo : v.getNoticeFileAttachVos()) {
                        try {
                            fileDelete.delete(new UploadInfo(noticeFileAttachVo.getOriginalFileName()
                                    , noticeFileAttachVo.getAttachFileName()
                                    , noticeFileAttachVo.getAttachFilePath()));
                        } catch (IOException e) {
                            throw new ProcessException(ProcessCode.NoticeCode.FILE_DELETE_ERROR);
                        }
                    }
                });

        this.fileUpload(notice, fileUploads);
        this.update(noticeId, notice);
    }

    @Override
    public void update(Long noticeId, Notice notice) {

        this.testNoticeServicePgImpl.update(noticeId, notice);
        this.testNoticeServiceMongoDBImpl.update(noticeId, notice);
    }

    private void fileUpload(Notice notice, List<FileUpload> fileUploads) {

        for(FileUpload fileUpload : fileUploads) {
            try {
                UploadInfo uploadInfo = fileUpload.upload();

                notice.addNoticeFileAttach(new NoticeFileAttach(uploadInfo.originalFileName()
                        , uploadInfo.uploadFileName()
                        , uploadInfo.uploadPath()));
            } catch (IOException e) {
                throw new ProcessException(ProcessCode.NoticeCode.FILE_UPLOAD_ERROR);
            }
        }
    }

    @Override
    public void delete(Long noticeId) {
        this.delete(noticeId, TestLocalFileUploadImpl.fileDelete);
    }

    @Override
    public void delete(Long noticeId, FileDelete fileDelete) {

        this.testNoticeServiceMongoDBImpl.findById(noticeId)
                .ifPresent(v -> {
                    for(NoticeFileAttachVo noticeFileAttachVo : v.getNoticeFileAttachVos()) {
                        try {
                            fileDelete.delete(new UploadInfo(noticeFileAttachVo.getOriginalFileName()
                                    , noticeFileAttachVo.getAttachFileName()
                                    , noticeFileAttachVo.getAttachFilePath()));
                        } catch (IOException e) {
                            throw new ProcessException(ProcessCode.NoticeCode.FILE_DELETE_ERROR);
                        }
                    }
                });
        this.testNoticeServicePgImpl.delete(noticeId);
        this.testNoticeServiceMongoDBImpl.delete(noticeId);
    }

    @Override
    public Optional<NoticeVo> findById(Long noticeId) {

        return this.findById(noticeId, readErrorNotification, updateErrorNotification);
    }

    @Override
    public Optional<NoticeVo> findById(Long seq
            , ErrorNotification readErrorNotification
            , ErrorNotification updateErrorNotification) {

        try {
            this.testNoticeRepositoryImpl.updateViewCount(seq);
            this.testNoticeMongoDBRepositoryImpl.updateViewCount(seq);
        } catch (Exception e) {
            updateErrorNotification.notification(e);
        } finally {
            try {
                logger.info("Notice Read!!");

                return this.testNoticeServiceMongoDBImpl.findById(seq);
            } catch (Exception e) {
                readErrorNotification.notification(e);

                return this.testNoticeServicePgImpl.findById(seq);
            }
        }
    }
}
