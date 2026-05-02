package com.lifeos.service;

import com.lifeos.model.NutritionEntry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FoodService {

    // Paste your USDA API key here after you get it
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
            // Extract food description
            int nameStart = json.indexOf("\"description\":\"") + 16;
            int nameEnd   = json.indexOf("\"", nameStart);
            String name   = nameStart > 15 ? json.substring(nameStart, nameEnd) : query;

            double calories = extractNutrient(json, "Energy",  208);
            double protein  = extractNutrient(json, "Protein", 203);
            double carbs    = extractNutrient(json, "Carbohydrate", 205);
            double fat      = extractNutrient(json, "Total lipid", 204);
            double fiber    = extractNutrient(json, "Fiber", 291);
            double sodium   = extractNutrient(json, "Sodium", 307);
            double sugar    = extractNutrient(json, "Sugars", 269);

            NutritionEntry entry = new NutritionEntry(name, calories, protein, carbs, fat);
            entry.setFiberG(fiber);
            entry.setSodiumMg(sodium);
            entry.setSugarG(sugar);
            return entry;

        } catch (Exception e) {
            return fallbackSearch(query);
        }
    }

    private double extractNutrient(String json, String nutrientName, int nutrientId) {
        try {
            // Search by nutrient name
            int idx = json.indexOf("\"" + nutrientName);
            if (idx == -1) idx = json.indexOf(String.valueOf(nutrientId));
            if (idx == -1) return 0;
            int valueIdx = json.indexOf("\"value\":", idx);
            if (valueIdx == -1) return 0;
            int start = valueIdx + 8;
            int end   = json.indexOf(",", start);
            if (end == -1) end = json.indexOf("}", start);
            return Double.parseDouble(json.substring(start, end).trim());
        } catch (Exception e) {
            return 0;
        }
    }

    // Built-in fallback database for common foods
    private NutritionEntry fallbackSearch(String query) {
        String q = query.toLowerCase();
        if (q.contains("chicken breast"))  return new NutritionEntry("Chicken Breast (100g)", 165, 31, 0, 3.6);
        if (q.contains("chicken"))         return new NutritionEntry("Chicken (100g)", 239, 27, 0, 14);
        if (q.contains("egg"))             return new NutritionEntry("Egg (1 large)", 78, 6, 0.6, 5);
        if (q.contains("rice"))            return new NutritionEntry("White Rice (1 cup)", 206, 4.3, 45, 0.4);
        if (q.contains("brown rice"))      return new NutritionEntry("Brown Rice (1 cup)", 216, 5, 45, 1.8);
        if (q.contains("oat"))             return new NutritionEntry("Oatmeal (1 cup)", 154, 5, 28, 3);
        if (q.contains("banana"))          return new NutritionEntry("Banana (1 medium)", 105, 1.3, 27, 0.4);
        if (q.contains("apple"))           return new NutritionEntry("Apple (1 medium)", 95, 0.5, 25, 0.3);
        if (q.contains("broccoli"))        return new NutritionEntry("Broccoli (1 cup)", 55, 3.7, 11, 0.6);
        if (q.contains("salmon"))          return new NutritionEntry("Salmon (100g)", 208, 20, 0, 13);
        if (q.contains("tuna"))            return new NutritionEntry("Tuna (100g)", 116, 26, 0, 1);
        if (q.contains("beef"))            return new NutritionEntry("Ground Beef (100g)", 250, 26, 0, 15);
        if (q.contains("milk"))            return new NutritionEntry("Whole Milk (1 cup)", 149, 8, 12, 8);
        if (q.contains("greek yogurt"))    return new NutritionEntry("Greek Yogurt (1 cup)", 130, 22, 8, 0);
        if (q.contains("yogurt"))          return new NutritionEntry("Yogurt (1 cup)", 150, 8, 17, 3.8);
        if (q.contains("bread"))           return new NutritionEntry("Whole Wheat Bread (1 slice)", 80, 4, 15, 1);
        if (q.contains("pasta"))           return new NutritionEntry("Pasta (1 cup cooked)", 220, 8, 43, 1.3);
        if (q.contains("potato"))          return new NutritionEntry("Potato (1 medium)", 163, 4, 37, 0.2);
        if (q.contains("sweet potato"))    return new NutritionEntry("Sweet Potato (1 medium)", 103, 2.3, 24, 0.1);
        if (q.contains("almond"))          return new NutritionEntry("Almonds (1 oz)", 164, 6, 6, 14);
        if (q.contains("peanut butter"))   return new NutritionEntry("Peanut Butter (2 tbsp)", 190, 8, 6, 16);
        if (q.contains("avocado"))         return new NutritionEntry("Avocado (1/2)", 120, 1.5, 6, 11);
        if (q.contains("cheese"))          return new NutritionEntry("Cheddar Cheese (1 oz)", 113, 7, 0.4, 9);
        if (q.contains("pizza"))           return new NutritionEntry("Pizza (1 slice)", 285, 12, 36, 10);
        if (q.contains("burger"))          return new NutritionEntry("Hamburger", 354, 20, 29, 17);
        if (q.contains("orange"))          return new NutritionEntry("Orange (1 medium)", 62, 1.2, 15, 0.2);
        if (q.contains("strawberr"))       return new NutritionEntry("Strawberries (1 cup)", 49, 1, 12, 0.5);
        if (q.contains("coffee"))          return new NutritionEntry("Black Coffee (8 oz)", 2, 0.3, 0, 0);
        if (q.contains("protein shake") || q.contains("whey")) return new NutritionEntry("Protein Shake", 120, 25, 3, 2);
        return null; // Not found
    }
}