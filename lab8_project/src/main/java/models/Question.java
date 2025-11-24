package models;

import java.util.*;

public class Question {
    private String text;
    private List<String> options = new ArrayList<>(); 
    private int correctIndex = 0; 
    public Question() {}
    public Question(String text, List<String> options, int correctIndex) {
        this.text = text;
        if (options != null) this.options = options;
        this.correctIndex = correctIndex;
    }
    public String getText(){ return text; }
    public List<String> getOptions(){ return options; }
    public int getCorrectIndex(){ return correctIndex; }
    public void setText(String s){ this.text=s; }
    public void setOptions(List<String> o){ this.options=o; }
    public void setCorrectIndex(int i){ this.correctIndex=i; }
}
