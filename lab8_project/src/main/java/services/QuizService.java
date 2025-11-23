package services;

import data.JsonDatabaseManager;
import models.*;

import java.util.*;

public class QuizService {
    private JsonDatabaseManager db = new JsonDatabaseManager();

    // Evaluate a quiz: return percent score 0-100
    public int evaluate(Quiz quiz, List<Integer> selectedIndices) {
        if (quiz == null || quiz.getQuestions() == null) return 0;
        int total = quiz.getQuestions().size();
        int correct=0;
        for (int i=0;i<total && i<selectedIndices.size();i++){
            if (selectedIndices.get(i) == quiz.getQuestions().get(i).getCorrectIndex()) correct++;
        }
        return (int)((correct * 100.0)/total);
    }

    // Save attempt: simple approach - stored inside student progress as metadata (for demo)
    public void recordAttempt(Student student, Quiz quiz, int score) {
        // In a full design you'd have a separate file; here we persist via updateUser
        // Mark lesson as passed if >= passingPercentage
        // The caller should map quiz->lesson and update student's progress
        // Placeholder: no-op here, but you can extend to keep attempts list in Student
    }
}
