package ru.rtstudy.educplatformsecurity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.dto.response.CourseShortDescriptionDto;
import ru.rtstudy.educplatformsecurity.dto.response.LessonDtoResponse;
import ru.rtstudy.educplatformsecurity.exception.author.NotCourseAuthorException;
import ru.rtstudy.educplatformsecurity.exception.entity.CourseNotFoundException;
import ru.rtstudy.educplatformsecurity.exception.entity.DifficultNotExistsException;
import ru.rtstudy.educplatformsecurity.model.Category;
import ru.rtstudy.educplatformsecurity.model.Course;
import ru.rtstudy.educplatformsecurity.model.Difficult;
import ru.rtstudy.educplatformsecurity.model.User;
import ru.rtstudy.educplatformsecurity.repository.CourseRepository;
import ru.rtstudy.educplatformsecurity.service.CategoryService;
import ru.rtstudy.educplatformsecurity.service.CourseService;
import ru.rtstudy.educplatformsecurity.service.DifficultService;
import ru.rtstudy.educplatformsecurity.service.UserCourseService;
import ru.rtstudy.educplatformsecurity.util.Util;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final UserCourseService userCourseService;
    private final CategoryService categoryService;
    private final DifficultService difficultService;
    private final Util util;

    @Override
    public List<CourseShortDescriptionDto> getCoursesByDifficultId(Long id) {
        log.info("{} trying to get courses by difficult: {}", util.findUserFromContext().getEmail(), id);
        Pageable pageable = Pageable.ofSize(5);
        difficultService.findById(id)
                .orElseThrow(() -> {
                    log.error("Difficult: {} not exists", id);
                    return new DifficultNotExistsException("Difficult not exists.");
                });
        return courseRepository.findCourseByDifficultId(id)
                .map(course -> {
                    if (course.size() == 0) {
                        log.warn("Courses in this difficult id: {} not exists.", id);
                        return null;
                    }
                    return course;
                })
                .orElseThrow(() -> {
                    log.error("Course not found.", new CourseNotFoundException("Course not found."));
                    return new CourseNotFoundException("Course not found.");
                });
    }

    @Override
    public CourseLongDescriptionDto findCourseById(Long id) {
        log.info("{} trying to get course by id: {}", util.findUserFromContext().getEmail(), id);
        return courseRepository.findCourseById(id)
                .orElseThrow(() -> {
                    log.error("Course id: {} was not found.", id);
                    return new CourseNotFoundException("Course was not found.");
                });
    }

    @Override
    public Course createCourse(Course course) {
        log.info("{} trying to create course: {}", util.findUserFromContext().getEmail(), course);
        Category category = categoryService.getCategoryByName(course.getCategory().getTitle());
        log.debug("Category: {} was find.", category);
        course.setCategory(category);

        Difficult difficult = difficultService.getDifficultByDifficultName(course.getDifficult().getDifficultLevel());
        course.setDifficult(difficult);
        log.debug("Difficult: {} was find.", difficult);

        User user = util.findUserFromContext();
        course.setCourseAuthor(user);
        log.info("Course was correct created: {}", course);

        course = courseRepository.save(course);

        userCourseService.makeCourseMentor(user, course);
        return course;
    }

    @Override
    public Course updateCourse(Course course, Long courseId) {
        log.info("{} trying to update course by id: {}", util.findUserFromContext().getEmail(), courseId);
        Course toUpdate = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.error("Course: {} was not found.", courseId, new CourseNotFoundException("Course was not found."));
                    return new CourseNotFoundException("Course was not found.");
                });
        log.info("COURSE FOR UPDATE: {}", toUpdate.getId());
        if (isAuthor(courseId)) {
            toUpdate.setDuration(course.getDuration());
            toUpdate.setTitle(course.getTitle());
            toUpdate.setDescription(course.getDescription());

            if (!course.getCategory().getTitle().equals(toUpdate.getCategory().getTitle())) {
                toUpdate.setCategory(categoryService.getCategoryByName(course.getCategory().getTitle()));
            }
            if (!course.getDifficult().getDifficultLevel().name().equals(toUpdate.getDifficult().getDifficultLevel().name())) {
                toUpdate.setDifficult(difficultService.getDifficultByDifficultName(course.getDifficult().getDifficultLevel()));
            }
        } else {
            log.error("{} trying to update another course.", util.findUserFromContext().getEmail(), new NotCourseAuthorException("You are not course author."));
            throw new NotCourseAuthorException("You are not course author.");
        }
        log.debug("Course was updated: {}", toUpdate);
        return toUpdate;
    }

    @Override
    public void deleteCourse(Long courseId) {
        log.info("{} trying to delete course by id: {}", util.findUserFromContext().getEmail(), courseId);
        if (isAuthor(courseId)) {
            courseRepository.deleteById(courseId);
        } else {
            log.error("{} trying to delete course: {}", util.findUserFromContext().getEmail(), courseId);
            throw new NotCourseAuthorException("You are not course author");
        }
    }

    public boolean isAuthor(Long courseId) {
        log.info("{} trying to verify course by id: {}", util.findUserFromContext().getEmail(), courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.error("Course: {} was not found", courseId, new CourseNotFoundException("Course was not found."));
                    return new CourseNotFoundException("Course was not found.");
                });
        Long authorCourseId = util.findUserFromContext().getId();
        boolean isAuthor = authorCourseId.equals(course.getCourseAuthor().getId());
        log.debug("{} is author: {}", util.findUserFromContext().getEmail(), isAuthor);
        return isAuthor;
    }

    @Override
    public List<LessonDtoResponse> getAllLessonByCourseId(Long courseId) {
        log.info("{} trying to get lesson by course_id: {}", util.findUserFromContext().getEmail(), courseId);
        return courseRepository.getAllLessonByCourseId(courseId)
                .orElseThrow(() -> {
                    log.warn("Course was not found: {}", courseId, new CourseNotFoundException("Course was not found."));
                    return new CourseNotFoundException("Course was not found.");
                });
    }

    @Override
    public Optional<Course> findByTitle(String title) {
        return courseRepository.findByTitle(title);
    }

    @Override
    public Optional<Course> findById(Long courseId) {
        return courseRepository.findById(courseId);
    }
}
