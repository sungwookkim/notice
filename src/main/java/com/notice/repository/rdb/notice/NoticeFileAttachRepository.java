package com.notice.repository.rdb.notice;

import com.notice.domain.NoticeFileAttach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 *     공지사항 게시물 첨부파일 JPA 인터페이스
 * </pre>
 */
@Repository
public interface NoticeFileAttachRepository extends JpaRepository<NoticeFileAttach, Long> {

    /**
     * <pre>
     *     게시물 첨부파일을 삭제하는 메서드
     * </pre>
     * @param noticeId 삭제 하고자 하는 게시물 번호
     */
    @Query("delete from NoticeFileAttach n where n.notice.noticeId = :noticeId")
    @Modifying
    void deleteAllByNoticeId(Long noticeId);
}
