/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String quizId;
    private List<Question> questions;
    private double passingScore; // e.g., 70.0 for 70%

    public Quiz(String quizId, double passingScore) {
        this.quizId = quizId;
        this.passingScore = passingScore;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    // Logic to calculate score
    public double calculateScore(List<Integer> studentAnswers) {
        int correctCount = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (i < studentAnswers.size() && 
                questions.get(i).getCorrectOptionIndex() == studentAnswers.get(i)) {
                correctCount++;
            }
        }
        return ((double) correctCount / questions.size()) * 100;
    }

    // Getters and Setters
    public List<Question> getQuestions() { return questions; }
    public double getPassingScore() { return passingScore; }
}
