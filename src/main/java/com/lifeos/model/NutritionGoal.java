package com.lifeos.model;

public class NutritionGoal {
    private double targetCalories;
    private double targetProteinG;
    private double targetCarbsG;
    private double targetFatG;
    private double targetWaterOz;

    public NutritionGoal() {}

    public NutritionGoal(double targetCalories, double targetProteinG,
                         double targetCarbsG, double targetFatG, double targetWaterOz) {
        this.targetCalories = targetCalories;
        this.targetProteinG = targetProteinG;
        this.targetCarbsG = targetCarbsG;
        this.targetFatG = targetFatG;
        this.targetWaterOz = targetWaterOz;
    }

    public double getTargetCalories() { return targetCalories; }
    public void setTargetCalories(double v) { this.targetCalories = v; }
    public double getTargetProteinG() { return targetProteinG; }
    public void setTargetProteinG(double v) { this.targetProteinG = v; }
    public double getTargetCarbsG() { return targetCarbsG; }
    public void setTargetCarbsG(double v) { this.targetCarbsG = v; }
    public double getTargetFatG() { return targetFatG; }
    public void setTargetFatG(double v) { this.targetFatG = v; }
    public double getTargetWaterOz() { return targetWaterOz; }
    public void setTargetWaterOz(double v) { this.targetWaterOz = v; }

    @Override
    public String toString() {
        return String.format("Goals: %.0f kcal | P:%.0fg | C:%.0fg | F:%.0fg | Water:%.0f oz",
                targetCalories, targetProteinG, targetCarbsG, targetFatG, targetWaterOz);
    }
}