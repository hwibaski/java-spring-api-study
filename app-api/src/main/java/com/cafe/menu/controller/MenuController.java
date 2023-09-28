package com.cafe.menu.controller;

import com.cafe.dto.ApiResponse;
import com.cafe.menu.controller.dto.CreateMenuRequest;
import com.cafe.menu.controller.dto.CreateMenuResponse;
import com.cafe.menu.service.MenuWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MenuWriteService menuWriteService;

    @PostMapping("/api/v1/menu")
    public ResponseEntity<ApiResponse<CreateMenuResponse>> createMenu(@Valid @RequestBody CreateMenuRequest request) {
        var result = menuWriteService.createMenu(request.name(), request.price());

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(ApiResponse.success("메뉴가 생성되었습니다.", new CreateMenuResponse(result)));
    }
}
