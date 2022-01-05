package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    private Button enter;
    private TextView error,success;
    private FirebaseDatabase database;
    private DatabaseReference ref, reqRef;
 private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        code = findViewById(R.id.code);
        enter = findViewById(R.id.button2);
        error = findViewById(R.id.textView6);
       success=findViewById(R.id.textView7);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("Users");
        reqRef = database.getReference();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(trackerActivity.this, MapsActivity2.class);
                String currentUsername = code.getText().toString();

            //    startActivity(intent);
                auth= FirebaseAuth.getInstance();
                String usersId= Objects.requireNonNull(auth.getCurrentUser()).getUid();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot12 : snapshot.getChildren()) {
                            Users users = new Users();
                            users = snapshot12.getValue(Users.class);
                            String databaseUsers = users.getUsername();
                            Log.d("ed", currentUsername);
                            Log.d("my", "sds" + databaseUsers);
                            if (!currentUsername.equals(databaseUsers)) {
                                error.setText("User Doesn't exists ");
                            } else {
                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c);
                                reqRef.child("Requests").child(usersId).child("userName").setValue(currentUsername);
                                reqRef.child("Requests").child(currentUsername).child("userName").setValue(usersId);
                                error.setText(" ");
                                success.setText("Request Sent ");
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

