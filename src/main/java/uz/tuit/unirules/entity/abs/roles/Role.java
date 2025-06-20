package uz.tuit.unirules.entity.abs.roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import uz.tuit.unirules.entity.abs.BaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
/*@SuperBuilder*/
public class Role extends BaseEntity implements GrantedAuthority {
    @Column(unique = true, nullable = false)
    private String role;

    @Override
    public String getAuthority() {
        return this.role;
    }
}
