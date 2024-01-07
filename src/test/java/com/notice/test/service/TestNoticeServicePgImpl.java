package com.notice.test.service;

import com.notice.common.http.ProcessCode;
import com.notice.domain.Notice;
import com.notice.domain.NoticeFileAttach;
import com.notice.exception.ProcessException;
import com.notice.service.notice.NoticeService;
import com.notice.repository.rdb.notice.NoticeRepository;
import com.notice.test.helper.ReflectionHelper;
import com.notice.test.repository.rdb.TestNoticeFileAttachRepositoryImpl;
import com.notice.test.repository.rdb.TestNoticeRepositoryImpl;
import com.notice.vo.notice.NoticeVo;

import java.util.Optional;

/**
 * <pre>
 *     테스트 RDB(Postgresql) 게시물 프로세스만 처리하는 구현체
 *
 *     테스트 구현체들을 사용.
 * </pre>
 */
public class TestNoticeServicePgImpl implements NoticeService {
    private final NoticeRepository testNoticeRepository;
    private final TestNoticeFileAttachRepositoryImpl testNoticeFileAttachRepositoryImpl;

    public TestNoticeServicePgImpl(NoticeRepository testNoticeRepository) {

        this.testNoticeRepository = testNoticeRepository;
        this.testNoticeFileAttachRepositoryImpl = new TestNoticeFileAttachRepositoryImpl((TestNoticeRepositoryImpl)this.testNoticeRepository);
    }

    @Override
    public void save(Notice notice) {
        this.testNoticeRepository.save(notice);
    }

    @Override
    public void update(Long noticeId, Notice notice) {

        Optional.ofNullable(noticeId)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK));

        Optional.ofNullable(notice.getTitle())
                .filter(v -> v.replace(" ", "").length() > 0)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK));

        Optional.ofNullable(notice.getContents())
                .filter(v -> v.replace(" ", "").length() > 0)
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK));

        Optional.ofNullable(notice.getNoticeStartDate())
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK));

        Optional.ofNullable(notice.getNoticeEndDate())
                .orElseThrow(()-> new ProcessException(ProcessCode.NoticeCode.BLANK));

        this.testNoticeRepository.findById(noticeId)
                .ifPresent(v -> {
                    for(int i = 0, len = notice.getNoticeFileAttach().size(); i < len; i++) {
                        NoticeFileAttach noticeFileAttach = notice.getNoticeFileAttach().get(i);
                        ReflectionHelper.reflection(noticeFileAttach, "noticeFileAttachId", i + 1L);
                    }

                    this.testNoticeFileAttachRepositoryImpl.deleteAllByNoticeId(noticeId);

                    ReflectionHelper.reflection(v, "title", notice.getTitle());
                    ReflectionHelper.reflection(v, "contents", notice.getContents());
                    ReflectionHelper.reflection(v, "noticeStartDate", notice.getNoticeStartDate());
                    ReflectionHelper.reflection(v, "noticeEndDate", notice.getNoticeEndDate());
                    ReflectionHelper.reflection(v, "noticeFileAttach", notice.getNoticeFileAttach());
                });
    }

    @Override
    public void delete(Long noticeId) {
        this.testNoticeRepository.deleteById(noticeId);
    }

    @Override
    public Optional<NoticeVo> findById(Long noticeId) {

        return this.testNoticeRepository.findById(noticeId)
                .map(v -> Optional.ofNullable(NoticeVo.newNoticeInstance(v)))
                .orElseGet(Optional::empty);
    }
}
