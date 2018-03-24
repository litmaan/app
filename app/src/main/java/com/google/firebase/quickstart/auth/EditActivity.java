package com.google.firebase.quickstart.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditActivity extends AppCompatActivity {
    public Button btn;
    public FirebaseAuth mAuth;
    public EditText age;
    public EditText weight;
    public EditText height;
    public Spinner activity;
    public Spinner sex;
    public Spinner goal;

    public Button sendToDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public FirebaseAuth myAtuh = FirebaseAuth.getInstance();
    public String curId;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(btnClick);
        mAuth = FirebaseAuth.getInstance();
        sendToDatabase = (Button) findViewById(R.id.sendToDatabaseBtn);
        sendToDatabase.setOnClickListener(sendToDatabaseOnClick);
        age = (EditText) findViewById(R.id.textEditAge);
        weight = (EditText) findViewById(R.id.textEditWeight);
        height = (EditText) findViewById(R.id.textEditHeight);
        activity = (Spinner) findViewById(R.id.spinnerActivityLevel);
        sex = (Spinner) findViewById(R.id.spinnerSex);
        goal = (Spinner) findViewById(R.id.spinnerGoal);
        curId = myAtuh.getUid();
        System.out.println("id zalogowany " + curId);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();


getFromDatabase();

    }

    View.OnClickListener sendToDatabaseOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");

            String ageString = age.getText().toString();
            Matcher ageMatcher = pattern.matcher(ageString);
            String weightString = weight.getText().toString();
            Matcher weightMatcher = pattern.matcher(weightString);

            String heightString = height.getText().toString();
            Matcher heightMatcher = pattern.matcher(heightString);

            if (ageString.isEmpty() || weightString.isEmpty() || heightString.isEmpty()) {
                Toast.makeText(EditActivity.this, "Wszystkie pola muszą być uzupełnione", Toast.LENGTH_SHORT).show();
            } else if (!ageMatcher.matches() || !weightMatcher.matches() || !heightMatcher.matches()) {
                Toast.makeText(EditActivity.this, "Podaj poprawne dane", Toast.LENGTH_SHORT).show();
            } else if (Double.valueOf(ageString) > 150) {
                Toast.makeText(EditActivity.this, "Podany wiek jest za wysoki", Toast.LENGTH_SHORT).show();
            } else if (Double.valueOf(weightString) > 300) {
                Toast.makeText(EditActivity.this, "Podana waga jest za duża", Toast.LENGTH_SHORT).show();
            } else if (Double.valueOf(heightString) > 250) {
                Toast.makeText(EditActivity.this, "Podany wzrost jest za duży", Toast.LENGTH_SHORT).show();
            } else {
                CurrentUser user = new CurrentUser(age.getText().toString(), weight.getText().toString(),
                        height.getText().toString(), activity.getSelectedItem().toString(), sex.getSelectedItem().toString(),goal.getSelectedItem().toString());
                UserBmi bmi = new UserBmi();
                user = bmi.calculateBmi(user);
                UserMacro macro = bmi.calculateMacro(user);
                RealtimeDatabase rd = new RealtimeDatabase();
                rd.setValue(user,macro);
                Toast.makeText(EditActivity.this, "Wysłano", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EditActivity.this, EditOrDelete.class);
                startActivity(intent);
            }
        }
    };
    View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            mAuth.signOut();
            updateUI();

        }
    };

    private void updateUI() {
        Intent intent = new Intent(this, EmailPasswordActivity.class);
        startActivity(intent);
    }


public void getFromDatabase(){
    myRef.child("users").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            Map<String, CurrentUser> map = new HashMap<>();
            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                CurrentUser currentUser = new CurrentUser(noteDataSnapshot.getValue(CurrentUser.class).getWiek(),
                        noteDataSnapshot.getValue(CurrentUser.class).getWaga(),
                        noteDataSnapshot.getValue(CurrentUser.class).getWzrost(),
                        noteDataSnapshot.getValue(CurrentUser.class).getActivity(),
                        noteDataSnapshot.getValue(CurrentUser.class).getSex(),
                        noteDataSnapshot.getValue(CurrentUser.class).getGoal());

                map.put(noteDataSnapshot.getKey(), currentUser);


            }


            for (Map.Entry<String, CurrentUser> entry : map.entrySet()) {
                if (entry.getKey().equals(curId)) {

                    age.setText(entry.getValue().getWiek());
                    weight.setText(entry.getValue().getWaga());
                    height.setText(entry.getValue().getWzrost());
                    switch (entry.getValue().getActivity()) {
                        case "niska":
                            activity.setSelection(0);
                            break;
                        case "średnia":
                            activity.setSelection(1);
                            break;
                        case "wysoka":
                            activity.setSelection(2);
                            break;
                        case "bardzo wysoka":
                            activity.setSelection(3);
                            break;
                        default:
                            System.out.println("NULL");
                            break;
                    }
                    switch (entry.getValue().getSex()) {
                        case "kobieta":
                            sex.setSelection(0);
                            break;
                        case "mężczyzna":
                            sex.setSelection(1);
                            break;

                        default:
                            System.out.println("NULL");
                            break;
                    }
                    switch(entry.getValue().getGoal()){
                        case "schudnąć":
                            goal.setSelection(0);
                            break;
                        case "utrzymać wagę":
                            goal.setSelection(1);
                            break;
                        case "przybrać na wadze":
                            goal.setSelection(2);
                            break;
                        default:
                            System.out.println("NULL");
                            break;
                    }

                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            System.out.println("-------------------");
            System.out.println("DATABASE ERROR");
            System.out.println("-------------------");

        }

    });
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.settings) {
            Toast.makeText(EditActivity.this,"settings",Toast.LENGTH_SHORT).show();
            return true;
        }if(item.getItemId() == R.id.logOut){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Na pewno chcesz się wylogować?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intent = new Intent(EditActivity.this,EmailPasswordActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        }

        return false;
    }
    public void createDrawer() {


        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(myToolbar)
                .withDrawerLayout(R.layout.drawer_layout)

                .addDrawerItems(new PrimaryDrawerItem().withIdentifier(1).withName("Menu"),
                        new SecondaryDrawerItem().withIdentifier(2).withName("Profil"),
                        new SecondaryDrawerItem().withIdentifier(3).withName("Dodaj produkt do bazy"),
                        new SecondaryDrawerItem().withIdentifier(4).withName("Dodaj produkt do dziennej listy"),
                        new SecondaryDrawerItem().withIdentifier(5).withName("Pokaż produkty dodane do dziennej listy")

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent intent;
                        switch (position) {
                            case 0:
                                break;
                            case 1:
                                intent = new Intent(EditActivity.this,UserProfile.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(EditActivity.this,AddProductToDatabase.class);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(EditActivity.this,AddDailyProducts.class);
                                startActivity(intent);
                                break;
                            case 4:
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                }).build();


    }
}
