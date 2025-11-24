package models;

import java.util.*;

public class Quiz {
    private String quizId;
    private List<Question> questions = new ArrayList<>();
    private int passingPercentage = 50;

    public Quiz() {}
    public Quiz(String id){ this.quizId=id; }
    public String getQuizId(){ return quizId; }
    public List<Question> getQuestions(){ return questions; }
    public int getPassingPercentage(){ return passingPercentage; }
    public void setQuizId(String s){ this.quizId=s; }
    public void setQuestions(List<Question> q){ this.questions=q; }
    public void setPassingPercentage(int p){ this.passingPercentage=p; }
}
