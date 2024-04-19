package ru.rtstudy.educplatformsecurity.dto.response;

import lombok.Builder;
import ru.rtstudy.educplatformsecurity.model.constant.Role;

import java.util.Date;

@Builder
public record JwtTokenDto(
        String email,
        String firstName,
        String lastName,
        String refreshToken,
        Date refreshExpiration,
        String accessToken,
        Date accessExpiration,
        Role role
) {
}
