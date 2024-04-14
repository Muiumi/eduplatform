package ru.rtstudy.educplatformsecurity.responsebuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.rtstudy.educplatformsecurity.dto.mapper.impl.LessonMapper;
import ru.rtstudy.educplatformsecurity.dto.request.LessonDtoRequest;
import ru.rtstudy.educplatformsecurity.dto.response.LessonDtoResponse;
import ru.rtstudy.educplatformsecurity.model.Lesson;
import ru.rtstudy.educplatformsecurity.service.LessonService;

@Service
@RequiredArgsConstructor
public class LessonResponseBuilder {

    private final LessonService lessonService;
    private final LessonMapper mapper;


    public ResponseEntity<LessonDtoResponse> findLessonById(Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lessonService.findLessonById(id));
    }

    public ResponseEntity<LessonDtoResponse> createLesson(LessonDtoRequest lessonDtoRequest) {
        Lesson lesson = lessonService.createLesson(lessonDtoRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(lesson));
    }

    public ResponseEntity<LessonDtoResponse> updateLesson(LessonDtoRequest lessonDtoRequest, Long lessonId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.toDto(lessonService.updateLesson(lessonDtoRequest, lessonId)));
    }

    public ResponseEntity<HttpStatus> deleteLesson(Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(HttpStatus.NO_CONTENT);
    }
}
