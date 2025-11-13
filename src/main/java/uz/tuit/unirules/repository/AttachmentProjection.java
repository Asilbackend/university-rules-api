package uz.tuit.unirules.repository;

public interface AttachmentProjection {
    Long getAttachmentId();

    Double getProgress();

    String getTitle();

    String getDescription();

    String getThumbnailImageUrl();
}
