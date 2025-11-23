package models;
import java.util.*;

public class Course {
    private String courseId;
    private String title;
    private String description;
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
    private String instructorId;
    private List<Lesson> lessons = new ArrayList<>();

    public Course(){}
    public Course(String id,String title,String desc,String instructorId){
        this.courseId=id; this.title=title; this.description=desc; this.instructorId=instructorId;
    }
    public String getCourseId(){return courseId;}
    public String getTitle(){return title;}
    public String getDescription(){return description;}
    public String getStatus(){return status;}
    public void setStatus(String s){ this.status=s; }
    public List<Lesson> getLessons(){return lessons;}
    public String getInstructorId(){return instructorId;}
}
