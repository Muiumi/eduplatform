package ru.rtstudy.educplatformsecurity.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import ru.rtstudy.educplatformsecurity.model.constant.Role;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record SuccessfulSignInDto(
        String email,
        String firstName,
        String surname,
        String refreshToken,
        Role role
) {
}
