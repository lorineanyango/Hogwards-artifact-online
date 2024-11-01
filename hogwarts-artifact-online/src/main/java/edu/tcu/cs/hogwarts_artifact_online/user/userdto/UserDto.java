package edu.tcu.cs.hogwarts_artifact_online.user.userdto;

import jakarta.validation.constraints.NotEmpty;

// the reason as to why the dto don't have the password It's because when the user wants to view all users then we dont want them to see the password of other users
public record UserDto(
        Integer id,

        @NotEmpty(message = "username required")
        String username,

        boolean enabled,

        @NotEmpty(message = "role required")
        String role
) {
}
