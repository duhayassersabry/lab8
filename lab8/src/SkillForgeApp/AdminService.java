package SkillForgeApp;

import java.util.*;

public class AdminService {
    public void approveCourse(Course c) {
        c.status = CourseStatus.APPROVED;
    }
    
    public void rejectCourse(Course c) {
        c.status = CourseStatus.REJECTED;
    }
    
    public List<Course> getPendingCourses() {
        List<Course> pending = new ArrayList<>();
        for(Course c : Database.courses) {
            if(c.status == CourseStatus.PENDING) pending.add(c);
        }
        return pending;
    }
}
