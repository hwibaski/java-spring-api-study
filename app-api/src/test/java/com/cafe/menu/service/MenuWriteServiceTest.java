package com.cafe.menu.service;

import com.cafe.domain.menu.Menu;
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


        @Test
        @DisplayName("id에 해당하는 메뉴가 없으면 예외를 발생시킨다")
        void updateMenuWhenNotFound() {
            // given
            Long menuNotSavedId = 1L;
            String nameToUpdate = "아메리카노";
            Integer priceToUpdate = 3000;

            // when & then
            assertThatThrownBy(() -> menuWriteService.updateMenu(menuNotSavedId, nameToUpdate, priceToUpdate))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("요청한 자원을 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("메뉴 삭제 테스트")
    class DeleteMenuTest {
        @Test
        @DisplayName("메뉴를 삭제한다")
        void deleteMenu() {
            // given
            String name = "아메리카노";
            Integer price = 3000;

            var savedMenu = menuRepository.save(Menu.create(name, price));

            // when
            menuWriteService.deleteMenu(savedMenu.getId());
            var result = menuRepository.findById(savedMenu.getId());

            // then
            assertThat(result.isEmpty()).isEqualTo(true);
        }

        @Test
        @DisplayName("id에 해당하는 메뉴가 없으면 예외를 발생시킨다")
        void deleteMenuWhenNotFound() {
            // given
            Long menuNotSavedId = 1L;
            String nameToUpdate = "아메리카노";
            Integer priceToUpdate = 3000;

            // when & then
            assertThatThrownBy(() -> menuWriteService.updateMenu(menuNotSavedId, nameToUpdate, priceToUpdate))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("요청한 자원을 찾을 수 없습니다");
        }
    }
}
