package com.example.recycler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.ToDoActivity;
import com.google.android.material.textfield.TextInputEditText;

public class UserProfile extends AppCompatActivity
{
    TextView titleName, titleUname;
    TextInputEditText username,email,phoneNo,fullname;
    RelativeLayout profileDashboardView, profileToDoView;
    Button logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //hooks
        username = findViewById(R.id.log_username);
        email = findViewById(R.id.log_email);
        phoneNo = findViewById(R.id.log_phone_no);
        fullname = findViewById(R.id.log_fullname);
        titleUname = findViewById(R.id.title_user_name);
        titleName = findViewById(R.id.title_full_name);

        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, Login.class);
                startActivity(intent);
            }
        });

        profileDashboardView = findViewById(R.id.profile_dashboard_view);
        profileDashboardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profileToDoView = findViewById(R.id.profile_todo_view);
        profileToDoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, ToDoActivity.class);
                startActivity(intent);
            }
        });

        showAllUserData();
    }


    public void showAllUserData()
    {

        String logUsername = getIntent().getStringExtra("username");
        String logEmail = getIntent().getStringExtra("email");
        String logPhoneNo = getIntent().getStringExtra("phoneNo");
        String logFullName = getIntent().getStringExtra("fullname");
        //setting values
        username.setText(logUsername);
        email.setText(logEmail);
        phoneNo.setText(logPhoneNo);
        fullname.setText(logFullName);
        titleUname.setText(logUsername);
        titleName.setText(logFullName);
    }
}