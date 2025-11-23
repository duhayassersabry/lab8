package gui;

import data.JsonDatabaseManager;
import models.*;
import services.CertificateService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class StudentDashboard extends JFrame {

    private Student student;
    private JList<String> listAvailable;
    private JList<String> listEnrolled;
    private DefaultListModel<String> availModel = new DefaultListModel<>();
    private DefaultListModel<String> enrolledModel = new DefaultListModel<>();
    private JButton btnEnroll, btnViewLessons, btnGenerateCertificate, btnBack, btnLogout;
    private JTable tblLessons;
    private DefaultTableModel lessonsTableModel;
    private JsonDatabaseManager db = new JsonDatabaseManager();
    private CertificateService certService = new CertificateService();

    public StudentDashboard(Student s) {
        this.student = s;
        setTitle("Student Dashboard - " + s.getEmail());
        init();
        loadLists();
    }

    private void init() {
        listAvailable = new JList<>(availModel);
        listEnrolled = new JList<>(enrolledModel);

        btnEnroll = new JButton("Enroll");
        btnViewLessons = new JButton("View Lessons");
        btnGenerateCertificate = new JButton("Generate Certificate");
        btnBack = new JButton("Back");
        btnLogout = new JButton("Logout");

        btnEnroll.addActionListener(e -> enrollSelected());
        btnViewLessons.addActionListener(e -> loadLessonsForSelectedCourse());
        btnGenerateCertificate.addActionListener(e -> handleGenerateCertificate());
        btnBack.addActionListener(e -> goBack());
        btnLogout.addActionListener(e -> logout());

        // lessons table
        lessonsTableModel = new DefaultTableModel(new Object[]{"Completed", "Lesson Title", "Content", "Solve"}, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (col == 0) return Boolean.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 || col == 3; // allow checkbox and Solve column (we use it as button trigger)
            }
        };

        tblLessons = new JTable(lessonsTableModel);
        tblLessons.getColumnModel().getColumn(2).setMinWidth(200);
        // the "Solve" column will contain text "Solve" — we will react when user double-clicks or clicks a separate button.

        // We'll respond to double clicks on the "Solve" cell or button press approach.
        tblLessons.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tblLessons.rowAtPoint(evt.getPoint());
                int col = tblLessons.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 3) { // Solve column clicked
                    solveBooleanQuestionForRow(row);
                }
            }
        });

        // Panels
        JPanel left = new JPanel(new BorderLayout());
        left.add(new JLabel("Available Courses"), BorderLayout.NORTH);
        left.add(new JScrollPane(listAvailable), BorderLayout.CENTER);
        left.add(btnEnroll, BorderLayout.SOUTH);

        JPanel center = new JPanel(new BorderLayout());
        center.add(new JLabel("Enrolled Courses"), BorderLayout.NORTH);
        center.add(new JScrollPane(listEnrolled), BorderLayout.CENTER);

        // Add buttons beneath enrolled list
        JPanel centerButtons = new JPanel(new FlowLayout());
        centerButtons.add(btnViewLessons);
        centerButtons.add(btnGenerateCertificate);
        centerButtons.add(btnBack);
        centerButtons.add(btnLogout);
        center.add(centerButtons, BorderLayout.SOUTH);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(new JLabel("Lessons (select enrolled course -> View Lessons). Click Solve to answer question."), BorderLayout.NORTH);
        bottom.add(new JScrollPane(tblLessons), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(left, BorderLayout.WEST);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void loadLists() {
        availModel.clear();
        enrolledModel.clear();
        List<Course> courses = db.loadCourses();
        Set<String> enrolledSet = new HashSet<>(student.getEnrolledCourses());

        for (Course c : courses) {
            String label = c.getCourseId() + " - " + c.getTitle();
            if (!enrolledSet.contains(c.getCourseId())) availModel.addElement(label);
            else enrolledModel.addElement(label);
        }
    }

    private void enrollSelected() {
        String sel = listAvailable.getSelectedValue();
        if (sel == null) { JOptionPane.showMessageDialog(this, "Select a course first."); return; }
        String courseId = sel.split(" - ")[0];
        if (student.getEnrolledCourses().contains(courseId)) { JOptionPane.showMessageDialog(this, "Already enrolled."); return; }
        student.getEnrolledCourses().add(courseId);
        student.getProgress().putIfAbsent(courseId, new HashMap<>());
        db.updateUser(student);
        loadLists();
        JOptionPane.showMessageDialog(this, "Enrolled in " + courseId);
    }

    // ------------------ LESSONS & BOOLEAN QUESTION ------------------------
    private void loadLessonsForSelectedCourse() {
        String sel = listEnrolled.getSelectedValue();
        if (sel == null) { JOptionPane.showMessageDialog(this, "Select an enrolled course."); return; }
        String courseId = sel.split(" - ")[0];
        Course c = db.findCourseById(courseId);
        if (c == null) { JOptionPane.showMessageDialog(this, "Course not found."); return; }

        lessonsTableModel.setRowCount(0);
        Map<String, Boolean> prog = student.getProgress().getOrDefault(courseId, new HashMap<>());

        for (Lesson L : c.getLessons()) {
            boolean done = prog.getOrDefault(L.getTitle(), false);
            // "Solve" column text
            lessonsTableModel.addRow(new Object[]{ done, L.getTitle(), L.getContent(), "Solve" });
        }

        tblLessons.putClientProperty("currentCourseId", courseId);

        // update Generate certificate button visibility:
        updateCertificateButtonState(courseId);
    }

    private void solveBooleanQuestionForRow(int row) {
        Object obj = tblLessons.getClientProperty("currentCourseId");
        if (obj == null) { JOptionPane.showMessageDialog(this, "No course selected."); return; }
        String courseId = (String) obj;
        String title = (String) lessonsTableModel.getValueAt(row, 1);

        // Simple deterministic boolean question:
        // "Is the number of characters in the lesson title even?"
        int len = title.length();
        boolean correctAnswer = (len % 2 == 0);

        String q = "Is the number of characters in the lesson title (" + len + ") even?\nChoose True or False.";
        int choice = JOptionPane.showOptionDialog(this, q, "Answer question",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[] {"True","False"}, "True");

        boolean answeredTrue = (choice == JOptionPane.YES_OPTION);
        if (answeredTrue == correctAnswer) {
            // mark done
            lessonsTableModel.setValueAt(true, row, 0);
            saveLessonProgressForDisplayedCourse(); // persist
            JOptionPane.showMessageDialog(this, "Correct! Lesson marked as completed.");
            // check for certificate unlock
            updateCertificateButtonState(courseId);
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect. Try again later.");
        }
    }

    private void saveLessonProgressForDisplayedCourse() {
        Object obj = tblLessons.getClientProperty("currentCourseId");
        if (obj == null) return;
        String courseId = (String) obj;
        Map<String, Boolean> prog = student.getProgress().computeIfAbsent(courseId, k -> new HashMap<>());
        for (int r = 0; r < lessonsTableModel.getRowCount(); r++) {
            String t = (String) lessonsTableModel.getValueAt(r, 1);
            Boolean done = (Boolean) lessonsTableModel.getValueAt(r, 0);
            prog.put(t, done != null && done);
        }
        db.updateUser(student);
    }

    private boolean allLessonsCompleted(String courseId) {
        Course c = db.findCourseById(courseId);
        if (c == null) return false;
        Map<String, Boolean> prog = student.getProgress().getOrDefault(courseId, new HashMap<>());
        for (Lesson L : c.getLessons()) {
            if (!prog.getOrDefault(L.getTitle(), false)) return false;
        }
        return true;
    }

    private void updateCertificateButtonState(String courseId) {
        btnGenerateCertificate.setEnabled(allLessonsCompleted(courseId));
    }

    private void handleGenerateCertificate() {
        Object obj = tblLessons.getClientProperty("currentCourseId");
        if (obj == null) { JOptionPane.showMessageDialog(this, "Select an enrolled course first."); return; }
        String courseId = (String) obj;
        Course c = db.findCourseById(courseId);
        if (c == null) { JOptionPane.showMessageDialog(this, "Course not found."); return; }
        if (!allLessonsCompleted(courseId)) { JOptionPane.showMessageDialog(this, "Complete all lessons first."); return; }

        // generate certificate using the student's email only
        CertificateService service = new CertificateService();
        Certificate cert = service.generateCertificate(student, c);

        // show certificate
        JOptionPane.showMessageDialog(this, "Certificate generated: " + cert.getCertificateId() + "\nIssued to: " + cert.getStudentEmail());
    }

    // --------- Back & Logout ----------
    private void goBack() {
        // Back returns to LoginFrame for simplicity
        new gui.LoginFrame().setVisible(true);
        dispose();
    }

    private void logout() {
        // clear any session (no session store here) and open login
        new gui.LoginFrame().setVisible(true);
        dispose();
    }
}
