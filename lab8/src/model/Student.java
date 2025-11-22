package model;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Student extends User {
    private Map<String, Map<String, Double>> quizScores;
    private Map<String, List<String>> completedLessons;
    private List<Certificate> certificates;
    private List<String> enrolledCourseIds;

    public Student(String userId, String username, String email, String passwordHash) {
        super(userId, username, email, passwordHash, UserRole.STUDENT);
        this.certificates = new ArrayList<>();
        this.quizScores = new HashMap<>();
        this.completedLessons = new HashMap<>();
        this.enrolledCourseIds = new ArrayList<>();
    }

    public void enrollInCourse(String courseId) {
        if (!enrolledCourseIds.contains(courseId)) {
            enrolledCourseIds.add(courseId);
        }
    }

    public List<String> getEnrolledCourseIds() {
        return enrolledCourseIds;
    }

    public boolean hasPassedLesson(String courseId, String lessonId) {
        if (completedLessons.containsKey(courseId)) {
            return completedLessons.get(courseId).contains(lessonId);
        }
        return false;
    }

    public void addCertificate(Certificate cert) {
        this.certificates.add(cert);
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public boolean hasCertificateFor(String courseId) {
        for (Certificate c : certificates) {
            if (c.getCourseId().equals(courseId)) {
                return true;
            }
        }
        return false;
    }

    public void markLessonComplete(String courseId, String lessonId, double score) {
        quizScores.putIfAbsent(courseId, new HashMap<>());
        quizScores.get(courseId).put(lessonId, score);
        completedLessons.putIfAbsent(courseId, new ArrayList<>());
        List<String> finishedLessons = completedLessons.get(courseId);
        if (!finishedLessons.contains(lessonId)) {
            finishedLessons.add(lessonId);
        }
    }

    public int getCompletedLessonCount(String courseId) {
        if (completedLessons.containsKey(courseId)) {
            return completedLessons.get(courseId).size();
        }
        return 0;
    }
}
