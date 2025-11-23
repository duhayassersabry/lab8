package models;

import java.util.*;

public class Student extends User {
    private List<String> enrolledCourses = new ArrayList<>();
    private Map<String, Map<String, Boolean>> progress = new HashMap<>();
    private List<Certificate> certificates = new ArrayList<>();

    public Student(){ this.role="Student"; }
    public Student(String id,String name,String email,String password){
        super(id,name,email,password,"Student");
    }
    public List<String> getEnrolledCourses(){ return enrolledCourses; }
    public Map<String, Map<String, Boolean>> getProgress(){ return progress; }
    public List<Certificate> getCertificates(){ return certificates; }
    public void setEnrolledCourses(List<String> e){ this.enrolledCourses=e; }
    public void setProgress(Map<String, Map<String, Boolean>> p){ this.progress=p; }
    public void setCertificates(List<Certificate> c){ this.certificates=c; }
}
