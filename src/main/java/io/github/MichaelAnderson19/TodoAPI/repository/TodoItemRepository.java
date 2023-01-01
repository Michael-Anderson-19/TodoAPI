package io.github.MichaelAnderson19.TodoAPI.repository;

import io.github.MichaelAnderson19.TodoAPI.model.TodoItem;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

    Optional<TodoItem> findByIdAndUser(Long id, User user);
    List<TodoItem> findAllByUser(User user);
}
