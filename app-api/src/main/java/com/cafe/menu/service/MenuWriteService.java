package com.cafe.menu.service;

import com.cafe.domain.menu.Menu;
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
}
