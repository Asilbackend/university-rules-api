package uz.tuit.unirules.projections;

public interface NewsVideoProjection {
    Long getAttachmentId();

    String getThumbNailUrl();

    String getVideoUrl();

    Integer getLikeCount();

    boolean getIsLiked();

    String getTitle();

    String getDescription();

    String getVideoDuration();

    Integer getSeenCount();
}
