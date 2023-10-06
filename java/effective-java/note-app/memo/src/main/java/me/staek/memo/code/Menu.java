package me.staek.memo.code;

import java.util.ArrayList;
import java.util.List;

/**
 * memo menu, menuitem
 */
public enum Menu {

    ROOT("Root", MenuType.MENU, null),
    FILE("File", MenuType.MENU, ROOT)
    , NEW("New", MenuType.ITEM, FILE)
    , OPEN("Open", MenuType.ITEM, FILE)
    , SAVE("Save", MenuType.ITEM, FILE)
    , SAVEAS("SaveAs", MenuType.ITEM, FILE)
    , EXIT("Exit", MenuType.ITEM, FILE),
    EDIT("Edit", MenuType.MENU,ROOT)
    , UNDO("Undo", MenuType.ITEM, EDIT)
    , REDO("Redo", MenuType.ITEM, EDIT),
    FORMAT("Format", MenuType.MENU,ROOT)
    , FONT("Font", MenuType.MENU, FORMAT)
        , ARIAL("Arial", MenuType.ITEM, FONT)
        , CSMS("Comic Sans MS", MenuType.ITEM,FONT)
        , TNR("Times New Roman", MenuType.ITEM,FONT)
    , FONTSIZE("FontSize", MenuType.MENU, FORMAT)
        , SIZE8("Size8", MenuType.ITEM, FONTSIZE)
        , SIZE12("Size12", MenuType.ITEM, FONTSIZE)
        , SIZE16("Size16", MenuType.ITEM, FONTSIZE)
        , SIZE24("Size24", MenuType.ITEM, FONTSIZE)
    ;

    private Menu parent;
    private List<Menu> children = null;

    private String menu;
    private MenuType type;
    Menu(String menu, MenuType type, Menu parant) {
        this.menu = menu;
        this.parent = parant;
        this.type = type;
        this.children = new ArrayList<>();
        if (parant != null) {
            this.parent.children.add(this);
        }
    }

    public List<Menu> children() {
        return this.children;
    }

    public String value() {
        return this.menu;
    }

    public MenuType type() {
        return this.type;
    }

    /**
     * child 여부
     */
    public boolean isChild() {
        Menu child = valueOf(this.name());
        if (child.children().size() == 0)
            return false;
        return true;
    }


}
