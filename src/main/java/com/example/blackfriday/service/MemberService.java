package com.example.blackfriday.service;

import com.example.blackfriday.controller.dto.MemberDto;
import com.example.blackfriday.domain.Member;
import com.example.blackfriday.exception.member.MemberBadRequestException;
import com.example.blackfriday.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    public void createMember(MemberDto.JoinRequest request) {
        if (checkDuplicatedEmail(request.getEmail())) {
            throw new MemberBadRequestException("이미 존재하는 이메일입니다.");
        }
        Member member = toEntity(request);
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public boolean checkDuplicatedEmail(String email) {
        return memberRepository.existsMemberByEmail(email);
    }

    private Member toEntity(MemberDto.JoinRequest request) {
        return Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .role(request.getRole())
                .build();
    }
}
