package com.kinitoapps.quicknotes;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kinitoapps.quicknotes.R;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog=new ProgressDialog(this);
        firebaseAuth= FirebaseAuth.getInstance();
       // skipButton = findViewById(R.id.btnSkip);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnLinkToRegister = findViewById(R.id.btnLinkToRegisterScreen);
//        skipButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this,ChooseClass.class));
//                finish();
//            }
//        });
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_login();
            }
        });
    }




    private void user_login(){
        firebaseAuth= FirebaseAuth.getInstance();
        String email=inputEmail.getText().toString().trim();
        String pass_word=inputPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            // email is empty
            Toast.makeText(this,"please enter email",Toast.LENGTH_SHORT).show();
            return;// to stop the function from executation.
        }
        if(TextUtils.isEmpty(pass_word)){
            // email is empty
            Toast.makeText(this,"please enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        // here if everything ok the user will be register
        progressDialog.setMessage("Signing,please wait...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,pass_word)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // start user profile activity
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainDrawerActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(LoginActivity.this," "+ task.getException(),Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }
}
