package me.staek.memo;

import java.util.ArrayList;
import java.util.List;

/**
 * memo menu
 */
public enum MenuEnum {

    FILE("File", null)
    , NEW("New", FILE)
    , OPEN("Open", FILE)
    , SAVE("Save", FILE)
    , SAVEAS("SaveAs", FILE)
    , EXIT("Exit", FILE),
    EDIT("Edit", null)
    , UNDO("Undo", EDIT)
    , REDO("Redo", EDIT),
    FORMAT("Format", null)
    , FONT("Font", FORMAT)
        , ARIAL("Arial", FONT)
        , CSMS("Comic Sans MS", FONT)
        , TNR("Times New Roman", FONT)
    , FONTSIZE("FontSize", FORMAT)
        , SIZE8("Size8", FONTSIZE)
        , SIZE12("Size12", FONTSIZE)
        , SIZE16("Size16", FONTSIZE)
        , SIZE24("Size24", FONTSIZE)
    ;

    private MenuEnum parent;
    private List<MenuEnum> children = new ArrayList<>();

    String menu;
    MenuEnum(String menu, MenuEnum parant) {
        this.menu = menu;
        this.parent = parant;
        if (parant != null) {
            this.parent.addChild(this);
        }
    }

    private void addChild(MenuEnum child) {
        for (MenuEnum c = this; c != null; c = c.parent) {
            c.children.add(child);
        }
    }

    public List<MenuEnum> children() {
        return this.children;
    }
}
