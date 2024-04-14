package ru.rtstudy.educplatformsecurity.dto.mapper.impl;

import org.mapstruct.Mapper;
import ru.rtstudy.educplatformsecurity.dto.mapper.Mappable;
import ru.rtstudy.educplatformsecurity.dto.response.LessonDtoResponse;
import ru.rtstudy.educplatformsecurity.model.Lesson;

@Mapper(componentModel = "spring")
public interface LessonMapper extends Mappable<Lesson, LessonDtoResponse> {

    default LessonDtoResponse toDto(Lesson lesson) {
        return LessonDtoResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .referenceOnFile(lesson.getFileName())
                .description(lesson.getDescription())
                .taskId(lesson.getTaskId().getId())
                .build();
    }

    default Lesson toEntity(LessonDtoResponse lessonDtoResponse) {
        return Lesson.builder()
                .title(lessonDtoResponse.title())
                .description(lessonDtoResponse.description())
                .build();
    }
}
