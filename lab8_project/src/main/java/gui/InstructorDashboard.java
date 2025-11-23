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
    // private JsonDatabaseManager db = new JsonDatabaseManager(); // Not needed if using CourseService

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
        // Assuming courseService.allCourses(false) loads all courses
        List<Course> courses = courseService.allCourses(false); 
        for (Course c: courses) {
            if (instructor.getId().equals(c.getInstructorId())) model.addElement(c.getCourseId() + " - " + c.getTitle() + " [" + c.getStatus()+"]");
        }
    }

    private void addCourse(){
        String id = "C" + System.currentTimeMillis();
        String title = JOptionPane.showInputDialog(this,"Course title:");
        if (title == null || title.trim().isEmpty()) return;
        
        // Input description for better data quality
        String description = JOptionPane.showInputDialog(this,"Course description:");
        if (description == null) description = "";
        
        Course c = new Course(id, title, description, instructor.getId());
        courseService.addCourse(c); // Use addCourse or updateCourse for saving
        loadCourses();
    }

    private void addLesson(){
        String sel = list.getSelectedValue();
        if (sel == null) { JOptionPane.showMessageDialog(this, "Select course."); return; }
        
        // 1. Extract Course ID
        String courseId = sel.split(" - ")[0].trim();
        Course c = courseService.find(courseId);
        if (c == null) { JOptionPane.showMessageDialog(this, "Course not found."); return; }
        
        String title = JOptionPane.showInputDialog(this,"Lesson title:");
        if (title == null || title.trim().isEmpty()) return;

        // Use a JTextArea for multi-line content input
        JTextArea contentArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        
        int result = JOptionPane.showConfirmDialog(this, scrollPane, "Enter Lesson Content:", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        String content = (result == JOptionPane.OK_OPTION) ? contentArea.getText() : null;
        if (content == null || content.trim().isEmpty()) return;
        
        // 2. IMPORTANT: Generate unique lessonId and use the new constructor
        String lessonId = "L" + System.currentTimeMillis();
        Lesson l = new Lesson(lessonId, title, content); // Using the constructor with ID
        
        // 3. Update the Course object
        c.getLessons().add(l);
        
        // 4. Persist the updated Course object
        // NOTE: You should use an update method here, not addCourse again, 
        // to prevent overwriting or creating duplicates if the service handles it simply.
        courseService.updateCourse(c); 
        
        JOptionPane.showMessageDialog(this,"Lesson added successfully! (ID: " + lessonId + ").");
    }
    
    // You should ensure CourseService has an updateCourse method 
    // that saves the modified course list back to courses.json.
}