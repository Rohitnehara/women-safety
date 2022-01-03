package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.womensafety.model.CountryData;

public class phoneLogIn extends AppCompatActivity {
    private Spinner spinner;
    private EditText editText;
    private Button continurrBTn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_log_in);
        spinner=findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String >(this,R.layout.support_simple_spinner_dropdown_item, CountryData.countryNames ));
        editText = findViewById(R.id.editTextPhone);
       continurrBTn=findViewById(R.id.buttonContinue);
       continurrBTn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

               String number = editText.getText().toString().trim();

               if (number.isEmpty() || number.length() < 10) {
                   editText.setError("Valid number is required");
                   editText.requestFocus();
                   return;
               }

               String phoneNumber = "+" + code + number;

               Intent intent = new Intent(phoneLogIn.this, VerifyPhoneActivity.class);
               intent.putExtra("phonenumber", phoneNumber);
               startActivity(intent);

           }
       });
    }
}