package services;

import data.JsonDatabaseManager;
import models.*;

import java.util.*;
import java.util.stream.Collectors;

public class CourseService {
    private JsonDatabaseManager db = new JsonDatabaseManager();

    public List<Course> allCourses(boolean onlyApproved) {
        List<Course> cs = db.loadCourses();
        if (onlyApproved) {
            return cs.stream().filter(c -> "APPROVED".equals(c.getStatus())).collect(Collectors.toList());
        }
        return cs;
    }
    public Course find(String id){ return db.findCourseById(id); }
    public void addCourse(Course c){ db.updateCourse(c); }
    public void approveCourse(String id, boolean approve) {
        Course c = find(id);
        if (c==null) return;
        c.setStatus(approve ? "APPROVED" : "REJECTED");
        db.updateCourse(c);
    }
    public List<Course> pendingCourses(){ return db.loadCourses().stream().filter(c->"PENDING".equals(c.getStatus())).collect(Collectors.toList()); }
}
