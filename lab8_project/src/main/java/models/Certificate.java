package models;

import java.util.Date;

public class Certificate {
    private String certificateId;
    private String studentEmail;
    private String courseId;
    private Date issuedAt;

    public Certificate() {}

    public Certificate(String certificateId, String studentEmail, String courseId) {
        this.certificateId = certificateId;
        this.studentEmail = studentEmail;
        this.courseId = courseId;
        this.issuedAt = new Date();
    }
    

    public String getCertificateId() { return certificateId; }
    public String getStudentEmail() { return studentEmail; }
    public String getCourseId() { return courseId; }
    public Date getIssuedAt() { return issuedAt; }
}
