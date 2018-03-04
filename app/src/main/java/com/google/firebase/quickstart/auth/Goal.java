package com.google.firebase.quickstart.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Goal extends AppCompatActivity {
        public Button btn;
        public FirebaseAuth mAuth;
    public Button sendToDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(btnClick);
        mAuth = FirebaseAuth.getInstance();
        sendToDatabase = (Button)findViewById(R.id.btnSend);
        sendToDatabase.setOnClickListener(sendToDatabaseOnClick);
    }
    View.OnClickListener sendToDatabaseOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RealtimeDatabase rd = new RealtimeDatabase();
            rd.setValue();
        }
    };
View.OnClickListener btnClick = new  View.OnClickListener(){
        public void onClick(View v) {
            mAuth.signOut();
            updateUI();

        }
    };
    private void updateUI() {
Intent intent = new Intent(this,EmailPasswordActivity.class);
startActivity(intent);
        }


}
