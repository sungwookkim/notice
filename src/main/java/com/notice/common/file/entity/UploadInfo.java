package com.notice.common.file.entity;

/**
 * 파일 업로드 프로세스 처리 관련 레코드
 */
public record UploadInfo(String originalFileName, String uploadFileName, String uploadPath) {
}
