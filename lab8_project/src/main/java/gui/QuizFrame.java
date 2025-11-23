package gui;

import models.*;
import services.QuizService;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class QuizFrame extends JFrame {
    private Quiz quiz;
    private Student student;
    private List<ButtonGroup> groups = new ArrayList<>();
    private QuizService quizService = new QuizService();

    public QuizFrame(Student s, Quiz q) {
        this.student = s; this.quiz=q;
        setTitle("Quiz: " + (q!=null? q.getQuizId():""));
        init();
    }

    private void init() {
        if (quiz==null) { JOptionPane.showMessageDialog(this,"No quiz."); return; }
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        int idx=0;
        for (Question Q : quiz.getQuestions()) {
            JPanel qpanel = new JPanel(new BorderLayout());
            qpanel.add(new JLabel((idx+1)+". "+Q.getText()), BorderLayout.NORTH);
            JPanel opts = new JPanel(new GridLayout(Q.getOptions().size(),1));
            ButtonGroup g = new ButtonGroup();
            for (int i=0;i<Q.getOptions().size();i++){
                JRadioButton rb = new JRadioButton(Q.getOptions().get(i));
                rb.setActionCommand(String.valueOf(i));
                g.add(rb);
                opts.add(rb);
            }
            groups.add(g);
            qpanel.add(opts, BorderLayout.CENTER);
            p.add(qpanel);
            idx++;
        }
        JButton submit = new JButton("Submit");
        submit.addActionListener(e -> submit());
        p.add(submit);
        add(new JScrollPane(p));
        setSize(600,600);
        setLocationRelativeTo(null);
    }

    private void submit(){
        List<Integer> selected = new ArrayList<>();
        for (ButtonGroup g : groups) {
            ButtonModel m = g.getSelection();
            if (m==null) selected.add(-1);
            else selected.add(Integer.parseInt(m.getActionCommand()));
        }
        int score = quizService.evaluate(quiz, selected);
        JOptionPane.showMessageDialog(this, "Score: " + score + "%");
        // Caller should process pass/fail and update student progress
        dispose();
    }
}
