package service;

import data.jsonDataBase;
import model.*;
import java.util.ArrayList;
import java.util.List;

public class CourseService {

    // Create a new course (defaults to PENDING via Course constructor)
    public void createCourse(Course newCourse, Instructor instructor) {
        // 1. Save to Global Course List
        List<Course> courses = jsonDataBase.loadCourses();
        courses.add(newCourse);
        jsonDataBase.saveCourses(courses);

        // 2. Link to Instructor
        List<User> users = jsonDataBase.loadUsers();
        for (User u : users) {
            if (u.getUserId().equals(instructor.getUserId()) && u instanceof Instructor) {
                ((Instructor) u).addCreatedCourse(newCourse.getCourseId());
                break;
            }
        }
        jsonDataBase.saveUsers(users);
    }

    public void addLessonToCourse(String courseId, Lesson lesson) {
        List<Course> courses = jsonDataBase.loadCourses();
        for (Course c : courses) {
            if (c.getCourseId().equals(courseId)) {
                c.addLesson(lesson);
                break;
            }
        }
        jsonDataBase.saveCourses(courses);
    }

    // Returns only APPROVED courses for Students
    public List<Course> getAvailableCourses() {
        List<Course> all = jsonDataBase.loadCourses();
        List<Course> approved = new ArrayList<>();
        for (Course c : all) {
            if (c.getStatus() == model.Course.CourseStatus.APPROVED) {
                approved.add(c);
            }
        }
        return approved;
    }
    
    // Returns all courses created by a specific instructor
    public List<Course> getInstructorCourses(String instructorId) {
        List<Course> all = jsonDataBase.loadCourses();
        List<Course> mine = new ArrayList<>();
        for (Course c : all) {
            if (c.getInstructorId().equals(instructorId)) {
                mine.add(c);
            }
        }
        return mine;
    }
}
