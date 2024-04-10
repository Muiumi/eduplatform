package ru.rtstudy.educplatformsecurity.service;

import ru.rtstudy.educplatformsecurity.dto.response.CategoryDto;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.model.Category;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllCategories();

    List<CourseLongDescriptionDto> getCoursesByCategory(Long id);

    Category getCategoryByName(String categoryName);
}
