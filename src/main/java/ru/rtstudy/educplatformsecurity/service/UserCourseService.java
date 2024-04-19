package ru.rtstudy.educplatformsecurity.service;

import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.model.Course;
import ru.rtstudy.educplatformsecurity.model.User;

import java.util.List;

public interface UserCourseService {

    void enterOnCourse(Long id);

    void upgradeToMentor(Long id);

    boolean onCourse(Long course, Long userId);

    void finishCourse(Long userId, Long courseId);

    List<CourseLongDescriptionDto> getAllStartedCourse(Long userId);

    void makeCourseMentor(User user, Course course);
}

