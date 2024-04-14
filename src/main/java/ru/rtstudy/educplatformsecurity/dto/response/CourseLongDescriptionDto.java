package ru.rtstudy.educplatformsecurity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.rtstudy.educplatformsecurity.model.constant.DifficultLevel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Course With Full Description DTO")
public class CourseLongDescriptionDto {

    @Schema(description = "Идентификатор курса", example = "1")
    private Long id;
    @Schema(description = "Название курса", example = "PostgreSQL для профи")
    private String title;
    @Schema(description = "Краткое описание курса",
            example = "Этот курс предназначен для разработчиков, которые уже знают основы PostgreSQL и хотят углубить свои знания.")
    private String description;
    @Schema(description = "Название категории, к которой относится курс", example = "BACKEND")
    private String category;
    @Schema(description = "Примерная продолжительность курса в часах", example = "52")
    private short duration;
    @Schema(description = "Сложность данного курса", example = "MEDIUM")
    private DifficultLevel difficultLevel;

}
