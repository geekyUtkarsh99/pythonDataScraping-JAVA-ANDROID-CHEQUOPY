package com.example.playerstats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.Objects;

public class createAccount extends AppCompatActivity {

    private CheckBox cb;
    private EditText fName,lName,phoneNum,email,password,confirmPass;
    private Button login,createAccount;
    private String emailAddr,pass;
    private boolean checkExistance =false;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference dataSnap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        findViews();
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        dataSnap = db.getReference();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String passPatern ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!(email.getText().toString().trim().matches(emailPattern)))
                    email.setError("Invalid email.Please try again");

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!(password.getText().toString().trim().matches(passPatern)))
                    password.setError("enter valid password");

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(createAccount.this,login.class));
                finish();

            }
        });

        createAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                emailAddr = email.getText().toString().trim();
                pass = password.getText().toString().trim();

                if(fName.length()<=0)
                    fName.setError("field cannot be ignored");
                if(lName.length()<= 0)
                    lName.setError("field cannot be ignored");
                if(phoneNum.length()<=9)
                    phoneNum.setError("enter valid phone number");
                if(!(confirmPass.getText().toString().trim().equals(password.getText().toString().trim())))
                    confirmPass.setError("check your password");
                if(!cb.isChecked())
                    cb.setError("please agree to terms and conditions");



                if(emailAddr.matches(emailPattern)&&pass.matches(passPatern) &&
                        !(fName.length()<=0)&&!(lName.length()<=0)&&!(phoneNum.length()<=9)&&
                        (confirmPass.getText().toString().trim().equals(password.getText().toString().trim()))&&cb.isChecked())
                 auth.createUserWithEmailAndPassword(emailAddr, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {


                    dataSnap.child("uid").setValue(Objects.requireNonNull(auth.getCurrentUser()).getUid().toString().trim());
                    dataSnap.child("uid").child(Objects.requireNonNull(auth.getCurrentUser()).getUid().toString().trim())
                            .child("name").setValue(fName.getText().toString().trim()+" "+lName.getText().toString().trim());
                    dataSnap.child("uid").child(Objects.requireNonNull(auth.getCurrentUser()).getUid().toString().trim()).
                            child("phoneNumber").setValue(phoneNum.getText().toString().trim());
                    dataSnap.child("uid").child(Objects.requireNonNull(auth.getCurrentUser()).getUid().toString().trim()).child("email").setValue(emailAddr);
                    dataSnap.child("uid").child(Objects.requireNonNull(auth.getCurrentUser()).getUid().toString().trim()).child("password").setValue(pass);


                startActivity(new Intent(createAccount.this, login.class));
                finish();
                } else Toast.makeText(createAccount.this,
                    "failed to create account. please enter correct details", Toast.LENGTH_SHORT).show();

                if(!task.isSuccessful()){
                    try{
                        throw Objects.requireNonNull(task.getException());
                    }catch(FirebaseAuthUserCollisionException e){

                        Toast.makeText(createAccount.getContext(),"email address already exists!",Toast.LENGTH_SHORT).show();;
                        email.setError("email already exists");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


        }



    });


            }
        });



    }


    private void findViews(){

       fName = (EditText)findViewById(R.id.fName);
        lName = (EditText)findViewById(R.id.lName);
        phoneNum = (EditText)findViewById(R.id.phoneNum);
        password = (EditText)findViewById(R.id.password);
       email = (EditText)findViewById(R.id.email);
        fName = (EditText)findViewById(R.id.fName);
        createAccount = (Button)findViewById(R.id.createAccount);
        login = (Button)findViewById(R.id.login);
        cb = (CheckBox)findViewById(R.id.checkBox);
        confirmPass = (EditText)findViewById(R.id.confirmPass);

    }

}
