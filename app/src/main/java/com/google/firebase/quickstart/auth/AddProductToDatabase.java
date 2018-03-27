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
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class AddProductToDatabase extends AppCompatActivity {
    public EditText name;
    public EditText amount;
    public EditText protein;
    public EditText carbs;
    public EditText fat;
    public EditText kcal;
    public Button send;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_database);
        name = (EditText) findViewById(R.id.name);
        amount = (EditText) findViewById(R.id.amount);
        protein = (EditText) findViewById(R.id.protein);
        carbs = (EditText) findViewById(R.id.carbs);
        fat = (EditText) findViewById(R.id.fat);
        kcal = (EditText) findViewById(R.id.kcal);

        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(sendOnClick);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();
    }

    View.OnClickListener sendOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Produkt produkt = new Produkt(name.getText().toString(), Double.valueOf(amount.getText().toString()),
                    Double.valueOf(protein.getText().toString()), Double.valueOf(carbs.getText().toString()),
                    Double.valueOf(fat.getText().toString()), Double.valueOf(kcal.getText().toString()));
            ProductsDatabase pd = new ProductsDatabase();
            pd.sendProdukt(produkt);
            Toast.makeText(AddProductToDatabase.this, "Wysłano produkt do bazy", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AddProductToDatabase.this,UserProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
startActivity(intent);


        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.settings) {
            Toast.makeText(AddProductToDatabase.this, "settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.logOut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Na pewno chcesz się wylogować?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intent = new Intent(AddProductToDatabase.this, EmailPasswordActivity.class);
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
                                intent = new Intent(AddProductToDatabase.this, UserProfile.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(AddProductToDatabase.this, EditActivity.class);
                                startActivity(intent);
                                break;
                            case 3:

                                break;
                            case 4:
                                intent = new Intent(AddProductToDatabase.this, AddDailyProducts.class);
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
