package ru.rtstudy.educplatformsecurity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rtstudy.educplatformsecurity.dto.response.CategoryDto;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.responsebuilder.CategoryResponseBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "Category Controller API")
@RequestMapping("api/v1/category")
public class CategoryController {

    private final CategoryResponseBuilder responseBuilder;

    @Operation(summary = "Получить список всех категорий")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllAvailableCategories() {
        return responseBuilder.getAllCategories();
    }

    @Operation(summary = "Получить список всех курсов по идентификатору категории")
    @GetMapping("/{id}")
    public ResponseEntity<List<CourseLongDescriptionDto>> getCoursesByCategoryId(@PathVariable(name = "id")
                                                                                  @Parameter(name = "id", description = "Идентификатор категории") Long id) {
        return responseBuilder.getCourseByCategory(id);
    }
}
