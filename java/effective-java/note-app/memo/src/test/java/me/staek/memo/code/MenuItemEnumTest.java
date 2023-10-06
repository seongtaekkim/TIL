package me.staek.memo.code;

import org.junit.jupiter.api.Test;

class MenuItemEnumTest {

    public void menu(Menu menu) {
        if (!menu.isChild())
            System.out.println(menu.value());
        menu.children().stream().forEach((f) -> {
            if (f.type() == MenuType.MENU) {
                System.out.println(f.value() + " =======");
            }
            menu(f);
        });
    }

    @Test
    public void 메뉴_아이템_조회() {
        menu(Menu.ROOT);
    }
}
