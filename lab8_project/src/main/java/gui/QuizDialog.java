package gui;

import models.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class QuizDialog extends JDialog {
    private Quiz quiz;
    private boolean submitted = false;
    private java.util.List<ButtonGroup> groups = new ArrayList<>();

    public QuizDialog(Frame owner, Quiz quiz) {
        super(owner, "Quiz", true);
        this.quiz = quiz;
        init();
    }

    private void init() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        for (Question q : quiz.getQuestions()) {
            p.add(new JLabel(q.getText()));
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            ButtonGroup bg = new ButtonGroup();
            groups.add(bg);
            List<String> opts = q.getOptions();
            if (opts == null || opts.isEmpty()) opts = Arrays.asList("True","False");
            for (int i=0;i<opts.size();i++) {
                JRadioButton rb = new JRadioButton(opts.get(i));
                rb.setActionCommand(String.valueOf(i));
                bg.add(rb);
                row.add(rb);
            }
            p.add(row);
        }
        JButton submit = new JButton("Submit");
        submit.addActionListener(e -> { submitted = true; setVisible(false); });
        p.add(submit);
        add(new JScrollPane(p));
        setSize(600,400); setLocationRelativeTo(getOwner());
    }

    public boolean isSubmitted(){ return submitted; }

    public List<Integer> getSelectedIndices() {
        List<Integer> out = new ArrayList<>();
        for (ButtonGroup bg : groups) {
            ButtonModel m = bg.getSelection();
            if (m==null) out.add(-1);
            else out.add(Integer.parseInt(m.getActionCommand()));
        }
        return out;
    }
}
