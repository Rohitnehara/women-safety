package com.example.womensafety;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafety.model.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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

public class SignUp extends AppCompatActivity {
    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference ref;
    ProgressDialog progressDialog;
    GoogleSignInClient googleSignInClient;
    private EditText username,email,password;
    private Button signUp,googleSignUp,phone;
    private TextView already;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        ref=database.getReference().child("UserNames");

//        updateUI(currentUser);
        if (auth.getCurrentUser() != null) {
            Intent intt = new Intent(SignUp.this, MainActivity.class);
            startActivity(intt);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //add this  line
        // binding = ActivityIntroBinding.inflate(getLayoutInflater());
        //this  is  the original line for code which you will need to change
        //setContentView(R.layout.activity_sign_up_acitvity);
        //make  these changes in this line and use it
        //this line is for removing top bar
//        getSupportActionBar().hide();
        //creating intense for the auth and database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        username=findViewById(R.id.usernameSignUp);
        email=findViewById(R.id.EmailSignUp);
        password=findViewById(R.id.EmailSignUp);

        signUp=findViewById(R.id.SignIn);
        googleSignUp=findViewById(R.id.googleSignIn);

        already=findViewById(R.id.AlreadyAccount);
phone=findViewById(R.id.phone);
phone.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
Intent intent=new Intent(SignUp.this,phoneLogIn.class);
startActivity(intent);
    }
});

        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setTitle("Creating account");
        progressDialog.setMessage("we're creating youre account");

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = (GoogleSignInClient) GoogleSignIn.getClient(this, gso);
//        Animation animatorInflater=Animation.AnimationListener
//        binding.SignUP.startAnimation(animatorInflater);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()
                        && !username.getText().toString().isEmpty()) {

                    ref = database.getReference().child("UserNames");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.child(username.getText().toString()).exists()) {
                                        Toast.makeText(getApplicationContext(), "User name alread exists", Toast.LENGTH_SHORT).show();
                                    }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    progressDialog.show();
                    auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task1) {
                            // now check for if task is  complete or not
                            progressDialog.dismiss();
                            if (task1.isSuccessful()) {
                               Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful())
                                       {
                                           Users user = new Users(username.getText().toString(), email.getText().toString(), password.getText().toString());
                                           //here we take result from fire base
                                           String id = Objects.requireNonNull(task1.getResult().getUser()).getUid();
                                           //this code writes result from firebase to the database
                                           Date c = Calendar.getInstance().getTime();
                                           System.out.println("Current time => " + c);

                                           SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                           String formattedDate = df.format(c);
                                           ref.child(username.getText().toString()).setValue(formattedDate);

                                           //here child("users") creates a child named as user
                                           database.getReference().child("Users").child(id).setValue(user);
                                           SharedPreferences prefEmail = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                           SharedPreferences.Editor editor = prefEmail.edit();
                                           editor.putString("UserEmail",task1.getResult().getUser().getUid());
                                           editor.apply();
                                           Intent intent=new Intent(SignUp.this,MainActivity.class);
                                           startActivity(intent);

                                       }
                                    else {
                                           Toast.makeText(getApplicationContext(), "Check your mail for  verification", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });

                                // Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                //this line
                                // task.getException().getMessage()
                                //show default error msgs of firebase
                                Toast.makeText(getApplicationContext(), task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Empty credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //code already have and account section
        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LogInent = new Intent(SignUp.this,Log_in_page.class);
                startActivity(LogInent);
            }
        });


        ///Code for signin user using google
        googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();

            }
        });

        ///this line keeps  user loged in

    }

    int RC_SIGN_IN = 69;

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                ///
                auth=FirebaseAuth.getInstance();
                String ususd=auth.getUid();
                Users users = new Users();
                users.setUserId(account.getId());
                users.setUsername(account.getDisplayName());
                users.setProfilePic(account.getPhotoUrl().toString());
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("displayName",account.getDisplayName());
                editor.putString("userId",account.getId());
                editor.apply();

                ///
                database.getReference().child("Users").child(account.getId()).setValue(users);

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                ref.child(account.getDisplayName()).setValue(formattedDate);
                ///

                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());

//
                Intent intent = new Intent(SignUp.this, MainActivity.class);

                startActivity(intent);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            Intent intent = new Intent(SignUp.this,MainActivity.class);
                            startActivity(intent);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //  updateUI(null);
                        }
                    }
                });

    }
}