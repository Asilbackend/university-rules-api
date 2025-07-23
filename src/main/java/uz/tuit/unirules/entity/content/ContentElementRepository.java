package uz.tuit.unirules.entity.content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContentElementRepository extends JpaRepository<ContentElement, Long> {
    boolean existsByTitle(String title);

    @Query("""
            select ce.text
            from ContentElement ce
            where ce.title = :title
            """)
    Optional<String> findTextByTitle(String title);
    @Query("select ce.id from ContentElement ce where ce.content.id=:contentId and  ce.attachment.id=:attachmentId and ce.content.isDeleted=false")
    Long findContentElementIdByContentIdAndAttachmentId(Long contentId, Long attachmentId);

    @Query(
            """
                    select ce from ContentElement ce where ce.title=:title
                                    """
    )
    Optional<ContentElement> findByTitle(String title);

    @Query("""
            select ce.id from ContentElement ce where ce.title=:title
                            """
    )
    Long findIdByTitle(String title);

    Long findIdById(Long contentElementId);

    @Query("""
               select ce.id from ContentElement ce where ce.title=:title and ce.content.id=:contentId
            """)
    Long findIdByContentIdAndTitle(Long contentId, String title);

    @Query("""
               select count (ce.id) from ContentElement ce where ce.content.id=:contentId
               and ce.content.isDeleted=false
            """)
    Integer countByContentId(Long id);

    @Query("""
               select ce from ContentElement ce where ce.content.id=:contentId
               and ce.content.isDeleted=false
            """)
    List<ContentElement> findAllByContentId(Long contentId);

}