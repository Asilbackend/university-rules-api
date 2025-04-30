package uz.tuit.unirules.projections;


import java.util.List;

public interface ContentProjection {
    Long getId();

    String getTitle();

    String getBody();

    List<Long> getAttachmentIds();

    Long getModuleId();

    Double getAverageContentRating();
}