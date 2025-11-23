package models;

public class Lesson {
    // 1. ADDED: lessonId is essential for tracking progress
    private String lessonId; 
    private String title;
    private String content;
    private Quiz quiz;

    public Lesson(){}
    
    // 2. UPDATED: Constructor to accept and set the ID
    public Lesson(String lessonId, String t, String c){ 
        this.lessonId = lessonId; // Assign the unique ID
        this.title = t; 
        this.content = c; 
    }
    
    // --- Getters and Setters ---
    
    // 3. NEW: Getter for the lessonId
    public String getLessonId() {
        return lessonId;
    }

    // 4. NEW: Setter for the lessonId (though usually set at creation)
    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getTitle(){ return title; }
    public String getContent(){ return content; }
    public Quiz getQuiz(){ return quiz; }
    public void setQuiz(Quiz q){ this.quiz = q; }
    
    // Optional: Add setters for title and content if editing is allowed
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
}