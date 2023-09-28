package com.cafe.menu.service;

import com.cafe.enums.ErrorCode;
import com.cafe.exception.NotFoundException;
import com.cafe.menu.controller.dto.GetMenuResponse;
import com.cafe.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuReadService {
    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public GetMenuResponse getMenuById(Long id) {
        var menu = menuRepository.findById(id)
                                 .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getStatus()));

        return new GetMenuResponse(menu.getId(), menu.getName(), menu.getPrice());
    }
}
