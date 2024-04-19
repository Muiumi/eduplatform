package ru.rtstudy.educplatformsecurity.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rtstudy.educplatformsecurity.dto.request.JwtRefreshToken;
import ru.rtstudy.educplatformsecurity.dto.request.SignInRequest;
import ru.rtstudy.educplatformsecurity.dto.request.SignUpRequest;
import ru.rtstudy.educplatformsecurity.dto.response.JwtTokenDto;
import ru.rtstudy.educplatformsecurity.dto.response.UserDtoResponse;
import ru.rtstudy.educplatformsecurity.responsebuilder.AuthenticationResponseBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication Controller", description = "Authentication Controller API")
public class AuthenticationController {

    private final AuthenticationResponseBuilder responseBuilder;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("signUp")
    public ResponseEntity<UserDtoResponse> signup(@Valid @RequestBody SignUpRequest request) {
        return responseBuilder.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("signIn")
    public ResponseEntity<JwtTokenDto> signIn(@Valid @RequestBody SignInRequest request) {
        return responseBuilder.signIn(request);
    }

    @Operation(summary = "Получение нового access-токена")
    @PostMapping("refresh-token")
    public ResponseEntity<JwtTokenDto> refreshToken(@Valid @RequestBody JwtRefreshToken jwtRefreshToken) {
        return responseBuilder.refreshToken(jwtRefreshToken);
    }

    @Hidden
    @Operation(summary = "Служебный метод для модуля MinIO")
    @GetMapping("verify")
    public Boolean verification() {
        return Boolean.TRUE;
    }

    @Hidden
    @Operation(summary = "Служебный метод для модуля MinIO")
    @GetMapping("author-verify")
    public Boolean authorVerification() {
        return responseBuilder.verificationAuthorRequest();
    }

    @Hidden
    @Operation(summary = "Служебный метод для модуля MinIO")
    @GetMapping("verify-for-delete")
    public Boolean deleteVerification(@RequestParam(value = "file-name") String fileName) {
        return responseBuilder.verificationToDelete(fileName);
    }
}
