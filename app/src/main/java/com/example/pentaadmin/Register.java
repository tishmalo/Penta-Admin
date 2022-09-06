package com.example.pentaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText username,email, password, cpassword;
    Button submit;
    TextView already;

    FirebaseAuth mAuth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        cpassword=findViewById(R.id.cpassword);
        already=findViewById(R.id.already);
        submit=findViewById(R.id.submit);
        mAuth=FirebaseAuth.getInstance();
        RegisterUser();

        goToLogin();




    }

    private void goToLogin() {

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

    }

    private void RegisterUser() {

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String USERNAME=username.getText().toString();
                final String EMAIL=email.getText().toString();
                final String PASSWORD=password.getText().toString();
                final String CPASSWORD=cpassword.getText().toString();

                if(TextUtils.isEmpty(USERNAME)){
                    username.setError("Enter Username");
                }if(!Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()){
                    email.setError("Enter Valid Email");
                }if(TextUtils.isEmpty(PASSWORD)){
                    password.setError("Enter password");
                }if(TextUtils.isEmpty(CPASSWORD)){
                    cpassword.setError("Passwords don't match");
                }if(PASSWORD.length()<6){
                    password.setError("Password should be longer than six");
                }if(!PASSWORD.equals(CPASSWORD)){
                    cpassword.setError("passwords don't match");
                }else{

                    mAuth.createUserWithEmailAndPassword(EMAIL,PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()){
                                Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }else{

                                Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(Register.this, Home.class);
                                startActivity(intent);

                                finish();

                            }




                        }
                    });

                }




            }
        });

    }

}