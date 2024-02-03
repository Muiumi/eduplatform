package ru.rtstudy.educplatformsecurity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rtstudy.educplatformsecurity.responsebuilder.StudentResponseBuilder;
import ru.rtstudy.educplatformsecurity.dto.ChangeStudentAnswerDto;
import ru.rtstudy.educplatformsecurity.dto.request.StudentAnswerDto;
import ru.rtstudy.educplatformsecurity.dto.response.AllStudentAnswers;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Student Controller", description = "Student Controller API")
@RequestMapping("api/v1/students")
public class StudentController {

    private final StudentResponseBuilder responseBuilder;

    @Operation(summary = "Поступить на выбранный курс")
    @PostMapping("start/{course_id}")
    public ResponseEntity<HttpStatus> enterOnCourse(@PathVariable(name = "course_id")
                                                    @Parameter(name = "course_id", description = "Идентификатор курса") Long courseId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseBuilder.enterOnCourse(courseId));
    }

    @Operation(summary = "Закончить выбранный курс")
    @PostMapping("finish-course/{course_id}")
    public ResponseEntity<HttpStatus> finishCourse(@PathVariable(name = "course_id")
                                                   @Parameter(name = "course_id", description = "Идентификатор курса") Long courseId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseBuilder.finishCourse(courseId));
    }

    @Operation(summary = "Отправить ответ на указанной урок для проверки")
    @PostMapping
    public ResponseEntity<StudentAnswerDto> sendAnswer(@RequestBody StudentAnswerDto studentAnswerDto) {
        return ResponseEntity
                .status(HttpStatus.valueOf(201))
                .body(responseBuilder.sendAnswer(studentAnswerDto));
    }

    @Operation(summary = "Получить все оценки и все свои ответы с комментариями преподавателя на сданные задания")
    @GetMapping
    public ResponseEntity<List<AllStudentAnswers>> receiveAllStudentsAnswer() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseBuilder.findAllStudentAnswer());
    }

    @Operation(summary = "Получить все оценки и все свои ответы с комментариями преподавателя за каждый урок курса по его идентификатору")
    @GetMapping("course/{course_id}")
    public ResponseEntity<List<AllStudentAnswers>> receiveAllStudentsAnswerForCourse(@PathVariable(name = "course_id")
                                                                                     @Parameter(name = "course_id", description = "Идентификатор курса") Long courseId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseBuilder.findAllStudentsAnswerForCourse(courseId));

    }

    @Operation(summary = "Изменить свой ответ к уроку, только если он не был проверен ментором")
    @PutMapping("lesson/{id}")
    public ResponseEntity<ChangeStudentAnswerDto> changeAnswer(@PathVariable(name = "id")
                                                               @Parameter(name = "id", description = "Идентификатор урока, к которому надо изменить ответ") Long id,
                                                               @RequestBody ChangeStudentAnswerDto studentsAnswerDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseBuilder.changeAnswer(id, studentsAnswerDto));
    }

    @Operation(summary = "Запрос на получение прав ментора на курс, получить возможность проверять ответы пользователей (при наличии среднего балла 8.0 за задания курса)")
    @PostMapping("upgrade-to-mentor/{course_id}")
    public ResponseEntity<HttpStatus> upgradeToMentor(@PathVariable(name = "course_id")
                                                      @Parameter(name = "course_id", description = "Идентификатор курса") Long courseId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseBuilder.upgradeToMentor(courseId));
    }
}