package com.kinitoapps.quicknotes;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kinitoapps.quicknotes.R;

public class splashScreen extends AppCompatActivity {


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String studentGrade;

    /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.activity_splash_screen);

            /* New Handler to start the Menu-Activity
             * and close this Splash-Screen after some seconds.*/
            int SPLASH_DISPLAY_LENGTH = 1000;
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {

                    /* Create an Intent that will start the Menu-Activity. */
                    if (user==null){

                        Intent mainIntent = new Intent(splashScreen.this,RegisterActivity.class);
                        splashScreen.this.startActivity(mainIntent);
                        splashScreen.this.finish();
                    }
                    else {
                        DatabaseReference userData = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("userInfo");
                           userData.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   Users users = dataSnapshot.getValue(Users.class);
                                   studentGrade = users.getStudentGrade();
                                   Log.v("Gulshan",studentGrade);
                                   Intent mainIntent = new Intent(splashScreen.this,Home.class);
                                   mainIntent.putExtra("class_name",studentGrade);
                                   splashScreen.this.startActivity(mainIntent);
                                   splashScreen.this.finish();
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });




                    }
                }
            }, SPLASH_DISPLAY_LENGTH);
        }


}


