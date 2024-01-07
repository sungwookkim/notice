package com.notice.service.notice;

import com.notice.common.file.FileDelete;
import com.notice.common.file.FileUpload;
import com.notice.domain.Notice;
import com.notice.vo.notice.NoticeVo;

import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     게시물 데코레이터 패턴 서비스 인터페이스
 *
 *     쓰기(RDB)/읽기(Nosql)를 제어하기 위해 생성한 인터페이스
 * </pre>
 */
public interface NoticeServiceDecorator extends NoticeService {

    /**
     * <pre>
     *     게시물 조회
     * </pre>
     *
     * @param noticeId 조회하고자 하는 게시물 번호
     * @param updateErrorNotification 조회 시 조회 수 증가 중 에러가 발생 했을 때 알림 프로세스를 처리 하기 위한 구현체
     * @param readErrorNotification 조회 시 읽기(Nosql)에 문제가 발생되어 쓰기(RDB)를 사용 했을 때 알림 프로세스를 처리 하기 위한 구현체
     * @return
     */
    Optional<NoticeVo> findById(Long noticeId
            , ErrorNotification updateErrorNotification
            , ErrorNotification readErrorNotification);

    /**
     * <pre>
     *     게시물 저장.
     *
     *     첨부파일 처리까지 수행.
     * </pre>
     *
     * @param notice
     * @param fileUploads
     */
    void save(Notice notice, List<FileUpload> fileUploads);

    /**
     * <pre>
     *     게시물 수정.
     *
     *     게시물 수정 시 파일첨부까지 수행.
     * </pre>
     *
     * @param noticeId
     * @param notice
     * @param fileUploads
     * @param fileDelete
     */
    void update(Long noticeId, Notice notice, List<FileUpload> fileUploads, FileDelete fileDelete);

    /**
     * <pre>
     *     게시물 삭제.
     *
     *     게시물 삭제 시 파일첨부까지 수행
     * </pre>
     *
     * @param noticeId
     * @param fileDelete
     */
    void delete(Long noticeId, FileDelete fileDelete);

    /**
     * <pre>
     *     게시물 처리 중 알림이 필요한 경우 사용되는 인터페이스
     * </pre>
     */
    interface ErrorNotification {

        /**
         * <pre>
         *     알림 처리
         * </pre>
         * @param e
         */
        void notification(Exception e);
    }
}
