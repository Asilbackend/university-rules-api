package uz.tuit.unirules.controller.student;

public record AttachmentDetail(
        String contentType,
        String videoDuration,
        Boolean isRead,
        String title,
        Integer order_element
) {
}