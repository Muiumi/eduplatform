package ru.rtstudy.educplatformsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.rtstudy.educplatformsecurity.dto.response.AllStudentAnswers;
import ru.rtstudy.educplatformsecurity.dto.response.GradeDtoResponse;
import ru.rtstudy.educplatformsecurity.dto.response.GradeStudentDtoResponse;
import ru.rtstudy.educplatformsecurity.model.Grade;
import ru.rtstudy.educplatformsecurity.model.User;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Query("""
            select g
            from Grade g
            join g.lesson l
            where l.course.id = :courseId
            order by g.id asc
            """)
    Optional<List<Grade>> findAllGradesByCourseId(Long courseId);

    @Query("""
            select new GradeStudentDtoResponse(g.id, s.firstName, s.lastName, l.title, t.description, g.grade, g.rework, g.studentAnswer, g.mentorAnswer)
            from Grade g
            join g.lesson l
            join g.student s
            join l.taskId t
            where l.id = :lessonId
            order by g.id
            """)
    Optional<List<GradeStudentDtoResponse>> findAllStudentsAnswersByLessonId(Long lessonId);

    @Query("""
            select new GradeStudentDtoResponse(g.id, s.firstName, s.lastName, l.title, t.description, g.grade, g.rework, g.studentAnswer, g.mentorAnswer)
            from Grade g
            join g.lesson l
            join g.student s
            join l.taskId t
            where l.course.id = :courseId
            order by g.id
            """)
    Optional<List<GradeStudentDtoResponse>> findAllStudentsAnswersByCourseId(Long courseId);

    @Query("""
            select new AllStudentAnswers(g.id,
             t.description,
             g.grade,
             g.rework,
             g.studentAnswer,
             g.mentorAnswer)
            from Grade g
            join Lesson l on g.lesson.id = l.id
            join Task t on l.taskId.id = t.id
            where g.student.id = :id
            order by g.id
            """)
    Optional<List<AllStudentAnswers>> getAllStudentAnswer(Long id);

    @Query("""
            select new AllStudentAnswers(g.id,
             t.description,
             g.grade,
             g.rework,
             g.studentAnswer,
             g.mentorAnswer)
            from Grade g
            join Lesson l on g.lesson.id = l.id
            join Task t on l.taskId.id = t.id
            where g.student.id = :userId
            and l.course.id = :courseId
            order by g.id
            """)
    Optional<List<AllStudentAnswers>> findAllStudentsAnswerForCourse(Long courseId, Long userId);

    @Modifying
    @Query("""
            update Grade g
            set g.grade = :grade,
            g.rework = :rework,
            g.mentorAnswer = :mentorAnswer,
            g.mentor = :mentor
            where g.id = :gradeId
            """)
    void addMentorReview(Long gradeId, Byte grade, Boolean rework, String mentorAnswer, User mentor);

    @Query("""
            select new GradeDtoResponse(g.id, l.title, t.description, g.grade, g.rework, g.studentAnswer, g.mentorAnswer)
            from Grade g
            join g.lesson l
            join l.taskId t
            where g.id = :gradeId
            """)
    Optional<GradeDtoResponse> getGradeById(Long gradeId);

    @Modifying
    @Query("""
            update Grade g
            set g.studentAnswer = :studentAnswer
            where g.id = :gradeId
            and g.student.id = :studentId
            """)
    void changeAnswer(Long gradeId, String studentAnswer, Long studentId);

    @Query("""
            select l.id
            from Lesson l
            where l.course.id = :courseId
            """)
    Optional<List<Long>> getAllLessonsId(Long courseId);

    @Query(value = """
            select g
            from Grade g
            where g.lesson.id in :lessonsIds
            and g.rework = false
            and g.grade > 0
            and g.student.id = :userId
            """)
    Optional<List<Grade>> getGrades(List<Long> lessonsIds, Long userId);

    @Modifying
    @Query("""
            update Grade g
            set g.grade =:grade,
            g.rework =:rework,
            g.mentorAnswer =:mentorAnswer
            where g.id =:gradeId
                    """)
    void updateMentorReview(Long gradeId, Byte grade, Boolean rework, String mentorAnswer);

    @Query("""
            select count(g)
            from Grade g
            where g.mentor.id =:userId
                   """)
    int countAllAnswersByMentorUserId(Long userId);

    @Query("""
            select new AllStudentAnswers(g.id, t.description, g.grade, g.rework, g.studentAnswer, g.mentorAnswer)
            from Grade g
            join Lesson l
                on l.id = g.lesson.id
            join Task t
                on l.taskId.id = t.id
            where l.id =:lessonId
                and g.student.id =:studentId
            """)
    List<AllStudentAnswers> getAnswersByStudentForLesson(Long studentId, Long lessonId);

}
