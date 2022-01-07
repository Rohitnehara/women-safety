package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafety.model.reqModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FriendRequests extends AppCompatActivity {
    private FirebaseAuth auth;
  private TextView textView,Noreq;
    private FirebaseDatabase database;
    private DatabaseReference ref,userNameRef,acceptedRef;
    private Button accept,decline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
 textView=findViewById(R.id.textView11);
 Noreq=findViewById(R.id.textView10);
 accept=findViewById(R.id.Accept);
 decline=findViewById(R.id.decline);
acceptedRef=database.getReference().child("Friends");
        SharedPreferences sharedPreferences=getSharedPreferences("shared_Pref",MODE_PRIVATE);
        String userFeromshare=sharedPreferences.getString("userName","nahi chala");

        ref=database.getReference().child("Requests Received").child(userFeromshare);
        Log.d("sda","asd"+userFeromshare);

        userNameRef=database.getReference().child("Requests Sent");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.exists()) {
//
                        String reqM =snapshot1.getKey();
                        textView.setText(reqM);

                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Date c = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + c);

                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c);

                                acceptedRef.child(userFeromshare).child(reqM).setValue(formattedDate);
                                acceptedRef.child(reqM).child(userFeromshare).setValue(formattedDate);
                                snapshot1.getRef().removeValue();
                                userNameRef.child(reqM).removeValue();

                            }
                        });
                        decline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snapshot1.getRef().removeValue();
                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "need more work", Toast.LENGTH_SHORT).show();
                        Noreq.setText("No requests");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}