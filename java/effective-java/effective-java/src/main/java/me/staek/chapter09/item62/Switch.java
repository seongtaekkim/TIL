package me.staek.chapter09.item62;

/**
 * toggle switch 는 인스턴스 생성시 false 상태이다.
 */
public class Switch {

    private boolean toggle;

    public Switch() {
        this.toggle = false;
    }

    public boolean isToggle() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    @Override
    public String toString() {
        return "Switch{" +
                "toggle=" + toggle +
                '}';
    }
}
