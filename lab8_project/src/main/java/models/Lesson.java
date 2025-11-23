package models;

public class Lesson {
    private String title;
    private String content;
    private Quiz quiz;

    public Lesson(){}
    public Lesson(String t, String c){ this.title=t; this.content=c; }
    public String getTitle(){ return title; }
    public String getContent(){ return content; }
    public Quiz getQuiz(){ return quiz; }
    public void setQuiz(Quiz q){ this.quiz=q; }
}
