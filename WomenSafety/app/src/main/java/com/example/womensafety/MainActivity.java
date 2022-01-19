package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafety.model.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
private Button tracker,host,logOut,frendReqs,botBtn;
private TextView random,sharedUsername;
private FirebaseAuth auth;

private FirebaseDatabase database;
private DatabaseReference ref;

    public static  final String shared_Pref="Shared";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tracker=findViewById(R.id.tracker);
        host=findViewById(R.id.host);
        logOut=findViewById(R.id.button);
        random=findViewById(R.id.textView8);
       database=FirebaseDatabase.getInstance();
       frendReqs=findViewById(R.id.frendReqs);
       sharedUsername=findViewById(R.id.textView);
       botBtn=findViewById(R.id.botBtn);

        GoogleSignInAccount account = null;
//      String userFromGooglr=  account.getId();
frendReqs.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(MainActivity.this,FriendRequests.class);
        startActivity(intent);
    }
});

botBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(MainActivity.this,chatActivity.class);
        startActivity(intent);
    }
});
//        String userName= getIntent().getStringExtra("userName");
//        String userNameFromGogle=getIntent().getStringExtra("currentUsername");
//if(TextUtils.isEmpty(userName))
//{
//    random.setText(userNameFromGogle);
//}
//else {
//    random.setText(userName);
//}
//
//        auth= FirebaseAuth.getInstance();
//        String usersId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String usersId= currentFirebaseUser.getUid();
        ref= database.getReference().child("Users").child(usersId);
ref.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Users users=new Users();
        if(snapshot.exists()){
            users= snapshot.getValue(Users.class);
            String sdd= users.getUsername();
            random.setText("Welcome! "+sdd);

            //shRED PREF
            SharedPreferences sharedPreferences=getSharedPreferences("shared_Pref",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("userName",sdd);
            editor.commit();

        }
        else {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            String userssss=pref.getString("displayName"," ");
            random.setText(userssss);
        }



    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent =new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });
        tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,trackerActivity.class);
                startActivity(intent);
            }
        });
    }
}