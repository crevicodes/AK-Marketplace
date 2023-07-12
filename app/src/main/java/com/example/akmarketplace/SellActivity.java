package com.example.akmarketplace;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;

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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_Browse2, btn_Sell2, btn_Profile2, btn_Location, btn_Image, btn_EnlistItem;
    private ImageView img_itemDisplay, img_itemImage, img_Default;
    private LatLng loc_meetupLocation;
    //private Item newItem;
    private Toolbar toolbar2;
    private String targetEmail, targetFullname, targetPhone;
    private EditText et_Title, et_Description, et_Price;
    private Uri imageUri;
    private LatLng loc2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        btn_Browse2 = findViewById(R.id.btn_Browse2);
        btn_Sell2 = findViewById(R.id.btn_Sell2);
        btn_Profile2 = findViewById(R.id.btn_Profile2);
        btn_Image = findViewById(R.id.btn_Image);
        btn_Location = findViewById(R.id.btn_Location);
        btn_EnlistItem = findViewById(R.id.btn_EnlistItem);
        img_itemDisplay = findViewById(R.id.img_itemDisplay);
        img_Default = new ImageView(getApplicationContext());
        img_Default.setImageDrawable(img_itemDisplay.getDrawable());
        img_itemImage = null;
        loc_meetupLocation = null;
        loc2 = new LatLng(25.310338125326922, 55.491244819864185);
        et_Title = findViewById(R.id.et_Title);
        et_Description = findViewById(R.id.et_Description);
        et_Price = findViewById(R.id.et_Price);

        btn_Browse2.setOnClickListener(this);
        btn_Profile2.setOnClickListener(this);
        btn_Image.setOnClickListener(this);
        btn_Location.setOnClickListener(this);
        btn_EnlistItem.setOnClickListener(this);

        toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, 102);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        targetEmail = user.getEmail();

        DocumentReference docRef = BrowseActivity.db.collection("users").document(targetEmail);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AccountUser user1 = documentSnapshot.toObject(AccountUser.class);
                targetFullname = user1.getFullname();
                targetPhone = user1.getPhone();
            }
        });


        //targetFullname = user.getDisplayName();
        // = user.getPhoneNumber();

        //newItem = new Item();

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Location) {
            Intent mapsIntent = new Intent(getApplicationContext(), SetLocActivity.class);
            mapsIntent.putExtra("locationLat", loc2.latitude);
            mapsIntent.putExtra("locationLng", loc2.longitude);
            startActivityForResult(mapsIntent,201);

        }
        else if (v.getId() == R.id.btn_Image) {
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
        else if (v.getId() == R.id.btn_EnlistItem) {
            boolean checkFields = verifyFields();
            if (checkFields) {

                btn_Browse2.setEnabled(false);

                CollectionReference items = BrowseActivity.db.collection("items");
                Map<String, Object> item = new HashMap<>();
                long timeAdded = currentTimeMillis();
                item.put("time_added_millis", timeAdded); //data type = long
                String temp_title = et_Title.getText().toString();
                String temp_desc = et_Description.getText().toString();
                item.put("title", temp_title);
                item.put("description", temp_desc);
                item.put("price", Double.parseDouble(et_Price.getText().toString()));


                item.put("image", "");
                item.put("locationLat", loc_meetupLocation.latitude); //data type = double
                item.put("locationLng", loc_meetupLocation.longitude);

                item.put("sellerName", targetFullname);
                item.put("sellerEmail", targetEmail);
                item.put("sellerPhone", targetPhone);

                item.put("sold", "false");
                item.put("buyerEmails", new ArrayList<String>());
                //item.put("buyerEmail", "");
                //item.put("buyerPhone", "");


                items.document(Long.toString(timeAdded)).set(item);

                clearFields();

                StorageReference storeRef = BrowseActivity.storage.getReference().child("items/"+temp_title+(temp_desc.length()>7 ? temp_desc.substring(0,7) : temp_desc)+".jpg");

                UploadTask uploadTask = storeRef.putFile(imageUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                        storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                items.document(Long.toString(timeAdded)).update("image", uri.toString());
                                Toast.makeText(getApplicationContext(),"Item Added", Toast.LENGTH_SHORT).show();
                                btn_Browse2.setEnabled(true);
                            }
                        });
                    }
                });


            }
        }
    }


    public boolean verifyFields() {
        if (et_Title.getText().toString().replaceAll(" ", "").isEmpty() ||
                et_Description.getText().toString().replaceAll(" ", "").isEmpty() ||
                img_itemImage == null || loc_meetupLocation == null) {
            AlertDialog alertDialog = new AlertDialog.Builder(SellActivity.this).create();
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
            if (img_itemImage == null) {
                message = message.concat("Item Image\n");
            }
            if (loc_meetupLocation == null) {
                message = message.concat("Meetup Location\n");
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

    public void clearFields() {
        et_Title.setText("");
        et_Description.setText("");
        et_Price.setText("");
        img_itemDisplay.setImageDrawable(img_Default.getDrawable());
        img_itemImage = null;
        loc_meetupLocation = null;
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
            loc2 = new LatLng(locLat, locLng);
        }
        else if (requestCode==RESULT_CANCELED) {
            
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(
                R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_About) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setView(R.layout.activity_about);
            dialog.setTitle("About");
            dialog.setPositiveButton("Ok", null);
            dialog.show();
            return true;
        } else if (item.getItemId() == R.id.menu_LogOut) {
            Intent logoutintent = new Intent(this, LoginActivity.class);
            startActivity(logoutintent);
            Intent foregroundService = new Intent(getApplicationContext(), MarketplaceService.class);
            stopService(foregroundService);
            return true;
        } else if (item.getItemId() == R.id.menu_QuitApp) {
            this.finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }



}