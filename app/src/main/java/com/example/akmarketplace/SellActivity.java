package com.example.akmarketplace;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.security.Permission;

public class SellActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_Browse2, btn_Sell2, btn_Profile2, btn_Location, btn_Image;
    ImageView img_itemDisplay;
    Item newItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        btn_Browse2 = findViewById(R.id.btn_Browse2);
        btn_Sell2 = findViewById(R.id.btn_Sell2);
        btn_Profile2 = findViewById(R.id.btn_Profile2);
        btn_Image = findViewById(R.id.btn_Image);
        btn_Location = findViewById(R.id.btn_Location);
        img_itemDisplay = findViewById(R.id.img_itemDisplay);

        btn_Browse2.setOnClickListener(this);
        btn_Profile2.setOnClickListener(this);
        btn_Image.setOnClickListener(this);
        btn_Location.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED);
        ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, 102);

        newItem = new Item();

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Location) {

        }
        else if (v.getId() == R.id.btn_Image) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 101);

        }
        else if (v.getId() == R.id.btn_Browse2) {
            Intent browseIntent = new Intent(getApplicationContext(), BrowseActivity.class);
            startActivity(browseIntent);
            finish();
        }
        else if (v.getId() == R.id.btn_Profile2) {
            Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(profileIntent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101) {
            Bitmap picture = data.getParcelableExtra("data");
            img_itemDisplay.setImageBitmap(picture);
        }
    }
}