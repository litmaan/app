package com.google.firebase.quickstart.auth;

/**
 * Created by Mateu on 04.03.2018.
 */

public class CurrentUser {


    public String wiek;
    public String waga;
    public String wzrost;
    public String activity;
    public String sex;
    public String goal;
    public String uId;

    public CurrentUser(String wiek, String waga, String wzrost, String activity, String sex,String goal, String uId, String bmi) {
        this.wiek = wiek;
        this.waga = waga;
        this.wzrost = wzrost;
        this.activity = activity;
        this.sex = sex;
        this.goal = goal;
        this.uId = uId;
        this.bmi = bmi;
    }



    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    CurrentUser(){

}
    CurrentUser(String wiek, String waga, String wzrost, String activity, String sex,String goal){
        this.wiek = wiek;
        this.waga = waga;
        this.wzrost =wzrost;
        this.activity = activity;
        this.sex = sex;
        this.goal = goal;

    }


    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String bmi;


    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getWiek() {
        return wiek;
    }

    public void setWiek(String wiek) {
        this.wiek = wiek;
    }

    public String getWaga() {
        return waga;
    }

    public void setWaga(String waga) {
        this.waga = waga;
    }

    public String getWzrost() {
        return wzrost;
    }

    public void setWzrost(String  wzrost) {
        this.wzrost = wzrost;
    }
}
