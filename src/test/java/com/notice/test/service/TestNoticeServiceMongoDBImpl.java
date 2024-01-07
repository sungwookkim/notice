package com.notice.test.service;

import com.notice.domain.Notice;
import com.notice.entity.mongodb.NoticeMongoDB;
import com.notice.repository.mongodb.notice.NoticeMongoDBRepository;
import com.notice.service.notice.NoticeService;
import com.notice.test.helper.ReflectionHelper;
import com.notice.vo.notice.NoticeVo;

import java.util.Optional;

/**
 * <pre>
 *     테스트 MongoDB 게시물 프로세스만 처리하는 구현체
 *
 *     테스트 구현체들을 사용.
 * </pre>
 */
public class TestNoticeServiceMongoDBImpl implements NoticeService {
    private final NoticeMongoDBRepository testNoticeMongoDBRepositoryImpl;

    public TestNoticeServiceMongoDBImpl(NoticeMongoDBRepository testNoticeMongoDBRepositoryImpl) {
        this.testNoticeMongoDBRepositoryImpl = testNoticeMongoDBRepositoryImpl;
    }

    @Override
    public void save(Notice notice) {

        NoticeMongoDB noticeMongoDB = NoticeMongoDB.newInstance(notice);

        this.testNoticeMongoDBRepositoryImpl.save(noticeMongoDB);

        ReflectionHelper.reflection(notice, "noticeId", noticeMongoDB.getNoticeId());
    }

    @Override
    public void update(Long noticeId, Notice notice) {

        NoticeMongoDB noticeMongoDB = NoticeMongoDB.newInstance(notice);

        this.testNoticeMongoDBRepositoryImpl.update(noticeId, noticeMongoDB);
    }

    @Override
    public void delete(Long noticeId) {
        this.testNoticeMongoDBRepositoryImpl.delete(noticeId);
    }

    @Override
    public Optional<NoticeVo> findById(Long noticeId) {

        return this.testNoticeMongoDBRepositoryImpl.findById(noticeId)
                .map(v -> Optional.ofNullable(NoticeVo.newNoticeInstance(v)))
                .orElseGet(Optional::empty);
    }
}
