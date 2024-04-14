package ru.rtstudy.educplatformsecurity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rtstudy.educplatformsecurity.dto.request.ChangeStudentAnswerDto;
import ru.rtstudy.educplatformsecurity.dto.request.StudentAnswerDto;
import ru.rtstudy.educplatformsecurity.dto.response.AllStudentAnswers;
import ru.rtstudy.educplatformsecurity.dto.response.GradeDtoResponse;
import ru.rtstudy.educplatformsecurity.dto.response.GradeStudentDtoResponse;
import ru.rtstudy.educplatformsecurity.exception.entity.AnswersNotFoundException;
import ru.rtstudy.educplatformsecurity.exception.entity.CourseNotFoundException;
import ru.rtstudy.educplatformsecurity.exception.entity.GradeNotFoundException;
import ru.rtstudy.educplatformsecurity.exception.entity.LessonNotFoundException;
import ru.rtstudy.educplatformsecurity.exception.student.EnterOnCourseException;
import ru.rtstudy.educplatformsecurity.exception.student.NotUserGradeException;
import ru.rtstudy.educplatformsecurity.exception.student.ResolveAllTaskException;
import ru.rtstudy.educplatformsecurity.model.Grade;
import ru.rtstudy.educplatformsecurity.model.Lesson;
import ru.rtstudy.educplatformsecurity.model.User;
import ru.rtstudy.educplatformsecurity.repository.GradeRepository;
import ru.rtstudy.educplatformsecurity.service.GradeService;
import ru.rtstudy.educplatformsecurity.service.LessonService;
import ru.rtstudy.educplatformsecurity.service.UserCourseService;
import ru.rtstudy.educplatformsecurity.util.Util;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;

    private final LessonService lessonService;
    private final UserCourseService userCourseService;
    private final Util util;

    @Override
    @Transactional
    public StudentAnswerDto sendAnswer(StudentAnswerDto studentAnswerDto) {
        User user = util.findUserFromContext();
        log.info("{} send answer on task: {}", user.getEmail(), studentAnswerDto);
        Lesson lesson = lessonService.findById(studentAnswerDto.lessonId())
                .orElseThrow(() -> {
                    log.error("Lesson was not found:{}", studentAnswerDto.lessonId(), new LessonNotFoundException("Lesson not found."));
                    return new LessonNotFoundException("Lesson not found.");
                });
        boolean onCourse = userCourseService.onCourse(lesson.getCourse().getId(), user.getId());
        if (onCourse) {
            Grade grade = Grade.builder()
                    .lesson(lesson)
                    .student(user)
                    .studentAnswer(studentAnswerDto.studentAnswer())
                    .build();
            gradeRepository.save(grade);
        } else {
            log.error("Student: {} not enter on course by lesson id: {}", user.getEmail(), studentAnswerDto.lessonId(), new EnterOnCourseException("Please enter on course."));
            throw new EnterOnCourseException("Please enter on course.");
        }
        return studentAnswerDto;
    }

    @Override
    public List<AllStudentAnswers> findAllStudentAnswer() {
        log.info("{} find all answer.", util.findUserFromContext().getEmail());
        Long id = util.findUserFromContext().getId();
        return gradeRepository.getAllStudentAnswer(id)
                .orElseThrow(() -> {
                    log.error("Student: {} not send answer.", util.findUserFromContext().getEmail(), new AnswersNotFoundException("Answers not found."));
                    return new AnswersNotFoundException("Answers not found.");
                });
    }

    @Override
    public List<AllStudentAnswers> findAllStudentsAnswerForCourse(Long courseId) {
        User user = util.findUserFromContext();
        log.info("{} find all answer for course id: {}.", user.getEmail(), courseId);
        return gradeRepository.findAllStudentsAnswerForCourse(courseId, user.getId())
                .orElseThrow(() -> {
                    log.error("{} students answer on course id not found: {}", util.findUserFromContext().getEmail(), courseId, new AnswersNotFoundException("Answers not found."));
                    return new AnswersNotFoundException("Answers not found.");
                });
    }


    @Override
    public ChangeStudentAnswerDto changeAnswer(Long gradeId, ChangeStudentAnswerDto studentsAnswerDto) {
        User user = util.findUserFromContext();
        log.info("{} change answer: {} for grade id: {}.", user.getEmail(), studentsAnswerDto.studentAnswer(), gradeId);
        Grade gradeToChange = gradeRepository.getReferenceById(gradeId);
        if (gradeToChange.getStudent().getId().equals(user.getId())) {
            gradeRepository.changeAnswer(gradeId, studentsAnswerDto.studentAnswer(), user.getId());
            log.info("Answer: {} was successful changed by student: {} ", studentsAnswerDto.studentAnswer(), user.getEmail());
            return studentsAnswerDto;
        }
        log.error("Student {} tried to change not his grade with id {}", user.getEmail(), gradeId, new NotUserGradeException("This user can't change this grade because it's not his answer."));
        throw new NotUserGradeException("This user can't change this grade because it's not his answer.");
    }

    @Override
    public List<AllStudentAnswers> getAllAnswersByStudentForLesson(Long lessonId) {
        User user = util.findUserFromContext();
        return gradeRepository.getAnswersByStudentForLesson(user.getId(), lessonId);
    }

    @Override
    public void finishCourse(Long courseId) {
        User user = util.findUserFromContext();
        log.info("{} finish course: {}", user.getEmail(), courseId);
        List<Long> lessonsIds = getAllLessonsId(courseId);
        log.debug("{} finish lessons: {}", user.getEmail(), lessonsIds);
        List<Long> gradesIds = getAllGradesByLesson(lessonsIds, user.getId())
                .stream()
                .map(lessonId -> lessonId.getLesson().getId())
                .toList();

        log.debug("{} receive grades: {}", user.getEmail(), gradesIds);

        if (new HashSet<>(gradesIds).containsAll(lessonsIds)) {
            userCourseService.finishCourse(user.getId(), courseId);
        } else {
            log.error("{} not resolve all task on course: {}", user.getEmail(), courseId);
            throw new ResolveAllTaskException("Please resolve all task in course and try again.");
        }
    }

    @Override
    public List<Long> getAllLessonsId(Long courseId) {
        log.info("{} get all lessons by course id: {}", util.findUserFromContext().getEmail(), courseId);
        return gradeRepository.getAllLessonsId(courseId)
                .orElseThrow(() -> {
                    log.error("Course not found: {}", courseId, new CourseNotFoundException("Course not found."));
                    return new CourseNotFoundException("Course not found.");
                });
    }

    @Override
    public List<Grade> getAllGradesByLesson(List<Long> lessonIds, Long userId) {
        log.info("{} get all grades by lessonsId id: {}", util.findUserFromContext().getEmail(), lessonIds);
        return gradeRepository.getGrades(lessonIds, userId)
                .orElseThrow(() -> {
                    log.error("Grades by lessonIds: {} was not found.", lessonIds, new GradeNotFoundException("Grade was not found."));
                    return new GradeNotFoundException("Grade was not found.");
                });
    }


    @Override
    public Optional<List<Grade>> findAllGradesByCourseId(Long courseId) {
        return gradeRepository.findAllGradesByCourseId(courseId);
    }

    @Override
    public Optional<GradeDtoResponse> getGradeById(Long gradeId) {
        return gradeRepository.getGradeById(gradeId);
    }

    @Override
    public Optional<List<GradeStudentDtoResponse>> findAllStudentsAnswersByCourseId(Long courseId) {
        return gradeRepository.findAllStudentsAnswersByCourseId(courseId);
    }

    @Override
    public Optional<List<GradeStudentDtoResponse>> findAllStudentsAnswersByLessonId(Long lessonId) {
        return gradeRepository.findAllStudentsAnswersByLessonId(lessonId);
    }

    @Override
    public Grade getReferenceById(Long gradeId) {
        return gradeRepository.getReferenceById(gradeId);
    }

    @Override
    public void addMentorReview(Long gradeId, Byte grade, Boolean rework, String mentorAnswer, User mentor) {
        gradeRepository.addMentorReview(gradeId, grade, rework, mentorAnswer, mentor);
    }

    @Override
    public void updateMentorReview(Long gradId, Byte grade, Boolean rework, String mentorAnswer) {
        gradeRepository.updateMentorReview(gradId, grade, rework, mentorAnswer);
    }

    @Override
    public int countAllAnswersByMentorUserId(Long mentorId) {
        return gradeRepository.countAllAnswersByMentorUserId(mentorId);
    }
}

