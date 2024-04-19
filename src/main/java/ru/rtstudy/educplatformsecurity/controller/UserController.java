package ru.rtstudy.educplatformsecurity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rtstudy.educplatformsecurity.dto.request.UserUpdateDto;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.dto.response.UserDtoResponse;
import ru.rtstudy.educplatformsecurity.responsebuilder.UserResponseBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "User Controller API")
@RequestMapping("api/v1/users")
public class UserController {

    private final UserResponseBuilder responseBuilder;

    @Operation(summary = "Получить информацию о пользователе")
    @GetMapping
    public ResponseEntity<UserDtoResponse> getUserInfo() {
        return responseBuilder.findUser();
    }

    @Operation(summary = "Изменить информацию о пользователе")
    @PutMapping
    public ResponseEntity<UserUpdateDto> updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        return responseBuilder.updateUser(userUpdateDto);
    }

    @Operation(summary = "Получить список курсов на которые поступил пользователь")
    @GetMapping("courses")
    public ResponseEntity<List<CourseLongDescriptionDto>> getAllStartedCourse() {
        return responseBuilder.getAllStartedCourse();
    }
}
