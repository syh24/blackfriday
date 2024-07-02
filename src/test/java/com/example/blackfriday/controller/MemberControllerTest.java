package com.example.blackfriday.controller;

import com.example.blackfriday.controller.dto.MemberDto;
import com.example.blackfriday.domain.Role;
import com.example.blackfriday.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

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
    @DisplayName("회원가입 성공 테스트")
    @WithMockUser
    void join_success_test() throws Exception {
        MemberDto.JoinRequest request = createJoinRequest();

        String content = objectMapper.writeValueAsString(request);

        mvc.perform(post("/api/v1/members/join")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk());
    }

}