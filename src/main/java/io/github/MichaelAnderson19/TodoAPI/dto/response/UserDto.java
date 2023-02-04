package io.github.MichaelAnderson19.TodoAPI.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String email;
    private String username;
    private String role;
    //private String first name;
    //private String last name;
    //private String displayImage;
}
