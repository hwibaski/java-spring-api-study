package com.cafe.domain.menu;

import com.cafe.domain.base.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseAuditEntity {
    @Column(unique = true)
    private String name;

    private Integer price;

    private Menu(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public static Menu create(String name, Integer price) {
        return new Menu(name, price);
    }
}
