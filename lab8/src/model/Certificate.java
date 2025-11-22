package model;

import java.time.LocalDate;
import java.util.UUID;

public class Certificate {
    private String certificateId;
    private String studentId;
    private String courseId;
    private String courseTitle; // Stored to display easily without looking up the Course object
    private String issueDate;   // Storing as String (ISO-8601) avoids JSON parsing headaches

    public Certificate(String studentId, String courseId, String courseTitle) {
        this.certificateId = UUID.randomUUID().toString(); // Auto-generate unique ID
        this.studentId = studentId;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.issueDate = LocalDate.now().toString(); // e.g., "2025-11-22"
    }

    // Getters
    public String getCertificateId() { return certificateId; }
    public String getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }
    public String getCourseTitle() { return courseTitle; }
    public String getIssueDate() { return issueDate; }

    public String toString() {
        return "Certificate [ID=" + certificateId + ", Course=" + courseTitle + "]";
    }
}