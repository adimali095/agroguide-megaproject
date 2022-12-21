package com.example.recycler;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    View callSignUp;
    ImageView ivFarmer;
    TextView logoText;
    TextView logoTagline;
    View inputBoxes;
    Button btnGo;
    TextInputLayout username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        ivFarmer = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_text);
        logoTagline = findViewById(R.id.slogan_name);
        inputBoxes = findViewById(R.id.input_boxes);
        callSignUp = findViewById(R.id.callsignup);
        btnGo = findViewById(R.id.btnGo);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(view);
            }
        });

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSignUpScreen(view);
            }
        });
    }


    private boolean validateUsername() {
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(View view) {
        if (!validateUsername() | !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    private void isUser() {
        String userEnteredUsername = username.getEditText().getText().toString().trim();
        String userEnteredPassword = password.getEditText().getText().toString().trim();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);
                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userEnteredPassword)) {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String phoneNoFromDB = snapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                        String nameFromDB = snapshot.child(userEnteredUsername).child("name").getValue(String.class);


                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.putExtra("fullname", nameFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("password", passwordFromDB);
                        intent.putExtra("phoneNo", phoneNoFromDB);

                        startActivity(intent);


                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        password.setError("Wrong password");
                        password.requestFocus();
                    }
                } else {
                    username.setError("Username does not exist");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void callSignUpScreen(View view) {
        Intent intent = new Intent(Login.this, SIgnUp.class);
        Pair[] pairs = new Pair[4]; //new Pair[6];
        pairs[0] = new Pair<View, String>(ivFarmer, "logo_image");
        pairs[1] = new Pair<View, String>(logoText, "logo_text");
        pairs[2] = new Pair<View, String>(logoTagline, "logo_tagline");
        pairs[3] = new Pair<View, String>(inputBoxes, "input_boxes");
        //pairs[4] = new Pair<View,String>(btnGo, "btnGo");
        //pairs[5] = new Pair<View,String>(callSignUp, "call");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
        startActivity(intent, options.toBundle());
    }
}