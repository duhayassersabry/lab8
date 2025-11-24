package services;

import data.JsonDatabaseManager;
import models.*;

import java.util.ArrayList;
import java.util.List;

public class CourseService {
    private JsonDatabaseManager db = new JsonDatabaseManager();

    public List<Course> allCourses(boolean approvedOnly) {
        List<Course> out = new ArrayList<>();
        for (Course c : db.loadCourses()) {
            if (!approvedOnly || "Approved".equalsIgnoreCase(c.getStatus())) out.add(c);
        }
        return out;
    }

    public Course find(String id) { return db.findCourseById(id); }

    public void save(Course c) { db.updateCourse(c); }

    public void addCourse(Course c) { db.updateCourse(c); }

    public void setCourseStatus(String courseId, String status) {
        Course c = db.findCourseById(courseId);
        if (c != null) { c.setStatus(status); db.updateCourse(c); }
    }
}
