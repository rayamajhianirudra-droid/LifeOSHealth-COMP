package com.lifeos.service;

import com.lifeos.model.NutritionGoal;
import com.lifeos.model.User;

public class NutritionCalculator {

    public double lbsToKg(double lbs) { return lbs * 0.453592; }
    public double inchesToCm(double inches) { return inches * 2.54; }

    public double calculateBMR(User user) {
        double weightKg = lbsToKg(user.getWeightLbs());
        double heightCm = inchesToCm(user.getHeightInches());
        double bmr = 10 * weightKg + 6.25 * heightCm - 5 * user.getAge();
        return "male".equalsIgnoreCase(user.getSex()) ? bmr + 5 : bmr - 161;
    }

    public double calculateTDEE(User user) {
        double bmr = calculateBMR(user);
        double multiplier = switch (user.getActivity().toLowerCase()) {
            case "sedentary"   -> 1.2;
            case "light"       -> 1.375;
            case "moderate"    -> 1.55;
            case "active"      -> 1.725;
            case "very_active" -> 1.9;
            default            -> 1.55;
        };
        return bmr * multiplier;
    }

    public NutritionGoal calculateGoals(User user) {
        double tdee = calculateTDEE(user);
        double calories = switch (user.getGoal().toLowerCase()) {
            case "lose_fat"     -> tdee - 500;
            case "build_muscle" -> tdee + 300;
            default             -> tdee;
        };
        double weightKg = lbsToKg(user.getWeightLbs());
        double proteinG = weightKg * 2.0;
        double fatG     = (calories * 0.28) / 9.0;
        double carbsG   = Math.max((calories - (proteinG * 4) - (fatG * 9)) / 4.0, 50);
        double waterOz  = user.getWeightLbs() * 0.5;
        return new NutritionGoal(
                Math.round(calories), Math.round(proteinG),
                Math.round(carbsG), Math.round(fatG), Math.round(waterOz));
    }

    /**
     * US Navy Body Fat Formula
     * Male:   %BF = 86.010 * log10(waist - neck) - 70.041 * log10(height) + 36.76
     * Female: %BF = 163.205 * log10(waist + hip - neck) - 97.684 * log10(height) - 78.387
     * All measurements in inches.
     */
    public double calculateNavyBodyFat(double heightInches, double waistInches,
                                       double neckInches, double hipInches, String sex) {
        try {
            if ("male".equalsIgnoreCase(sex)) {
                double diff = waistInches - neckInches;
                if (diff <= 0) return -1;
                return 86.010 * Math.log10(diff)
                        - 70.041 * Math.log10(heightInches)
                        + 36.76;
            } else {
                double diff = waistInches + hipInches - neckInches;
                if (diff <= 0) return -1;
                return 163.205 * Math.log10(diff)
                        - 97.684 * Math.log10(heightInches)
                        - 78.387;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * BMI-based body fat estimate (fallback when measurements not available)
     * Formula: (1.20 * BMI) + (0.23 * age) - (10.8 * sexFactor) - 5.4
     */
    public double calculateBMIBodyFat(double weightLbs, double heightInches,
                                      int age, String sex) {
        double heightM = heightInches * 0.0254;
        double weightKg = lbsToKg(weightLbs);
        double bmi = weightKg / (heightM * heightM);
        double sexFactor = "male".equalsIgnoreCase(sex) ? 1 : 0;
        return (1.20 * bmi) + (0.23 * age) - (10.8 * sexFactor) - 5.4;
    }

    public int scoreProgress(double actual, double target) {
        if (target == 0) return 100;
        double ratio = actual / target;
        if (ratio >= 0.9 && ratio <= 1.1) return 100;
        if (ratio < 0.9) return (int) Math.round((ratio / 0.9) * 100);
        return (int) Math.max(0, Math.round(100 - (ratio - 1.1) * 200));
    }

    public String getBodyFatCategory(double bodyFatPct, String sex) {
        if ("male".equalsIgnoreCase(sex)) {
            if (bodyFatPct < 6)  return "Essential Fat";
            if (bodyFatPct < 14) return "Athletic";
            if (bodyFatPct < 18) return "Fit";
            if (bodyFatPct < 25) return "Average";
            return "Above Average";
        } else {
            if (bodyFatPct < 14) return "Essential Fat";
            if (bodyFatPct < 21) return "Athletic";
            if (bodyFatPct < 25) return "Fit";
            if (bodyFatPct < 32) return "Average";
            return "Above Average";
        }
    }
}