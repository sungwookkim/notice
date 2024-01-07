package com.notice.test.repository.rdb;

import com.notice.domain.Notice;
import com.notice.domain.NoticeFileAttach;
import com.notice.repository.rdb.notice.NoticeRepository;
import com.notice.test.helper.ReflectionHelper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * <pre>
 *     테스트용 PG 게시물 저장소
 *
 *     테스트를 위해서 List 객체에 게시물 정보를 저장.
 * </pre>
 */
public class TestNoticeRepositoryImpl implements NoticeRepository {
    private final List<Notice> notices = new ArrayList<>();

    public List<Notice> getNotices() {
        return notices;
    }

    @Override
    public void updateViewCount(Long seq) {

        Optional<Notice> notice = this.findById(seq);

        notice.ifPresent(v -> {
            ReflectionHelper.reflection(v, "viewCount", v.getViewCount() + 1);
        });
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Notice> S saveAndFlush(S entity) {

        ReflectionHelper.reflection(entity, "noticeId", this.notices.size() + 1L);

        this.notices.add(entity);

        return entity;
    }

    @Override
    public <S extends Notice> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Notice> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Notice getOne(Long aLong) {
        return null;
    }

    @Override
    public Notice getById(Long aLong) {
        return null;
    }

    @Override
    public Notice getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Notice> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Notice> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Notice> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Notice> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Notice> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Notice> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Notice, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Notice> S save(S entity) {

        ReflectionHelper.reflection(entity, "noticeId", this.notices.size() + 1L);

        for(int i = 0, len = entity.getNoticeFileAttach().size(); i < len; i++) {
            NoticeFileAttach noticeFileAttach = entity.getNoticeFileAttach().get(i);
            ReflectionHelper.reflection(noticeFileAttach, "noticeFileAttachId", i + 1L);
        }

        this.notices.add(entity);

        return entity;
    }

    @Override
    public <S extends Notice> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Notice> findById(Long aLong) {

        try {
            return Optional.ofNullable(this.notices.get(aLong.intValue() - 1));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Notice> findAll() {
        return this.notices;
    }

    @Override
    public List<Notice> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

        try {
            this.notices.remove(aLong.intValue() - 1);
        } catch (Exception e) {}
    }

    @Override
    public void delete(Notice entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Notice> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Notice> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Notice> findAll(Pageable pageable) {
        return null;
    }
}
