package ru.rtstudy.educplatformsecurity.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.rtstudy.educplatformsecurity.api.AuthenticationApi;
import ru.rtstudy.educplatformsecurity.auth.AuthenticationService;
import ru.rtstudy.educplatformsecurity.dto.request.SignInRequest;
import ru.rtstudy.educplatformsecurity.dto.request.SignUpRequest;
import ru.rtstudy.educplatformsecurity.dto.response.TokenDto;
import ru.rtstudy.educplatformsecurity.dto.response.UserDtoResponse;
import ru.rtstudy.educplatformsecurity.model.User;
import ru.rtstudy.educplatformsecurity.service.LessonService;
import ru.rtstudy.educplatformsecurity.util.Util;

//TODO: Где ResponseBuilder?
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationApi {

    private final AuthenticationService authenticationService;
    private final LessonService lessonService;
    private final Util util;

    @Override
    public ResponseEntity<UserDtoResponse> signup(SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    @Override
    public ResponseEntity<TokenDto> signIn(SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }
    //TODO: В Controller !!!НЕ!!! должно быть логики. Вынести в ResponseBuilder
    @Override
    public Boolean forAuthentication() {
        User user = util.findUserFromContext();
        return authenticationService.isAuthor(user.getId());
    }
    //TODO: В Controller !!!НЕ!!! должно быть логики. Вынести в AuthenticationServiceImpl
    @Override
    public Boolean forAuthenticationToDelete(String fileName) {
        User user = util.findUserFromContext();
        if (authenticationService.isAuthor(user.getId()) &&
            authenticationService.hasCredentialToDelete(fileName)) {
            lessonService.deleteFile(fileName);
            return true;
        } else {
            return false;
        }
    }
}
