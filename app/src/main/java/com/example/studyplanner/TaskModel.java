package com.example.studyplanner;
public class TaskModel {
    private String id;
    private String name;
    private String subject;
    private String date;
    private boolean completed;
    public TaskModel() {}
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSubject() {
        return subject;
    }
    public String getDate() {
        return date;
    }
    public boolean isCompleted() {   // 🔥 IMPORTANT NAME
        return completed;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setCompleted(boolean completed) { // 🔥 IMPORTANT NAME
        this.completed = completed;
    }
}