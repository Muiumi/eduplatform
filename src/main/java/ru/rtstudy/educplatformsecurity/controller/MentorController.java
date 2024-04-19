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
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.responsebuilder.MentorResponseBuilder;
import ru.rtstudy.educplatformsecurity.dto.request.MentorAnswerDtoRequest;
import ru.rtstudy.educplatformsecurity.dto.response.GradeDtoResponse;
import ru.rtstudy.educplatformsecurity.dto.response.GradeStudentDtoResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MENTOR')")
@Tag(name = "Mentor Controller", description = "Mentor Controller API")
@RequestMapping("/api/v1/mentors")
public class MentorController {

    private final MentorResponseBuilder responseBuilder;

    @Operation(summary = "Получить все курсы, где пользвоатель - ментор")
    @GetMapping("/courses")
    public ResponseEntity<List<CourseLongDescriptionDto>> getAllMentorCourses() {
        return responseBuilder.getAllMentorCourses();
    }

    @Operation(summary = "Получить ответы всех студентов, за все курсы, которые ты можешь проверять")
    @GetMapping
    public ResponseEntity<List<GradeDtoResponse>> getAllAnswersForMentorCourses() {
        return responseBuilder.getAllAnswersForMentorCourses();
    }

    @Operation(summary = "Получить ответы всех студентов за определённый курс по его идентификатору")
    @GetMapping("/course/{course_id}")
    public ResponseEntity<List<GradeStudentDtoResponse>> getAllStudentAnswersForCourse(@PathVariable(name = "course_id")
                                                                                       @Parameter(name = "course_id", description = "Идентификатор курса") Long id) {
        return responseBuilder.getAllAnswersForCourse(id);
    }

    @Operation(summary = "Получить ответы всех студентов за определённый урок по его идентификатору")
    @GetMapping("/lessons/{lesson_id}")
    public ResponseEntity<List<GradeStudentDtoResponse>> getAllStudentAnswersForLesson(@PathVariable(name = "lesson_id")
                                                                                       @Parameter(name = "lesson_id", description = "Идентификатор урока") Long id) {
        return responseBuilder.getAllAnswersForLesson(id);
    }

    @Operation(summary = "Оценить полученный ответ студента по идентификатору ответа")
    @PutMapping("/grades/{grade_id}")
    public ResponseEntity<MentorAnswerDtoRequest> reviewStudentAnswer(@PathVariable(name = "grade_id")
                                                                      @Parameter(name = "grade_id", description = "Идентификатор ответа студента") Long id,
                                                                      @Valid @RequestBody MentorAnswerDtoRequest mentorAnswerDtoRequest) {
        return responseBuilder.reviewStudentAnswer(id, mentorAnswerDtoRequest);
    }

    @Operation(summary = "Изменить опубликованную оценку и комментарий ментора по идентификатору ответа")
    @PutMapping("/grades/change/{grade_id}")
    public ResponseEntity<MentorAnswerDtoRequest> updateMentorAnswer(@PathVariable(name = "grade_id")
                                                                     @Parameter(name = "grade_id", description = "Идентификатор ответа студента") Long id,
                                                                     @Valid @RequestBody MentorAnswerDtoRequest mentorAnswerDtoRequest) {
        return responseBuilder.updateMentorAnswer(id, mentorAnswerDtoRequest);
    }

    @Operation(summary = "Запрос на получение прав автора, получить возможность создавать свои курсы (при наличии 100 и более проверенных заданий)")
    @PostMapping("/upgrade-to-author")
    public ResponseEntity<HttpStatus> upgradeMentorToAuthor() {
        return responseBuilder.upgradeToAuthor();
    }
}
