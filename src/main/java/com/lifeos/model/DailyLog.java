package com.lifeos.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyLog {
    private LocalDate date;
    private List<NutritionEntry> entries;
    private double waterOz;
    private double bodyFatPct;
    private double weightLbs;

    public DailyLog() {
        this.date = LocalDate.now();
        this.entries = new ArrayList<>();
    }

    public DailyLog(LocalDate date) {
        this.date = date;
        this.entries = new ArrayList<>();
    }

    public void addEntry(NutritionEntry entry) {
        entries.add(entry);
    }

    public void removeEntry(NutritionEntry entry) {
        entries.remove(entry);
    }

    public double getTotalCalories() {
        return entries.stream().mapToDouble(NutritionEntry::getCalories).sum();
    }

    public double getTotalProtein() {
        return entries.stream().mapToDouble(NutritionEntry::getProteinG).sum();
    }

    public double getTotalCarbs() {
        return entries.stream().mapToDouble(NutritionEntry::getCarbsG).sum();
    }

    public double getTotalFat() {
        return entries.stream().mapToDouble(NutritionEntry::getFatG).sum();
    }

    public double getTotalFiber() {
        return entries.stream().mapToDouble(NutritionEntry::getFiberG).sum();
    }

    public double getTotalSodium() {
        return entries.stream().mapToDouble(NutritionEntry::getSodiumMg).sum();
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public List<NutritionEntry> getEntries() { return entries; }
    public void setEntries(List<NutritionEntry> entries) { this.entries = entries; }
    public double getWaterOz() { return waterOz; }
    public void setWaterOz(double waterOz) { this.waterOz = waterOz; }
    public double getBodyFatPct() { return bodyFatPct; }
    public void setBodyFatPct(double bodyFatPct) { this.bodyFatPct = bodyFatPct; }
    public double getWeightLbs() { return weightLbs; }
    public void setWeightLbs(double weightLbs) { this.weightLbs = weightLbs; }
}