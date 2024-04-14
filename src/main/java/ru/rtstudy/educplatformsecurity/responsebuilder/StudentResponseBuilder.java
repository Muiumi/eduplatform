package ru.rtstudy.educplatformsecurity.responsebuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.rtstudy.educplatformsecurity.dto.request.ChangeStudentAnswerDto;
import ru.rtstudy.educplatformsecurity.dto.request.StudentAnswerDto;
import ru.rtstudy.educplatformsecurity.dto.response.AllStudentAnswers;
import ru.rtstudy.educplatformsecurity.service.GradeService;
import ru.rtstudy.educplatformsecurity.service.UserCourseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentResponseBuilder {

    private final UserCourseService userCourseService;
    private final GradeService gradeService;

    public ResponseEntity<HttpStatus> enterOnCourse(Long courseId) {
        userCourseService.enterOnCourse(courseId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(HttpStatus.CREATED);
    }

    public ResponseEntity<HttpStatus> finishCourse(Long courseId) {
        gradeService.finishCourse(courseId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(HttpStatus.CREATED);
    }

    public ResponseEntity<StudentAnswerDto> sendAnswer(StudentAnswerDto studentAnswerDto) {
        return ResponseEntity
                .status(HttpStatus.valueOf(201))
                .body(gradeService.sendAnswer(studentAnswerDto));
    }


    public ResponseEntity<List<AllStudentAnswers>> findAllStudentAnswer() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gradeService.findAllStudentAnswer());
    }

    public ResponseEntity<List<AllStudentAnswers>> findAllStudentsAnswerForCourse(Long courseId) {
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(gradeService.findAllStudentsAnswerForCourse(courseId));
    }

    public ResponseEntity<List<AllStudentAnswers>> findAllStudentAnswersForLesson(Long lessonId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gradeService.getAllAnswersByStudentForLesson(lessonId));
    }

    public ResponseEntity<ChangeStudentAnswerDto> changeAnswer(Long gradeId, ChangeStudentAnswerDto studentsAnswerDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gradeService.changeAnswer(gradeId, studentsAnswerDto));
    }

    public ResponseEntity<HttpStatus> upgradeToMentor(Long courseId) {
        userCourseService.upgradeToMentor(courseId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(HttpStatus.CREATED);
    }

}
