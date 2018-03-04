package com.google.firebase.quickstart.auth;

/**
 * Created by Mateu on 04.03.2018.
 */

public class CurrentUser {


    public int wiek;
    public double waga;
    public double wzrost;


    CurrentUser(int wiek, double waga, double wzrost){

        this.wiek = wiek;
        this.waga = waga;
        this.wzrost =wzrost;
    }





    public int getWiek() {
        return wiek;
    }

    public void setWiek(int wiek) {
        this.wiek = wiek;
    }

    public double getWaga() {
        return waga;
    }

    public void setWaga(double waga) {
        this.waga = waga;
    }

    public double getWzrost() {
        return wzrost;
    }

    public void setWzrost(double wzrost) {
        this.wzrost = wzrost;
    }
}
