package com.google.firebase.quickstart.auth;

/**
 * Created by linux on 24.03.18.
 */

public class UserMacro {
    public double kcal;
    public double protein;
    public double carbs;
    public double fat;
    public UserMacro(double kcal, double protein, double carbs, double fat) {
        this.kcal = kcal;

        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }
    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }


}
