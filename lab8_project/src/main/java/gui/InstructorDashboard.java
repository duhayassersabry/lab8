package gui;

import models.*;
import services.CourseService;
import data.JsonDatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class InstructorDashboard extends JFrame {
    private Instructor instructor;
    private CourseService courseService = new CourseService();
    private DefaultListModel<String> courseModel = new DefaultListModel<>();
    private JList<String> courseList = new JList<>(courseModel);
    private DefaultListModel<String> lessonModel = new DefaultListModel<>();
    private JList<String> lessonList = new JList<>(lessonModel);

    public InstructorDashboard(Instructor i) {
        this.instructor = i;
        setTitle("Instructor - " + i.getEmail());
        init();
        loadCourses();
    }

    private void init() {
        JButton addCourse = new JButton("Add Course");
        JButton addLesson = new JButton("Add Lesson");
        JButton addQuestion = new JButton("Add Question");
        JButton back = new JButton("Back");
        JButton logout = new JButton("Logout");

        addCourse.addActionListener(e -> addCourse());
        addLesson.addActionListener(e -> addLesson());
        addQuestion.addActionListener(e -> addQuestion());
        back.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });
        logout.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });

        courseList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) updateLessons();
        });

        JPanel left = new JPanel(new BorderLayout());
        left.add(new JLabel("My Courses"), BorderLayout.NORTH);
        left.add(new JScrollPane(courseList), BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout());
        right.add(new JLabel("Lessons"), BorderLayout.NORTH);
        right.add(new JScrollPane(lessonList), BorderLayout.CENTER);

        JPanel ctrl = new JPanel(new GridLayout(5,1,6,6));
        ctrl.add(addCourse); ctrl.add(addLesson); ctrl.add(addQuestion); ctrl.add(back); ctrl.add(logout);

        setLayout(new GridLayout(1,3));
        add(left); add(right); add(ctrl);
        setSize(1000,500); setLocationRelativeTo(null); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void loadCourses() {
        courseModel.clear();
        List<Course> all = courseService.allCourses(false);
        for (Course c : all) {
            if (instructor.getId().equals(c.getInstructorId())) courseModel.addElement(c.getCourseId() + " - " + c.getTitle() + " [" + c.getStatus() + "]");
        }
    }

    private void updateLessons() {
        lessonModel.clear();
        String sel = courseList.getSelectedValue();
        if (sel==null) return;
        String cid = sel.split(" - ")[0];
        Course c = courseService.find(cid);
        if (c==null) return;
        for (Lesson L : c.getLessons()) lessonModel.addElement(L.getTitle());
    }

    private void addCourse() {
        String title = JOptionPane.showInputDialog(this, "Course title:");
        if (title==null || title.trim().isEmpty()) return;
        String id = "C" + System.currentTimeMillis();
        Course c = new Course(id, title, "", instructor.getId());
        courseService.addCourse(c);
        loadCourses();
    }

    private void addLesson() {
        String sel = courseList.getSelectedValue();
        if (sel==null) { JOptionPane.showMessageDialog(this,"Select course"); return; }
        String cid = sel.split(" - ")[0];
        String title = JOptionPane.showInputDialog(this,"Lesson title:");
        String content = JOptionPane.showInputDialog(this,"Lesson content:");
        if (title==null) return;
        Course c = courseService.find(cid);
        Lesson L = new Lesson("L"+System.currentTimeMillis(), title, content);
        c.getLessons().add(L);
        courseService.save(c);
        updateLessons();
    }

    private void addQuestion() {
        String selCourse = courseList.getSelectedValue();
        String selLesson = lessonList.getSelectedValue();
        if (selCourse==null || selLesson==null) { JOptionPane.showMessageDialog(this,"Select course and lesson"); return; }
        String cid = selCourse.split(" - ")[0];
        Course c = courseService.find(cid);
        Lesson target = null;
        for (Lesson L : c.getLessons()) if (L.getTitle().equals(selLesson)) { target = L; break; }
        if (target==null) return;
        String qtext = JOptionPane.showInputDialog(this,"Question text:");
        if (qtext==null) return;
        int type = JOptionPane.showOptionDialog(this, "Question type", "Type", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[] {"True/False","Multiple Choice"}, "True/False");
        Question q;
        if (type==0) {
            java.util.List<String> opts = new java.util.ArrayList<>(); opts.add("True"); opts.add("False");
            int correct = JOptionPane.showOptionDialog(this,"Correct answer","Correct",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,new String[]{"True","False"},"True");
            q = new Question(qtext, opts, correct==0?0:1);
        } else {
            String optsRaw = JOptionPane.showInputDialog(this,"Comma-separated options (e.g. A,B,C):");
            if (optsRaw==null) return;
            String[] parts = optsRaw.split(",");
            java.util.List<String> opts = new ArrayList<>();
            for (String p : parts) opts.add(p.trim());
            String corr = JOptionPane.showInputDialog(this,"Index (0-based) of correct option:");
            int idx = 0;
            try { idx = Integer.parseInt(corr); } catch (Exception ex) { idx=0; }
            q = new Question(qtext, opts, idx);
        }
        if (target.getQuiz()==null) target.setQuiz(new Quiz("Q"+System.currentTimeMillis()));
        target.getQuiz().getQuestions().add(q);
        courseService.save(c);
        JOptionPane.showMessageDialog(this,"Question added");
    }
}
