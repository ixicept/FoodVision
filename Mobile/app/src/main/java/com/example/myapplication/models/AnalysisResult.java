package com.example.myapplication.models;

public class AnalysisResult {
    public String prediction;
    public String message;

    public AnalysisResult(String prediction, String message) {
        this.prediction = prediction;
        this.message = message;
    }
}