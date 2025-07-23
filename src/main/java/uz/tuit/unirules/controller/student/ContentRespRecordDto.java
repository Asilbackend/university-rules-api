package uz.tuit.unirules.controller.student;

import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ContentRespRecordDto {

    private Long contentId;
    private String contentTitle;
    private String userContentStatus;
    private Boolean isRequiredContent;
    private List<Map<Object, Object>> attachmentDetails;

    @Setter
    @Getter
    public static class AttachmentDetail {
        // Getter va Setter'lar
        @JsonProperty("contentType")
        private String contentType;

        @JsonProperty("videoDuration")
        private String videoDuration; // Integer o‘rniga String

        @JsonProperty("isRead")
        private Boolean isRead;

        @JsonProperty("title")
        private String title;

        @JsonProperty("orderElement") // JSON'da "order_element" sifatida kelayapti
        private Integer orderElement;

    }
}

