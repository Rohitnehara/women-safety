package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Log_in_page extends AppCompatActivity {
    private TextView NewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);
        NewUser=findViewById(R.id.NewUser);
        NewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Log_in_page.this,SignUp.class);
                startActivity(intent);
            }
        });
    }
}