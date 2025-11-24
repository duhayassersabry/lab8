
package SkillForgeApp;
import java.util.*;

public class Database {
    public static List<User> users = new ArrayList<>();
    public static List<Course> courses = new ArrayList<>();
    public static User currentUser = null;

    // Seed Data
    static {
        users.add(new Admin("A1", "SuperAdmin", "admin@test.com", "123"));
        users.add(new Instructor("I1", "Dr. Smith", "inst@test.com", "123"));
        users.add(new Student("S1", "John Doe", "student@test.com", "123"));

        Course javaC = new Course("C1", "Java 101", "Learn Java Basics", "Dr. Smith");
        javaC.status = CourseStatus.APPROVED; 
        
        Lesson l1 = new Lesson("L1", "Variables", "Int, String, Boolean...");
        Quiz q1 = new Quiz();
        q1.addQuestion("What is 2+2?", "4");
        l1.quiz = q1;
        javaC.lessons.add(l1);
        
        courses.add(javaC);
    }
}