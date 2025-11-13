package uz.tuit.unirules.projections;

public interface CommentProjection {
    Long getCommentId();

    String getComment();

    String getFirstName();

    String getLastName();

    Long getUserId();

    boolean getOwn();
}
