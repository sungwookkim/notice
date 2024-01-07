package com.notice.controller.v1;

import com.notice.controller.abs.NoticeControllerAbs;
import com.notice.service.notice.NoticeServiceDecorator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *     공지사항 v1 API 컨트롤러
 * </pre>
 */
@RestController
@RequestMapping("v1/notice")
public class NoticeController extends NoticeControllerAbs {

    public NoticeController(NoticeServiceDecorator noticeServiceDecoratorImpl) {

        super(noticeServiceDecoratorImpl);
    }
}
