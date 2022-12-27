package io.github.MichaelAnderson19.TodoAPI.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id") //user_id?
    private Long id;
    @Column(name="username", nullable=false)
    private String username;
    @Column(name="email", nullable=false)
    private String email;
    @Column(name="password", nullable=false)
    private String password;
    @Column(name="roles")
    private String roles; //should be a collection

}
