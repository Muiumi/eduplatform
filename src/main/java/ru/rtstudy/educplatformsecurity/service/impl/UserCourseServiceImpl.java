package ru.rtstudy.educplatformsecurity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.exception.entity.CourseNotFoundException;
import ru.rtstudy.educplatformsecurity.exception.mentor.NotEnoughScoreToMentorException;
import ru.rtstudy.educplatformsecurity.exception.student.AlreadyMentorException;
import ru.rtstudy.educplatformsecurity.exception.user.UserNotEnterOnAnyCourseException;
import ru.rtstudy.educplatformsecurity.model.Course;
import ru.rtstudy.educplatformsecurity.model.Grade;
import ru.rtstudy.educplatformsecurity.model.User;
import ru.rtstudy.educplatformsecurity.model.UserCourse;
import ru.rtstudy.educplatformsecurity.model.constant.Role;
import ru.rtstudy.educplatformsecurity.repository.UserCourseRepository;
import ru.rtstudy.educplatformsecurity.service.CourseService;
import ru.rtstudy.educplatformsecurity.service.GradeService;
import ru.rtstudy.educplatformsecurity.service.UserCourseService;
import ru.rtstudy.educplatformsecurity.service.UserService;
import ru.rtstudy.educplatformsecurity.util.Util;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseRepository userCourseRepository;

    private final CourseService courseService;
    private final GradeService gradeService;
    private final UserService userService;
    private final Util util;

    private final int THRESHOLD_TO_BECOME_MENTOR = 8;

    @Override
    public void enterOnCourse(Long id) {
        log.info("{} enter on course: {}", util.findUserFromContext().getEmail(), id);
        UserCourse userCourse = UserCourse.builder()
                .user(util.findUserFromContext())
                .beginCourse(LocalDateTime.now())
                .course(courseService
                        .findById(id)
                        .orElseThrow(() -> {
                            log.error("Course was not found: {}", id, new CourseNotFoundException("Course was not found."));
                            return new CourseNotFoundException("Course was not found.");
                        }))
                .build();
        userCourseRepository.save(userCourse);
        log.info("{} was successful enter on course: {}", util.findUserFromContext().getEmail(), userCourse.getCourse().getId());
    }

    @Override
    public void upgradeToMentor(Long courseId) {
        User user = util.findUserFromContext();
        log.info("{} send request upgrade to mentor by course: {}", user.getEmail(), courseId);
        boolean courseMentor = userCourseRepository.alreadyCourseMentor(user.getId(), courseId);
        if (courseMentor) {
            log.error("{} already mentor of course: {}", user.getEmail(), courseId, new AlreadyMentorException("You already mentor this course"));
            throw new AlreadyMentorException("You already mentor this course");
        }
        List<Long> lessonsIds = gradeService.getAllLessonsId(courseId);
        List<Grade> gradeList = gradeService.getAllGradesByLesson(lessonsIds, user.getId());
        double sumOfAllGrades = 0;
        for (Grade grade : gradeList) {
            sumOfAllGrades += grade.getGrade();
        }
        double averageValue = sumOfAllGrades / gradeList.size();
        if (averageValue >= THRESHOLD_TO_BECOME_MENTOR) {
            userService.changeUserRole(user.getId(), Role.ROLE_MENTOR);
        } else {
            log.error("Student: {}, don't have enough score to became mentor: {}", user.getEmail(), averageValue, new NotEnoughScoreToMentorException("You don't have enough score to became a mentor."));
            throw new NotEnoughScoreToMentorException("You don't have enough score to became a mentor.");
        }
    }

    @Override
    public boolean onCourse(Long courseId, Long userId) {
        return userCourseRepository.onCourse(courseId, userId);
    }

    @Override
    public void finishCourse(Long userId, Long courseId) {
        userCourseRepository.finishCourse(userId, courseId);
    }

    @Override
    public List<CourseLongDescriptionDto> getAllStartedCourse(Long userId) {
        return userCourseRepository
                .getAllStartedCourse(userId)
                .orElseThrow(() -> new UserNotEnterOnAnyCourseException("You are not enter on any course."));
    }

    @Override
    public void makeCourseMentor(User user, Course course) {
        UserCourse userCourse = UserCourse.builder()
                .mentorCourse(true)
                .beginCourse(LocalDateTime.now())
                .finishCourse(LocalDateTime.now())
                .endCourse(true)
                .user(user)
                .course(course)
                .build();
        userCourseRepository.save(userCourse);
    }
}