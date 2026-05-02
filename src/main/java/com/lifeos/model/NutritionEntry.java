package com.lifeos.model;

public class NutritionEntry {
    private String foodName;
    private double calories;
    private double proteinG;
    private double carbsG;
    private double fatG;
    private double fiberG;
    private double sodiumMg;
    private double sugarG;

    public NutritionEntry() {}

    public NutritionEntry(String foodName, double calories, double proteinG,
                          double carbsG, double fatG) {
        this.foodName = foodName;
        this.calories = calories;
        this.proteinG = proteinG;
        this.carbsG = carbsG;
        this.fatG = fatG;
    }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }
    public double getProteinG() { return proteinG; }
    public void setProteinG(double proteinG) { this.proteinG = proteinG; }
    public double getCarbsG() { return carbsG; }
    public void setCarbsG(double carbsG) { this.carbsG = carbsG; }
    public double getFatG() { return fatG; }
    public void setFatG(double fatG) { this.fatG = fatG; }
    public double getFiberG() { return fiberG; }
    public void setFiberG(double fiberG) { this.fiberG = fiberG; }
    public double getSodiumMg() { return sodiumMg; }
    public void setSodiumMg(double sodiumMg) { this.sodiumMg = sodiumMg; }
    public double getSugarG() { return sugarG; }
    public void setSugarG(double sugarG) { this.sugarG = sugarG; }

    @Override
    public String toString() {
        return foodName + " (" + (int)calories + " kcal | P:" +
                (int)proteinG + "g C:" + (int)carbsG + "g F:" + (int)fatG + "g)";
    }
}