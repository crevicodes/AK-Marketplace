package com.example.akmarketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_welcometitle;

    private String fullname, phone, email;
    private Button btn_Browse3;
    private Button btn_Sell3;
    private Button btn_Profile3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address etc
            fullname = user.getDisplayName();
            email = user.getEmail();

            tv_welcometitle.setText("Welcome, " + fullname);
        }

        btn_Browse3 = findViewById(R.id.btn_Browse3);
        btn_Sell3 = findViewById(R.id.btn_Sell3);
        btn_Profile3 = findViewById(R.id.btn_Profile3);

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_Browse3)
        {

        }
        else if(v.getId() == R.id.btn_Sell3)
        {

        }
        else if(v.getId() == R.id.btn_Profile3)
        {
            Intent profile = new Intent(this, ProfileActivity.class);
            startActivity(profile);
        }
    }
}