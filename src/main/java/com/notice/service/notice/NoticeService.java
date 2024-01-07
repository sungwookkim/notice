package com.notice.service.notice;

import com.notice.domain.Notice;
import com.notice.vo.notice.NoticeVo;

import java.util.Optional;

/**
 * 게시물 서비스 인터페이스
 */
public interface NoticeService {
    /**
     * <pre>
     *     게시물 저장
     * </pre>
     *
     * @param notice
     */
    void save(Notice notice);

    /**
     * <pre>
     *     게시물 수정
     * </pre>
     *
     * @param noticeId
     * @param notice
     */
    void update(Long noticeId, Notice notice);

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
    Optional<NoticeVo> findById(Long noticeId);
}
