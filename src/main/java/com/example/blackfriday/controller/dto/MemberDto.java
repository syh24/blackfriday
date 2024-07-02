package com.example.blackfriday.controller.dto;

import com.example.blackfriday.domain.Member;
import com.example.blackfriday.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

public class MemberDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class JoinRequest {

        @NotNull(message = "이메일을 입력해주세요.")
        @Email
        private String email;
        @NotNull(message = "비밀번호를 입력해주세요")
        private String password;
        @NotNull(message = "이름을 입력해주세요.")
        private String name;
        @NotNull(message = "전화번호를 입력해주세요.")
        @Pattern(message = "전화번호 포멧을 확인해주세요 '010-123-1234' 또는 '010-1234-1234'", regexp = "^\\d{3}-\\{d{3,4}-\\d{4}$")
        private String phone;
        @NotNull(message = "회원권한을 입력해주세요.")
        private Role role;
    }
}
