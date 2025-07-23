package uz.tuit.unirules.projections;

public interface NewsVideoProjection {
    Long getAttachmentId();

    String getThumbNailUrl();

    String getLikeCount();

    boolean getIsLiked();

    String getTitle();

    String getDescription();

    String getVideoDuration();
}
