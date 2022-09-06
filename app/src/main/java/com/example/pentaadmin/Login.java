package com.example.pentaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText email, password;
    TextView forgot, already;
    Button login;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        forgot=findViewById(R.id.forgot);
        already=findViewById(R.id.already);
        login=findViewById(R.id.login);

        mAuth=FirebaseAuth.getInstance();

        LoginUser();
        goToRegister();
        checkifLoggedin();

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Login.this, Reset.class);
                startActivity(intent);

            }
        });


    }

    private void checkifLoggedin() {

        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user;
                user=FirebaseAuth.getInstance().getCurrentUser();

                if(user!=null){

                    Intent intent=new Intent(Login.this,Home.class);
                    startActivity(intent);

                }
            }
        };

    }

    private void goToRegister() {

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });



    }

    private void LoginUser() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String EMAIL=email.getText().toString();
                final String PASSWORD=password.getText().toString();

                if(TextUtils.isEmpty(EMAIL)){
                    email.setError("Enter Email");
                }if(TextUtils.isEmpty(PASSWORD)){
                    password.setError("Enter password");
                }else{

                    mAuth.signInWithEmailAndPassword(EMAIL,PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                Intent intent= new Intent(Login.this, Home.class);
                                startActivity(intent);

                                finish();


                            }

                        }
                    });

                }



            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthStateListener);

    }
}