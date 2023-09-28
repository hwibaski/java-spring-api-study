package com.cafe.menu.service;

import com.cafe.domain.menu.Menu;
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

    @Nested
    @DisplayName("메뉴 수정 테스트")
    class UpdateMenuTest {
        @Test
        @DisplayName("메뉴를 수정한다")
        void updateMenu() {
            // given
            String name = "아메리카노";
            Integer price = 3000;

            var savedMenu = menuRepository.save(Menu.create(name, price));

            // when
            var result = menuWriteService.updateMenu(savedMenu.getId(), "라떼", 4000);
            var updatedMenu = menuRepository.findById(result);

            // then
            assertThat(updatedMenu).isNotNull();
            assertThat(updatedMenu.get().getName()).isEqualTo("라떼");
            assertThat(updatedMenu.get().getPrice()).isEqualTo(4000);
        }
    }
}
