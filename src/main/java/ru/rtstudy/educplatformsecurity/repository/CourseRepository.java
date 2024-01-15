package ru.rtstudy.educplatformsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.dto.response.CourseShortDescriptionDto;
import ru.rtstudy.educplatformsecurity.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
            select new CourseShortDescriptionDto(c.title, c.description) 
            from Course c
            where c.difficult.id = :id
            """)
    Optional<List<CourseShortDescriptionDto>> findCourseByDifficultId(@Param(value = "id") Long id);

    @Query("""
            select new CourseLongDescriptionDto(c.title, c.description, c.category.title, c.duration, c.difficult.difficult) 
            from Course c 
            where c.id = :id
            """)
    Optional<CourseLongDescriptionDto> findCourseById(@Param(value = "id") Long id);

    @Query("""
            select new CourseShortDescriptionDto(c.title, c.description) 
            from Course c
            where c.category.id = :id
            """)
    Optional<CourseShortDescriptionDto> findCourseByCategoryId(@Param(value = "id") Long id);
}
