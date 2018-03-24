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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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

public class SelectDate extends AppCompatActivity {
    public ListView listView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        listView = (ListView)findViewById(R.id.listView);

        myRef.child("lista").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                    list.add(noteDataSnapshot.getKey());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SelectDate.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        ArrayList arrayList = new ArrayList();
                       myRef.child("lista").child(list.get(position)).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                              arrayList.add(dataSnapshot.getValue());
                               ArrayAdapter<String> secondArrayAdapter = new ArrayAdapter<String>(SelectDate.this, android.R.layout.simple_list_item_1, arrayList);
                               listView.setAdapter(secondArrayAdapter);
                           }



                           @Override
                           public void onCancelled(DatabaseError databaseError) {

                           }
                       });
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();
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
            Toast.makeText(SelectDate.this,"settings",Toast.LENGTH_SHORT).show();
            return true;
        }if(item.getItemId() == R.id.logOut){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Na pewno chcesz się wylogować?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intent = new Intent(SelectDate.this,EmailPasswordActivity.class);
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
                                intent = new Intent(SelectDate.this,UserProfile.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(SelectDate.this,AddProductToDatabase.class);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(SelectDate.this,AddDailyProducts.class);
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
