package model;
import java.util.ArrayList;
import java.util.List;

// Enum for Admin Approval Workflow


public class Course {
    private String courseId;
    private String title;
    private String description;
    private String instructorId; // Links to the Instructor
    private CourseStatus status; // PENDING by default
    private List<Lesson> lessons;
    private List<String> enrolledStudentIds; // List of User IDs
public enum CourseStatus {
    PENDING,
    APPROVED,
    REJECTED
}
    public Course(String courseId, String title, String description, String instructorId) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.status = CourseStatus.PENDING; // Default for new courses
        this.lessons = new ArrayList<>();
        this.enrolledStudentIds = new ArrayList<>();
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public void enrollStudent(String studentId) {
        if (!enrolledStudentIds.contains(studentId)) {
            enrolledStudentIds.add(studentId);
        }
    }

    // Getters and Setters
    public String getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public String getInstructorId() { return instructorId; }
    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }
    public List<Lesson> getLessons() { return lessons; }
    public List<String> getEnrolledStudentIds() { return enrolledStudentIds; }
}