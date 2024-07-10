package com.example.blackfriday.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class EventProductMessageDto {
    private Long memberId;
    private Long eventProductId;
}
