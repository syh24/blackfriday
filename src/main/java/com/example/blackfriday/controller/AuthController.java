package com.example.blackfriday.controller;

import com.example.blackfriday.controller.dto.MemberDto;
import com.example.blackfriday.utils.ApiUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/login")
    public ApiUtil.ApiSuccessResult<String> login() {
        return ApiUtil.success("로그인이 완료되었습니다.");
    }
}
