package me.staek._09_decorator.after;

public class App {
    public static void main(String[] args) {
        CommentService commentService = new DefaultCommentService();

        if (false)
            commentService = new TrimmingCommentDecorator(commentService);

        if (true)
            commentService = new SpamFilteringCommentDecorator(commentService);


        Client client = new Client(commentService);
        client.writeComment("1111");
        client.writeComment("222   222");
        client.writeComment("httpawgaweg");
    }
}
