package com.example.myapplication.models;

public class ItemsNutrient {
    public double energy_kcal;
    public double fat;
    public double saturated_fat;
    public double carbohydrates;
    public double sugars;
    public double proteins;
    public double fiber;
    public double salt;
    public double sodium;

    public static ItemsNutrient fromText(String text) {
        ItemsNutrient n = new ItemsNutrient();

        n.energy_kcal = extractDouble(text, "energy", 200.0);
        n.fat = extractDouble(text, "fat", 8.0);
        n.saturated_fat = extractDouble(text, "saturated", 3.0);
        n.carbohydrates = extractDouble(text, "carbohydrate", 30.0);
        n.sugars = extractDouble(text, "sugar", 10.0);
        n.proteins = extractDouble(text, "protein", 5.0);
        n.fiber = extractDouble(text, "fiber", 2.0);
        n.salt = extractDouble(text, "salt", 0.5);
        n.sodium = extractDouble(text, "sodium", 0.2);

        return n;
    }

    private static double extractDouble(String src, String key, double fallback) {
        try {
            src = src.toLowerCase();
            if (src.contains(key)) {
                String[] parts = src.split(key);
                String num = parts[1].replaceAll("[^0-9.]", "");
                return Double.parseDouble(num);
            }
        } catch (Exception e) {
        }
        return fallback;
    }
}