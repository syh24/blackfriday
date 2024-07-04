package com.example.blackfriday.controller.dto;

import com.example.blackfriday.domain.Order;
import com.example.blackfriday.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

public class OrderDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class OrderRequest {
        @NotNull(message = "유저 id를 입력해주세요.")
        private Long memberId;
        @NotNull(message = "이벤트 id를 입력해주세요.")
        private Long eventId;
    }
}
