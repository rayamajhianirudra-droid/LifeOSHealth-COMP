package com.lifeos.service;

import com.lifeos.model.NutritionEntry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FoodService {

    private static final String API_KEY = "DEMO_KEY";
    private static final String BASE_URL = "https://api.nal.usda.gov/fdc/v1/foods/search";

    public NutritionEntry searchFood(String query) {
        try {
            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String urlStr  = BASE_URL + "?query=" + encoded +
                    "&pageSize=1&api_key=" + API_KEY;

            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() != 200) return fallbackSearch(query);

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }

            return parseResponse(sb.toString(), query);

        } catch (Exception e) {
            System.err.println("Food search error: " + e.getMessage());
            return fallbackSearch(query);
        }
    }

    private NutritionEntry parseResponse(String json, String query) {
        try {
            // ── Fix: find description field and extract full name ──
            String descKey = "\"description\":\"";
            int nameStart  = json.indexOf(descKey);
            String name    = query; // default fallback to original query

            if (nameStart != -1) {
                nameStart += descKey.length(); // move past the key including the opening quote
                int nameEnd = json.indexOf("\"", nameStart);
                if (nameEnd != -1) {
                    name = json.substring(nameStart, nameEnd);
                }
            }

            // Capitalise first letter nicely
            if (name.length() > 0) {
                name = name.substring(0, 1).toUpperCase()
                        + name.substring(1).toLowerCase();
            }

            double calories = extractNutrient(json, "Energy",       1008);
            double protein  = extractNutrient(json, "Protein",      1003);
            double carbs    = extractNutrient(json, "Carbohydrate", 1005);
            double fat      = extractNutrient(json, "Total lipid",  1004);
            double fiber    = extractNutrient(json, "Fiber",        1079);
            double sodium   = extractNutrient(json, "Sodium",       1093);
            double sugar    = extractNutrient(json, "Sugars",       2000);

            NutritionEntry entry = new NutritionEntry(name, calories, protein, carbs, fat);
            entry.setFiberG(fiber);
            entry.setSodiumMg(sodium);
            entry.setSugarG(sugar);
            return entry;

        } catch (Exception e) {
            System.err.println("Parse error: " + e.getMessage());
            return fallbackSearch(query);
        }
    }

    private double extractNutrient(String json, String nutrientName, int nutrientId) {
        try {
            int idx = json.indexOf("\"" + nutrientName);
            if (idx == -1) idx = json.indexOf("\"nutrientId\":" + nutrientId);
            if (idx == -1) return 0;
            int valueIdx = json.indexOf("\"value\":", idx);
            if (valueIdx == -1) return 0;
            int start = valueIdx + 8;
            int end   = json.indexOf(",", start);
            if (end == -1) end = json.indexOf("}", start);
            if (end == -1) return 0;
            return Double.parseDouble(json.substring(start, end).trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private NutritionEntry fallbackSearch(String query) {
        String q = query.toLowerCase().trim();
        if (q.contains("chicken breast"))  return new NutritionEntry("Chicken Breast (100g)",  165, 31.0, 0.0,  3.6);
        if (q.contains("chicken"))         return new NutritionEntry("Chicken (100g)",          239, 27.0, 0.0,  14.0);
        if (q.contains("egg"))             return new NutritionEntry("Egg (1 large)",            78,  6.0, 0.6,  5.0);
        if (q.contains("brown rice"))      return new NutritionEntry("Brown Rice (1 cup)",      216,  5.0, 45.0, 1.8);
        if (q.contains("rice"))            return new NutritionEntry("White Rice (1 cup)",      206,  4.3, 45.0, 0.4);
        if (q.contains("oat"))             return new NutritionEntry("Oatmeal (1 cup)",         154,  5.0, 28.0, 3.0);
        if (q.contains("banana"))          return new NutritionEntry("Banana (1 medium)",       105,  1.3, 27.0, 0.4);
        if (q.contains("apple"))           return new NutritionEntry("Apple (1 medium)",         95,  0.5, 25.0, 0.3);
        if (q.contains("broccoli"))        return new NutritionEntry("Broccoli (1 cup)",         55,  3.7, 11.0, 0.6);
        if (q.contains("salmon"))          return new NutritionEntry("Salmon (100g)",           208, 20.0, 0.0,  13.0);
        if (q.contains("tuna"))            return new NutritionEntry("Tuna (100g)",             116, 26.0, 0.0,  1.0);
        if (q.contains("beef"))            return new NutritionEntry("Ground Beef (100g)",      250, 26.0, 0.0,  15.0);
        if (q.contains("milk"))            return new NutritionEntry("Whole Milk (1 cup)",      149,  8.0, 12.0, 8.0);
        if (q.contains("greek yogurt"))    return new NutritionEntry("Greek Yogurt (1 cup)",    130, 22.0, 8.0,  0.0);
        if (q.contains("yogurt"))          return new NutritionEntry("Yogurt (1 cup)",          150,  8.0, 17.0, 3.8);
        if (q.contains("bread"))           return new NutritionEntry("Whole Wheat Bread (1 slice)", 80, 4.0, 15.0, 1.0);
        if (q.contains("pasta"))           return new NutritionEntry("Pasta (1 cup cooked)",   220,  8.0, 43.0, 1.3);
        if (q.contains("sweet potato"))    return new NutritionEntry("Sweet Potato (1 medium)", 103, 2.3, 24.0, 0.1);
        if (q.contains("potato"))          return new NutritionEntry("Potato (1 medium)",       163,  4.0, 37.0, 0.2);
        if (q.contains("almond"))          return new NutritionEntry("Almonds (1 oz)",          164,  6.0, 6.0,  14.0);
        if (q.contains("peanut butter"))   return new NutritionEntry("Peanut Butter (2 tbsp)",  190,  8.0, 6.0,  16.0);
        if (q.contains("avocado"))         return new NutritionEntry("Avocado (1/2)",           120,  1.5, 6.0,  11.0);
        if (q.contains("cheese"))          return new NutritionEntry("Cheddar Cheese (1 oz)",   113,  7.0, 0.4,  9.0);
        if (q.contains("pizza"))           return new NutritionEntry("Pizza (1 slice)",         285, 12.0, 36.0, 10.0);
        if (q.contains("burger"))          return new NutritionEntry("Hamburger",               354, 20.0, 29.0, 17.0);
        if (q.contains("orange"))          return new NutritionEntry("Orange (1 medium)",        62,  1.2, 15.0, 0.2);
        if (q.contains("strawberr"))       return new NutritionEntry("Strawberries (1 cup)",     49,  1.0, 12.0, 0.5);
        if (q.contains("coffee"))          return new NutritionEntry("Black Coffee (8 oz)",       2,  0.3, 0.0,  0.0);
        if (q.contains("protein shake") || q.contains("whey"))
            return new NutritionEntry("Protein Shake",           120, 25.0, 3.0,  2.0);
        if (q.contains("steak"))           return new NutritionEntry("Steak (100g)",            271, 26.0, 0.0,  17.0);
        if (q.contains("shrimp"))          return new NutritionEntry("Shrimp (100g)",            99, 24.0, 0.0,  0.3);
        if (q.contains("turkey"))          return new NutritionEntry("Turkey (100g)",           189, 29.0, 0.0,  7.0);
        if (q.contains("spinach"))         return new NutritionEntry("Spinach (1 cup)",          23,  3.0, 3.6,  0.4);
        return null;
    }
}