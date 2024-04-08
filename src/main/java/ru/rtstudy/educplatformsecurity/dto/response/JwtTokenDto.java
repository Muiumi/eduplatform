package ru.rtstudy.educplatformsecurity.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import ru.rtstudy.educplatformsecurity.model.User;


@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record JwtTokenDto(
        User user,
        String token,
        String refreshToken)
{
}
