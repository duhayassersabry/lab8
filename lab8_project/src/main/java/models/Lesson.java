package models;

public class Lesson {
    private String lessonId;
    private String title;
    private String content;
    private Quiz quiz;

    public Lesson() {}
    public Lesson(String lessonId, String title, String content) {
        this.lessonId = lessonId; this.title = title; this.content = content;
    }
    public String getLessonId(){ return lessonId; }
    public String getTitle(){ return title; }
    public String getContent(){ return content; }
    public Quiz getQuiz(){ return quiz; }
    public void setLessonId(String s){ this.lessonId=s; }
    public void setTitle(String s){ this.title=s; }
    public void setContent(String s){ this.content=s; }
    public void setQuiz(Quiz q){ this.quiz=q; }
}
