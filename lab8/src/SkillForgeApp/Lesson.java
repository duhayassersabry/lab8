package SkillForgeApp;
public class Lesson {
    String id;
    String title;
    String content;
    Quiz quiz; 

    public Lesson(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}