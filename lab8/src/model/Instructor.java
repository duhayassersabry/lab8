package model;

import java.util.ArrayList;
import java.util.List;

public class Instructor extends User {
    // Inherits: userId, username, email, passwordHash, role from User
    
    private List<String> createdCourseIds; // References to courses they own

    public Instructor(String userId, String username, String email, String passwordHash) {
       super(userId, username, email, passwordHash, UserRole.INSTRUCTOR);
        this.createdCourseIds = new ArrayList<>();
    }

    public void addCreatedCourse(String courseId) {
        this.createdCourseIds.add(courseId);
    }

    public List<String> getCreatedCourseIds() {
        return createdCourseIds;
    }
}