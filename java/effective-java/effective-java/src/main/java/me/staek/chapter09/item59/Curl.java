package me.staek.chapter09.item59;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * java9에 요청 url 에 대한 응답을 출력 해주는 함수가 추가되었다.
 * 알면 사용하고, 모르면 힘들게 만들어야 할 것이다.
 */
public class Curl {
    public static void main(String[] args) throws IOException {
        try (InputStream in = new URL(args[0]).openStream()) {
            in.transferTo(System.out);
        }
    }
}
