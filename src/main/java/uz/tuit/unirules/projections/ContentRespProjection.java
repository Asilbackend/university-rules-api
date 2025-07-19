package uz.tuit.unirules.projections;

import java.util.List;
import java.util.Map;

public interface ContentRespProjection {
    Long getContentId();

    String getContentTitle();

    List<Map<String, Object>> getAttachmentDetails();
}
