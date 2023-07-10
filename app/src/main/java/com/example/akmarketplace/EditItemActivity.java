package com.example.akmarketplace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_Browse2, btn_Sell2, btn_Profile2, btn_Location, btn_Image, btn_DeleteItem, btn_ConfirmEdit;
    private ImageView img_itemDisplay, img_itemImage, img_Default;
    private TextView tv_Title;
    private LatLng loc_meetupLocation;
    //private Item newItem;
    private Toolbar toolbar2;
    private String targetEmail;
    private EditText et_Title, et_Description, et_Price;
    private Uri imageUri;
    private ArrayList<Item> items;
    private ArrayList<Item> filteredItems;

    private Item currentItem;
    private int itemPosition;

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
        btn_ConfirmEdit = findViewById(R.id.btn_ConfirmEdit);
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
        btn_ConfirmEdit.setOnClickListener(this);

        toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        Intent intent = getIntent();
        targetEmail = intent.getStringExtra("targetemail");
        itemPosition = intent.getIntExtra("itemposition", 0);


        items = new ArrayList<>();
        filteredItems = new ArrayList<>();

        updateDisplay(targetEmail);


        //img_itemDisplay.setImageURI(Uri.parse(intent.getStringExtra("imageUri")));
    }

    public void updateDisplay(String key)
    {
        items = new ArrayList<>();
        filteredItems = new ArrayList<>();

        BrowseActivity.db.collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        items.add(document.toObject(Item.class));
                    }

                    items.sort(Comparator.comparingLong(Item::getTime_added_millis));
                    Collections.reverse(items);

                    for (Item i : items) {
                        if (i.getSellerEmail().equals(key)) {

                            filteredItems.add(i);
                        }
                    }
                    currentItem = filteredItems.get(itemPosition);
                    imageUri = Uri.parse(currentItem.getImage());
                    Picasso.get().load(currentItem.getImage()).into(img_itemDisplay);
                    et_Title.setText(currentItem.getTitle());
                    et_Description.setText(currentItem.getDescription());
                    et_Price.setText(Double.toString(currentItem.getPrice()));
                    locationLat = currentItem.getLocationLat();
                    locationLng = currentItem.getLocationLng();
                }
            }
        });
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
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, 102);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 101);
                }
            }
            else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 101);
            }
        }
        else if(v.getId() == R.id.btn_ConfirmEdit)
        {
            if(verifyFields())
            {
                BrowseActivity.db.collection("items").document(Long.toString(currentItem.getTime_added_millis())).update("title", et_Title.getText().toString());
                BrowseActivity.db.collection("items").document(Long.toString(currentItem.getTime_added_millis())).update("description", et_Description.getText().toString());
                BrowseActivity.db.collection("items").document(Long.toString(currentItem.getTime_added_millis())).update("price", Double.parseDouble(et_Price.getText().toString()));

                //BrowseActivity.db.document(Long.toString(currentItem.getTime_added_millis())).update("image", imageUri.toString());
                BrowseActivity.db.collection("items").document(Long.toString(currentItem.getTime_added_millis())).update("locationLat", loc_meetupLocation.latitude);
                BrowseActivity.db.collection("items").document(Long.toString(currentItem.getTime_added_millis())).update("locationLng", loc_meetupLocation.longitude);

                StorageReference storeRef = BrowseActivity.storage.getReference().child("items/"+et_Title.getText().toString()+(et_Description.getText().toString().length()>7 ? et_Description.getText().toString().substring(0,7) : et_Description.getText().toString())+".jpg");

                UploadTask uploadTask = storeRef.putFile(imageUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                BrowseActivity.db.collection("items").document(Long.toString(currentItem.getTime_added_millis())).update("image", uri.toString());
                            }
                        });
                    }
                });
                //finish();
                Toast.makeText(this, "Confirm Edit", Toast.LENGTH_SHORT).show();
            }
            else if (v.getId() == R.id.btn_DeleteItem) {
                BrowseActivity.db.collection("items").document(Long.toString(currentItem.getTime_added_millis())).delete();
                finish();
            }

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

    public boolean verifyFields() {
        if (et_Title.getText().toString().replaceAll(" ", "").isEmpty() ||
                et_Description.getText().toString().replaceAll(" ", "").isEmpty()) {
            AlertDialog alertDialog = new AlertDialog.Builder(EditItemActivity.this).create();
            alertDialog.setTitle("Populate Fields");

            String message = "The following fields must be populated to enlist the item:\n\n";
            if (et_Title.getText().toString().replaceAll(" ", "").isEmpty()) {
                message = message.concat("Item Title\n");
            }
            if (et_Description.getText().toString().replaceAll(" ", "").isEmpty()) {
                message = message.concat("Item Description\n");
            }
            if (et_Price.getText().toString().replaceAll(" ", "").isEmpty()) {
                message = message.concat("Item Price\n");
            }


            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            return false;
        }
        else return true;
    }
}