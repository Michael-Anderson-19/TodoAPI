package io.github.MichaelAnderson19.TodoAPI.repository;

import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.model.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    // @Modifying
    //not needed only for JPQL and @Query stuff
    int deleteByUser(User user); // userid and

    void deleteByUserEmail(String email);
}
