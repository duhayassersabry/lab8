
package SkillForgeApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;

public class StudentPanel extends JPanel {
    StudentService studentService = new StudentService();
    Student currentStudent;

    public StudentPanel(SkillForgeApp frame, Student s) {
        this.currentStudent = s;
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();

        // --- Tab 1: Browse Available Courses ---
        JPanel browseContainer = new JPanel();
        browseContainer.setLayout(new BoxLayout(browseContainer, BoxLayout.Y_AXIS));

        for(Course c : Database.courses) {
            // Only show Approved courses
            if(c.status == CourseStatus.APPROVED) {
                JPanel courseCard = new JPanel(new BorderLayout());
                courseCard.setBorder(BorderFactory.createTitledBorder(c.title + " (by " + c.instructorName + ")"));
                courseCard.setMaximumSize(new Dimension(800, 150)); // Prevent stretching

                // Center: Description and Lesson Preview
                JPanel centerPanel = new JPanel(new GridLayout(0, 1));
                centerPanel.add(new JLabel("<html>Description: " + c.description + "</html>"));
                
                String lessonList = "Lessons: ";
                if(c.lessons.isEmpty()) {
                    lessonList += "None yet.";
                } else {
                    StringBuilder sb = new StringBuilder();
                    for(Lesson l : c.lessons) {
                        sb.append(l.title).append(", ");
                    }
                    // Remove last comma
                    if(sb.length() > 2) lessonList += sb.substring(0, sb.length()-2);
                }
                centerPanel.add(new JLabel("<html><i>" + lessonList + "</i></html>"));
                courseCard.add(centerPanel, BorderLayout.CENTER);

                // Right: Enroll Button
                JButton enrollBtn = new JButton("Enroll");
                // Disable if already enrolled
                if(s.enrolledCourseIds.contains(c.id)) {
                    enrollBtn.setText("Enrolled");
                    enrollBtn.setEnabled(false);
                } else {
                    enrollBtn.addActionListener(e -> {
                        studentService.enroll(s, c);
                        JOptionPane.showMessageDialog(this, "Enrolled in " + c.title);
                        enrollBtn.setText("Enrolled");
                        enrollBtn.setEnabled(false);
                    });
                }
                courseCard.add(enrollBtn, BorderLayout.EAST);

                browseContainer.add(courseCard);
                browseContainer.add(Box.createVerticalStrut(10));
            }
        }

        // --- Tab 2: My Learning (Active Learning) ---
        JPanel learnContainer = new JPanel();
        learnContainer.setLayout(new BoxLayout(learnContainer, BoxLayout.Y_AXIS));
        
        for(Course c : Database.courses) {
            if(s.enrolledCourseIds.contains(c.id)) {
                JPanel myCoursePanel = new JPanel(new BorderLayout());
                myCoursePanel.setBorder(BorderFactory.createTitledBorder("Studying: " + c.title));
                
                JPanel lessonsGrid = new JPanel(new GridLayout(0, 1, 5, 5));
                
                for(Lesson l : c.lessons) {
                    Set<String> completedSet = s.completedLessonsMap.get(c.id);
                    boolean isDone = completedSet != null && completedSet.contains(l.id);
                    
                    JButton lessonBtn = new JButton(l.title + (isDone ? " [✓ COMPLETED]" : " [START]"));
                    if(isDone) lessonBtn.setBackground(Color.GREEN);
                    
                    lessonBtn.addActionListener(e -> {
                        // Show Content
                        JOptionPane.showMessageDialog(this, "Content:\n" + l.content);
                        // Take Quiz if exists and not done
                        if(l.quiz != null && !isDone) {
                            takeQuiz(l, c);
                            // Update button text simply by repainting or re-init (simplified here)
                            if(s.completedLessonsMap.get(c.id).contains(l.id)) {
                                lessonBtn.setText(l.title + " [✓ COMPLETED]");
                                lessonBtn.setBackground(Color.GREEN);
                            }
                        } else if (l.quiz == null && !isDone) {
                             // Mark done if no quiz
                             studentService.completeLesson(currentStudent, c.id, l);
                             lessonBtn.setText(l.title + " [✓ COMPLETED]");
                        }
                    });
                    lessonsGrid.add(lessonBtn);
                }

                // Certificate Button
                JButton certBtn = new JButton("Check Certificate Status");
                certBtn.addActionListener(e -> {
                    Certificate cert = studentService.checkAndGenerateCertificate(s, c);
                    if(cert != null) {
                        JOptionPane.showMessageDialog(this, "Congratulations!\n" + cert.toString());
                    } else {
                        JOptionPane.showMessageDialog(this, "You must complete all lessons/quizzes first.");
                    }
                });
                
                myCoursePanel.add(lessonsGrid, BorderLayout.CENTER);
                myCoursePanel.add(certBtn, BorderLayout.SOUTH);
                
                learnContainer.add(myCoursePanel);
                learnContainer.add(Box.createVerticalStrut(20));
            }
        }

        tabs.add("Browse Available Courses", new JScrollPane(browseContainer));
        tabs.add("My Enrolled Courses", new JScrollPane(learnContainer));

        add(tabs, BorderLayout.CENTER);
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> frame.logout());
        add(logout, BorderLayout.SOUTH);
    }

    private void takeQuiz(Lesson l, Course c) {
        int score = 0;
        for(int i=0; i<l.quiz.questions.size(); i++) {
            String q = l.quiz.questions.get(i);
            String ans = JOptionPane.showInputDialog(this, "QUIZ: " + q);
            if(ans != null && ans.equalsIgnoreCase(l.quiz.answers.get(i))) {
                score++;
            }
        }
        
        if(score == l.quiz.questions.size()) {
            JOptionPane.showMessageDialog(this, "Quiz Passed!");
            studentService.completeLesson(currentStudent, c.id, l);
        } else {
            JOptionPane.showMessageDialog(this, "Failed. Try again.");
        }
    }
}