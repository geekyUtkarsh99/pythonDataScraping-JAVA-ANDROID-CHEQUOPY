package com.example.playerstats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity {


    private String email,password;
    FirebaseAuth auth;
    private Button signUp,signIn;
    private EditText emailText,passText;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       auth = FirebaseAuth.getInstance();
       db = FirebaseDatabase.getInstance();

       signUp = (Button)findViewById(R.id.signupButton);
       signIn = (Button)findViewById(R.id.signIn);
       emailText = (EditText)findViewById(R.id.emailText);
       passText = (EditText)findViewById(R.id.passwordText);

       signUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              startActivity(new Intent(login.this,createAccount.class));
              finish();
           }
       });

       signIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               email = emailText.getText().toString().trim();
               password = passText.getText().toString().trim();
               if(emailText.length()<=0)
                   emailText.setError("please enter valid email address");
               if(passText.length()<=0)
                   passText.setError("please enter correct password");

                   if(emailText.length()>0 && passText.length()>0)
                   auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {

                           if (task.isSuccessful()) {
                               Toast.makeText(login.this, "sign in successful !", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(login.this, MainActivity.class));
                               finish();
                           } else
                               Toast.makeText(login.this, "failed to sign in !", Toast.LENGTH_SHORT).show();

                       }
                   });


           }
       });



    }
}
