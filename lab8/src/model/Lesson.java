package model;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private String lessonId;
    private String title;
    private String content; // Could be text or a video URL
    private List<String> resources; // Optional external links/files
    private Quiz quiz; // One quiz per lesson

    public Lesson(String lessonId, String title, String content) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.resources = new ArrayList<>();
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    // Getters and Setters
    public String getLessonId() { return lessonId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Quiz getQuiz() { return quiz; }
}