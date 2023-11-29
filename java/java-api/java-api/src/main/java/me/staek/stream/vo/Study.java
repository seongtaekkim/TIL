package me.staek.stream.vo;

public class Study {
    private int id;
    private String title;
    private boolean isClosed;

    public Study(int id, String title, boolean isClosed) {
        this.id = id;
        this.title = title;
        this.isClosed = isClosed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
