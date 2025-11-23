
package SkillForgeApp;
import java.util.*;

public class InstructorService {
    public void createCourse(String title, String desc, String instName) {
        String id = UUID.randomUUID().toString().substring(0,5);
        Course c = new Course(id, title, desc, instName);
        Database.courses.add(c);
    }
    
    public void addLessonToCourse(Course c, String title, String content, Quiz q) {
        String id = UUID.randomUUID().toString().substring(0,5);
        Lesson l = new Lesson(id, title, content);
        l.quiz = q;
        c.lessons.add(l);
    }

    // New method to get courses for a specific instructor
    public List<Course> getInstructorCourses(String instructorName) {
        List<Course> myCourses = new ArrayList<>();
        for(Course c : Database.courses) {
            if(c.instructorName.equals(instructorName)) {
                myCourses.add(c);
            }
        }
        return myCourses;
    }
}