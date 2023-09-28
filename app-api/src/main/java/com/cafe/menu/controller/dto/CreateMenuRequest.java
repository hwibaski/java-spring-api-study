package com.cafe.menu.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateMenuRequest(@NotBlank(message = "이름을 확인해주세요") String name,
                                @Min(value = 500, message = "메뉴의 가격을 확인해주세요") Integer price) {
}
