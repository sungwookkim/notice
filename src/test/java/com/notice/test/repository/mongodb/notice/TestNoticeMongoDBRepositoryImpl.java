package com.notice.test.repository.mongodb.notice;

import com.notice.common.http.ProcessCode;
import com.notice.domain.NoticeFileAttach;
import com.notice.entity.mongodb.NoticeMongoDB;
import com.notice.entity.mongodb.NoticeMongoDBFileAttach;
import com.notice.exception.ProcessException;
import com.notice.repository.mongodb.notice.NoticeMongoDBRepository;
import com.notice.test.helper.ReflectionHelper;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     테스트용 MongoDB 게시물 저장소
 *
 *     테스트를 위해서 List 객체에 게시물 정보를 저장.
 * </pre>
 */
public class TestNoticeMongoDBRepositoryImpl implements NoticeMongoDBRepository {
    private final List<NoticeMongoDB> noticeMongoDBList = new ArrayList<>();

    @Override
    public void update(Long seq, NoticeMongoDB noticeMongoDB) {

        Optional.ofNullable(seq)
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

        Optional<NoticeMongoDB> findNoticeMongoDB = this.findById(seq);

        findNoticeMongoDB
                .ifPresent(v -> {
                    ReflectionHelper.reflection(v, "title", noticeMongoDB.getTitle());
                    ReflectionHelper.reflection(v, "contents", noticeMongoDB.getContents());
                    ReflectionHelper.reflection(v, "noticeStartDate", noticeMongoDB.getNoticeStartDate());
                    ReflectionHelper.reflection(v, "noticeEndDate", noticeMongoDB.getNoticeEndDate());
                    ReflectionHelper.reflection(v, "noticeMongoDBFileAttaches", noticeMongoDB.getNoticeMongoDBFileAttaches());
                });
    }

    @Override
    public void delete(Long seq) {
        try {
            this.noticeMongoDBList.remove(seq.intValue() - 1);
        } catch (Exception e) {}
    }

    @Override
    public void save(NoticeMongoDB noticeMongoDB) {

        ReflectionHelper.reflection(noticeMongoDB, "noticeId", this.noticeMongoDBList.size() + 1L);

        for(int i = 0, len = noticeMongoDB.getNoticeMongoDBFileAttaches().size(); i < len; i++) {
            NoticeMongoDBFileAttach noticeFileAttach = noticeMongoDB.getNoticeMongoDBFileAttaches().get(i);
            ReflectionHelper.reflection(noticeFileAttach, "noticeFileAttachId", i + 1L);
        }

        this.noticeMongoDBList.add(noticeMongoDB);
    }

    @Override
    public Optional<NoticeMongoDB> findById(Long seq) {
        try {
            return Optional.ofNullable(this.noticeMongoDBList.get(seq.intValue() - 1));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void updateViewCount(Long seq) {

        Optional<NoticeMongoDB> notice = this.findById(seq);

        notice.ifPresent(v -> {
            ReflectionHelper.reflection(v, "viewCount", v.getViewCount() + 1);
        });
    }
}
