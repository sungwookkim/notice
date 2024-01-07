package com.notice.repository.mongodb.notice.impl;

import com.notice.common.http.ProcessCode;
import com.notice.entity.mongodb.NoticeMongoDB;
import com.notice.exception.ProcessException;
import com.notice.repository.mongodb.notice.NoticeMongoDBRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <pre>
 *     공지사항 게시물 구현체
 * </pre>
 */
@Repository
public class NoticeMongoDBRepositoryImpl implements NoticeMongoDBRepository {
    private final MongoTemplate mongoTemplate;

    public NoticeMongoDBRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(NoticeMongoDB noticeMongoDB) {
        this.mongoTemplate.insert(noticeMongoDB);
    }

    @Override
    public void update(Long noticeId, NoticeMongoDB noticeMongoDB) {

        Optional.ofNullable(noticeId)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK));

        Optional.ofNullable(noticeMongoDB.getTitle())
                .filter(v -> v.replace(" ", "").length() > 0)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK));

        Optional.ofNullable(noticeMongoDB.getContents())
                .filter(v -> v.replace(" ", "").length() > 0)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK));

        Optional.ofNullable(noticeMongoDB.getNoticeStartDate())
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK));

        Optional.ofNullable(noticeMongoDB.getNoticeEndDate())
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK));

        Criteria cri = Criteria.where("noticeId").is(noticeId);
        Query query = Query.query(cri);

        Update update = new Update();
        update.set("title", noticeMongoDB.getTitle())
                .set("contents", noticeMongoDB.getContents())
                .set("noticeStartDate", noticeMongoDB.getNoticeStartDate())
                .set("noticeEndDate", noticeMongoDB.getNoticeEndDate())
                .set("noticeMongoDBFileAttaches", noticeMongoDB.getNoticeMongoDBFileAttaches());

        this.mongoTemplate.updateFirst(query, update, NoticeMongoDB.class);
    }

    @Override
    public void delete(Long noticeId) {

        Optional.ofNullable(noticeId)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK));

        Query query = Query.query(Criteria.where("noticeId").is(noticeId));

        this.mongoTemplate.remove(query, NoticeMongoDB.class);
    }

    @Override
    public Optional<NoticeMongoDB> findById(Long noticeId) {

        Optional.ofNullable(noticeId)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK));

        Query query = Query.query(Criteria.where("noticeId").is(noticeId));

        return Optional.ofNullable(mongoTemplate.findOne(query, NoticeMongoDB.class));
    }

    @Override
    public void updateViewCount(Long noticeId) {
        Query query = new Query(Criteria.where("noticeId").is(noticeId));
        Update update = new Update().inc("viewCount", 1);

        this.mongoTemplate.updateFirst(query, update, NoticeMongoDB.class);
    }
}
