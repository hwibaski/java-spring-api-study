package com.cafe.menu.service;

import com.cafe.repository.MenuRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class MenuWriteServiceTest {
    @Autowired
    private MenuWriteService menuWriteService;

    @Autowired
    private MenuRepository menuRepository;

    @AfterEach
    void tearDown() {
        menuRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("메뉴 생성 테스트")
    class CreateMenuTest {
        @Test
        @DisplayName("메뉴를 생성한다")
        void createMenu() {
            // given
            String name = "아메리카노";
            Integer price = 3000;

            // when
            var result = menuWriteService.createMenu(name, price);

            // then
            assertThat(result).isNotNull();
        }
    }
}
