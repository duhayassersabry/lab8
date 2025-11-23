
package SkillForgeApp;
import java.util.*;

public class Quiz {
    List<String> questions = new ArrayList<>();
    List<String> answers = new ArrayList<>();

    public void addQuestion(String question, String correctAnswer) {
        questions.add(question);
        answers.add(correctAnswer);
    }
}