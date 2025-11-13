package uz.tuit.unirules.projections;

public interface TopVideoProjection {
    String getTitle();

    Long getAttachmentId();

    String getThumbnailImageUrl();

    boolean getRequired();

    String getDescription();
}
