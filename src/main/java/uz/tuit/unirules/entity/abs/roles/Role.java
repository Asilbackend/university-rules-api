package uz.tuit.unirules.entity.abs.roles;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import uz.tuit.unirules.entity.abs.BaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
/*@SuperBuilder*/
public class Role extends BaseEntity implements GrantedAuthority {
    private String role;

    @Override
    public String getAuthority() {
        return this.role;
    }
}
