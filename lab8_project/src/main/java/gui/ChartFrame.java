// gui/ChartFrame.java
package gui;

import services.AnalyticsService;
import services.AnalyticsService.LessonStats;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class ChartFrame extends JFrame {

    private AnalyticsService analyticsService = new AnalyticsService();

    public ChartFrame(String courseId, String courseTitle) {
        setTitle("Insights - " + courseTitle);
        setLayout(new BorderLayout(10, 10));
        
        Map<String, Object> performanceData = analyticsService.getCoursePerformance(courseId);
        int totalEnrolled = (int) performanceData.getOrDefault("totalEnrolled", 0);
        
        if (totalEnrolled == 0) {
            JLabel message = new JLabel("No enrolled students or performance data available.", SwingConstants.CENTER);
            message.setFont(new Font("SansSerif", Font.BOLD, 16));
            add(message, BorderLayout.CENTER);
        } else {
            JTabbedPane tabbedPane = new JTabbedPane();
            
            tabbedPane.addTab("Quiz Averages", createQuizChartPanel(performanceData, totalEnrolled));
            tabbedPane.addTab("Completion Rates", createCompletionChartPanel(performanceData, totalEnrolled));
            
            add(tabbedPane, BorderLayout.CENTER);
        }

        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private JComponent createQuizChartPanel(Map<String, Object> data, int totalEnrolled) {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        StringBuilder sb = new StringBuilder();
        sb.append("📊 **COURSE QUIZ PERFORMANCE**\n");
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("Total Enrolled Students: %d\n\n", totalEnrolled));

        Map<String, LessonStats> lessonData = (Map<String, LessonStats>) data.get("lessonData");
        
        for (LessonStats stats : lessonData.values()) {
            int avg = (int) stats.getAverageScore();
            sb.append(String.format("Lesson: %s\n", stats.lessonTitle));
            sb.append(String.format("Average Quiz Score: %d%%\n", avg));
            
            // Visual Bar Chart Simulation
            sb.append("Bar: [");
            for(int i = 0; i < avg / 5; i++) sb.append("#");
            for(int i = avg / 5; i < 20; i++) sb.append("-"); // Max 20 characters for 100%
            sb.append("]\n\n");
        }
        area.setText(sb.toString());
        return new JScrollPane(area);
    }

    private JComponent createCompletionChartPanel(Map<String, Object> data, int totalEnrolled) {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));

        StringBuilder sb = new StringBuilder();
        sb.append("✅ **COURSE COMPLETION RATES**\n");
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("Total Enrolled Students: %d\n\n", totalEnrolled));
        
        Map<String, LessonStats> lessonData = (Map<String, LessonStats>) data.get("lessonData");

        for (LessonStats stats : lessonData.values()) {
            int completed = stats.completedCount;
            int percentage = (totalEnrolled > 0) ? (completed * 100 / totalEnrolled) : 0;

            sb.append(String.format("Lesson: %s\n", stats.lessonTitle));
            sb.append(String.format("Completion Rate: %d%% (%d/%d students completed)\n", 
                percentage, completed, totalEnrolled));
            
            // Visual Bar Chart Simulation
            sb.append("Bar: [");
            for(int i = 0; i < percentage / 5; i++) sb.append("*");
            for(int i = percentage / 5; i < 20; i++) sb.append("-"); 
            sb.append("]\n\n");
        }
        area.setText(sb.toString());
        return new JScrollPane(area);
    }
}