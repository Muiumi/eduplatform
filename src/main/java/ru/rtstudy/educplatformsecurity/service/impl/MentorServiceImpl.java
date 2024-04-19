package ru.rtstudy.educplatformsecurity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rtstudy.educplatformsecurity.dto.request.MentorAnswerDtoRequest;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.dto.response.GradeDtoResponse;
import ru.rtstudy.educplatformsecurity.dto.response.GradeStudentDtoResponse;
import ru.rtstudy.educplatformsecurity.exception.entity.GradeNotFoundException;
import ru.rtstudy.educplatformsecurity.exception.mentor.GradeWasNotReviewed;
import ru.rtstudy.educplatformsecurity.exception.mentor.MentorAnswerAlreadyExistsException;
import ru.rtstudy.educplatformsecurity.exception.mentor.NoCompletedTasksException;
import ru.rtstudy.educplatformsecurity.exception.author.NotEnoughScoreToAuthorException;
import ru.rtstudy.educplatformsecurity.exception.student.UserNotMentorException;
import ru.rtstudy.educplatformsecurity.model.Course;
import ru.rtstudy.educplatformsecurity.model.Grade;
import ru.rtstudy.educplatformsecurity.model.User;
import ru.rtstudy.educplatformsecurity.model.UserCourse;
import ru.rtstudy.educplatformsecurity.model.constant.Role;
import ru.rtstudy.educplatformsecurity.repository.UserCourseRepository;
import ru.rtstudy.educplatformsecurity.service.GradeService;
import ru.rtstudy.educplatformsecurity.service.MentorService;
import ru.rtstudy.educplatformsecurity.service.UserService;
import ru.rtstudy.educplatformsecurity.util.Util;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MentorServiceImpl implements MentorService {

    private final UserCourseRepository userCourseRepository;

    private final GradeService gradeService;
    private final UserService userService;
    private final Util util;

    private final int MIN_AMOUNT_OF_ANSWERS_TO_BECOME_AUTHOR = 100;

    @Override
    public List<Course> getAllCoursesForMentor() {
        User mentor = util.findUserFromContext();
        log.info("{} get all courses for mentor", mentor.getEmail());
        return userCourseRepository.findAllByUserAndMentorCourse(mentor, true)
                .orElseThrow(() -> {
                    log.error("{} not a mentor", mentor.getEmail(), new UserNotMentorException("User is not a mentor for any course"));
                    return new UserNotMentorException("User is not a mentor for any course");
                })
                .stream()
                .map(UserCourse::getCourse)
                .toList();
    }
    @Override
    public List<CourseLongDescriptionDto> getMentorCourses() {
        User mentor = util.findUserFromContext();
        log.info("{} get all courses for mentor", mentor.getEmail());
        return userCourseRepository.getMentorCourses(mentor.getId());
    }

    @Override
    public List<GradeDtoResponse> getAllAnswersForMentorCourses() {
        List<Course> mentorCourses = getAllCoursesForMentor();
        return mentorCourses.stream()
                .flatMap(course -> gradeService.findAllGradesByCourseId(course.getId())
                        .orElseThrow(() -> {
                            log.error("Not completed task.", new NoCompletedTasksException("There are no completed tasks"));
                            return new NoCompletedTasksException("There are no completed tasks");
                        })
                        .stream())
                .map(grade -> gradeService.getGradeById(grade.getId())
                        .orElseThrow(() -> {
                            log.error("Grade was not found", new GradeNotFoundException("Grade not found"));
                            return new GradeNotFoundException("Grade not found");
                        }))
                .toList();
    }

    @Override
    public List<GradeStudentDtoResponse> getAllAnswersForCourse(Long courseId) {
        User mentor = util.findUserFromContext();
        log.info("{} get all answers for course: {}", mentor.getEmail(), courseId);
        boolean isMentorForCourse = userCourseRepository.isMentorForCourse(mentor.getId(), courseId);
        if (isMentorForCourse) {
            return gradeService.findAllStudentsAnswersByCourseId(courseId)
                    .orElseThrow(() -> {
                        log.error("No completed task was not found", new NoCompletedTasksException("There are no completed tasks"));
                        return new NoCompletedTasksException("There are no completed tasks");
                    });
        } else {
            log.error("User is not a mentor", new UserNotMentorException("User is not a mentor for any course"));
            throw new UserNotMentorException("User is not a mentor for any course");
        }
    }

    @Override
    public List<GradeStudentDtoResponse> getAllAnswersForLesson(Long lessonId) {
        User mentor = util.findUserFromContext();
        log.info("{} get all answers for lesson: {}", mentor.getEmail(), lessonId);
        boolean isMentorForLesson = userCourseRepository.isMentorForLesson(mentor.getId(), lessonId);
        if (isMentorForLesson) {
            return gradeService.findAllStudentsAnswersByLessonId(lessonId)
                    .orElseThrow(() -> {
                        log.error("Not any task was completed", new NoCompletedTasksException("There are no completed tasks"));
                        return new NoCompletedTasksException("There are no completed tasks");
                    });
        } else {
            log.error("{} not mentor", mentor.getEmail(), new UserNotMentorException("User is not a mentor for any course"));
            throw new UserNotMentorException("User is not a mentor for any course");
        }

    }

    @Override
    public MentorAnswerDtoRequest reviewStudentAnswer(Long gradeId, MentorAnswerDtoRequest mentorAnswerDtoRequest) {
        User mentor = util.findUserFromContext();
        log.info("{} review students answer: {}", mentor.getEmail(), gradeId);
        Grade gradeToUpdate = gradeService.getReferenceById(gradeId);
        boolean isMentorForLesson = userCourseRepository.isMentorForLesson(mentor.getId(), gradeToUpdate.getLesson().getId());
        if (!isMentorForLesson) {
            log.error("{} is not mentor for this course: {}.", mentor.getEmail(), gradeToUpdate.getLesson().getCourse(), new UserNotMentorException("User is not a mentor for this course lesson"));
            throw new UserNotMentorException("User is not a mentor for this course lesson");
        }
        if (gradeToUpdate.getMentor() != null) {
            log.error("Mentors for this grade is: {}", gradeToUpdate.getMentor().getEmail());
            throw new MentorAnswerAlreadyExistsException("This task is already reviewed");
        }
        gradeService.addMentorReview(gradeId, mentorAnswerDtoRequest.grade(), mentorAnswerDtoRequest.rework(), mentorAnswerDtoRequest.mentorAnswer(), mentor);
        return mentorAnswerDtoRequest;
    }

    @Override
    public MentorAnswerDtoRequest updateMentorAnswer(Long gradeId, MentorAnswerDtoRequest mentorAnswerDtoRequest) {
        User mentor = util.findUserFromContext();
        log.info("{} update his answer for grade: {}", mentor.getEmail(), gradeId);
        Grade gradeToUpdate = gradeService.getReferenceById(gradeId);
        boolean isMentorForLesson = userCourseRepository.isMentorForLesson(mentor.getId(), gradeToUpdate.getLesson().getId());
        if (!isMentorForLesson) {
            log.error("{} is not mentor for this course: {}.", mentor.getEmail(), gradeToUpdate.getLesson().getCourse(), new UserNotMentorException("User is not a mentor for this course lesson"));
            throw new UserNotMentorException("User is not a mentor for this course lesson");
        }

        if (gradeToUpdate.getMentor() == null) {
            log.error("Student Answer with ID {} was not reviewed to update review", gradeId, new GradeWasNotReviewed("This grade was not reviewed by any mentor to update it"));
            throw new GradeWasNotReviewed("This grade was not reviewed by any mentor to update it");
        }

        if (gradeToUpdate.getMentor().getId().equals(mentor.getId())) {
            gradeService.updateMentorReview(gradeId, mentorAnswerDtoRequest.grade(), mentorAnswerDtoRequest.rework(), mentorAnswerDtoRequest.mentorAnswer());
            return mentorAnswerDtoRequest;
        } else {
            log.error("{} is not mentor. The mentor is: {}", mentor.getEmail(), gradeToUpdate.getMentor().getEmail());
            throw new UserNotMentorException("User is not a mentor for this course lesson");
        }
    }

    @Override
    public void upgradeToAuthor() {
        User mentor = util.findUserFromContext();
        log.info("{} upgrade to author", mentor.getEmail());
        if (gradeService.countAllAnswersByMentorUserId(mentor.getId()) >= MIN_AMOUNT_OF_ANSWERS_TO_BECOME_AUTHOR) {
            userService.changeUserRole(mentor.getId(), Role.ROLE_AUTHOR);
        } else {
            log.error("{} don't have enough reviewed task", mentor.getEmail(), new NotEnoughScoreToAuthorException("Not enough reviewed tasks to become author"));
            throw new NotEnoughScoreToAuthorException("Not enough reviewed tasks to become author");
        }
    }
}