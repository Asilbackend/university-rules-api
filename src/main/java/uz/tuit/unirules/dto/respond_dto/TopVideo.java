package uz.tuit.unirules.dto.respond_dto;

public record TopVideo(
        Long attachmentId,
        Long contentId,
        String posterUrl,
        String name,
        Boolean isRequired
) {

}
