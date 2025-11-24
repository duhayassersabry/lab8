package services;

import data.JsonDatabaseManager;
import models.*;

import java.util.*;

public class AnalyticsService {
    private JsonDatabaseManager db = new JsonDatabaseManager();

    public Map<String,Integer> courseStudentAverages(String courseId) {
        Map<String,Integer> map = new LinkedHashMap<>();
        for (User u : db.loadUsers()) {
            if (u instanceof Student) {
                Student s = (Student) u;
                if (s.getEnrolledCourses().contains(courseId)) {
                    int total=0,count=0;
                    Course c = db.findCourseById(courseId);
                    if (c==null) continue;
                    for (Lesson L : c.getLessons()) {
                        int best=0;
                        for (QuizAttempt a : s.getAttempts().getOrDefault(courseId, new ArrayList<>()))
                            if (a.getLessonTitle().equals(L.getTitle())) best = Math.max(best, a.getScore());
                        total += best; count++;
                    }
                    int avg = count==0?0:total/count;
                    map.put(s.getId() + " - " + s.getEmail(), avg);
                }
            }
        }
        return map;
    }
}
