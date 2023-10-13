package com.example.OnlineExam.dto.user;

public record UserDTO(
        Integer id,
        String username,
        boolean enabled,
        String name,
        String surname
) {
}
