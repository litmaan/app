package com.google.firebase.quickstart.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.widget.Toolbar;

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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddDailyProductsFrom extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView productName;

    TextView productCarbs;
    TextView productFat;
    TextView productProtein;
    EditText productAmount;
    TextView productKcal;

    Double productCarbsTemp;
    Double productFatTemp;
    Double productProteinTemp;
    Double productKcalTemp;

    Double productAmountTemp;

    Button dodajBtn;

    Double productResultCarbs;
    Double productResultFat;
    Double productResultProtein;
    Double productResultKcal;
    Double productResultAmount;

    CheckBox sniadanie;
    CheckBox obiad;
    CheckBox kolacja;
    CheckBox przekaski;

    String chooser;
    Toolbar myToolbar;
    public String dateFormat;
    public String name;
    public Map<String, String> map = new HashMap<>();
    public DatabaseReference kcalRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_products_from);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createDrawer();
        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat = simpleDateFormat.format(date);
        kcalRef = myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child("kcal");

        dodajBtn = (Button) findViewById(R.id.dodajBtn);

        productName = (TextView) findViewById(R.id.productName);

        productCarbs = (TextView) findViewById(R.id.carbs);
        productFat = (TextView) findViewById(R.id.fat);
        productProtein = (TextView) findViewById(R.id.protein);
        productAmount = (EditText) findViewById(R.id.amount);
        productKcal = (TextView) findViewById(R.id.kcal);

        sniadanie = (CheckBox) findViewById(R.id.sniadanie);
        obiad = (CheckBox) findViewById(R.id.obiad);
        kolacja = (CheckBox) findViewById(R.id.kolacja);
        przekaski = (CheckBox) findViewById(R.id.przekaski);

        sniadanie.setOnClickListener(onRadioButtonClick);
        obiad.setOnClickListener(onRadioButtonClick);
        kolacja.setOnClickListener(onRadioButtonClick);
        przekaski.setOnClickListener(onRadioButtonClick);
        productAmount.setText("100");


        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        productName.setText(name);


        getFromDatabase();
        productAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    productAmount.setText("");
                } else {
                    if (productAmount.getText().toString().isEmpty()) {
                        productAmount.setText("100");
                    }




                    productResultAmount =productAmountTemp;
                    productResultCarbs = productCarbsTemp * productAmountTemp;
                    productResultFat = productFatTemp * productAmountTemp;
                    productResultProtein =productProteinTemp * productAmountTemp;
                    productResultKcal = productKcalTemp * productAmountTemp;

                    productCarbs.setText(String.valueOf(productResultCarbs));
                    productFat.setText(String.valueOf(productResultFat));
                    productProtein.setText(String.valueOf(productResultProtein));
                    productKcal.setText(String.valueOf(productResultKcal));


                }
            }
        });


        dodajBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productAmount.getText().toString().isEmpty()) {
                    Toast.makeText(AddDailyProductsFrom.this, "Ilość nie może być pusta!", Toast.LENGTH_SHORT).show();
                } else {
                    if (sniadanie.isChecked() || obiad.isChecked() || kolacja.isChecked() || przekaski.isChecked()) {
                        productAmountTemp = Double.valueOf(productAmount.getText().toString()) / 100;



                        productResultAmount = productAmountTemp;
                        productResultCarbs = productCarbsTemp * productAmountTemp;
                        productResultFat = productFatTemp * productAmountTemp;
                        productResultProtein = productProteinTemp * productAmountTemp;
                        productResultKcal = productKcalTemp * productAmountTemp;


                        productResultAmount = BigDecimal.valueOf(productResultAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
                        productResultCarbs = BigDecimal.valueOf(productResultCarbs).setScale(2, RoundingMode.HALF_UP).doubleValue();
                        productResultFat = BigDecimal.valueOf(productResultFat).setScale(2, RoundingMode.HALF_UP).doubleValue();
                        productResultProtein = BigDecimal.valueOf(productResultProtein).setScale(2, RoundingMode.HALF_UP).doubleValue();
                        productResultKcal = BigDecimal.valueOf(productResultKcal).setScale(2, RoundingMode.HALF_UP).doubleValue();


                        Produkt produkt = new Produkt(productResultAmount, productResultProtein, productResultCarbs, productResultFat, productResultKcal);

                        myRef.child("lista").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    myRef.child("lista").child(mAuth.getCurrentUser().getUid()).setValue(dateFormat);
                                    myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).setValue(chooser);
                                    myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).setValue(name);
                                    myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).child(name).setValue(produkt);
                                    setKcal();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    //myRef.child("lista").child(mAuth.getCurrentUser().getUid()).setValue(dateFormat);
                                    myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).setValue(chooser);
                                    myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).setValue(name);
                                    myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).child(name).setValue(produkt);
                                    setKcal();
                                } else {

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    //myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).setValue(chooser);
                                    myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).setValue(name);
                                    myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).child(name).setValue(produkt);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).child(name).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).child(name).setValue(produkt);
                                }
                                if (dataSnapshot.exists()) {
                                    if (dataSnapshot.getValue().equals(name)) {
                                        myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).child(name).setValue(produkt);

                                    }
                                    if (!dataSnapshot.getValue().equals(name)) {
                                        myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child(chooser).child(name).setValue(produkt);


                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child("kcal").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child("kcal").setValue(0.0);

                                } else {

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        setKcal();

                        Intent intent = new Intent(AddDailyProductsFrom.this, AddDailyProducts.class);
                        startActivity(intent);

                    } else {

                        Toast.makeText(AddDailyProductsFrom.this, "Zaznacz jakąś opcje ", Toast.LENGTH_SHORT).show();
                    }

                }

            }

        });


    }

    public void setKcal() {
        myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child("sniadanie").addValueEventListener(new ValueEventListener() {
            Map<String, Double> map = new HashMap<>();
            double curKcal;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    map.put(noteDataSnapshot.getKey(), noteDataSnapshot.getValue(Produkt.class).getKcal());
                }
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    curKcal += entry.getValue();
                }
                myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child("obiad").addValueEventListener(new ValueEventListener() {
                    Map<String, Double> map = new HashMap<>();

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            map.put(noteDataSnapshot.getKey(), noteDataSnapshot.getValue(Produkt.class).getKcal());
                        }
                        for (Map.Entry<String, Double> entry : map.entrySet()) {
                            curKcal += entry.getValue();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child("kolacja").addValueEventListener(new ValueEventListener() {
                    Map<String, Double> map = new HashMap<>();

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            map.put(noteDataSnapshot.getKey(), noteDataSnapshot.getValue(Produkt.class).getKcal());
                        }
                        for (Map.Entry<String, Double> entry : map.entrySet()) {
                            curKcal += entry.getValue();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                myRef.child("lista").child(mAuth.getCurrentUser().getUid()).child(dateFormat).child("przekaski").addValueEventListener(new ValueEventListener() {
                    Map<String, Double> map = new HashMap<>();

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            map.put(noteDataSnapshot.getKey(), noteDataSnapshot.getValue(Produkt.class).getKcal());
                        }
                        for (Map.Entry<String, Double> entry : map.entrySet()) {
                            curKcal += entry.getValue();
                        }
                        kcalRef.setValue(curKcal);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    View.OnClickListener onRadioButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sniadanie:
                    obiad.setChecked(false);
                    kolacja.setChecked(false);
                    przekaski.setChecked(false);
                    chooser = "sniadanie";
                    break;
                case R.id.obiad:
                    sniadanie.setChecked(false);
                    kolacja.setChecked(false);
                    przekaski.setChecked(false);
                    chooser = "obiad";
                    break;
                case R.id.kolacja:
                    obiad.setChecked(false);
                    sniadanie.setChecked(false);
                    przekaski.setChecked(false);
                    chooser = "kolacja";
                    break;
                case R.id.przekaski:
                    obiad.setChecked(false);
                    kolacja.setChecked(false);
                    sniadanie.setChecked(false);
                    chooser = "przekaski";
                    break;
                default:
                    break;

            }
        }
    };

    public void getFromDatabase() {
        myRef.child("produkty").child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    map.put(noteDataSnapshot.getKey(), String.valueOf(noteDataSnapshot.getValue()));
                }
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getKey().equals("carbs")) {
                        productCarbsTemp = Double.valueOf(entry.getValue());
                        productCarbs.setText(entry.getValue());
                    }
                    if (entry.getKey().equals("fat")) {
                        productFatTemp = Double.valueOf(entry.getValue());
                        productFat.setText(entry.getValue());

                    }

                    if (entry.getKey().equals("kcal")) {
                        productKcalTemp = Double.valueOf(entry.getValue());
                        productKcal.setText(entry.getValue());

                    }
                    if (entry.getKey().equals("protein")) {
                        productProteinTemp = Double.valueOf(entry.getValue());
                        productProtein.setText(entry.getValue());

                    }
                    System.out.println(entry.getKey() + " ----- " + entry.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.settings) {
            Toast.makeText(AddDailyProductsFrom.this, "settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.logOut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Na pewno chcesz się wylogować?");
            builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intent = new Intent(AddDailyProductsFrom.this, EmailPasswordActivity.class);
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
                                intent = new Intent(AddDailyProductsFrom.this, UserProfile.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(AddDailyProductsFrom.this, EditActivity.class);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(AddDailyProductsFrom.this, AddProductToDatabase.class);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(AddDailyProductsFrom.this, AddDailyProducts.class);
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
