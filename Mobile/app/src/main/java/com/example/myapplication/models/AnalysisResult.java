package com.example.myapplication.models;

public class AnalysisResult {
    public boolean isHealthy;
    public String message;

    public AnalysisResult(boolean h, String m) {
        isHealthy = h;
        message = m;
    }
}