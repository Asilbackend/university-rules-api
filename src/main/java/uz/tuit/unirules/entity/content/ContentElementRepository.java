package uz.tuit.unirules.entity.content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContentElementRepository extends JpaRepository<ContentElement, Long> {
    boolean existsByTitle(String title);
    @Query("""
       select ce.text
       from ContentElement ce
       where ce.title = :title
       """)
    Optional<String> findTextByTitle(String title);

}