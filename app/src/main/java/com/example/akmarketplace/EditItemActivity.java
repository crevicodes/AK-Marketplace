package com.example.akmarketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_Browse2, btn_Sell2, btn_Profile2, btn_Location, btn_Image, btn_EnlistItem;
    private ImageView img_itemDisplay, img_itemImage, img_Default;
    private TextView tv_Title;
    private LatLng loc_meetupLocation;
    //private Item newItem;
    private Toolbar toolbar2;
    private String targetEmail, targetFullname, targetPhone;
    private EditText et_Title, et_Description, et_Price;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        btn_Browse2 = findViewById(R.id.btn_Browse2);
        btn_Sell2 = findViewById(R.id.btn_Sell2);
        btn_Profile2 = findViewById(R.id.btn_Profile2);

        btn_Browse2.setVisibility(View.GONE);
        btn_Sell2.setVisibility(View.GONE);
        btn_Profile2.setVisibility(View.GONE);

        tv_Title = findViewById(R.id.tv_Title);
        tv_Title.setText("Edit an Item");

        btn_Image = findViewById(R.id.btn_Image);
        btn_Location = findViewById(R.id.btn_Location);
        btn_EnlistItem = findViewById(R.id.btn_EnlistItem);
        img_itemDisplay = findViewById(R.id.img_itemDisplay);
        img_Default = new ImageView(getApplicationContext());
        img_Default.setImageDrawable(img_itemDisplay.getDrawable());
        img_itemImage = null;
        loc_meetupLocation = null;
        et_Title = findViewById(R.id.et_Title);
        et_Description = findViewById(R.id.et_Description);
        et_Price = findViewById(R.id.et_Price);

        //btn_Browse2.setOnClickListener(this);
        //btn_Profile2.setOnClickListener(this);
        btn_Image.setOnClickListener(this);
        btn_Location.setOnClickListener(this);
        btn_EnlistItem.setOnClickListener(this);

        toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        Intent intent = getIntent();
        et_Title.setText(intent.getStringExtra("title"));
        et_Description.setText(intent.getStringExtra("description"));
        et_Price.setText(intent.getDoubleExtra("price", 0) + "");
        double locationLat = intent.getDoubleExtra("locationLat", 25.310338125326922);
        double locationLng = intent.getDoubleExtra("locationLng", 55.491244819864185);
        loc_meetupLocation = new LatLng(locationLat, locationLng);

    }

    @Override
    public void onClick(View v) {


    }
}