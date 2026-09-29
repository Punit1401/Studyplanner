package com.example.studyplanner;
import java.util.HashSet;
import java.util.Set;
public class HistoryModel {
    String date;
    Set<String> subjects;
    int completedCount, pendingCount;
    public HistoryModel(String date) {
        this.date = date;
        this.subjects = new HashSet<>();
    }
}