package services;

import data.JsonDatabaseManager;
import models.*;

import java.util.*;

public class QuizService {
    private JsonDatabaseManager db = new JsonDatabaseManager();

    public int evaluate(Quiz quiz, List<Integer> selectedIndices) {
        if (quiz == null || quiz.getQuestions() == null) return 0;
        int total = quiz.getQuestions().size();
        int correct = 0;
        for (int i=0;i<total && i<selectedIndices.size();i++) {
            int sel = selectedIndices.get(i);
            Question q = quiz.getQuestions().get(i);
            if (sel == q.getCorrectIndex()) correct++;
        }
        return (int) Math.round( (correct * 100.0) / total );
    }

    public QuizAttempt recordAttempt(Student s, String courseId, String lessonTitle, int score) {
        List<QuizAttempt> list = s.getAttempts().computeIfAbsent(courseId, k -> new ArrayList<>());
        boolean firstAttempt = list.stream().noneMatch(a -> a.getLessonTitle().equals(lessonTitle));
        QuizAttempt a = new QuizAttempt(lessonTitle, score, firstAttempt);
        list.add(a);
        new JsonDatabaseManager().updateUser(s);
        return a;
    }

    public boolean mayRetakeFirstThreshold(Student s, String courseId, String lessonTitle, int passingPercentage) {
        List<QuizAttempt> list = s.getAttempts().getOrDefault(courseId, new ArrayList<>());
        for (QuizAttempt a : list) {
            if (a.getLessonTitle().equals(lessonTitle) && a.isFirstAttempt() && a.getScore() >= passingPercentage) return false;
        }
        return true;
    }
}
