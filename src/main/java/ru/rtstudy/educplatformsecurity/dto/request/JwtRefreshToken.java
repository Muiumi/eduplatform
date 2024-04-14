package ru.rtstudy.educplatformsecurity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record JwtRefreshToken(
        @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJpbmFAeWFuZGV4LmNvbSIsImlhdCI6MTcwODc3MTUxMSwiZXhwIjoxNzA4ODU3OTExfQ.zzsW4AvOjwoDmeS0CRAojZBbWvIj6mgoUH3Rt4EXy2w")
        @NotBlank(message = "Token must be input")
        String refreshToken) {
}
