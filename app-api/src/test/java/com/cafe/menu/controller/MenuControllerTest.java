package com.cafe.menu.controller;

import com.cafe.enums.ErrorCode;
import com.cafe.exception.NotFoundException;
import com.cafe.menu.controller.dto.CreateMenuRequest;
import com.cafe.menu.controller.dto.GetMenuResponse;
import com.cafe.menu.controller.dto.UpdateMenuRequest;
import com.cafe.menu.service.MenuReadService;
import com.cafe.menu.service.MenuWriteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
//@WebMvcTest
class MenuControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MenuWriteService menuWriteService;
    @MockBean
    private MenuReadService menuReadService;

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

    @Nested
    @DisplayName("메뉴 수정 테스트")
    class UpdateMenuTest {
        private final String testApiPath = "/api/v1/menu";

        @Test
        @DisplayName("메뉴를 수정한다")
        void updateMenuSuccess() throws Exception {
            // given
            Long id = 1L;
            String menu = "아메리카노";
            Integer price = 3000;

            var requestDto = new UpdateMenuRequest(id, menu, price);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            Long updatedMenuId = 1L;

            given(menuWriteService.updateMenu(any(), any(), any())).willReturn(updatedMenuId);

            // when & then
            mockMvc.perform(patch(testApiPath).contentType(APPLICATION_JSON).content(requestBody))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.success").value(true))
                   .andExpect(jsonPath("$.message").value("메뉴가 수정되었습니다."))
                   .andExpect(jsonPath("$.code").value("OK"))
                   .andExpect(jsonPath("$.data.id").isNotEmpty())
                   .andExpect(jsonPath("$.validation").isEmpty());
        }

        @Test
        @DisplayName("수정하고자 하는 메뉴가 없으면 요청이 실패한다")
        void updateMenuWhenNotFoundMenu() throws Exception {
            // given
            Long id = 1L;
            String menu = "아메리카노";
            Integer price = 3000;

            var requestDto = new UpdateMenuRequest(id, menu, price);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            given(menuWriteService.updateMenu(any(), any(), any()))
                    .willThrow(new NotFoundException(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getStatus()));

            // when & then
            mockMvc.perform(patch(testApiPath).contentType(APPLICATION_JSON).content(requestBody))
                   .andExpect(status().isNotFound())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("요청한 자원을 찾을 수 없습니다"))
                   .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                   .andExpect(jsonPath("$.data").isEmpty())
                   .andExpect(jsonPath("$.validation").isEmpty());
        }

        @Test
        @DisplayName("id가 null일 경우 요청이 실패한다")
        void updateMenuFailWhenIdIsNull() throws Exception {
            // given
            Long id = null;
            String menu = "아메리카노";
            Integer price = 3000;

            var requestDto = new UpdateMenuRequest(id, menu, price);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            Long updatedMenuId = 1L;

            given(menuWriteService.updateMenu(any(), any(), any())).willReturn(updatedMenuId);

            // when & then
            mockMvc.perform(patch(testApiPath).contentType(APPLICATION_JSON).content(requestBody))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                   .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                   .andExpect(jsonPath("$.data").isEmpty())
                   .andExpect(jsonPath("$.validation.id").value("ID를 확인해주세요"));
        }

        @Test
        @DisplayName("메뉴를 생성 시 이름이 null 이면 실패한다")
        void updateMenuFailWhenNameIsNull() throws Exception {
            // given
            Long id = 1L;
            String menu = null;
            Integer price = 3000;

            var requestDto = new UpdateMenuRequest(id, menu, price);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            Long updatedMenuId = 1L;

            given(menuWriteService.updateMenu(any(), any(), any())).willReturn(updatedMenuId);

            // when & then
            mockMvc.perform(patch(testApiPath).contentType(APPLICATION_JSON).content(requestBody))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                   .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                   .andExpect(jsonPath("$.data").isEmpty())
                   .andExpect(jsonPath("$.validation.name").value("이름을 확인해주세요"));
        }

        @Test
        @DisplayName("메뉴를 생성 시 이름이 빈문자열 이면 실패한다")
        void updateMenuFailWhenNameIsBlank() throws Exception {
            // given
            Long id = 1L;
            String menu = "";
            Integer price = 3000;

            var requestDto = new UpdateMenuRequest(id, menu, price);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            Long updatedMenuId = 1L;

            given(menuWriteService.updateMenu(any(), any(), any())).willReturn(updatedMenuId);

            // when & then
            mockMvc.perform(patch(testApiPath).contentType(APPLICATION_JSON).content(requestBody))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                   .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                   .andExpect(jsonPath("$.data").isEmpty())
                   .andExpect(jsonPath("$.validation.name").value("이름을 확인해주세요"));
        }

        @Test
        @DisplayName("메뉴를 수정 시 가격이 500원 보다 낮으면 실패한다")
        void createMenuFailWhenPriceLessThan500() throws Exception {
            // given
            Long id = 1L;
            String menu = "아메리카노";
            Integer price = 100;

            var requestDto = new UpdateMenuRequest(id, menu, price);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when & then
            mockMvc.perform(patch(testApiPath).contentType(APPLICATION_JSON).content(requestBody))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                   .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                   .andExpect(jsonPath("$.data").isEmpty())
                   .andExpect(jsonPath("$.validation.price").value("메뉴의 가격을 확인해주세요"));
        }
    }

    @Nested
    @DisplayName("단건 메뉴 조회 테스트")
    class FindMenuTest {
        private final String testApiPath = "/api/v1/menu/{menuId}";

        @Test
        @DisplayName("단건 메뉴를 조회한다")
        void getMenuSuccess() throws Exception {
            // given
            Long menuIdToGet = 1L;
            String name = "아메리카노";
            Integer price = 3000;

            given(menuReadService.getMenuById(menuIdToGet)).willReturn(new GetMenuResponse(menuIdToGet, name, price));

            // when & then
            mockMvc.perform(get(testApiPath, menuIdToGet)
                                    .contentType(APPLICATION_JSON))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.success").value(true))
                   .andExpect(jsonPath("$.message").value("메뉴가 조회되었습니다."))
                   .andExpect(jsonPath("$.code").value("OK"))
                   .andExpect(jsonPath("$.data.id").isNotEmpty())
                   .andExpect(jsonPath("$.data.name").value(name))
                   .andExpect(jsonPath("$.data.price").value(price))
                   .andExpect(jsonPath("$.validation").isEmpty());
        }

        @Test
        @DisplayName("조회하고자 하는 메뉴가 없으면 요청이 실패한다")
        void getMenuWhenNotFoundMenu() throws Exception {
            // given
            Long menuIdToGet = 1L;

            given(menuReadService.getMenuById(any()))
                    .willThrow(new NotFoundException(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getStatus()));

            // when & then
            mockMvc.perform(get(testApiPath, menuIdToGet).
                                    contentType(APPLICATION_JSON)
                   )
                   .andExpect(status().isNotFound())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("요청한 자원을 찾을 수 없습니다"))
                   .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                   .andExpect(jsonPath("$.data").isEmpty())
                   .andExpect(jsonPath("$.validation").isEmpty());
        }
    }

    @Nested
    @DisplayName("메뉴 삭제 테스트")
    class DeleteMenuTest {
        private final String testApiPath = "/api/v1/menu/{menuId}";

        @Test
        @DisplayName("메뉴를 삭제한다.")
        void deleteMenuSuccess() throws Exception {
            // given
            Long menuIdToDelete = 1L;

            given(menuWriteService.deleteMenu(menuIdToDelete)).willReturn(menuIdToDelete);

            // when & then
            mockMvc.perform(delete(testApiPath, menuIdToDelete)
                                    .contentType(APPLICATION_JSON))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.success").value(true))
                   .andExpect(jsonPath("$.message").value("메뉴가 삭제되었습니다."))
                   .andExpect(jsonPath("$.code").value("OK"))
                   .andExpect(jsonPath("$.data.id").isNotEmpty())
                   .andExpect(jsonPath("$.validation").isEmpty());
        }

        @Test
        @DisplayName("삭제하고자 하는 메뉴가 없으면 요청이 실패한다")
        void deleteMenuWhenNotFoundMenu() throws Exception {
            // given
            Long menuIdToGet = 1L;

            given(menuWriteService.deleteMenu(any()))
                    .willThrow(new NotFoundException(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getStatus()));

            // when & then
            mockMvc.perform(delete(testApiPath, menuIdToGet).
                                    contentType(APPLICATION_JSON)
                   )
                   .andExpect(status().isNotFound())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("요청한 자원을 찾을 수 없습니다"))
                   .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                   .andExpect(jsonPath("$.data").isEmpty())
                   .andExpect(jsonPath("$.validation").isEmpty());
        }
    }
}
