package ru.rtstudy.educplatformsecurity.service;

import ru.rtstudy.educplatformsecurity.dto.request.ChangeStudentAnswerDto;
import ru.rtstudy.educplatformsecurity.dto.request.StudentAnswerDto;
import ru.rtstudy.educplatformsecurity.dto.response.AllStudentAnswers;
import ru.rtstudy.educplatformsecurity.dto.response.GradeDtoResponse;
import ru.rtstudy.educplatformsecurity.dto.response.GradeStudentDtoResponse;
import ru.rtstudy.educplatformsecurity.model.Grade;
import ru.rtstudy.educplatformsecurity.model.User;

import java.util.List;
import java.util.Optional;

public interface GradeService {

    StudentAnswerDto sendAnswer(StudentAnswerDto studentAnswerDto);

    List<AllStudentAnswers> findAllStudentAnswer();

    List<AllStudentAnswers> findAllStudentsAnswerForCourse(Long id);

    ChangeStudentAnswerDto changeAnswer(Long gradeId, ChangeStudentAnswerDto studentsAnswerDto);

    void finishCourse(Long id);

    List<Long> getAllLessonsId(Long courseId);

    List<Grade> getAllGradesByLesson(List<Long> lessonIds, Long userId);

    List<AllStudentAnswers> getAllAnswersByStudentForLesson(Long lessonId);

    Optional<List<Grade>> findAllGradesByCourseId(Long courseId);

    Optional<GradeDtoResponse> getGradeById(Long gradeId);

    Optional<List<GradeStudentDtoResponse>> findAllStudentsAnswersByCourseId(Long courseId);

    Optional<List<GradeStudentDtoResponse>> findAllStudentsAnswersByLessonId(Long lessonId);

    Grade getReferenceById(Long gradeId);

    void addMentorReview(Long id, Byte grade, Boolean rework, String mentorAnswer, User mentor);

    void updateMentorReview(Long gradId, Byte grade, Boolean rework, String mentorAnswer);

    int countAllAnswersByMentorUserId(Long mentorId);
}
