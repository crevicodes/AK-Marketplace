package com.example.akmarketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {


    private Button btn_Browse3;
    private Button btn_Sell3;
    private Button btn_Profile3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}