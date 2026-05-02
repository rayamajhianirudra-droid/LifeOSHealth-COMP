package com.lifeos.model;

public class User {
    private String id;
    private String name;
    private int age;
    private double weightLbs;
    private int heightFeet;
    private int heightInches;
    private String goal;
    private String activity;
    private String sex;

    public User() {}

    public User(String name, int age, double weightLbs, int heightFeet, int heightInches,
                String goal, String activity, String sex) {
        this.id = java.util.UUID.randomUUID().toString();
        this.name = name;
        this.age = age;
        this.weightLbs = weightLbs;
        this.heightFeet = heightFeet;
        this.heightInches = heightInches;
        this.goal = goal;
        this.activity = activity;
        this.sex = sex;
    }

    public double getHeightInches() { return (heightFeet * 12) + heightInches; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getWeightLbs() { return weightLbs; }
    public void setWeightLbs(double weightLbs) { this.weightLbs = weightLbs; }
    public int getHeightFeet() { return heightFeet; }
    public void setHeightFeet(int heightFeet) { this.heightFeet = heightFeet; }
    public int getHeightInchesOnly() { return heightInches; }
    public void setHeightInches(int heightInches) { this.heightInches = heightInches; }
    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }
    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    @Override
    public String toString() { return name; }
}