package models;

import java.util.Date;

public class Certificate {
    private String certificateId;
    private String studentEmail;
    private String studentId;
    private String courseId;
    private int totalMarks;
    private Date issuedAt;

    public Certificate() {}
    public Certificate(String id, String studentEmail, String studentId, String courseId, int totalMarks) {
        this.certificateId = id; this.studentEmail = studentEmail; this.studentId = studentId;
        this.courseId = courseId; this.totalMarks = totalMarks; this.issuedAt = new Date();
    }

    public String getCertificateId(){ return certificateId; }
    public String getStudentEmail(){ return studentEmail; }
    public String getStudentId(){ return studentId; }
    public String getCourseId(){ return courseId; }
    public int getTotalMarks(){ return totalMarks; }
    public Date getIssuedAt(){ return issuedAt; }
}
