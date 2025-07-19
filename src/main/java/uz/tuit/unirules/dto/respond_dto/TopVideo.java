package uz.tuit.unirules.dto.respond_dto;

public record TopVideo(
        Long attachmentId,
        Long contentId,
        String thumbNailUrl,
        String name,
        Boolean isRequired
) {

}
