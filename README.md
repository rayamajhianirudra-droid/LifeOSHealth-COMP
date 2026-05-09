# 💪 LifeOS Health

> A JavaFX desktop application for intelligent nutrition tracking, body composition analysis, and AI-powered fitness coaching.


---

## 📸 Overview

LifeOS Health is a full-featured desktop fitness tracker built entirely in Java with JavaFX. It calculates personalized calorie and macro targets, lets users search any food and auto-fills its nutritional data, tracks water intake, and estimates body fat percentage using the US Navy formula — all wrapped in a dark, futuristic UI.

Built as the COMP 306 Final Project at Southwest Minnesota State University.

---

## ✨ Features

- **Multi-User Login System** — Create, switch between, and delete user profiles with persistent data per user
- **Personalized Macro Targets** — Uses the Mifflin-St Jeor BMR equation + TDEE calculation to generate daily calorie, protein, carb, fat, and water goals
- **Food Search & Auto-Fill** — Type any food and the app automatically fills in calories, protein, carbs, fat, fiber, sodium, and sugar using the USDA FoodData Central API (with a 30+ food fallback database)
- **Body Fat Calculator** — Calculates body fat percentage using the US Navy Body Fat Formula (waist, neck, hip measurements) with a BMI-based fallback when measurements aren't available
- **Progress Tracking** — Color-coded progress bars for each macro showing today vs. target in real time
- **Water Intake Logging** — Track daily hydration in fluid ounces
- **AI Coach** — Personalized feedback based on daily intake patterns, identifying the biggest gap and providing motivational guidance
- **Data Persistence** — All user data saved locally as JSON files using Gson; fully persists between sessions
- **Imperial Units** — Weight in lbs, height in feet/inches, water in oz — built for US users

---

## 🏗️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| GUI Framework | JavaFX 21.0.2 |
| Build Tool | Maven |
| Data Persistence | Gson (JSON) |
| Nutrition Data | USDA FoodData Central API |
| Testing | JUnit Jupiter 5.10.2 |
| IDE | IntelliJ IDEA |

---

## 🗂️ Project Structure

```
LifeOSHealth/
├── src/
│   ├── main/
│   │   ├── java/com/lifeos/
│   │   │   ├── model/
│   │   │   │   ├── User.java              # User profile with imperial units
│   │   │   │   ├── DailyLog.java          # Daily food + water + body stats
│   │   │   │   ├── NutritionEntry.java    # Individual food item with macros/micros
│   │   │   │   └── NutritionGoal.java     # Calculated daily targets
│   │   │   ├── service/
│   │   │   │   ├── NutritionCalculator.java  # BMR, TDEE, macros, Navy body fat
│   │   │   │   ├── DataService.java          # JSON read/write per user
│   │   │   │   └── FoodService.java          # USDA API + fallback database
│   │   │   ├── controller/
│   │   │   │   ├── LoginController.java      # Multi-user login screen
│   │   │   │   ├── SetupController.java      # New profile creation
│   │   │   │   └── DashboardController.java  # Main app screen (all interactions)
│   │   │   └── MainApp.java                  # JavaFX Application entry point
│   │   └── resources/com/lifeos/
│   │       ├── login.fxml
│   │       ├── setup.fxml
│   │       ├── dashboard.fxml
│   │       └── style.css
│   └── test/
│       └── java/com/lifeos/
│           └── NutritionCalculatorTest.java  # 15 JUnit tests
└── pom.xml
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- IntelliJ IDEA (recommended) or any Maven-compatible IDE
- Internet connection (for USDA food data API calls)

### Run the App

1. Clone the repository:
```bash
git clone https://github.com/rayamajhianirudra-droid/LifeOSHealth-COMP306.git
```

2. Open in IntelliJ IDEA: `File → Open → select the LifeOSHealth folder`

3. Add VM options in Run Configuration:
```
--module-path "PATH_TO_JAVAFX_JARS" --add-modules javafx.controls,javafx.fxml
```
Replace `PATH_TO_JAVAFX_JARS` with the path to your local JavaFX 21 jars (found in your `.m2` Maven cache).

4. Run `MainApp.java` using the green play button.

### Run Tests
Right-click `NutritionCalculatorTest.java` → Run. All 15 tests should pass green.

---

## 🧮 How the Calculations Work

### BMR (Mifflin-St Jeor Equation)
```
Male:   BMR = (10 × weight_kg) + (6.25 × height_cm) − (5 × age) + 5
Female: BMR = (10 × weight_kg) + (6.25 × height_cm) − (5 × age) − 161
```

### TDEE
```
TDEE = BMR × Activity Multiplier
(Sedentary: 1.2 → Very Active: 1.9)
```

### Macro Targets
- **Protein:** 2g per kg of bodyweight
- **Fat:** 28% of total calories
- **Carbs:** Remaining calories ÷ 4 (minimum 50g)
- **Water:** 0.5 oz per lb of bodyweight

### Navy Body Fat Formula
```
Male:   %BF = 86.010 × log10(waist − neck) − 70.041 × log10(height) + 36.76
Female: %BF = 163.205 × log10(waist + hip − neck) − 97.684 × log10(height) − 78.387
```

---

## ✅ Test Coverage

15 unit tests covering:
- BMR calculation accuracy for male and female
- TDEE always greater than BMR
- Calorie targets for lose fat, build muscle, and maintain goals
- Protein goal based on bodyweight
- Progress scoring at 0%, 50%, and 100% of target
- Body fat category classification for males and females
- Navy formula output within realistic physiological ranges
- Unit conversions (lbs → kg, inches → cm)

---

## 👥 Team

| Name | Role |
|---|---|
| **AJ Rayamajhi** | Team Lead, Lead Programmer & Video Presenter |
| Ujjwal Shrestha | UI/UX Designer & QA Tester |
| Aashish Gaire | Documentation & Report Writer |
| Aayush Gaire | Research |

---

## 🎓 Academic Context

Built for **COMP 306 — Object-Oriented Programming** at Southwest Minnesota State University, May 2026.

Demonstrates: OOP design patterns, MVC architecture, multi-threading (JavaFX Platform.runLater), JSON persistence, REST API integration, and unit testing.

---

## 📬 Contact

**AJ Rayamajhi** — [LinkedIn](https://www.linkedin.com/in/anirudra-rayamajhi-bb48b83b4) · [GitHub](https://github.com/rayamajhianirudra-droid)
