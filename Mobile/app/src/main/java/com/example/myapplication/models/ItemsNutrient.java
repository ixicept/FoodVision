package com.example.myapplication.models;

public class ItemsNutrient {
    public int calories;
    public int sugar;
    public int fat;

    public static ItemsNutrient fromText(String text) {
        ItemsNutrient n = new ItemsNutrient();

        n.calories = extract(text, "calories", 200);
        n.sugar = extract(text, "sugar", 12);
        n.fat = extract(text, "fat", 8);

        return n;
    }

    private static int extract(String src, String key, int fallback) {
        try {
            src = src.toLowerCase();
            if (src.contains(key)) {
                String[] parts = src.split(key);
                String num = parts[1].replaceAll("[^0-9]", "");
                return Integer.parseInt(num);
            }
        } catch (Exception e) {
        }
        return fallback;
    }
}