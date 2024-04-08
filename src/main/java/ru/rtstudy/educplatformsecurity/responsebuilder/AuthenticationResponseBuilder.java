package ru.rtstudy.educplatformsecurity.responsebuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.rtstudy.educplatformsecurity.auth.AuthenticationService;
import ru.rtstudy.educplatformsecurity.dto.request.JwtRefreshToken;
import ru.rtstudy.educplatformsecurity.dto.request.SignInRequest;
import ru.rtstudy.educplatformsecurity.dto.request.SignUpRequest;
import ru.rtstudy.educplatformsecurity.dto.response.JwtTokenDto;
import ru.rtstudy.educplatformsecurity.dto.response.SuccessfulSignInDto;
import ru.rtstudy.educplatformsecurity.dto.response.UserDtoResponse;
import ru.rtstudy.educplatformsecurity.model.User;
import ru.rtstudy.educplatformsecurity.service.LessonService;
import ru.rtstudy.educplatformsecurity.util.Util;


@Service
@RequiredArgsConstructor
public class AuthenticationResponseBuilder {

    private final AuthenticationService authenticationService;
    private final LessonService lessonService;
    private final Util util;

    public ResponseEntity<UserDtoResponse> signUp(SignUpRequest signUpRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authenticationService.signUp(signUpRequest));
    }

    public ResponseEntity<SuccessfulSignInDto> signIn(SignInRequest signInRequest) {
        JwtTokenDto tokenDto = authenticationService.signIn(signInRequest);
        SuccessfulSignInDto response = SuccessfulSignInDto.builder()
                .email(tokenDto.user().getEmail())
                .firstName(tokenDto.user().getFirstName())
                .surname(tokenDto.user().getLastName())
                .role(tokenDto.user().getRole())
                .refreshToken(tokenDto.refreshToken())
                .build();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .header("Authorization", "Bearer " + tokenDto.token())
                .body(response);
    }

    public Boolean verificationAuthorRequest() {
        User user = util.findUserFromContext();
        return authenticationService.isAuthor(user.getId());

    }

    public Boolean verificationToDelete(String fileName) {
        User user = util.findUserFromContext();
        if (authenticationService.isAuthor(user.getId()) &&
            authenticationService.hasCredentialToDelete(fileName)) {
            lessonService.deleteFile(fileName);
            return true;
        } else {
            return false;
        }
    }

    public ResponseEntity<JwtTokenDto> refreshToken(JwtRefreshToken jwtRefreshToken) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(authenticationService.refreshToken(jwtRefreshToken));
    }
}
