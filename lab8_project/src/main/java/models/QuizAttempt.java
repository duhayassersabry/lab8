package models;

import java.util.Date;

public class QuizAttempt {
    private String lessonTitle;
    private int score; 
    private boolean firstAttempt;
    private Date attemptedAt;

    public QuizAttempt() {}
    public QuizAttempt(String lessonTitle, int score, boolean firstAttempt) {
        this.lessonTitle = lessonTitle;
        this.score = score;
        this.firstAttempt = firstAttempt;
        this.attemptedAt = new Date();
    }
    public String getLessonTitle(){ return lessonTitle; }
    public int getScore(){ return score; }
    public boolean isFirstAttempt(){ return firstAttempt; }
    public Date getAttemptedAt(){ return attemptedAt; }
}
