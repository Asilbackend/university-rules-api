package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.tuit.unirules.entity.user.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
