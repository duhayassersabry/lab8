package services;

import data.JsonDatabaseManager;
import models.Course;
import models.Lesson;
import java.util.*;
import java.util.stream.Collectors;

public class CourseService {
    private JsonDatabaseManager db = new JsonDatabaseManager();

    // R - Read/Retrieve Operations
    public List<Course> allCourses(boolean onlyApproved) {
        List<Course> cs = db.loadCourses();
        if (onlyApproved) {
            return cs.stream().filter(c -> "APPROVED".equals(c.getStatus())).collect(Collectors.toList());
        }
        return cs;
    }
    
    public Course find(String id){ 
        return db.findCourseById(id); 
    }
    
    public List<Course> pendingCourses(){ 
        return db.loadCourses().stream()
                 .filter(c->"PENDING".equals(c.getStatus()))
                 .collect(Collectors.toList()); 
    }
    
    // C - Create Operation (Handles initial save)
    /**
     * Creates a new course and saves it to the database.
     * Note: This delegates to updateCourse in the DB Manager for simplicity.
     */
    public void addCourse(Course c){ 
        // Ensure status is PENDING upon creation if not already set
        if (c.getStatus() == null || c.getStatus().isEmpty()) {
            c.setStatus("PENDING");
        }
        db.updateCourse(c); 
    }
    
    // U - Update Operation (Handles modifications like adding lessons or students)
    /**
     * Updates an existing course object (e.g., adding a lesson, enrolling a student).
     */
    public void updateCourse(Course c){
        db.updateCourse(c);
    }
    
    // U - Update Status Operation (Used by Admin Dashboard)
    public void approveCourse(String id, boolean approve) {
        Course c = find(id);
        if (c==null) return;
        c.setStatus(approve ? "APPROVED" : "REJECTED");
        db.updateCourse(c);
    }
    
    // U - Utility for adding a lesson (Ensures persistence)
    /**
     * Adds a lesson to a course and updates the database.
     */
    public boolean addLessonToCourse(String courseId, Lesson lesson) {
        Course c = find(courseId);
        if (c == null) return false;
        
        // Validation: Prevent duplicate lesson IDs (essential for progress tracking)
        boolean idExists = c.getLessons().stream()
                           .anyMatch(l -> l.getLessonId().equals(lesson.getLessonId()));
        if (idExists) {
            System.err.println("Error: Lesson ID already exists.");
            return false;
        }

        c.getLessons().add(lesson);
        db.updateCourse(c);
        return true;
    }

    // Note: If you implement D - Delete, you'll need a deleteCourse method here.
}