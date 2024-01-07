package com.notice.service.notice.impl;

import com.notice.common.http.ProcessCode;
import com.notice.domain.Notice;
import com.notice.domain.NoticeFileAttach;
import com.notice.exception.ProcessException;
import com.notice.repository.rdb.notice.NoticeFileAttachRepository;
import com.notice.repository.rdb.notice.NoticeRepository;
import com.notice.service.notice.NoticeService;
import com.notice.vo.notice.NoticeVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * <pre>
 *     RDB(Postgresql) 게시물 프로세스만 처리하는 구현체
 * </pre>
 */
@Service
public class NoticeServicePgImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeFileAttachRepository noticeFileAttachRepository;

    public NoticeServicePgImpl(NoticeRepository noticeRepository
            , NoticeFileAttachRepository noticeFileAttachRepository) {

        this.noticeRepository = noticeRepository;
        this.noticeFileAttachRepository = noticeFileAttachRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Notice notice) {
        this.noticeRepository.save(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

        this.noticeRepository.findById(noticeId)
                .ifPresent(v -> {
                    v.title(notice.getTitle())
                            .contents(notice.getContents())
                            .noticeStartDate(notice.getNoticeStartDate())
                            .noticeEndDate(notice.getNoticeEndDate());

                    this.noticeFileAttachRepository.deleteAllByNoticeId(noticeId);

                    for(NoticeFileAttach noticeFileAttach : notice.getNoticeFileAttach()) {
                        this.noticeFileAttachRepository.save(new NoticeFileAttach(noticeFileAttach.getOriginalFileName()
                                , noticeFileAttach.getAttachFileName()
                                , noticeFileAttach.getAttachFilePath()
                                , v));
                    }
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long noticeId) {
        this.noticeRepository.deleteById(noticeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NoticeVo> findById(Long noticeId) {

        return this.noticeRepository.findById(noticeId)
                .map(v -> Optional.ofNullable(NoticeVo.newNoticeInstance(v)))
                .orElseGet(Optional::empty);
    }
}
