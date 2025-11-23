
package SkillForgeApp;

import java.util.*;

public class Student extends User {
    List<String> enrolledCourseIds = new ArrayList<>();
    // Tracks progress: CourseID -> Set of Completed Lesson IDs
    Map<String, Set<String>> completedLessonsMap = new HashMap<>();
    List<Certificate> certificates = new ArrayList<>();

    public Student(String id, String u, String e, String p) {
        super(id, u, e, p, UserRole.STUDENT);
    }
}