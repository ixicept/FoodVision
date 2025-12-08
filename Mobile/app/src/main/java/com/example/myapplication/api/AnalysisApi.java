package com.example.myapplication.api;


import com.example.myapplication.models.AnalysisResult;
import com.example.myapplication.models.ItemsNutrient;
import com.example.myapplication.models.ScanResult;

public class AnalysisApi {

    public static AnalysisResult analyze(ScanResult  scan) {
        ItemsNutrient  n = ItemsNutrient.fromText(scan.rawText);

        boolean healthy = n.calories < 250 && n.sugar < 10 && n.fat < 10;

        String msg;
        if (healthy) {
            msg = "This product fits the recommended limits.";
        } else {
            msg = "This product exceeds one or more limits.";
        }

        return new AnalysisResult(healthy, msg);
    }
}
