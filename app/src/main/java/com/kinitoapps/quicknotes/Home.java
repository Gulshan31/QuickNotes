package com.kinitoapps.quicknotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Objects;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    String class_name;
    Button ncert_exem,sub_phy,sub_chem,sub_bio,pr_phy,pr_chem,pr_bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
         sub_phy = findViewById(R.id.sub_phy);
         sub_phy.setOnClickListener(this);
         sub_chem = findViewById(R.id.sub_chem);
         sub_chem.setOnClickListener(this);
         sub_bio = findViewById(R.id.sub_bio);
         sub_bio.setOnClickListener(this);
         pr_phy = findViewById(R.id.pr_phy);
         pr_phy.setOnClickListener(this);
         pr_chem = findViewById(R.id.pr_chem);
         pr_chem.setOnClickListener(this);
         pr_bio = findViewById(R.id.pr_bio);
         pr_bio.setOnClickListener(this);
         ncert_exem = findViewById(R.id.ncert_exem);
         ncert_exem.setOnClickListener(this);
         class_name = getIntent().getStringExtra("class_name");


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("HOME");
        NavigationView navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        greeting();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        switch (menuItem.getItemId()) {

            case R.id.db: {
                //do somthing
                AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle("DASHBOARD");
                alertDialog.setMessage("This feature will implemented soon");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                break;
            }

            case R.id.doubt: {
                //do somthing

                AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle("DOUBT");
                alertDialog.setMessage("This feature will implemented soon");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                alertDialog.show();


                break;
            }

            case R.id.hire: {
                //do somthing
                AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle("For Any Query");
                alertDialog.setMessage("Call at 6239974510");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                break;
            }
            case R.id.logOut: {
                //do somthing
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this, RegisterActivity.class));
                finish();
                break;
            }
        }
        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void greeting() {
        TextView greeting = findViewById(R.id.greeting);


        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            greeting.setText("Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greeting.setText("Good Afternoon");
        } else if (timeOfDay >= 16 && timeOfDay < 24) {
            greeting.setText("Good Evening");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflating the menu item
        getMenuInflater().inflate(R.menu.home_menu_items, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_share) {

            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "QuickNotes");
                String shareMessage = "\nIts a cool app cleared all my doubts, I recommending you to download \n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
                Log.v("Share", e.toString());
            }

            return true;
        }


        return super.onOptionsItemSelected(item);


    }


    @Override
    public void onClick(View v) {


        if (v == sub_phy) {
            Intent intent = new Intent(Home.this,MainDrawerActivity.class);
            intent.putExtra("Subject","Physics");
            intent.putExtra("Class",class_name);
            intent.putExtra("Section","Theory");
            startActivity(intent);
            Toast.makeText(Home.this,"Physics",Toast.LENGTH_LONG).show();
        }
        if (v == sub_chem) {
            Intent intent = new Intent(Home.this,MainDrawerActivity.class);
            intent.putExtra("Subject","Chemistry");
            intent.putExtra("Class",class_name);
            intent.putExtra("Section","Theory");
            startActivity(intent);
            Toast.makeText(Home.this,"Chemistry",Toast.LENGTH_LONG).show();
        }
        if (v == sub_bio) {
            Intent intent = new Intent(Home.this,MainDrawerActivity.class);
            intent.putExtra("Subject","Biology");
            intent.putExtra("Class",class_name);
            intent.putExtra("Section","Theory");
            startActivity(intent);
            Toast.makeText(Home.this,"Biology",Toast.LENGTH_LONG).show();
        }
        if (v == pr_phy) {
            Intent intent = new Intent(Home.this,MainDrawerActivity.class);
            intent.putExtra("Subject","Physics ");
            intent.putExtra("Class",class_name);
            intent.putExtra("Section","Practicals");

            startActivity(intent);
            Toast.makeText(Home.this,"practicals Physics",Toast.LENGTH_LONG).show();
        }
        if (v == pr_chem) {
            Intent intent = new Intent(Home.this,MainDrawerActivity.class);
            intent.putExtra("Subject","Physics");
            intent.putExtra("Class",class_name);
            intent.putExtra("Section","Practicals");
            startActivity(intent);
            Toast.makeText(Home.this,"Practical chemistry",Toast.LENGTH_LONG).show();
        }
        if (v == pr_bio) {
            Intent intent = new Intent(Home.this,MainDrawerActivity.class);
            intent.putExtra("Subject","Physics");
            intent.putExtra("Class",class_name);
            intent.putExtra("Section","Practicals");
            startActivity(intent);
            Toast.makeText(Home.this,"Practical Biology",Toast.LENGTH_LONG).show();
        }

//        if (v == ncert_exem) {
//            Intent intent = new Intent(Home.this,MainDrawerActivity.class);
//            intent.putExtra("Subject","Physics");
//            intent.putExtra("Class",class_name);
//            startActivity(intent);
//            Toast.makeText(Home.this,"Ncert Exemplar",Toast.LENGTH_LONG).show();
//        }

    }




    }

