package io.github.MichaelAnderson19.TodoAPI.repository;

import io.github.MichaelAnderson19.TodoAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
