package gui;

import models.*;
import services.CourseService;
import data.JsonDatabaseManager;
import services.AnalyticsService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AdminDashboard extends JFrame {
    private Admin admin;
    private CourseService courseService = new CourseService();
    private DefaultListModel<String> courseModel = new DefaultListModel<>();
    private JList<String> courseList = new JList<>(courseModel);
    private JTextArea details = new JTextArea();
    private JButton approve, reject, back, logout, insights;
    private AnalyticsService analytics = new AnalyticsService();

    public AdminDashboard(Admin a) {
        this.admin = a;
        setTitle("Admin - " + a.getEmail());
        init();
        loadCourses();
    }
    private void init() {
        details.setEditable(false);
        approve = new JButton("Approve");
        reject = new JButton("Reject");
        back = new JButton("Back");
        logout = new JButton("Logout");
        insights = new JButton("Insights");

        approve.addActionListener(e-> setStatus("Approved"));
        reject.addActionListener(e-> setStatus("Rejected"));
        back.addActionListener(e-> { new LoginFrame().setVisible(true); dispose(); });
        logout.addActionListener(e-> { new LoginFrame().setVisible(true); dispose(); });
        insights.addActionListener(e-> showInsights());

        courseList.addListSelectionListener(e -> { if(!e.getValueIsAdjusting()) showDetails(); });

        JPanel left = new JPanel(new BorderLayout());
        left.add(new JLabel("Courses"), BorderLayout.NORTH);
        left.add(new JScrollPane(courseList), BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout());
        right.add(new JLabel("Details"), BorderLayout.NORTH);
        right.add(new JScrollPane(details), BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.add(approve); south.add(reject); south.add(insights); south.add(back); south.add(logout);

        setLayout(new BorderLayout());
        add(left, BorderLayout.WEST); add(right, BorderLayout.CENTER); add(south, BorderLayout.SOUTH);
        setSize(1000,600); setLocationRelativeTo(null); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void loadCourses() {
        courseModel.clear();
        for (Course c : courseService.allCourses(false)) courseModel.addElement(c.getCourseId() + " - " + c.getTitle() + " [" + c.getStatus() + "]");
    }

    private void showDetails() {
        String sel = courseList.getSelectedValue();
        if (sel==null) return;
        String cid = sel.split(" - ")[0];
        Course c = courseService.find(cid);
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(c.getCourseId()).append("\nTitle: ").append(c.getTitle()).append("\nDesc: ").append(c.getDescription()).append("\nInstructor: ").append(c.getInstructorId()).append("\nStatus: ").append(c.getStatus()).append("\n\nLessons:\n");
        for (Lesson L : c.getLessons()) {
            sb.append(" - ").append(L.getTitle()).append("\n");
            if (L.getQuiz()!=null) {
                sb.append("   Quiz: ").append(L.getQuiz().getQuestions().size()).append(" questions\n");
                int i=1;
                for (Question Q : L.getQuiz().getQuestions()) {
                    sb.append("     ").append(i++).append(". ").append(Q.getText()).append(" (opts: ").append(Q.getOptions().size()).append(") correctIndex=").append(Q.getCorrectIndex()).append("\n");
                }
            }
        }
        details.setText(sb.toString());
    }

    private void setStatus(String status) {
        String sel = courseList.getSelectedValue();
        if (sel==null) { JOptionPane.showMessageDialog(this,"Select course"); return; }
        String cid = sel.split(" - ")[0];
        courseService.setCourseStatus(cid, status);
        loadCourses();
    }

    private void showInsights() {
        String sel = courseList.getSelectedValue();
        if (sel==null) { JOptionPane.showMessageDialog(this,"Select course"); return; }
        String cid = sel.split(" - ")[0];
        Map<String,Integer> map = analytics.courseStudentAverages(cid);
        new ChartFrame("Course Insights", map).setVisible(true);
    }
}
