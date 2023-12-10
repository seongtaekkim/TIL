package me.staek.chapter08.item49;

import java.util.Objects;

class NullTest {

}

/**
 * requireNonNull
 * - 널체크, 예외메세지 추가 가능
 *
 * check** 매서드
 * - 닫힌범위 체크 불가
 * - 리스트, 배열에서 사용
 * - 예외메세지 추가 못함
 */
public class ObjectsNullCheck {
    public static void main(String[] args) {
        NullTest nullTest = null;
//        Objects.requireNonNull(nullTest, "널났다!ㄴ");

        /**
         * check: index < length
         */
        Objects.checkIndex(1,2);

        /**
         * check: fromIndex + size <= length
         *        Range [fromIndex, fromIndex + size)
         */
        Objects.checkFromIndexSize(1,2,3);

        /**
         * check: toIndex <= length
         */
        Objects.checkFromToIndex(1,2,4);
    }
}
