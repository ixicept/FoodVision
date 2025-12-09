package com.example.myapplication.models;

public class AnalysisResult {
    public boolean isHealthy;
    public String message;

    public AnalysisResult(boolean isHealthy, String message) {
        this.isHealthy = isHealthy;
        this.message = message;
    }
}