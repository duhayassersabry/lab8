package SkillForgeApp;

import java.util.*;

public class StudentService {
    public void enroll(Student s, Course c) {
        if(!s.enrolledCourseIds.contains(c.id)) {
            s.enrolledCourseIds.add(c.id);
            s.completedLessonsMap.put(c.id, new HashSet<>());
        }
    }

    public boolean completeLesson(Student s, String courseId, Lesson l) {
        // Mark lesson as done
        if(s.completedLessonsMap.containsKey(courseId)) {
            s.completedLessonsMap.get(courseId).add(l.id);
            return true;
        }
        return false;
    }

    public Certificate checkAndGenerateCertificate(Student s, Course c) {
        Set<String> completed = s.completedLessonsMap.get(c.id);
        // If completed lessons count == total lessons count
        if(completed != null && completed.size() == c.lessons.size() && !c.lessons.isEmpty()) {
            Certificate cert = new Certificate(s.username, c.title);
            s.certificates.add(cert);
            return cert;
        }
        return null;
    }
}
