package ru.rtstudy.educplatformsecurity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rtstudy.educplatformsecurity.dto.request.UserUpdateDto;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.exception.entity.UserNotFoundException;
import ru.rtstudy.educplatformsecurity.exception.student.UserNotMentorException;
import ru.rtstudy.educplatformsecurity.exception.user.UserNotEnterOnAnyCourseException;
import ru.rtstudy.educplatformsecurity.model.User;
import ru.rtstudy.educplatformsecurity.model.constant.Role;
import ru.rtstudy.educplatformsecurity.repository.UserRepository;
import ru.rtstudy.educplatformsecurity.service.UserCourseService;
import ru.rtstudy.educplatformsecurity.service.UserService;
import ru.rtstudy.educplatformsecurity.util.Util;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserCourseService userCourseService;
    private final UserRepository userRepository;
    private final Util util;

    @Override
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findUserByEmail(email)
                .orElseThrow(() -> {
                    log.info("{} was not found.", util.findUserFromContext().getEmail(), new UserNotMentorException("User not found."));
                    return new UserNotFoundException("User not found.");
                });
    }

    @Override
    public void changeUserRole(Long id, Role newRole) {
        User user = userRepository.getReferenceById(id);
        log.info("{} change role to: {}", util.findUserFromContext().getEmail(), newRole);
        user.setRole(newRole);
        userRepository.saveAndFlush(user);
    }

    @Override
    public User findUser() {
        User user = util.findUserFromContext();
        log.info("{} searching information about user.", user.getEmail());
        return userRepository
                .findById(user.getId())
                .orElseThrow(() -> {
                    log.error("{} was not found", user.getEmail());
                    return new UserNotFoundException("User not found.");
                });
    }

    @Override
    public UserUpdateDto updateUser(UserUpdateDto userUpdateDto) {
        User user = util.findUserFromContext();
        log.info("{} update his information to: {}", user.getEmail(), userUpdateDto);
        user.setFirstName(userUpdateDto.firstName());
        user.setLastName(userUpdateDto.lastName());
        userRepository.saveAndFlush(user);
        log.info("{} was upgraded to: {} {}", util.findUserFromContext().getEmail(), user.getFirstName(), user.getLastName());
        return userUpdateDto;
    }

    @Override
    public List<CourseLongDescriptionDto> getAllStartedCourse() {
        User user = util.findUserFromContext();
        log.info("{} change trying to get his started courses.", user.getEmail());
        List<CourseLongDescriptionDto> allStartedCourse = userCourseService.getAllStartedCourse(user.getId());
        if (allStartedCourse.isEmpty()) {
            log.error("{} not enter on any course", user.getEmail(), new UserNotEnterOnAnyCourseException("User not enter on any course."));
            throw new UserNotEnterOnAnyCourseException("You are not enter on any course.");
        } else {
            return allStartedCourse;
        }
    }
}