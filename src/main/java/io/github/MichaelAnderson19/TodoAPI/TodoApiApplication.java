package io.github.MichaelAnderson19.TodoAPI;

import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.RegistrationRequestDto;
import io.github.MichaelAnderson19.TodoAPI.service.impl.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class TodoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoApiApplication.class, args);
    }


}
