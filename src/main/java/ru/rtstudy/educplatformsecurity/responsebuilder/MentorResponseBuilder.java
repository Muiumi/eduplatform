package ru.rtstudy.educplatformsecurity.responsebuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.rtstudy.educplatformsecurity.dto.request.MentorAnswerDtoRequest;
import ru.rtstudy.educplatformsecurity.dto.response.CourseLongDescriptionDto;
import ru.rtstudy.educplatformsecurity.dto.response.GradeDtoResponse;
import ru.rtstudy.educplatformsecurity.dto.response.GradeStudentDtoResponse;
import ru.rtstudy.educplatformsecurity.service.MentorService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorResponseBuilder {

    private final MentorService mentorService;

    public ResponseEntity<List<GradeDtoResponse>> getAllAnswersForMentorCourses() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mentorService.getAllAnswersForMentorCourses());
    }

    public ResponseEntity<List<GradeStudentDtoResponse>> getAllAnswersForCourse(Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mentorService.getAllAnswersForCourse(id));
    }

    public ResponseEntity<List<GradeStudentDtoResponse>> getAllAnswersForLesson(Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mentorService.getAllAnswersForLesson(id));
    }

    public ResponseEntity<MentorAnswerDtoRequest> reviewStudentAnswer(Long id, MentorAnswerDtoRequest mentorAnswerDtoRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mentorService.reviewStudentAnswer(id, mentorAnswerDtoRequest));
    }

    public ResponseEntity<MentorAnswerDtoRequest> updateMentorAnswer(Long id, MentorAnswerDtoRequest mentorAnswerDtoRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mentorService.updateMentorAnswer(id, mentorAnswerDtoRequest));
    }

    public ResponseEntity<HttpStatus> upgradeToAuthor() {
        mentorService.upgradeToAuthor();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(HttpStatus.CREATED);
    }

    public ResponseEntity<List<CourseLongDescriptionDto>> getAllMentorCourses() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mentorService.getMentorCourses());
    }
}
