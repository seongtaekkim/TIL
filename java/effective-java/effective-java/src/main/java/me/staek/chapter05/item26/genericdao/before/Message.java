package me.staek.chapter05.item26.genericdao.before;

public class Message implements Entity {

    private Long id;

    private String body;

    @Override
    public Long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }
}
