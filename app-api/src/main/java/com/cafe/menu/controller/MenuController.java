package com.cafe.menu.controller;

import com.cafe.dto.ApiResponse;
import com.cafe.menu.controller.dto.CreateMenuRequest;
import com.cafe.menu.controller.dto.CreateMenuResponse;
import com.cafe.menu.controller.dto.DeleteMenuResponse;
import com.cafe.menu.controller.dto.GetMenuResponse;
import com.cafe.menu.controller.dto.UpdateMenuRequest;
import com.cafe.menu.controller.dto.UpdateMenuResponse;
import com.cafe.menu.service.MenuReadService;
import com.cafe.menu.service.MenuWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MenuWriteService menuWriteService;
    private final MenuReadService menuReadService;

    @PostMapping("/api/v1/menu")
    public ResponseEntity<ApiResponse<CreateMenuResponse>> createMenu(@Valid @RequestBody CreateMenuRequest request) {
        var result = menuWriteService.createMenu(request.name(), request.price());

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(ApiResponse.success("메뉴가 생성되었습니다.", new CreateMenuResponse(result)));
    }

    @PatchMapping("/api/v1/menu")
    public ResponseEntity<ApiResponse<UpdateMenuResponse>> updateMenu(@Valid @RequestBody UpdateMenuRequest request) {
        var result = menuWriteService.updateMenu(request.id(), request.name(), request.price());

        return ResponseEntity.status(HttpStatus.OK)
                             .body(ApiResponse.success("메뉴가 수정되었습니다.", new UpdateMenuResponse(result)));
    }

    @GetMapping("/api/v1/menu/{menuId}")
    public ResponseEntity<ApiResponse<GetMenuResponse>> getMenu(@PathVariable("menuId") Long menuId) {
        var result = menuReadService.getMenuById(menuId);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(ApiResponse.success("메뉴가 조회되었습니다.", result));
    }

    @DeleteMapping("/api/v1/menu/{menuId}")
    public ResponseEntity<ApiResponse<DeleteMenuResponse>> deleteMenu(@PathVariable("menuId") Long menuId) {
        var result = menuWriteService.deleteMenu(menuId);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(ApiResponse.success("메뉴가 삭제되었습니다.", new DeleteMenuResponse(result)));
    }
}
