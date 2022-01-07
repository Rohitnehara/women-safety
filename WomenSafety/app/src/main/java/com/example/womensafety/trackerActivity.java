package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafety.model.Users;
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
import java.util.Objects;

public class trackerActivity extends AppCompatActivity {
    private EditText code;
    private Button enter,connect;
    private TextView error,success;
    private FirebaseDatabase database;
    private DatabaseReference ref, reqRef,ref2,frndsRef;
 private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        code = findViewById(R.id.code);
        enter = findViewById(R.id.button2);
        error = findViewById(R.id.textView6);
       success=findViewById(R.id.textView7);
       connect=findViewById(R.id.button4);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("UserNames");
        reqRef = database.getReference();
        SharedPreferences sharedPreferences=getSharedPreferences("shared_Pref",MODE_PRIVATE);
        String userFeromshare=sharedPreferences.getString("userName","nahi chala");
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(trackerActivity.this, MapsActivity2.class);
                String currentUsername = code.getText().toString();

                SharedPreferences preferencesOfuser= getApplicationContext().getSharedPreferences("Friends userName",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferencesOfuser.edit();
                editor.putString("UserNameFrnd",currentUsername);
                editor.apply();


            //    startActivity(intent);
                auth= FirebaseAuth.getInstance();



                String usersId= Objects.requireNonNull(auth.getCurrentUser()).getUid();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.child(currentUsername).exists()) {
                                error.setText("User Doesn't exists ");
                            } else {
                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c);
                                reqRef.child("Requests Received").child(currentUsername).child(userFeromshare).setValue(formattedDate);
                                reqRef.child("Requests Sent").child(userFeromshare).child(currentUsername).setValue(formattedDate);
                                error.setText(" ");
                                success.setText("Request Sent ");
                            }

                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
connect.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        SharedPreferences preferencesOfuser= getApplicationContext().getSharedPreferences("Friends userName",MODE_PRIVATE);
        String frnd=  preferencesOfuser.getString("UserNameFrnd",null);

        frndsRef=database.getReference().child("Friends");

        frndsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(frnd).child(userFeromshare).exists()){
                    Intent intent =new Intent(trackerActivity.this,MapsActivity2.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Friend Pending ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
});




//        if(TextUtils.isEmpty(frnd))
//        {
//            Toast.makeText(getApplicationContext(), "Friend Req pending", Toast.LENGTH_SHORT).show();
//        }
//        frndsRef.child(frnd).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        }) ;
    }
}

