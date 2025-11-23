package models;
import java.util.Date;

public class QuizAttempt {
    private String studentId;
    private String quizId;
    private int score; // 0-100
    private Date date;

    public QuizAttempt(){}
    public QuizAttempt(String studentId,String quizId,int score){
        this.studentId=studentId; this.quizId=quizId; this.score=score; this.date=new Date();
    }
    public String getStudentId(){return studentId;}
    public String getQuizId(){return quizId;}
    public int getScore(){return score;}
    public Date getDate(){return date;}
}
