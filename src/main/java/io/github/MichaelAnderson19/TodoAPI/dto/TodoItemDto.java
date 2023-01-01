package io.github.MichaelAnderson19.TodoAPI.dto;

import io.github.MichaelAnderson19.TodoAPI.model.TodoItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoItemDto {

    private Long id;
    private String userEmail;
    private String content;

    public static TodoItemDto mapToDto(TodoItem item) {
        return TodoItemDto.builder()
                .id(item.getId())
                .content(item.getContent())
                .userEmail(item.getUser().getEmail())
                .build();
    }
}
