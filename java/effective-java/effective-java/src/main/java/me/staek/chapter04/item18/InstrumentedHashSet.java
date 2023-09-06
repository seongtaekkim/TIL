package me.staek.chapter04.item18;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * TODO 코드의 기댓값은 3이지만 실제론 6이다.
 *      -> 부모클래스의 addAll는 add를 호출하기에 자식클래스의 add를 호출. (override 했기 때문)
 *      상속의 위험성
 *      예제에서처럼 부모 함수내부에서 어떤 일이 일어나는지 완전히 모르면, side effect가능성이 항상 있다.
 *      부모클래스에 어떤 요소가 추가(변경)하는 함수가 만들어졌을 때, 그 기능이 추가(변경)되었다는 사실 자체를 알기어렵다.
 *      -> 컴파일은 되지만, 런타임중 기능이 바뀌어버림
 *      자식클래스가 정의한 함수 abc가 잇는데, 어느 순간 부모클래스가 같은 이름으로 정의하게 되었다면 코드가 깨진다.
 *
 * TODO 상속은 지양하는게 좋다.
 */
public class InstrumentedHashSet<E> extends HashSet<E> {
    // 추가된 원소의 수
    private int addCount = 0;

    public InstrumentedHashSet() {
    }

    public InstrumentedHashSet(int initCap, float loadFactor) {
        super(initCap, loadFactor);
    }

    @Override public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }

    public static void main(String[] args) {
        InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
        s.addAll(List.of("apple", "banana", "cherry"));
        System.out.println(s.getAddCount());
    }
}
