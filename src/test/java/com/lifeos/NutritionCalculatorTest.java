package com.lifeos;

import com.lifeos.model.NutritionGoal;
import com.lifeos.model.User;
import com.lifeos.service.NutritionCalculator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NutritionCalculatorTest {

    NutritionCalculator calc = new NutritionCalculator();

    User maleUser = new User("Test", 25, 175, 5, 11,
            "maintain", "moderate", "male");

    User femaleUser = new User("Test", 25, 135, 5, 5,
            "lose_fat", "moderate", "female");

    @Test
    void testBMRMaleIsPositive() {
        double bmr = calc.calculateBMR(maleUser);
        assertTrue(bmr > 0, "BMR should be positive");
    }

    @Test
    void testBMRFemaleIsPositive() {
        double bmr = calc.calculateBMR(femaleUser);
        assertTrue(bmr > 0, "BMR should be positive");
    }

    @Test
    void testTDEEGreaterThanBMR() {
        double bmr  = calc.calculateBMR(maleUser);
        double tdee = calc.calculateTDEE(maleUser);
        assertTrue(tdee > bmr, "TDEE should be greater than BMR");
    }

    @Test
    void testGoalsCaloriesLoseFat() {
        NutritionGoal goals = calc.calculateGoals(femaleUser);
        double tdee = calc.calculateTDEE(femaleUser);
        assertEquals(Math.round(tdee - 500), (long) goals.getTargetCalories(),
                "Lose fat calories should be TDEE minus 500");
    }

    @Test
    void testGoalsCaloriesBuildMuscle() {
        User muscleUser = new User("Test", 25, 175, 5, 11,
                "build_muscle", "moderate", "male");
        NutritionGoal goals = calc.calculateGoals(muscleUser);
        double tdee = calc.calculateTDEE(muscleUser);
        assertEquals(Math.round(tdee + 300), (long) goals.getTargetCalories(),
                "Build muscle calories should be TDEE plus 300");
    }

    @Test
    void testProteinGoalBasedOnWeight() {
        NutritionGoal goals = calc.calculateGoals(maleUser);
        double weightKg = calc.lbsToKg(175);
        assertEquals(Math.round(weightKg * 2.0), (long) goals.getTargetProteinG(),
                "Protein should be 2g per kg bodyweight");
    }

    @Test
    void testScoreProgressPerfect() {
        assertEquals(100, calc.scoreProgress(100, 100));
    }

    @Test
    void testScoreProgressUnder() {
        int score = calc.scoreProgress(50, 100);
        assertTrue(score < 100 && score > 0, "Score should be between 0-100");
    }

    @Test
    void testScoreProgressZeroTarget() {
        assertEquals(100, calc.scoreProgress(0, 0));
    }

    @Test
    void testBodyFatCategoryMale() {
        assertEquals("Athletic", calc.getBodyFatCategory(12, "male"));
        assertEquals("Fit",      calc.getBodyFatCategory(16, "male"));
        assertEquals("Average",  calc.getBodyFatCategory(22, "male"));
    }

    @Test
    void testBodyFatCategoryFemale() {
        assertEquals("Athletic", calc.getBodyFatCategory(18, "female"));
        assertEquals("Fit",      calc.getBodyFatCategory(23, "female"));
    }

    @Test
    void testNavyBodyFatMale() {
        double bf = calc.calculateNavyBodyFat(71, 34, 15, 0, "male");
        assertTrue(bf > 5 && bf < 35, "Body fat should be in realistic range");
    }

    @Test
    void testNavyBodyFatFemale() {
        double bf = calc.calculateNavyBodyFat(65, 30, 13, 38, "female");
        assertTrue(bf > 10 && bf < 45, "Body fat should be in realistic range");
    }

    @Test
    void testLbsToKgConversion() {
        assertEquals(45.3592, calc.lbsToKg(100), 0.001);
    }

    @Test
    void testInchesToCmConversion() {
        assertEquals(25.4, calc.inchesToCm(10), 0.001);
    }
}