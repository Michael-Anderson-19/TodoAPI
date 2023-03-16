package io.github.MichaelAnderson19.TodoAPI.repository;

import io.github.MichaelAnderson19.TodoAPI.model.TodoItem;
import io.github.MichaelAnderson19.TodoAPI.shared.ItemPriority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

    Optional<TodoItem> findByIdAndUserEmail(Long id, String userEmail);

    List<TodoItem> findAllByUserEmail(String userEmail);

    List<TodoItem> findAllByUserEmailAndPriority(String email, ItemPriority priority);

    List<TodoItem> findAllByUserEmailAndComplete(String email, boolean complete);

    List<TodoItem> findAllByUserEmailAndPriorityAndComplete(String email, ItemPriority priority, boolean complete);
}
