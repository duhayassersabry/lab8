package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Placeholder - you can integrate JFreeChart or other libs later.
 * For now this shows simple textual stats.
 */
public class ChartFrame extends JFrame {
    public ChartFrame(String title, String text) {
        setTitle(title);
        JTextArea ta = new JTextArea(text);
        ta.setEditable(false);
        add(new JScrollPane(ta));
        setSize(600,400);
        setLocationRelativeTo(null);
    }
}
