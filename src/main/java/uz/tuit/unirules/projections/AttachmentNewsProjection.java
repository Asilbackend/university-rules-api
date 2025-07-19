package uz.tuit.unirules.projections;

public interface AttachmentNewsProjection {
    Long getNewsId();
    Long getAttachmentId();
    String getThumbNailUrl();

    String getName();
    String getDescription();
}
