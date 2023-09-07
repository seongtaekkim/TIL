package me.staek.chapter03.item13.clone_use_constructor;


public class Item implements Cloneable {

    private String name;

    /**
     * Object 의 clone() 을 호출하지 않고 임의대로 생성해서 리턴하게 되면
     * 자식클래스에 Item 타입이 전달되는데, 부모타입은 자식타입으로 cast가 안되기 때문에 ClasscastException이 발생한다.
     * @return
     */
    @Override
    public Item clone() {
        Item item = new Item();
        item.name = this.name;
        return item;
    }

    /**
     * clone은 아래처럼 구현해야 한다.
     * 1. 리턴타입은 해당 클래스 타입
     * 2. CloneNotSupportedException 은 unchecked 로 변경
     * 3. protected -> public 변경
     */
//    @Override
//    public Item clone() {
//        Object clone = null;
//        try {
//            clone = super.clone();
//            return (Item) clone;
//        } catch (CloneNotSupportedException e) {
//            throw new RuntimeException();
//        }
//    }
}
