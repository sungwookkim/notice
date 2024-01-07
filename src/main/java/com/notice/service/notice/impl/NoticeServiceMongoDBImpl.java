package com.notice.service.notice.impl;

import com.notice.domain.Notice;
import com.notice.entity.mongodb.NoticeMongoDB;
import com.notice.repository.mongodb.notice.NoticeMongoDBRepository;
import com.notice.service.notice.NoticeService;
import com.notice.vo.notice.NoticeVo;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <pre>
 *     MongoDB 게시물 프로세스만 처리하는 구현체
 * </pre>
 */
@Service
public class NoticeServiceMongoDBImpl implements NoticeService {
    private final NoticeMongoDBRepository noticeMongoDBRepositoryImpl;

    public NoticeServiceMongoDBImpl(NoticeMongoDBRepository noticeMongoDBRepositoryImpl) {
        this.noticeMongoDBRepositoryImpl = noticeMongoDBRepositoryImpl;
    }

    @Override
    public void save(Notice notice) {
        this.noticeMongoDBRepositoryImpl.save(NoticeMongoDB.newInstance(notice));
    }

    @Override
    public void update(Long noticeId, Notice notice) {

        NoticeMongoDB noticeMongoDB = NoticeMongoDB.newInstance(notice);

        this.noticeMongoDBRepositoryImpl.update(noticeId, noticeMongoDB);
    }

    @Override
    public void delete(Long noticeId) {
        this.noticeMongoDBRepositoryImpl.delete(noticeId);
    }

    @Override
    public Optional<NoticeVo> findById(Long noticeId) {

        return this.noticeMongoDBRepositoryImpl.findById(noticeId)
                .map(v -> Optional.ofNullable(NoticeVo.newNoticeInstance(v)))
                .orElseGet(Optional::empty);
    }
}
