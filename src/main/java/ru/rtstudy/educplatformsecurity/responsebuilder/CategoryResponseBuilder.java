package ru.rtstudy.educplatformsecurity.responsebuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.rtstudy.educplatformsecurity.dto.response.CategoryDto;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CategoryResponseBuilder {

    private final CategoryService categoryService;

    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getAllCategories());
    }

    public ResponseEntity<List<CourseLongDescriptionDto>> getCourseByCategory(Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.getCoursesByCategory(id));
    }
}
