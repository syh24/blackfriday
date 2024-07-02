package com.example.blackfriday.service;

import com.example.blackfriday.controller.dto.MemberDto;
import com.example.blackfriday.domain.Role;
import com.example.blackfriday.exception.member.MemberBadRequestException;
import com.example.blackfriday.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    public MemberDto.JoinRequest createJoinRequest() {
        return MemberDto.JoinRequest.builder()
                .email("test@gmail.com")
                .name("서윤혁")
                .password("abc123")
                .phone("010-1234-1234")
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    @DisplayName("회원가입 시 이메일 중복 테스트")
    void checkDuplicatedEmail() {
        when(memberRepository.existsMemberByEmail(anyString())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> memberService.createMember(createJoinRequest()))
                .isInstanceOf(MemberBadRequestException.class)
                .hasMessageStartingWith("이미 존재하는 이메일입니다.");
    }
}