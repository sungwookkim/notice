package com.notice.service.notice.impl;

import com.notice.common.file.FileDelete;
import com.notice.common.file.FileUpload;
import com.notice.common.file.LocalFileUploadImpl;
import com.notice.common.file.entity.UploadInfo;
import com.notice.common.http.ProcessCode;
import com.notice.domain.Notice;
import com.notice.domain.NoticeFileAttach;
import com.notice.exception.ProcessException;
import com.notice.repository.mongodb.notice.NoticeMongoDBRepository;
import com.notice.repository.rdb.notice.NoticeRepository;
import com.notice.service.notice.NoticeService;
import com.notice.service.notice.NoticeServiceDecorator;
import com.notice.vo.notice.NoticeFileAttachVo;
import com.notice.vo.notice.NoticeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     게시물 프로세스의 쓰기(RDB)/읽기(Nosql) 구현체들을 활용하여 전체 프로세스를 관리하는 구현체
 * </pre>
 */
@Service
public class NoticeServiceDecoratorImpl implements NoticeServiceDecorator {
    private final static Logger logger = LoggerFactory.getLogger(NoticeServiceDecoratorImpl.class);
    // 읽기 예외 발생 기본 알림 구현체
    private final static ErrorNotification readErrorNotification = e -> logger.error("[{}] Notice Read Error!!", e.toString());

    // 조회 수 업데이트 예외 발생 기본 알림 구현체
    private final static ErrorNotification updateErrorNotification = e -> logger.error("[{}] Notice Update Error!!", e.toString());

    private final NoticeService noticeServicePgImpl;
    private final NoticeService noticeServiceMongoDBImpl;

    private final NoticeRepository noticeRepository;
    private final NoticeMongoDBRepository noticeMongoDBRepositoryImpl;

    public NoticeServiceDecoratorImpl(NoticeService noticeServicePgImpl
            , NoticeService noticeServiceMongoDBImpl

            , NoticeRepository noticeRepository
            , NoticeMongoDBRepository noticeMongoDBRepositoryImpl) {

        this.noticeServicePgImpl = noticeServicePgImpl;
        this.noticeServiceMongoDBImpl = noticeServiceMongoDBImpl;

        this.noticeRepository = noticeRepository;
        this.noticeMongoDBRepositoryImpl = noticeMongoDBRepositoryImpl;
    }

    @Override
    public void save(Notice notice, List<FileUpload> fileUploads) {

        this.fileUpload(notice, fileUploads);
        this.save(notice);
    }

    @Override
    public void save(Notice notice) {

        this.noticeServicePgImpl.save(notice);
        this.noticeServiceMongoDBImpl.save(notice);
    }

    @Override
    public void update(Long noticeId, Notice notice, List<FileUpload> fileUploads, FileDelete fileDelete) {

        this.noticeServiceMongoDBImpl.findById(noticeId)
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

        this.noticeServicePgImpl.update(noticeId, notice);
        this.noticeServiceMongoDBImpl.update(noticeId, notice);
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
        this.delete(noticeId, LocalFileUploadImpl.fileDelete);
    }

    @Override
    public void delete(Long noticeId, FileDelete fileDelete) {

        this.noticeServiceMongoDBImpl.findById(noticeId)
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

        this.noticeServicePgImpl.delete(noticeId);
        this.noticeServiceMongoDBImpl.delete(noticeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<NoticeVo> findById(Long noticeId) {
        return this.findById(noticeId, readErrorNotification, updateErrorNotification);
    }

    /**
     * <pre>
     *     쓰기/읽기 부하를 분산 시키기 위해 조회 시 RDB가 아닌 MongoDB를 사용한다.
     *     단 MongoDB에서 장애가 발생하여 조회가 불가능한 경우에는 RDB를 사용. 이때 {@link com.notice.service.notice.NoticeServiceDecorator.ErrorNotification}
     *     구현체 프로세스를 실행하니 읽기에 문제가 발생한 경우 장애에 대응 하도록 한다.
     * </pre>
     *
     * @param seq 조회하고자 하는 게시물 번호
     * @param updateErrorNotification 조회 시 조회 수 증가 중 에러가 발생 했을 때 알림 프로세스를 처리 하기 위한 구현체
     * @param readErrorNotification 조회 시 읽기(Nosql)에 문제가 발생되어 쓰기(RDB)를 사용 했을 때 알림 프로세스를 처리 하기 위한 구현체
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<NoticeVo> findById(Long seq
            , ErrorNotification updateErrorNotification
            , ErrorNotification readErrorNotification) {

        try {
            this.noticeRepository.updateViewCount(seq);
            this.noticeMongoDBRepositoryImpl.updateViewCount(seq);
        } catch (Exception e) {
            updateErrorNotification.notification(e);
        } finally {
            try {
                return this.noticeServiceMongoDBImpl.findById(seq);
            } catch (Exception e) {
                readErrorNotification.notification(e);

                return this.noticeServicePgImpl.findById(seq);
            }
        }
    }
}
