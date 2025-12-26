package com.example.s3_ex02.controller;

import com.example.s3_ex02.dto.S3FileDto;
import com.example.s3_ex02.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    // 파일 목록
    @GetMapping("/")
    public String index(Model model) {
        S3FileDto dto = new S3FileDto("hhi", 10L, "2025-12-26");
        List<S3FileDto> files = Arrays.asList(dto); // 서비스로부터 반환받은 값
        model.addAttribute("files", files); // 화면으로 files 전달
        return "index";
    }
}
