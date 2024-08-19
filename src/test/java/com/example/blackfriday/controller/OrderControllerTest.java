package com.example.blackfriday.controller;

import com.example.blackfriday.controller.dto.OrderDto;
import com.example.blackfriday.service.async.AsyncEventProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
class OrderControllerTest extends RestDocsTestCommon {

    @MockBean
    private AsyncEventProductService asyncEventProductService;

    OrderDto.EventOrderRequest createOrderDto() {
        return OrderDto.EventOrderRequest
                .builder()
                .eventId(1L)
                .memberId(1L)
                .build();
    }

    @Test
    @DisplayName("주문 생성 테스트")
    void createOrder() throws Exception {
        String content = objectMapper.writeValueAsString(createOrderDto());
        Long id = 1L;

        mvc.perform(post("/api/v1/orders/asyncEventProducts/{id}", id)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(RequestDocumentation.parameterWithName("id").description("주문 id")),
                        requestFields(
                                fieldWithPath("memberId")
                                        .description("유저 id"),
                                fieldWithPath("eventId")
                                        .description("이벤트 id")
                        ),
                        responseFields(
                                fieldWithPath("result")
                                        .description("결과"),
                                fieldWithPath("body")
                                        .description("메세지"))
                ));
    }
}