package ru.rtstudy.educplatformsecurity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.rtstudy.educplatformsecurity.dto.request.CourseDtoRequest;
import ru.rtstudy.educplatformsecurity.dto.response.CourseDtoResponse;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.dto.response.LessonDtoResponse;
import ru.rtstudy.educplatformsecurity.responsebuilder.CourseResponseBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Course Controller", description = "Course Controller API")
@RequestMapping("api/v1/courses")
public class CourseController {

    private final CourseResponseBuilder responseBuilder;

    @Operation(summary = "Получить описание курса по его идентификатору")
    @GetMapping("{id}")
    public ResponseEntity<CourseLongDescriptionDto> getCourse(@PathVariable(name = "id")
                                                              @Parameter(name = "id", description = "Идентификатор курса") Long id) {
        return responseBuilder.findCourseById(id);
    }

    @Operation(summary = "Изменить информацию о курсе по его идентификатору")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @PutMapping("{id}")
    public ResponseEntity<CourseDtoResponse> updateCourse(@PathVariable(name = "id")
                                                         @Parameter(name = "id", description = "Идентификатор курса") Long id,
                                                         @Valid @RequestBody CourseDtoRequest courseDtoRequest) {
        return responseBuilder.updateCourse(id, courseDtoRequest);
    }

    @Operation(summary = "Получить краткое описание всех уроков курса")
    @GetMapping("lessons/{id}")
    public ResponseEntity<List<LessonDtoResponse>> getAllLessonByCourseId(@PathVariable(name = "id")
                                                                                  @Parameter(name = "id", description = "Идентификатор курса") Long courseId) {
        return responseBuilder.getAllLessonByCourseId(courseId);
    }

    @Operation(summary = "Создать новый курс")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @PostMapping
    public ResponseEntity<CourseDtoResponse> createCourse(@Valid @RequestBody CourseDtoRequest courseDtoRequest) {
        return responseBuilder.createCourse(courseDtoRequest);
    }

    @Operation(summary = "Удалить курс по его идентификатору")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable(name = "id")
                                                   @Parameter(name = "id", description = "Идентификатор курса") Long id) {
        return responseBuilder.deleteCourse(id);
    }
}
