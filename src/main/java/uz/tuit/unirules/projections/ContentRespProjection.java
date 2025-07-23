package uz.tuit.unirules.projections;

import java.util.List;
import java.util.Map;

public interface ContentRespProjection {

    Long getContentId();

    String getContentTitle();

    String getUserContentStatus();

    Boolean getIsRequiredContent();

    List<AttachmentDetail> getAttachmentDetails();

    interface AttachmentDetail {
        String getContentType();

        String getVideoDuration();

        Boolean getIsRead();

        String getTitle();

        Integer getOrderElement();
    }
}
