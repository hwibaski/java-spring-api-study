package com.cafe.menu.service;

import com.cafe.domain.menu.Menu;
import com.cafe.enums.ErrorCode;
import com.cafe.exception.NotFoundException;
import com.cafe.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuWriteService {
    private final MenuRepository menuRepository;

    @Transactional
    public Long createMenu(String name, Integer price) {
        var result = menuRepository.save(Menu.create(name, price));

        return result.getId();
    }

    @Transactional
    public Long updateMenu(Long id, String name, Integer price) {
        var menu = findMenuBYId(id);
        menu.update(name, price);

        return menu.getId();
    }

    @Transactional
    public Long deleteMenu(Long id) {
        var menu = findMenuBYId(id);
        menuRepository.delete(menu);

        return menu.getId();
    }

    private Menu findMenuBYId(Long menuId) {
        return menuRepository.findById(menuId)
                             .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getStatus()));

    }
}
