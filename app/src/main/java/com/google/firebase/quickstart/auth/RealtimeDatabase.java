package com.google.firebase.quickstart.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mateu on 04.03.2018.
 */

public class RealtimeDatabase {
    public DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
    public FirebaseAuth myRef = FirebaseAuth.getInstance();

    public void setValue(){
        CurrentUser user = new CurrentUser(23,43.4,34.4);
        mDatabase.child(myRef.getUid()).setValue(user);



    }
}
