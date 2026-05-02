package com.lifeos.controller;

import com.lifeos.model.*;
import com.lifeos.service.DataService;
import com.lifeos.service.FoodService;
import com.lifeos.service.NutritionCalculator;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label dateLabel;
    @FXML private Label targetCalLabel;
    @FXML private Label targetProteinLabel;
    @FXML private Label targetCarbsLabel;
    @FXML private Label targetFatLabel;
    @FXML private Label targetWaterLabel;
    @FXML private Label todayCalLabel;
    @FXML private Label todayProteinLabel;
    @FXML private Label todayCarbsLabel;
    @FXML private Label todayFatLabel;
    @FXML private Label todayWaterLabel;
    @FXML private ProgressBar calProgress;
    @FXML private ProgressBar proteinProgress;
    @FXML private ProgressBar carbsProgress;
    @FXML private ProgressBar fatProgress;
    @FXML private ProgressBar waterProgress;
    @FXML private TableView<NutritionEntry> foodTable;
    @FXML private TableColumn<NutritionEntry, String> foodNameCol;
    @FXML private TableColumn<NutritionEntry, Double> calCol;
    @FXML private TableColumn<NutritionEntry, Double> proteinCol;
    @FXML private TableColumn<NutritionEntry, Double> carbsCol;
    @FXML private TableColumn<NutritionEntry, Double> fatCol;
    @FXML private TextField foodSearchField;
    @FXML private Label addFoodError;
    @FXML private TextField waterField;
    @FXML private TextField bodyWeightField;
    @FXML private TextField neckField;
    @FXML private TextField waistField;
    @FXML private TextField hipField;
    @FXML private Label bodyFatResultLabel;
    @FXML private Label bodyFatCategoryLabel;
    @FXML private Label bmiEstimateLabel;
    @FXML private Text coachText;

    private Stage stage;
    private User user;
    private NutritionGoal goals;
    private DailyLog todayLog;
    private List<DailyLog> allLogs;
    private final DataService dataService = new DataService();
    private final NutritionCalculator calculator = new NutritionCalculator();
    private final FoodService foodService = new FoodService();

    public void setStage(Stage stage) { this.stage = stage; }

    public void initData(User user) {
        this.user = user;
        this.goals = calculator.calculateGoals(user);
        this.allLogs = dataService.loadLogs(user);
        this.todayLog = dataService.getTodayLog(allLogs);
        if (todayLog == null) {
            todayLog = new DailyLog();
            allLogs.add(todayLog);
        }
        setupTableColumns();
        refreshUI();
    }

    private void setupTableColumns() {
        foodNameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFoodName()));
        calCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getCalories()));
        proteinCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getProteinG()));
        carbsCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getCarbsG()));
        fatCol.setCellValueFactory(d -> new SimpleObjectProperty<>(d.getValue().getFatG()));
    }

    private void refreshUI() {
        welcomeLabel.setText("Welcome back, " + user.getName() + "!");
        dateLabel.setText(LocalDate.now().toString());
        targetCalLabel.setText((int) goals.getTargetCalories() + " kcal");
        targetProteinLabel.setText((int) goals.getTargetProteinG() + "g");
        targetCarbsLabel.setText((int) goals.getTargetCarbsG() + "g");
        targetFatLabel.setText((int) goals.getTargetFatG() + "g");
        targetWaterLabel.setText((int) goals.getTargetWaterOz() + " oz");
        double cal   = todayLog.getTotalCalories();
        double pro   = todayLog.getTotalProtein();
        double carb  = todayLog.getTotalCarbs();
        double fat   = todayLog.getTotalFat();
        double water = todayLog.getWaterOz();
        todayCalLabel.setText((int) cal + " kcal");
        todayProteinLabel.setText((int) pro + "g");
        todayCarbsLabel.setText((int) carb + "g");
        todayFatLabel.setText((int) fat + "g");
        todayWaterLabel.setText((int) water + " oz");
        calProgress.setProgress(goals.getTargetCalories() > 0 ?
                Math.min(cal / goals.getTargetCalories(), 1.0) : 0);
        proteinProgress.setProgress(goals.getTargetProteinG() > 0 ?
                Math.min(pro / goals.getTargetProteinG(), 1.0) : 0);
        carbsProgress.setProgress(goals.getTargetCarbsG() > 0 ?
                Math.min(carb / goals.getTargetCarbsG(), 1.0) : 0);
        fatProgress.setProgress(goals.getTargetFatG() > 0 ?
                Math.min(fat / goals.getTargetFatG(), 1.0) : 0);
        waterProgress.setProgress(goals.getTargetWaterOz() > 0 ?
                Math.min(water / goals.getTargetWaterOz(), 1.0) : 0);
        foodTable.setItems(FXCollections.observableArrayList(todayLog.getEntries()));
        updateCoachMessage(cal, pro, water);

        // Pre-fill weight if available
        if (todayLog.getWeightLbs() > 0)
            bodyWeightField.setText(String.valueOf((int) todayLog.getWeightLbs()));
    }

    private void updateCoachMessage(double cal, double pro, double water) {
        StringBuilder msg = new StringBuilder();
        double calPct   = goals.getTargetCalories() > 0 ? cal / goals.getTargetCalories() : 0;
        double proPct   = goals.getTargetProteinG() > 0 ? pro / goals.getTargetProteinG() : 0;
        double waterPct = goals.getTargetWaterOz()  > 0 ? water / goals.getTargetWaterOz() : 0;
        if (cal == 0) {
            msg.append("Start logging your meals to get personalized coaching!");
        } else if (calPct < 0.5) {
            msg.append("You've eaten less than half your calorie target. Fuel up — your body needs energy.");
        } else if (calPct > 1.15) {
            msg.append("You've exceeded your calorie target today. Tomorrow is a fresh start.");
        } else {
            msg.append("Solid day so far! You're right on track with your calories.");
        }
        if (proPct < 0.7 && cal > 0)
            msg.append(" Protein is your biggest gap — grab a high-protein snack.");
        if (waterPct < 0.5)
            msg.append(" Don't forget to hydrate — you're under 50% of your water goal.");
        coachText.setText(msg.toString());
    }

    @FXML
    private void handleCalculateBodyFat() {
        try {
            double weightLbs = Double.parseDouble(bodyWeightField.getText().trim());
            double heightIn  = user.getHeightInches();
            String sex       = user.getSex();

            // Save weight to today's log
            todayLog.setWeightLbs(weightLbs);

            double bodyFat;
            String method;

            // Try Navy formula first if neck + waist provided
            if (!neckField.getText().isEmpty() && !waistField.getText().isEmpty()) {
                double neck  = Double.parseDouble(neckField.getText().trim());
                double waist = Double.parseDouble(waistField.getText().trim());
                double hip   = hipField.getText().isEmpty() ? 0 :
                        Double.parseDouble(hipField.getText().trim());

                bodyFat = calculator.calculateNavyBodyFat(heightIn, waist, neck, hip, sex);
                method  = "Navy Formula";
            } else {
                // Fallback to BMI estimate
                bodyFat = calculator.calculateBMIBodyFat(weightLbs, heightIn, user.getAge(), sex);
                method  = "BMI Estimate";
            }

            if (bodyFat < 0) {
                bodyFatResultLabel.setText("Invalid");
                bodyFatCategoryLabel.setText("Check measurements");
                return;
            }

            // BMI for display
            double heightM  = heightIn * 0.0254;
            double weightKg = calculator.lbsToKg(weightLbs);
            double bmi      = weightKg / (heightM * heightM);

            String category = calculator.getBodyFatCategory(bodyFat, sex);
            todayLog.setBodyFatPct(bodyFat);
            dataService.saveLogs(allLogs, user);

            bodyFatResultLabel.setText(String.format("%.1f%%", bodyFat));
            bodyFatCategoryLabel.setText(category);
            bmiEstimateLabel.setText(String.format("BMI: %.1f  (%s)", bmi, method));

        } catch (NumberFormatException e) {
            bodyFatResultLabel.setText("--");
            bodyFatCategoryLabel.setText("Enter valid numbers");
        }
    }

    @FXML
    private void handleSearchFood() {
        String query = foodSearchField.getText().trim();
        if (query.isEmpty()) {
            addFoodError.setText("Enter a food name to search.");
            return;
        }
        addFoodError.setText("Searching...");
        new Thread(() -> {
            NutritionEntry entry = foodService.searchFood(query);
            javafx.application.Platform.runLater(() -> {
                if (entry != null) {
                    todayLog.addEntry(entry);
                    dataService.saveLogs(allLogs, user);
                    foodSearchField.clear();
                    addFoodError.setText("");
                    refreshUI();
                } else {
                    addFoodError.setText("Food not found. Try a different name.");
                }
            });
        }).start();
    }

    @FXML
    private void handleRemoveFood() {
        NutritionEntry selected = foodTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            todayLog.removeEntry(selected);
            dataService.saveLogs(allLogs, user);
            refreshUI();
        }
    }

    @FXML
    private void handleLogWater() {
        try {
            double oz = Double.parseDouble(waterField.getText().trim());
            if (oz < 0) return;
            todayLog.setWaterOz(todayLog.getWaterOz() + oz);
            dataService.saveLogs(allLogs, user);
            waterField.clear();
            refreshUI();
        } catch (NumberFormatException ignored) {}
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/lifeos/login.fxml"));
            Scene scene = new Scene(loader.load(), 500, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/com/lifeos/style.css").toExternalForm());
            LoginController controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("LifeOS Health — Login");
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}