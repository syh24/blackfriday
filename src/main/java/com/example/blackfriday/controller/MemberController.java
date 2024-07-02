package com.example.blackfriday.controller;

import com.example.blackfriday.controller.dto.MemberDto;
import com.example.blackfriday.service.MemberService;
import com.example.blackfriday.utils.ApiUtil;
import com.example.blackfriday.utils.ApiUtil.ApiSuccessResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ApiSuccessResult<String> createMember(@RequestBody @Valid MemberDto.JoinRequest joinRequest) {
        memberService.createMember(joinRequest);
        return ApiUtil.success("회원가입이 완료되었습니다.");
    }

}
