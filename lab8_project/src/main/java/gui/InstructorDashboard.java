package gui;

import services.CourseService;
import data.JsonDatabaseManager;
import models.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InstructorDashboard extends JFrame {
    private Instructor instructor;
    private DefaultListModel<String> model = new DefaultListModel<>();
    private JList<String> list;
    private JButton addCourseBtn, addLessonBtn, refreshBtn;
    private CourseService courseService = new CourseService();
    private JsonDatabaseManager db = new JsonDatabaseManager();

    public InstructorDashboard(Instructor ins){
        this.instructor = ins;
        setTitle("Instructor - " + ins.getEmail());
        init();
        loadCourses();
    }

    private void init(){
        list = new JList<>(model);
        addCourseBtn = new JButton("Add Course");
        addLessonBtn = new JButton("Add Lesson");
        refreshBtn = new JButton("Refresh");

        addCourseBtn.addActionListener(e -> addCourse());
        addLessonBtn.addActionListener(e -> addLesson());
        refreshBtn.addActionListener(e -> loadCourses());

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel south = new JPanel(new GridLayout(1,3));
        south.add(addCourseBtn); south.add(addLessonBtn); south.add(refreshBtn);
        p.add(south, BorderLayout.SOUTH);

        add(p);
        setSize(700,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void loadCourses(){
        model.clear();
        List<Course> courses = courseService.allCourses(false);
        for (Course c: courses) {
            if (instructor.getId().equals(c.getInstructorId())) model.addElement(c.getCourseId() + " - " + c.getTitle() + " [" + c.getStatus()+"]");
        }
    }

    private void addCourse(){
        String id = "C" + System.currentTimeMillis();
        String title = JOptionPane.showInputDialog(this,"Course title:");
        if (title == null || title.trim().isEmpty()) return;
        Course c = new Course(id, title, "", instructor.getId());
        courseService.addCourse(c);
        loadCourses();
    }

    private void addLesson(){
        String sel = list.getSelectedValue();
        if (sel == null) { JOptionPane.showMessageDialog(this, "Select course."); return; }
        String courseId = sel.split(" - ")[0];
        Course c = courseService.find(courseId);
        if (c == null) return;
        String title = JOptionPane.showInputDialog(this,"Lesson title:");
        String content = JOptionPane.showInputDialog(this,"Lesson content:");
        Lesson l = new Lesson(title, content);
        c.getLessons().add(l);
        courseService.addCourse(c);
        JOptionPane.showMessageDialog(this,"Lesson added (status still: "+c.getStatus()+"). Admin must approve course to make it visible.");
    }
}
