package models;

import java.util.*;

public class Student extends User {
    private List<String> enrolledCourses = new ArrayList<>();
    private Map<String, Map<String,Boolean>> progress = new HashMap<>(); 
    private Map<String, List<QuizAttempt>> attempts = new HashMap<>(); 
    private List<Certificate> certificates = new ArrayList<>();

    public Student() { this.role = "Student"; }
    public Student(String id, String name, String email, String password) {
        super(id,name,email,password,"Student");
    }

    public List<String> getEnrolledCourses(){ return enrolledCourses; }
    public Map<String, Map<String,Boolean>> getProgress(){ return progress; }
    public Map<String, List<QuizAttempt>> getAttempts(){ return attempts; }
    public List<Certificate> getCertificates(){ return certificates; }

    public void setEnrolledCourses(List<String> l){ this.enrolledCourses = l; }
    public void setProgress(Map<String, Map<String,Boolean>> m){ this.progress = m; }
    public void setAttempts(Map<String, List<QuizAttempt>> a){ this.attempts = a; }
    public void setCertificates(List<Certificate> c){ this.certificates = c; }
}
