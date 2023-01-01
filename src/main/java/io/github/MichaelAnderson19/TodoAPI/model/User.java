package io.github.MichaelAnderson19.TodoAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="users")
public class User extends BaseEntity{

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
    private String roles;
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @JsonIgnore
    private List<TodoItem> items = new ArrayList<>();

}
