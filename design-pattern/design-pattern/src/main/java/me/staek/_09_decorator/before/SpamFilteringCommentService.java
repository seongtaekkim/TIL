package me.staek._09_decorator.before;

class SpamFilteringCommentService extends CommentService {

    @Override
    public void addComment(String comment) {
        if (isNotSpam(comment)) {
            super.addComment(comment);
        }
    }
    private boolean isNotSpam(String comment) {
        return !comment.contains("http");
    }
}
