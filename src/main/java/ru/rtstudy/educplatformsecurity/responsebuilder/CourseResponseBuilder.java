package ru.rtstudy.educplatformsecurity.responsebuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.rtstudy.educplatformsecurity.dto.mapper.impl.CourseMapper;
import ru.rtstudy.educplatformsecurity.dto.request.CourseDtoRequest;
import ru.rtstudy.educplatformsecurity.dto.response.CourseDtoResponse;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.dto.response.LessonDtoResponse;
import ru.rtstudy.educplatformsecurity.model.Course;
import ru.rtstudy.educplatformsecurity.service.CourseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseResponseBuilder {

    private final CourseService courseService;
    private final CourseMapper mapper;


    public ResponseEntity<CourseLongDescriptionDto> findCourseById(Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(courseService.findCourseById(id));
    }

    public ResponseEntity<CourseDtoResponse> updateCourse(Long id, CourseDtoRequest courseDtoRequest) {
        Course course = mapper.toEntity(courseDtoRequest);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(mapper.toCourseDtoResponse(courseService.updateCourse(course, id)));
    }

    public ResponseEntity<List<LessonDtoResponse>> getAllLessonByCourseId(Long courseId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(courseService.getAllLessonByCourseId(courseId));
    }

    public ResponseEntity<CourseDtoResponse> createCourse(CourseDtoRequest dtoRequest) {
        Course course = mapper.toEntity(dtoRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.toCourseDtoResponse(courseService.createCourse(course)));
    }

    public ResponseEntity<HttpStatus> deleteCourse(Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(HttpStatus.NO_CONTENT);
    }
}
