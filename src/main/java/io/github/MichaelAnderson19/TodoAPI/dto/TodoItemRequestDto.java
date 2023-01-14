package io.github.MichaelAnderson19.TodoAPI.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoItemRequestDto {

    @NotEmpty(message="Content must not be empty")
    private String content;

    @NotEmpty(message="Items must have a priority")
    private String priority;

    private boolean complete;


}
