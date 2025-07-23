package uz.tuit.unirules.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uz.tuit.unirules.dto.respond_dto.NewsRespDto;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.news.News;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    @Mapping(target = "newsId", source = "id")
    @Mapping(target = "attachmentId", source = "attachment", qualifiedByName = "findAttachmentId")
    NewsRespDto toRespDto(News news);

    @Named(value = "findAttachmentId")
    static Long findUserIdByUser(Attachment attachment) {
        return attachment == null ? null : attachment.getId();
    }
}
