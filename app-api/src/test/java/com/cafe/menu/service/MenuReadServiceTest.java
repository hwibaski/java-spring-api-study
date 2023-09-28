package com.cafe.menu.service;

import com.cafe.exception.NotFoundException;
import com.cafe.repository.MenuRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class MenuReadServiceTest {
    @Autowired
    private MenuReadService menuReadService;

    @Autowired
    private MenuRepository menuRepository;

    @AfterEach
    void tearDown() {
        menuRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("메뉴 단건 조회 테스트")
    class FindMenuTest {
        @Test
        @DisplayName("메뉴를 단건 조회한다")
        void getMenuById() {
            // given
            String name = "아메리카노";
            Integer price = 3000;
            var savedMenu = menuRepository.save(com.cafe.domain.menu.Menu.create(name, price));

            // when
            var result = menuReadService.getMenuById(savedMenu.getId());

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(savedMenu.getId());
            assertThat(result.name()).isEqualTo(savedMenu.getName());
            assertThat(result.price()).isEqualTo(savedMenu.getPrice());
        }

        @Test
        @DisplayName("id에 해당하는 메뉴가 없으면 예외를 발생시킨다")
        void getMenuByIdWhenNotFound() {
            // given
            Long menuNotSavedId = 1L;

            // when & then
            assertThatThrownBy(() -> menuReadService.getMenuById(menuNotSavedId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("요청한 자원을 찾을 수 없습니다");
        }
    }
}
