package com.google.firebase.quickstart.auth;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by linux on 24.03.18.
 */

public class UserBmi {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String curId = mAuth.getUid();
    CurrentUser user;

    public CurrentUser calculateBmi(CurrentUser user){
        double bmi = (Double.valueOf(user.getWaga())*(Math.pow(Double.valueOf(user.getWzrost()),2)));
        CurrentUser resUser = new CurrentUser(user.getWiek(),user.getWaga(),user.getWzrost(),user.getActivity(),user.getSex(),curId,String.valueOf(bmi),user.getGoal());

        return resUser;
    };
}

// if(bmi < 18.49  ){
//
//        }if(bmi > 18.5 && bmi < 24.99){
//
//        }if(bmi >25 && bmi < 29.99){
//
//        }if(bmi > 30.0 && bmi < 34.99){
//
//        }if(bmi > 35.0 && bmi < 39.99){
//
//        }if(bmi >= 40.0){
//
//        }