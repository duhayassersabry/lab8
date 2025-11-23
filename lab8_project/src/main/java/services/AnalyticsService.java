// services/AnalyticsService.java
package services;

import data.JsonDatabaseManager;
import models.*;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsService {
    private JsonDatabaseManager db = new JsonDatabaseManager();

    // Data structure to simplify analytics transfer to the UI
    public static class LessonStats {
        public String lessonTitle;
        public int attemptsCount = 0;
        public int scoreSum = 0;
        public int completedCount = 0;
        
        public LessonStats(String title) {
            this.lessonTitle = title;
        }

        public double getAverageScore() {
            return attemptsCount > 0 ? (double) scoreSum / attemptsCount : 0.0;
        }
    }

    /**
     * Aggregates performance statistics for a specific course.
     * @param courseId The ID of the course to analyze.
     * @return Map containing: "totalEnrolled" (int) and "lessonData" (Map<String, LessonStats>).
     */
    public Map<String, Object> getCoursePerformance(String courseId) {
        Course course = db.findCourseById(courseId);
        if (course == null) return Collections.emptyMap();
        
        List<Student> students = db.loadUsers().stream()
                .filter(u -> u.getRole().equals("Student"))
                .map(u -> (Student) u)
                .collect(Collectors.toList());

        Map<String, LessonStats> lessonData = new HashMap<>();
        // Initialize lesson stats map based on the course structure
        for (Lesson lesson : course.getLessons()) {
            lessonData.put(lesson.getLessonId(), new LessonStats(lesson.getTitle()));
        }

        int totalEnrolled = 0;
        for (Student student : students) {
            StudentCourseProgress progress = student.getCourseProgress().get(courseId);
            if (progress != null) {
                totalEnrolled++;
                for (Lesson lesson : course.getLessons()) {
                    LessonStats stats = lessonData.get(lesson.getLessonId());
                    
                    // 1. Completion Percentage
                    if (progress.getLessonCompletion().getOrDefault(lesson.getLessonId(), false)) {
                        stats.completedCount++;
                    }
                    
                    // 2. Quiz Averages
                    // Assuming one Quiz per Lesson (or using the Lesson ID as the Quiz ID)
                    List<QuizAttempt> attempts = progress.getQuizAttempts().get(lesson.getLessonId());
                    if (attempts != null && !attempts.isEmpty()) {
                        QuizAttempt latestAttempt = attempts.get(attempts.size() - 1);
                        stats.scoreSum += latestAttempt.getScore();
                        stats.attemptsCount++; // Counting the student's *best* or *latest* score is often better
                    }
                }
            }
        }
        
        Map<String, Object> results = new HashMap<>();
        results.put("totalEnrolled", totalEnrolled);
        results.put("lessonData", lessonData); 

        return results;
    }
}