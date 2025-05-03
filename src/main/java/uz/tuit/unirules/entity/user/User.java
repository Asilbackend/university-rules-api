package uz.tuit.unirules.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.tuit.unirules.entity.abs.BaseEntity;
import uz.tuit.unirules.entity.abs.roles.Role;
import uz.tuit.unirules.entity.faculty.group.Group;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
/*
 * User, module, videolar, testlar,
 *  user-question, question-natija, ichki-tartib-intizom-table,
 * sertifikat, modul ichidagi videoni baholash, supportga
 * murojaat, notifications
 * */
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    @Column(nullable = false, length = 50)
    private String firstname;

    @Column(nullable = false, length = 50)
    private String lastname;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 20)
    private String phone;
    @Builder.Default
    private boolean active = false;
    @Builder.Default
    private boolean passedTest = false;

    private String language;
    @Builder.Default
    private Boolean isDeleted = false;
    @ManyToOne(optional = true)
    private Group group;

    @ManyToOne
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }
}
