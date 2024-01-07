package com.notice.repository.rdb.notice;

import com.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 *     공지사항 게시물 JPA 인터페이스
 * </pre>
 */
@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    /**
     * <pre>
     *     공지사항 게시물 조회 수를 증가 시키는 메서드
     * </pre>
     *
     * @param noticeId 증가 시키고자 하는 게시물 번호
     */
    @Query("UPDATE Notice n SET n.viewCount = n.viewCount + 1 WHERE n.noticeId = :noticeId")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void updateViewCount(Long noticeId);
}
