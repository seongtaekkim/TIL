package me.staek.chapter06.item35;

/**
 * 열거타입상수 값을 정하지않고 original을 사용한다면,
 * 처음에 정의한 열거타입상수 순서를 변경하거나 삭제,추가한다면 모든 열거타입상수의 original 값이 변경될 수 있어
 * 유지보수가 매우 까다롭다.
 */
public enum EnumWithOriginal {
        SOLO, DUET, TRIO, QUARTET, QUINTET,
        SEXTET, SEPTET, OCTET, DOUBLE_QUARTET,
        NONET, DECTET, TRIPLE_QUARTET;


    public int numberOfMusicians() {
            return ordinal() + 1;
    }

    public static void main(String[] args) {
        System.out.println(EnumWithOriginal.SOLO.numberOfMusicians());
        System.out.println(EnumWithOriginal.TRIPLE_QUARTET.numberOfMusicians());
    }
}
