package com.example.recycler;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class SIgnUp extends AppCompatActivity {
    private TextInputLayout regName, regUsername, regEmail, regPhoneNo, regPassword;
    private Button btnReg, btnRegToLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        //hooks
        regName = (TextInputLayout) findViewById(R.id.reg_name);
        regUsername = (TextInputLayout) findViewById(R.id.reg_username);
        regEmail = (TextInputLayout) findViewById(R.id.reg_email);
        regPhoneNo = (TextInputLayout) findViewById(R.id.reg_phone_no);
        regPassword = (TextInputLayout) findViewById(R.id.reg_password);

        btnReg = findViewById(R.id.btn_reg);
        btnRegToLogin = findViewById(R.id.btn_reg_to_login);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registerUser(view)) {
                    Toast.makeText(SIgnUp.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SIgnUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validateName() {
        String val = regName.getEditText().getText().toString();
        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUsername() {
        String val = regUsername.getEditText().getText().toString();
        final String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() <= 6) {
            regUsername.setError("Username too short");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            regUsername.setError("Username should not contain whitespaces");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhoneNo() {
        String val = regPhoneNo.getEditText().getText().toString();
        if (val.isEmpty()) {
            regPhoneNo.setError("Field cannot be empty");
            return false;
        } else {
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (val.length() <= 6) {
            regPassword.setError("Password too short");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    public boolean registerUser(View view) {
        if (!validateName() | !validateUsername() | !validateEmail() | !validatePhoneNo() | !validatePassword()) {
            return false;
        }
        String name = regName.getEditText().getText().toString();
        String username = regUsername.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String phoneNo = regPhoneNo.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();

        UserHelperClass user = new UserHelperClass(name, username, email, phoneNo, password);
        DatabaseHelperClass db = new DatabaseHelperClass();
        db.addUser(user);
        return true;
    }
}