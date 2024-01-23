package ru.rtstudy.educplatformsecurity.model;

import jakarta.persistence.*;
import lombok.*;
import ru.rtstudy.educplatformsecurity.model.constant.CreateUpdateTime;

@Entity(name = "Lesson")
@Table(name = "lessons")
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "file_name")
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task taskId;

    @Embedded
    private CreateUpdateTime time;

}
