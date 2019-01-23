package com.kinitoapps.quicknotes;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;




public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private BroadcastReceiver mNetworkReceiver;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
     private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText inputFullName;
     private EditText inputEmail;
     private EditText inputPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;
    private static final int RC_SIGN_IN=1;
    private  GoogleSignInClient mGoogleSignInClient;
    private String studentGrade;
    private ProgressDialog progress;
    private DatabaseReference forUsers;
    private SignInButton signInButton;
    static TextView tv_check_connection;



    @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_register);
        tv_check_connection= findViewById(R.id.tv_check_connection);
        mNetworkReceiver = new ConnectivityReceiver();
        registerNetworkBroadcastForNougat();
        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale( RegisterActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions( RegisterActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }



        signInButton = findViewById(R.id.sign_in_button);
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(RegisterActivity.this);
        forUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth=FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.show();
                signIn();
            }
        });

        progress=new ProgressDialog(RegisterActivity.this);
        progress.setMessage("Connecting to Google Account, please wait...");


        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        inputFullName = findViewById(R.id.name);
        Button skipButton = findViewById(R.id.btnSkip);
         inputEmail = findViewById(R.id.email);
         inputPassword = findViewById(R.id.password);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnLinkToLogin = findViewById(R.id.btnLinkToLoginScreen);

         // Progress dialog
        ProgressDialog pDialog = new ProgressDialog(this);
         pDialog.setCancelable(false);

       skipButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(RegisterActivity.this,ChooseClass.class));
               finish();
           }
       });
         // Register Button Click event
         btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                 finish();
             }
         });
         btnRegister.setOnClickListener(new View.OnClickListener() {
             public void onClick(View view) {


                     registerUser();


             }
         });


        // Spinner element
        Spinner spinner =  findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        categories.add("10th Class");
        categories.add("9th Class");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }



    public static void dialog(boolean value){

        if(value){
            tv_check_connection.setText(R.string.network_connection);
            tv_check_connection.setBackgroundColor(Color.WHITE);
            tv_check_connection.setTextColor(Color.BLACK);

            Handler handler = new Handler();
            Runnable delayrunnable = new Runnable() {
                @Override
                public void run() {
                    tv_check_connection.setVisibility(View.GONE);
                }
            };
            handler.postDelayed(delayrunnable, 3000);
        }else {
            tv_check_connection.setVisibility(View.VISIBLE);
            tv_check_connection.setText(R.string.no_network_connection);
            tv_check_connection.setBackgroundColor(Color.RED);
            tv_check_connection.setTextColor(Color.WHITE);
        }
    }






    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private  void registerUser() {
         String email = inputEmail.getText().toString().trim();
         String pass_word = inputPassword.getText().toString().trim();
         final String user_name = inputFullName.getText().toString();
         if (TextUtils.isEmpty(user_name)) {
             // email is empty
             Toast.makeText(RegisterActivity.this, "please select  name", Toast.LENGTH_SHORT).show();
             return;// to stop the function from executation.
         }


         if (TextUtils.isEmpty(email)) {
             // email is empty
             Toast.makeText(RegisterActivity.this, "please enter email", Toast.LENGTH_SHORT).show();
             return;
             // to stop the function from executation.
         }
         if (TextUtils.isEmpty(pass_word)) {
             // email is empty
             Toast.makeText(RegisterActivity.this, "please enter password", Toast.LENGTH_SHORT).show();
             return;
         }
         // here if everything ok the user will be register
         progressDialog.setMessage("Registering User, please wait...");
         progressDialog.show();
         mAuth.createUserWithEmailAndPassword(email, pass_word)
                 .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             //show user profile
                             Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                             firebaseUser = mAuth.getCurrentUser();
                             DatabaseReference userData = forUsers.child(firebaseUser.getUid()).child("userInfo");
                             String userDesc = "Yesterday is history, tomorrow's a mystery, today is a gift, that's why we call it 'present'";
                             String personName = user_name;
                             String personPhoto = "https://firebasestorage.googleapis.com/v0/b/heartful-dc3ac.appspot.com/o/profilepic.png?alt=media&token=5b98dc2e-1e36-4eb8-86e9-54d10222120e";
                             Users user = new Users(personName, personPhoto,  userDesc,studentGrade);
                             userData.setValue(user);

                             Intent intent = new Intent(RegisterActivity.this, ChooseClass.class);
                             intent.putExtra("class_name",studentGrade);
                             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                             startActivity(intent);
                             progressDialog.dismiss();
                         } else {
                             Toast.makeText(RegisterActivity.this, "could not register, pls try again Error is" + task.getException(), Toast.LENGTH_LONG).show();
                             progressDialog.dismiss();
                         }
                     }
                 });


     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else {
            progress.dismiss();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {



        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Log.v("signed","SUCESS");
            AuthWithGoogle(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Log.v("signed","signInResult:failed code=" + e.getStatusCode());
            progress.dismiss();


        }

    }

    private void AuthWithGoogle(GoogleSignInAccount account) {


        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String EASYNOTES_USER ="GoogleUser";

                               Log.d(TAG, "signInWithCredential:success");
                            firebaseUser = mAuth.getCurrentUser();
                            final DatabaseReference userData = forUsers.child(firebaseUser.getUid()).child("userInfo");
                            Log.v("GoogleSign","chla");
                            SharedPreferences pref = getApplicationContext().getSharedPreferences(EASYNOTES_USER, Activity.MODE_PRIVATE);
                            if (!pref.contains(EASYNOTES_USER)) {
                                Log.v("chal","google");
                                String userDesc ="Yesterday is history, tomorrow's a mystery, today is a gift, that's why we call it 'present'";
                                String personName = firebaseUser.getDisplayName();
                                Uri personPhoto = firebaseUser.getPhotoUrl();
                                Users user = new Users(personName, personPhoto.toString(),userDesc,studentGrade);
                                userData.setValue(user);
                                pref.edit().putBoolean(EASYNOTES_USER, true).apply();
                            }


                            Log.v(TAG, "signInWithCredential:success");

                            progress.dismiss();
                            Intent intent = new Intent(RegisterActivity.this, MainDrawerActivity.class);
                            intent.putExtra("class_name",studentGrade);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                           // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });







    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
         studentGrade = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // getAppDirectory(DownloadTask.this);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }


    }

}