package com.example.blackfriday.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

public class OrderDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class EventOrderRequest {
        @NotNull(message = "유저 id를 입력해주세요.")
        private Long memberId;
        @NotNull(message = "이벤트 id를 입력해주세요.")
        private Long eventId;
    }
}
