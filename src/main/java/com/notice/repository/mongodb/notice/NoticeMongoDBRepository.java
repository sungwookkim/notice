package com.notice.repository.mongodb.notice;

import com.notice.entity.mongodb.NoticeMongoDB;

import java.util.Optional;

/**
 * <pre>
 *     공지사항 게시물 저장소 인터페이스
 * </pre>
 */
public interface NoticeMongoDBRepository {
    /**
     * <pre>
     *     게시물 저장
     * </pre>
     *
     * @param noticeMongoDB
     */
    void save(NoticeMongoDB noticeMongoDB);

    /**
     * <pre>
     *     게시물 수정
     * </pre>
     *
     * @param noticeId
     * @param noticeMongoDB
     */
    void update(Long noticeId, NoticeMongoDB noticeMongoDB);

    /**
     * <pre>
     *     게시물 삭제
     * </pre>
     *
     * @param noticeId
     */
    void delete(Long noticeId);

    /**
     * <pre>
     *     게시물 조회
     * </pre>
     *
     * @param noticeId
     * @return
     */
    Optional<NoticeMongoDB> findById(Long noticeId);

    /**
     * <pre>
     *     게시물 조회 수를 증가
     * </pre>
     *
     * @param noticeId
     */
    void updateViewCount(Long noticeId);
}
