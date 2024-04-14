package ru.rtstudy.educplatformsecurity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.rtstudy.educplatformsecurity.exception.ErrorMessage;
import ru.rtstudy.educplatformsecurity.exception.ErrorResponseModel;
import ru.rtstudy.educplatformsecurity.exception.ValidateErrorMessage;
import ru.rtstudy.educplatformsecurity.exception.author.NotCourseAuthorException;
import ru.rtstudy.educplatformsecurity.exception.author.NotEnoughScoreToAuthorException;
import ru.rtstudy.educplatformsecurity.exception.entity.*;
import ru.rtstudy.educplatformsecurity.exception.mentor.GradeWasNotReviewed;
import ru.rtstudy.educplatformsecurity.exception.mentor.MentorAnswerAlreadyExistsException;
import ru.rtstudy.educplatformsecurity.exception.mentor.NoCompletedTasksException;
import ru.rtstudy.educplatformsecurity.exception.mentor.NotEnoughScoreToMentorException;
import ru.rtstudy.educplatformsecurity.exception.student.*;
import ru.rtstudy.educplatformsecurity.exception.user.UserAlreadyExistsException;
import ru.rtstudy.educplatformsecurity.exception.user.UserNotEnterOnAnyCourseException;
import ru.rtstudy.educplatformsecurity.exception.user.UserNotRegisterException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = {
            CourseNotFoundException.class,
            LessonNotFoundException.class,
            AnswersNotFoundException.class,
            TaskNotFoundException.class,
            UserNotFoundException.class,
            UserAlreadyExistsException.class,
            UserNotRegisterException.class
    })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> resourceNotFoundException(RuntimeException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorMessage.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .description(ex.getMessage())
                        .currentTime(LocalDateTime.now())
                        .endpoint(request.getDescription(false))
                        .build());
    }

    @ExceptionHandler(value = {
            CategoryNotExistsException.class,
            DifficultNotExistsException.class
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<ErrorMessage> resourceNotExistsException(RuntimeException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ErrorMessage.builder()
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .description(ex.getMessage())
                        .currentTime(LocalDateTime.now())
                        .endpoint(request.getDescription(false))
                        .build());
    }

    @ExceptionHandler(value = {
            NotEnoughScoreToMentorException.class,
            ResolveAllTaskException.class,
            EnterOnCourseException.class,
            UserNotEnterOnAnyCourseException.class,
            NotUserGradeException.class
    })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> studentsException(RuntimeException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorMessage.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .description(ex.getMessage())
                        .currentTime(LocalDateTime.now())
                        .endpoint(request.getDescription(false))
                        .build());
    }

    @ExceptionHandler(value = {
            UserNotMentorException.class,
            MentorAnswerAlreadyExistsException.class,
            AlreadyMentorException.class,
            NotEnoughScoreToAuthorException.class,
            GradeNotFoundException.class,
            NoCompletedTasksException.class,
            NotCourseAuthorException.class,
            GradeWasNotReviewed.class
    })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> mentorsException(RuntimeException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorMessage.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .description(ex.getMessage())
                        .currentTime(LocalDateTime.now())
                        .endpoint(request.getDescription(false))
                        .build());
    }

    @ExceptionHandler(value = {
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseModel> validationException(MethodArgumentNotValidException ex) {
        List<ValidateErrorMessage> errorModels = processErrors(ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseModel
                        .builder()
                        .type("VALIDATION")
                        .errorModels(errorModels)
                        .build());
    }

    private List<ValidateErrorMessage> processErrors(MethodArgumentNotValidException e) {
        List<ValidateErrorMessage> validationErrorModels = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            ValidateErrorMessage validationErrorModel = ValidateErrorMessage
                    .builder()
                    .code(fieldError.getCode())
                    .source(fieldError.getField())
                    .detail(fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .build();
            validationErrorModels.add(validationErrorModel);
        }
        return validationErrorModels;
    }
}