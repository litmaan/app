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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class DeleteActivity extends AppCompatActivity {
    public EditText emailInput;
   //public EditText passwordInput;
    public CheckBox takCheck;
    public CheckBox nieCheck;
    public Button deleteBtn;


    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        emailInput = (EditText)findViewById(R.id.emailInput);
      // passwordInput = (EditText)findViewById(R.id.passwordInput);
        takCheck = (CheckBox)findViewById(R.id.takCheck);
        nieCheck = (CheckBox)findViewById(R.id.nieCheck);
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(deleteBtnOnClick);

        takCheck.setOnClickListener(takCheckListener);
        nieCheck.setOnClickListener(nieCheckListener);


        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();
    }
    View.OnClickListener takCheckListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(nieCheck.isChecked()){
                nieCheck.toggle();
            }
        }
    };

    View.OnClickListener nieCheckListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(takCheck.isChecked()){
                takCheck.toggle();
            }
        }
    };
    View.OnClickListener deleteBtnOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!takCheck.isChecked() && !nieCheck.isChecked()){
                Toast.makeText(DeleteActivity.this,"WYBIERZ OPCJE!!!",Toast.LENGTH_SHORT).show();
            }else{
                if(takCheck.isChecked()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user.getEmail().equals((emailInput.getText().toString()).replaceAll("\\s",""))) {
                        user.delete();
                        Toast.makeText(DeleteActivity.this, "Twoje konto zostało usunięte", Toast.LENGTH_SHORT).show();

//                        Intent intent = new Intent(DeleteActivity.this,EmailPasswordActivity.class);
//                        startActivity(intent);

                        mAuth.signOut();
                        updateUI();
                    }else{
                        Toast.makeText(DeleteActivity.this, "Podałeś nieprawidłowy email", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(DeleteActivity.this,"NIE TO NIE!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void updateUI() {
        Intent intent = new Intent(this, EmailPasswordActivity.class);
        startActivity(intent);
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
            Toast.makeText(DeleteActivity.this,"settings",Toast.LENGTH_SHORT).show();
            return true;
        }if(item.getItemId() == R.id.logOut){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Na pewno chcesz się wylogować?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intent = new Intent(DeleteActivity.this,EmailPasswordActivity.class);
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
                        new SecondaryDrawerItem().withIdentifier(3).withName("Edytuj Profil"),
                        new SecondaryDrawerItem().withIdentifier(4).withName("Dodaj produkt do bazy"),
                        new SecondaryDrawerItem().withIdentifier(5).withName("Dodaj produkt do dziennej listy"),
                        new SecondaryDrawerItem().withIdentifier(6).withName("Pokaż produkty dodane do dziennej listy")

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent intent;
                        switch (position) {
                            case 0:
                                break;
                            case 1:
                                intent = new Intent(DeleteActivity.this,UserProfile.class);
                                startActivity(intent);
                                break;
                                case 2:
                                intent = new Intent(DeleteActivity.this,EditActivity.class);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(DeleteActivity.this,AddProductToDatabase.class);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(DeleteActivity.this,AddDailyProducts.class);
                                startActivity(intent);
                                break;
                            case 5:
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                }).build();


    }
}
