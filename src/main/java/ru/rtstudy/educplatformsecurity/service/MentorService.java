package ru.rtstudy.educplatformsecurity.service;

import ru.rtstudy.educplatformsecurity.dto.request.MentorAnswerDtoRequest;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.dto.response.GradeDtoResponse;
import ru.rtstudy.educplatformsecurity.dto.response.GradeStudentDtoResponse;
import ru.rtstudy.educplatformsecurity.model.Course;

import java.util.List;

public interface MentorService {

    List<Course> getAllCoursesForMentor();

    List<GradeDtoResponse> getAllAnswersForMentorCourses();

    List<GradeStudentDtoResponse> getAllAnswersForCourse(Long id);

    List<GradeStudentDtoResponse> getAllAnswersForLesson(Long id);

    MentorAnswerDtoRequest reviewStudentAnswer(Long id, MentorAnswerDtoRequest mentorAnswerDtoRequest);

    MentorAnswerDtoRequest updateMentorAnswer(Long id, MentorAnswerDtoRequest mentorAnswerDtoRequest);

    void upgradeToAuthor();

    List<CourseLongDescriptionDto> getMentorCourses();
}
