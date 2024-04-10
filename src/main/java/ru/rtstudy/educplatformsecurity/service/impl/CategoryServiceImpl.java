package ru.rtstudy.educplatformsecurity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rtstudy.educplatformsecurity.dto.response.CategoryDto;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.exception.entity.CategoryNotExistsException;
import ru.rtstudy.educplatformsecurity.exception.entity.CourseNotFoundException;
import ru.rtstudy.educplatformsecurity.model.Category;
import ru.rtstudy.educplatformsecurity.repository.CategoryRepository;
import ru.rtstudy.educplatformsecurity.service.CategoryService;
import ru.rtstudy.educplatformsecurity.util.Util;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final Util util;

    @Override
    public List<CategoryDto> getAllCategories() {
        log.info("{} trying to get category list.", util.findUserFromContext().getEmail());
        return categoryRepository.getAllCategories();
    }

    @Transactional
    @Override
    public List<CourseLongDescriptionDto> getCoursesByCategory(Long categoryId) {
        log.info("{} trying to get course by category", util.findUserFromContext().getEmail());
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category: {} not found, incorrect input.", categoryId, new CategoryNotExistsException("Category not found."));
                    return new CategoryNotExistsException("Category not found!");
                });
        return categoryRepository.getCourseByCategoryId(categoryId)
                .map(courses -> {
                    if (courses.isEmpty()) {
                        log.warn("Courses list is empty with category id: {}", categoryId);
                        return null;
                    }
                    return courses;
                })
                .orElseThrow(() -> {
                    log.error("Courses not found.", new CourseNotFoundException("Courses not found."));
                    return new CourseNotFoundException("Courses not found.");
                });
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        return categoryRepository.getCategoryByName(categoryName);
    }
}
