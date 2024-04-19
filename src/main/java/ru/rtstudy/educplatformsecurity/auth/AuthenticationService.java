package ru.rtstudy.educplatformsecurity.auth;

import ru.rtstudy.educplatformsecurity.dto.request.JwtRefreshToken;
import ru.rtstudy.educplatformsecurity.dto.request.SignInRequest;
import ru.rtstudy.educplatformsecurity.dto.request.SignUpRequest;
import ru.rtstudy.educplatformsecurity.dto.response.JwtTokenDto;
import ru.rtstudy.educplatformsecurity.dto.response.UserDtoResponse;

public interface AuthenticationService {

    UserDtoResponse signUp(SignUpRequest request);

    JwtTokenDto signIn(SignInRequest request);

    boolean hasCredentialToDelete(String fileName);

    boolean isAuthor(Long userId);

    JwtTokenDto refreshToken(JwtRefreshToken jwtRefreshToken);
}
