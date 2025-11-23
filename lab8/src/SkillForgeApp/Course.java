
package SkillForgeApp;
import java.util.*;

public class Course {
    String id;
    String title;
    String description;
    String instructorName;
    CourseStatus status; 
    List<Lesson> lessons = new ArrayList<>();

    public Course(String id, String t, String d, String iName) {
        this.id = id;
        this.title = t;
        this.description = d;
        this.instructorName = iName;
        this.status = CourseStatus.PENDING; // Default is Pending
    }
}
