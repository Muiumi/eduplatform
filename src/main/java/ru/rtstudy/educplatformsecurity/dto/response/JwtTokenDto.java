package ru.rtstudy.educplatformsecurity.dto.response;

import lombok.Builder;
import ru.rtstudy.educplatformsecurity.model.User;

@Builder
public record JwtTokenDto(
        User user,
        String token,
        String refreshToken)
{
}
