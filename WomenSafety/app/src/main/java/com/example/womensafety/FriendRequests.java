package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafety.model.reqModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendRequests extends AppCompatActivity {
    private FirebaseAuth auth;
  private TextView textView;
    private FirebaseDatabase database;
    private DatabaseReference ref,userNameRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
 textView=findViewById(R.id.textView11);

        SharedPreferences sharedPreferences=getSharedPreferences("shared_Pref",MODE_PRIVATE);
        String userFeromshare=sharedPreferences.getString("userName","nahi chala");

        ref=database.getReference().child("Requests").child(userFeromshare);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.exists()) {
//                        reqModel reqM = new reqModel();
//                        reqM = snapshot1.getValue(reqModel.class);
                        String reqM =snapshot1.getValue().toString();
                        textView.setText(reqM);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "need more work", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}