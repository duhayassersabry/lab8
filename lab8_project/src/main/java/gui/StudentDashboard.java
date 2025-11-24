package gui;

import data.JsonDatabaseManager;
import models.*;
import services.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class StudentDashboard extends JFrame {
    private Student student;
    private JsonDatabaseManager db = new JsonDatabaseManager();
    private CourseService courseService = new CourseService();
    private QuizService quizService = new QuizService();
    private CertificateService certService = new CertificateService();

    private DefaultListModel<String> availModel = new DefaultListModel<>();
    private DefaultListModel<String> enrolledModel = new DefaultListModel<>();
    private JList<String> listAvailable, listEnrolled;
    private JTable tblLessons;
    private DefaultTableModel lessonsModel;
    private JButton btnEnroll, btnViewLessons, btnGenerateCert, btnBack, btnLogout, btnInsights;

    public StudentDashboard(Student s) {
        this.student = s;
        setTitle("Student - " + s.getEmail());
        init();
        loadLists();
    }

    private void init() {
        listAvailable = new JList<>(availModel);
        listEnrolled = new JList<>(enrolledModel);
        btnEnroll = new JButton("Enroll");
        btnViewLessons = new JButton("View Lessons");
        btnGenerateCert = new JButton("Generate Certificate");
        btnBack = new JButton("Back");
        btnLogout = new JButton("Logout");
        btnInsights = new JButton("Insights");

        btnEnroll.addActionListener(e -> enroll());
        btnViewLessons.addActionListener(e -> loadLessons());
        btnGenerateCert.addActionListener(e -> generateCertificate());
        btnBack.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });
        btnLogout.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });
        btnInsights.addActionListener(e -> showInsights());

        lessonsModel = new DefaultTableModel(new Object[]{"Lesson", "Status", "Mark", "Action"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 3;
            }

            @Override
            public Class<?> getColumnClass(int c) {
                if (c == 2) return Integer.class;
                return String.class;
            }
        };
        tblLessons = new JTable(lessonsModel);
        tblLessons.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int r = tblLessons.rowAtPoint(evt.getPoint());
                int c = tblLessons.columnAtPoint(evt.getPoint());
                if (r >= 0 && c == 3) takeQuizForRow(r);
            }
        });

        JPanel left = new JPanel(new BorderLayout());
        left.add(new JLabel("Available"), BorderLayout.NORTH);
        left.add(new JScrollPane(listAvailable), BorderLayout.CENTER);
        left.add(btnEnroll, BorderLayout.SOUTH);

        JPanel center = new JPanel(new BorderLayout());
        center.add(new JLabel("Enrolled"), BorderLayout.NORTH);
        center.add(new JScrollPane(listEnrolled), BorderLayout.CENTER);
        JPanel cb = new JPanel();
        cb.add(btnViewLessons);
        cb.add(btnGenerateCert);
        cb.add(btnInsights);
        cb.add(btnBack);
        cb.add(btnLogout);
        center.add(cb, BorderLayout.SOUTH);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(new JLabel("Lessons (Action -> Take Quiz)"), BorderLayout.NORTH);
        bottom.add(new JScrollPane(tblLessons), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(left, BorderLayout.WEST);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void loadLists() {
        availModel.clear();
        enrolledModel.clear();
        for (Course c : courseService.allCourses(true)) {
            String label = c.getCourseId() + " - " + c.getTitle();
            if (student.getEnrolledCourses().contains(c.getCourseId())) enrolledModel.addElement(label);
            else availModel.addElement(label);
        }
    }

    private void enroll() {
        String sel = listAvailable.getSelectedValue();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Select course");
            return;
        }
        String id = sel.split(" - ")[0];
        if (!student.getEnrolledCourses().contains(id)) {
            student.getEnrolledCourses().add(id);
            student.getProgress().putIfAbsent(id, new HashMap<>());
            new JsonDatabaseManager().updateUser(student);
            loadLists();
            JOptionPane.showMessageDialog(this, "Enrolled");
        }
    }

    private void loadLessons() {
        lessonsModel.setRowCount(0);
        String sel = listEnrolled.getSelectedValue();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Select enrolled course");
            return;
        }
        String cid = sel.split(" - ")[0];
        Course c = courseService.find(cid);
        if (c == null) return;
        Map<String, Boolean> prog = student.getProgress().getOrDefault(cid, new HashMap<>());
        Map<String, Integer> marks = computeBestMarksForCourse(cid);
        List<Lesson> lessons = c.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            Lesson L = lessons.get(i);
            boolean done = prog.getOrDefault(L.getTitle(), false);
            String status;
            if (done) status = "Completed";
            else if (i == 0 || prog.getOrDefault(lessons.get(i - 1).getTitle(), false)) status = "Available";
            else status = "Locked";
            Integer m = marks.getOrDefault(L.getTitle(), -1);
            lessonsModel.addRow(new Object[]{L.getTitle(), status, m >= 0 ? m : null, status.equals("Available") ? "Take Quiz" : "-"});
        }
        tblLessons.putClientProperty("currentCourseId", cid);
    }

    private Map<String, Integer> computeBestMarksForCourse(String cid) {
        Map<String, Integer> map = new HashMap<>();
        for (QuizAttempt a : student.getAttempts().getOrDefault(cid, new ArrayList<>())) {
            map.put(a.getLessonTitle(), Math.max(map.getOrDefault(a.getLessonTitle(), 0), a.getScore()));
        }
        return map;
    }

    private void takeQuizForRow(int row) {
        String cid = (String) tblLessons.getClientProperty("currentCourseId");
        if (cid == null) return;
        String lessonTitle = (String) lessonsModel.getValueAt(row, 0);
        String status = (String) lessonsModel.getValueAt(row, 1);
        if (!"Available".equals(status)) {
            JOptionPane.showMessageDialog(this, "Lesson locked");
            return;
        }
        Course c = courseService.find(cid);
        Lesson L = null;
        for (Lesson l : c.getLessons()) if (l.getTitle().equals(lessonTitle)) L = l;
        if (L == null || L.getQuiz() == null) {
            JOptionPane.showMessageDialog(this, "No quiz");
            return;
        }
        Quiz q = L.getQuiz();
        if (!quizService.mayRetakeFirstThreshold(student, cid, lessonTitle, q.getPassingPercentage())) {
            JOptionPane.showMessageDialog(this, "You passed >= " + q.getPassingPercentage() + "% on first attempt; retake not allowed.");
            return;
        }
        QuizDialog dialog = new QuizDialog(this, q);
        dialog.setVisible(true);
        if (!dialog.isSubmitted()) return;
        List<Integer> answers = dialog.getSelectedIndices();
        int score = quizService.evaluate(q, answers);
        quizService.recordAttempt(student, cid, lessonTitle, score);
        if (score >= q.getPassingPercentage()) {
            student.getProgress().computeIfAbsent(cid, k -> new HashMap<>()).put(lessonTitle, true);
            new JsonDatabaseManager().updateUser(student);
            JOptionPane.showMessageDialog(this, "Passed: " + score + "%");
        } else JOptionPane.showMessageDialog(this, "Failed: " + score + "% (need " + q.getPassingPercentage() + "%)");
        loadLessons();
    }

    private void generateCertificate() {
        String cid = (String) tblLessons.getClientProperty("currentCourseId");
        if (cid == null) {
            JOptionPane.showMessageDialog(this, "Select course and load lessons first");
            return;
        }
        Course course = courseService.find(cid);
        if (course == null) return;

        Map<String, Boolean> prog = student.getProgress().getOrDefault(cid, new HashMap<>());
        boolean allPassed = course.getLessons().stream().allMatch(l -> prog.getOrDefault(l.getTitle(), false));
        if (!allPassed) {
            JOptionPane.showMessageDialog(this, "Complete all lessons first");
            return;
        }

        Certificate cert;
        try {
            cert = certService.getOrGenerateCertificate(student, course);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Certificate error: " + ex.getMessage());
            return;
        }

        JOptionPane.showMessageDialog(this, "Certificate: " + cert.getCertificateId() + " | Total marks: " + cert.getTotalMarks());

        try {
            String out = "certs/" + cert.getCertificateId() + ".pdf";
            certService.exportCertificatePdf(cert, out);
            int open = JOptionPane.showConfirmDialog(this, "Open certificate PDF?", "View", JOptionPane.YES_NO_OPTION);
            if (open == JOptionPane.YES_OPTION) Desktop.getDesktop().open(new File(out));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Cannot open PDF: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "PDF fail: " + ex.getMessage());
        }
    }

    private void showInsights() {
        String sel = listEnrolled.getSelectedValue();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Select course");
            return;
        }
        String cid = sel.split(" - ")[0];
        new ChartFrame("Lesson Marks", computeBestMarksForCourse(cid)).setVisible(true);
    }
}
