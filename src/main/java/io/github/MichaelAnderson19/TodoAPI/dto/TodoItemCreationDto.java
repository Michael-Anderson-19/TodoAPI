package io.github.MichaelAnderson19.TodoAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoItemCreationDto {

    private String content;
    //private Priority priority; <- list of priorities to get from the backend and display in creation form drop down


}
