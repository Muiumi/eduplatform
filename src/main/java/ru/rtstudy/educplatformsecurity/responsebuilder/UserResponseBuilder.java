package ru.rtstudy.educplatformsecurity.responsebuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.rtstudy.educplatformsecurity.dto.mapper.impl.UserMapper;
import ru.rtstudy.educplatformsecurity.dto.request.UserUpdateDto;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.dto.response.UserDtoResponse;
import ru.rtstudy.educplatformsecurity.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserResponseBuilder {

    private final UserService userService;
    private final UserMapper mapper;

    public ResponseEntity<UserDtoResponse> findUser() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.toDto(userService.findUser()));
    }

    public ResponseEntity<UserUpdateDto> updateUser(UserUpdateDto userUpdateDto) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(userService.updateUser(userUpdateDto));
    }

    public ResponseEntity<List<CourseLongDescriptionDto>> getAllStartedCourse() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAllStartedCourse());
    }
}
