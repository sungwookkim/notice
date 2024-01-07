package com.notice.test.repository.rdb;

import com.notice.domain.Notice;
import com.notice.domain.NoticeFileAttach;
import com.notice.repository.rdb.notice.NoticeFileAttachRepository;
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
 *     테스트용 PG 게시물 첨부파일 저장소
 * </pre>
 */
public class TestNoticeFileAttachRepositoryImpl implements NoticeFileAttachRepository {
    private final TestNoticeRepositoryImpl testNoticeRepositoryImpl;

    public TestNoticeFileAttachRepositoryImpl(TestNoticeRepositoryImpl testNoticeRepositoryImpl) {
        this.testNoticeRepositoryImpl = testNoticeRepositoryImpl;
    }

    @Override
    public void deleteAllByNoticeId(Long noticeId) {

        Notice notice = this.testNoticeRepositoryImpl.getNotices().get(noticeId.intValue() - 1);
        ReflectionHelper.reflection(notice, "noticeFileAttach", new ArrayList<>());
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends NoticeFileAttach> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends NoticeFileAttach> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<NoticeFileAttach> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public NoticeFileAttach getOne(Long aLong) {
        return null;
    }

    @Override
    public NoticeFileAttach getById(Long aLong) {
        return null;
    }

    @Override
    public NoticeFileAttach getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends NoticeFileAttach> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends NoticeFileAttach> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends NoticeFileAttach> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends NoticeFileAttach> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends NoticeFileAttach> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends NoticeFileAttach> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends NoticeFileAttach, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends NoticeFileAttach> S save(S entity) {
        return null;
    }

    @Override
    public <S extends NoticeFileAttach> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<NoticeFileAttach> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<NoticeFileAttach> findAll() {
        return null;
    }

    @Override
    public List<NoticeFileAttach> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(NoticeFileAttach entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends NoticeFileAttach> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<NoticeFileAttach> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<NoticeFileAttach> findAll(Pageable pageable) {
        return null;
    }
}
