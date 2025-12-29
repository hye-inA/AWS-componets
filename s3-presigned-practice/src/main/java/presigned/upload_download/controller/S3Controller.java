package presigned.upload_download.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import presigned.upload_download.dto.PresignedUrlResponse;
import presigned.upload_download.service.S3Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/presigned-url")
    public ResponseEntity<PresignedUrlResponse> uploadUrl(
            @RequestParam("filename") String filename,
            @RequestParam("contentType") String contentType) {

        PresignedUrlResponse response = s3Service.uploadUrl(filename, contentType);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> download(
            @RequestParam("key") String key) {

        try {
            // S3에서 객체 스트림 가져오기
            ResponseInputStream<GetObjectResponse> s3InputStream = s3Service.getObjectStream(key);
            GetObjectResponse s3Response = s3InputStream.response();

            // key에서 파일명만 추출
            String fileName = key.contains("/")
                    ? key.substring(key.lastIndexOf("/") + 1)
                    : key;

            // UUID_원본파일명 형식인 경우 UUID 제거
            if (fileName.contains("_")) {
                fileName = fileName.substring(fileName.indexOf("_") + 1);
            }

            // 한글 파일명 인코딩
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            // StreamingResponseBody: 청크 단위로 데이터를 클라이언트에게 전송
            StreamingResponseBody streamingBody = outputStream -> {
                try (InputStream inputStream = s3InputStream) {
                    // 8KB 버퍼로 스트리밍 (메모리 효율적)
                    byte[] buffer = new byte[8192];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        outputStream.flush(); // 즉시 클라이언트로 전송
                    }
                } catch (IOException e) {
                    log.error("오류 발생", e);
                    throw e;
                }
            };

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(s3Response.contentLength())
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + encodedFileName + "\"")
                    .body(streamingBody);

        } catch (Exception e) {
            log.error("다운로드 실패 - key: {}", key, e);
            return ResponseEntity.notFound().build();
        }
    }
}
