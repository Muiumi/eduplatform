package ru.rtstudy.educplatformsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.model.User;
import ru.rtstudy.educplatformsecurity.model.UserCourse;

import java.util.List;
import java.util.Optional;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {

    Optional<List<UserCourse>> findAllByUserAndMentorCourse(User user, boolean mentorForCourse);

    @Query("""
            select count(us) > 0
            from UserCourse us
            where us.user.id = :userId
                AND us.course.id = :courseId
                AND us.mentorCourse = true
            """)
    boolean isMentorForCourse(Long userId, Long courseId);

    @Query("""
            select count(us) > 0
            from UserCourse us
            join Lesson l
            with l.id = :lessonId
            where us.user.id = :userId
                AND us.course.id = l.course.id
                AND us.mentorCourse = true
            """)
    boolean isMentorForLesson(Long userId, Long lessonId);

    @Modifying
    @Query("""
            update UserCourse us
            set us.endCourse = true, us.finishCourse = current_timestamp
            where us.user.id = :userId
            and us.course.id = :courseId
            """)
    void finishCourse(Long userId, Long courseId);

    @Query("""
            select count(uc) > 0
            from UserCourse uc
            where uc.course.id = :courseId
            and uc.user.id = :userId
            """)
    boolean onCourse(Long courseId, Long userId);

    @Query("""
            select count(*) > 0
            from UserCourse us
            where us.user.id = :userId
            and us.course.id = :courseId
            """)
    boolean alreadyCourseMentor(Long userId, Long courseId);

    @Query("""
            select new CourseLongDescriptionDto(us.course.id, us.course.title, us.course.description, us.course.category.title, us.course.duration, us.course.difficult.difficultLevel)
            from UserCourse us
            where us.user.id = :userId
            """)
    Optional<List<CourseLongDescriptionDto>> getAllStartedCourse(Long userId);

    @Query("""
            select new CourseLongDescriptionDto(us.course.id, us.course.title, us.course.description, us.course.category.title, us.course.duration, us.course.difficult.difficultLevel)
            from UserCourse us
            where us.user.id = :userId and us.mentorCourse = true
            """)
    List<CourseLongDescriptionDto> getMentorCourses(Long userId);
}