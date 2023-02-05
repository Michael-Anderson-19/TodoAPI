package io.github.MichaelAnderson19.TodoAPI.model.security;

import io.github.MichaelAnderson19.TodoAPI.model.BaseEntity;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "refresh_token")
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "token")
    private String token;

    @Column(name = "expiry_date")
    private Instant expiryDate;


}
