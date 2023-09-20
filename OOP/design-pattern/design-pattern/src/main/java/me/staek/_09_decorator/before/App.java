package me.staek._09_decorator.before;

public class App {

    public static void main(String[] args) {
        Client client = new Client(new SpamFilteringCommentService());
        client.writeComment("데코레이터");
        client.writeComment("패       턴");
        client.writeComment("https://:ㅁㅈㅎㅈㄷㅎ");
        System.out.println("=============================");
        client = new Client(new TrimmingCommentService());
        client.writeComment("데코레이터");
        client.writeComment("패       턴");
        client.writeComment("https://:ㅁㅈㅎㅈㄷㅎ");
    }
}
