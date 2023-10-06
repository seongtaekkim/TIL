package me.staek.memo.code;

/**
 * menu type
 */
public enum MenuType {

    MENU("MENU"), ITEM("ITEM");

    private String type;
    MenuType(String type) {
        this.type = type;
    }

    public String value() {
        return this.type;
    }
}
