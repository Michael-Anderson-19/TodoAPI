package io.github.MichaelAnderson19.TodoAPI.model;

import io.github.MichaelAnderson19.TodoAPI.shared.ItemPriority;
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
@Table(name="todo_items")
public class TodoItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="content")
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(name="priority")
    private ItemPriority priority;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
    @Column(name="complete")
    private boolean complete;
}
