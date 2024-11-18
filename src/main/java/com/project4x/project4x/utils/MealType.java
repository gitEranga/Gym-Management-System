package com.project4x.project4x.utils;

public enum MealType {
    MEAL_1("Breakfast"),
    MEAL_2("Lunch"),
    MEAL_3("Dinner"),
    MEAL_4("Snack"),
    MEAL_5("Supper");

    private final String description;
    MealType(String description) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }
}
