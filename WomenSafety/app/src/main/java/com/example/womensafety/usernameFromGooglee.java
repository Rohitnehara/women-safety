package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafety.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class usernameFromGooglee extends AppCompatActivity {
private EditText userName;
private Button enter;
private FirebaseDatabase database;
private DatabaseReference ref;
private TextView  error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_from_googlee);
        userName =findViewById(R.id.username);
        enter=findViewById(R.id.button3);
        database=FirebaseDatabase.getInstance();
        ref=database.getReference().child("Users");
        error=findViewById(R.id.textView4);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUsername=userName.getText().toString();
//                Intent intent =new Intent(usernameFromGooglee.this,MainActivity.class);
//                intent.putExtra("currentUsername",currentUsername);
//                startActivity(intent);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot12:snapshot.getChildren())
                        {
                            Users users=new Users();
                            users=snapshot12.getValue(Users.class);
                            String databaseUsers= users.getUsername();
                            Log.d("ed",currentUsername);
                            Log.d("my","sds"+databaseUsers);
                            if(!currentUsername.equals(databaseUsers))
                            {
                                Intent intent =new Intent(usernameFromGooglee.this,MainActivity.class);
                intent.putExtra("currentUsername",currentUsername);
                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "sfhdb", Toast.LENGTH_SHORT).show();
                               break;
                            }
                            else {
                                error.setText("User Already exists");
                            }
                            break;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}