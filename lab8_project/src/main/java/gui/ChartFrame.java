package gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.Map;

public class ChartFrame extends JFrame {
    public ChartFrame(String title, Map<String,Integer> data) {
        setTitle(title);
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        for (Map.Entry<String,Integer> e : data.entrySet()) ds.addValue(e.getValue(), "Marks", e.getKey());
        var chart = ChartFactory.createBarChart(title, "Item", "Mark", ds);
        ChartPanel cp = new ChartPanel(chart);
        add(cp);
        setSize(900,600); setLocationRelativeTo(null);
    }
    public ChartFrame(String title, java.util.Map<String,Integer> data, boolean dummy) { this(title,data); }
    public ChartFrame(String title, java.util.Map<String,Integer> data, int dummy) { this(title,data); }
    public ChartFrame(String title, java.util.Map<String,Integer> data, long dummy) { this(title,data); }
    public ChartFrame(String title, java.util.Map<String,Integer> data, char dummy) { this(title,data); }
    // convenience constructor from simple map of lesson->mark
    public ChartFrame(String title, java.util.Map<String,Integer> data, Object... o) { this(title,data); }
    public ChartFrame(String title, java.util.Map<String,Integer> data, String s) { this(title,data); }
}
