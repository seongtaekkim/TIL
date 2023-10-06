package me.staek.memo;

/**
 * memo menu
 */
public enum MenuEnum {
    FILE("FIEL"), EDIT("EDIT"), FORMAT("FORMAT"), COLOR("COLOR");
    String menu;
    MenuEnum(String menu) {
        this.menu = menu;
    }
}
