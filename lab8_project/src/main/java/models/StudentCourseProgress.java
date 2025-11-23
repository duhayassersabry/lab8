// models/StudentCourseProgress.java
package models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
public class StudentCourseProgress implements Serializable {
 
    private Map<String, Boolean> lessonCompletion = new HashMap<>(); 
    
    private Map<String, List<QuizAttempt>> quizAttempts = new HashMap<>(); 

    public StudentCourseProgress() {}

    public Map<String, Boolean> getLessonCompletion() {
        return lessonCompletion;
    }

    public void setLessonCompletion(Map<String, Boolean> lessonCompletion) {
        this.lessonCompletion = lessonCompletion;
    }

    public Map<String, List<QuizAttempt>> getQuizAttempts() {
        return quizAttempts;
    }

    public void setQuizAttempts(Map<String, List<QuizAttempt>> quizAttempts) {
        this.quizAttempts = quizAttempts;
    }
}