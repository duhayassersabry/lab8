package models;
import java.util.List;

public class Quiz {
    private String quizId;
    private List<Question> questions;
    private int passingPercentage = 50;

    public Quiz(){}
    public Quiz(String id, List<Question> questions){ this.quizId=id; this.questions=questions; }
    public String getQuizId(){return quizId;}
    public List<Question> getQuestions(){return questions;}
    public int getPassingPercentage(){return passingPercentage;}
    public void setPassingPercentage(int p){ this.passingPercentage=p; }
}
