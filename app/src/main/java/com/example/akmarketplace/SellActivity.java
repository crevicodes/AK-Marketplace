package com.example.akmarketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class SellActivity extends AppCompatActivity {

    private Button btn_Browse2;
    private Button btn_Sell2;
    private Button btn_Profile2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        btn_Browse2 = findViewById(R.id.btn_Browse2);
        btn_Sell2 = findViewById(R.id.btn_Sell2);
        btn_Profile2 = findViewById(R.id.btn_Profile2);

    }




}