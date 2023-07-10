package com.example.akmarketplace;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_Browse2, btn_Sell2, btn_Profile2, btn_Location, btn_Image, btn_DeleteItem;
    private ImageView img_itemDisplay, img_itemImage, img_Default;
    private TextView tv_Title;
    private LatLng loc_meetupLocation;
    //private Item newItem;
    private Toolbar toolbar2;
    private String targetEmail, targetFullname, targetPhone;
    private EditText et_Title, et_Description, et_Price;
    private Uri imageUri;

    private double locationLat, locationLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell2);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED);
        ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, 102);

        tv_Title = findViewById(R.id.tv_Title);
        tv_Title.setText("Edit an Item");

        btn_Image = findViewById(R.id.btn_Image);
        btn_Location = findViewById(R.id.btn_Location);
        btn_DeleteItem = findViewById(R.id.btn_DeleteItem);
        img_itemDisplay = findViewById(R.id.img_itemDisplay);
        img_Default = new ImageView(getApplicationContext());
        img_Default.setImageDrawable(img_itemDisplay.getDrawable());
        //img_itemImage = null;
        loc_meetupLocation = null;
        et_Title = findViewById(R.id.et_Title);
        et_Description = findViewById(R.id.et_Description);
        et_Price = findViewById(R.id.et_Price);

        //btn_Browse2.setOnClickListener(this);
        //btn_Profile2.setOnClickListener(this);
        btn_Image.setOnClickListener(this);
        btn_Location.setOnClickListener(this);
        btn_DeleteItem.setOnClickListener(this);

        toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        Intent intent = getIntent();
        et_Title.setText(intent.getStringExtra("title"));
        et_Description.setText(intent.getStringExtra("description"));
        et_Price.setText(intent.getDoubleExtra("price", 0) + "");
        locationLat = intent.getDoubleExtra("locationLat", 25.310338125326922);
        locationLng = intent.getDoubleExtra("locationLng", 55.491244819864185);
        img_itemDisplay.setImageURI(Uri.parse(intent.getStringExtra("imageUri")));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_Location)
        {
            Intent mapsIntent = new Intent(getApplicationContext(), SetLocActivity.class);
            mapsIntent.putExtra("locationLat", locationLat);
            mapsIntent.putExtra("locationLng", locationLng);
            startActivityForResult(mapsIntent,201);
        }
        else if(v.getId() == R.id.btn_Image)
        {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 101);
        }
        if(v.getId() == R.id.btn_ConfirmEdit)
        {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101) {
            Bitmap pic = data.getParcelableExtra("data");
            imageUri = getImageUri(getApplicationContext(), pic);
            img_itemDisplay.setImageURI(imageUri);
            //imageUri = picture;
            img_itemImage = img_itemDisplay;
            Toast.makeText(getApplicationContext(),imageUri.toString(),Toast.LENGTH_SHORT).show();
        }
        else if (requestCode==201) {
            Double locLat = data.getDoubleExtra("replyLat",0);
            Double locLng = data.getDoubleExtra("replyLng",0);
            loc_meetupLocation = new LatLng(locLat,locLng);
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}