package models;

import java.util.*;

public class Course {
    private String courseId;
    private String title;
    private String description;
    private String instructorId;
    private String status = "Pending"; 
    private List<Lesson> lessons = new ArrayList<>();

    public Course() {}
    public Course(String id, String title, String descr, String instructorId) {
        this.courseId=id; this.title=title; this.description=descr; this.instructorId=instructorId;
    }

    public String getCourseId(){ return courseId; }
    public String getTitle(){ return title; }
    public String getDescription(){ return description; }
    public String getInstructorId(){ return instructorId; }
    public String getStatus(){ return status; }
    public List<Lesson> getLessons(){ return lessons; }

    public void setCourseId(String s){ this.courseId=s; }
    public void setTitle(String s){ this.title=s; }
    public void setDescription(String s){ this.description=s; }
    public void setInstructorId(String s){ this.instructorId=s; }
    public void setStatus(String s){ this.status=s; }
    public void setLessons(List<Lesson> l){ this.lessons = l; }
}
