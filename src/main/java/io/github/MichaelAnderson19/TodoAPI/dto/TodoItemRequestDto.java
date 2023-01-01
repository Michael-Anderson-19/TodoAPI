package io.github.MichaelAnderson19.TodoAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoItemRequestDto {

    private String content;
    private String priority;
    private boolean complete;


}
