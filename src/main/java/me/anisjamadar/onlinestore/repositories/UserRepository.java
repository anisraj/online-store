package me.anisjamadar.onlinestore.repositories;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import me.anisjamadar.onlinestore.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}