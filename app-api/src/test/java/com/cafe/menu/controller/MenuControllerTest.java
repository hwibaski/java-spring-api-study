package com.cafe.menu.controller;

import com.cafe.menu.controller.dto.CreateMenuRequest;
import com.cafe.menu.service.MenuWriteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@WebMvcTest
class MenuControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MenuWriteService menuWriteService;

    @Nested
    @DisplayName("메뉴 생성 테스트")
    class CreateMenuTest {
        private final String testApiPath = "/api/v1/menu";

        @Test
        @DisplayName("메뉴를 생성한다")
        void createMenu() throws Exception {
            // given
            String menu = "아메리카노";
            Integer price = 3000;

            var requestDto = new CreateMenuRequest(menu, price);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            Long createdMenuId = 1L;

            given(menuWriteService.createMenu(any(), any())).willReturn(createdMenuId);

            // when & then
            mockMvc.perform(post(testApiPath).contentType(APPLICATION_JSON).content(requestBody))
                   .andExpect(status().isCreated())
                   .andExpect(jsonPath("$.success").value(true))
                   .andExpect(jsonPath("$.message").value("메뉴가 생성되었습니다."))
                   .andExpect(jsonPath("$.code").value("OK"))
                   .andExpect(jsonPath("$.data.id").isNotEmpty())
                   .andExpect(jsonPath("$.validation").isEmpty());
        }

        @Test
        @DisplayName("메뉴를 생성 시 이름이 빈문자열이면 실패한다")
        void createMenuFailWhenNameIsBlank() throws Exception {
            // given
            String menu = "";
            Integer price = 3000;

            var requestDto = new CreateMenuRequest(menu, price);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when & then
            mockMvc.perform(post(testApiPath).contentType(APPLICATION_JSON).content(requestBody))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                   .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                   .andExpect(jsonPath("$.data").isEmpty())
                   .andExpect(jsonPath("$.validation.name").value("이름을 확인해주세요"));
        }

        @Test
        @DisplayName("메뉴를 생성 시 이름이 null 이면 실패한다")
        void createMenuFailWhenNameIsNull() throws Exception {
            // given
            String menu = null;
            Integer price = 3000;

            var requestDto = new CreateMenuRequest(menu, price);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when & then
            mockMvc.perform(post(testApiPath).contentType(APPLICATION_JSON).content(requestBody))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                   .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                   .andExpect(jsonPath("$.data").isEmpty())
                   .andExpect(jsonPath("$.validation.name").value("이름을 확인해주세요"));
        }

        @Test
        @DisplayName("메뉴를 생성 시 가격이 500원 보다 낮으면 실패한다")
        void createMenuFailWhenPriceLessThan500() throws Exception {
            // given
            String menu = "아메리카노";
            Integer price = 100;

            var requestDto = new CreateMenuRequest(menu, price);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when & then
            mockMvc.perform(post(testApiPath).contentType(APPLICATION_JSON).content(requestBody))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                   .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                   .andExpect(jsonPath("$.data").isEmpty())
                   .andExpect(jsonPath("$.validation.price").value("메뉴의 가격을 확인해주세요"));
        }
    }
}
