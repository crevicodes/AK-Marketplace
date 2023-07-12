package com.example.akmarketplace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_Browse3;
    private Button btn_Sell3;
    private Button btn_Profile3;

    private Button btn_editViewItems, btn_SellHistory;
    private TextView tv_Fullname, tv_Email, tv_Phone;
    private String targetEmail;

    private Button btn_changePhone;

    private Toolbar toolbar3;

    private ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tv_Fullname = findViewById(R.id.tv_Fullname);
        tv_Email = findViewById(R.id.tv_Email);
        tv_Phone = findViewById(R.id.tv_Phone);
        btn_Browse3 = findViewById(R.id.btn_Browse3);
        btn_Sell3 = findViewById(R.id.btn_Sell3);
        btn_Profile3 = findViewById(R.id.btn_Profile3);
        btn_changePhone = findViewById(R.id.btn_changePhone);
        btn_editViewItems = findViewById(R.id.btn_editViewItems);
        btn_SellHistory = findViewById(R.id.btn_SellHistory);
        items = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        targetEmail = user.getEmail();

        Task task = BrowseActivity.db.collection("users").document(targetEmail).get();
        Log.d("Test1", "docRef get: " + targetEmail);
        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot res = task.getResult();
                tv_Fullname.setText(res.getString("fullname"));
                tv_Email.setText(res.getString("email"));
                tv_Phone.setText(res.getString("phone"));
                Log.d("CMP", "Set up Profile Name");
            }
        });

        btn_changePhone.setOnClickListener(this);
        btn_Browse3.setOnClickListener(this);
        btn_Sell3.setOnClickListener(this);
        btn_editViewItems.setOnClickListener(this);
        btn_SellHistory.setOnClickListener(this);


        toolbar3 = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar3);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.POST_NOTIFICATIONS}, 301);
        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Browse3) {
            Intent browseIntent = new Intent(getApplicationContext(), BrowseActivity.class);
            startActivity(browseIntent);
            finish();
        } else if (v.getId() == R.id.btn_Sell3) {
            Intent sellIntent = new Intent(getApplicationContext(), SellActivity.class);
            startActivity(sellIntent);
            finish();
        }
        else if(v.getId()==R.id.btn_changePhone) //allow phone change, also changes the items listed
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Enter New Phone Number");
            alert.setTitle("Change Phone Number");
            final EditText edittext = new EditText(this);

            alert.setView(edittext);

            alert.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (edittext.getText().toString().equals("")) {
                                Toast.makeText(ProfileActivity.this, "Phone Number was not Entered", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                DocumentReference docRef = BrowseActivity.db.collection("users").document(targetEmail);

                                docRef.update("phone", edittext.getText().toString());
                                items = new ArrayList<>();
                                BrowseActivity.db.collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                items.add(document.toObject(Item.class));
                                                Log.d("CMP", "Added to ArrayList");
                                            }
                                            for (Item i : items) {
                                                if (i.getSellerEmail().equals(targetEmail)) {
                                                    BrowseActivity.db.collection("items").document(Long.toString(i.getTime_added_millis())).update("sellerPhone", edittext.getText().toString());
                                                    Log.d("CMP", "Updated item " + i.getTitle());
                                                }
                                            }


                                        }
                                    }


                                });

                            }
                        }
                    });


            alert.setNegativeButton("Cancel", null);

            alert.show();

        }
        else if(v.getId() == R.id.btn_editViewItems)
        {
            Intent editViewItems = new Intent(this, EditViewListActivity.class);
            startActivity(editViewItems);
        }
        else if(v.getId() == R.id.btn_SellHistory)
        {
            Intent listSoldIntent = new Intent(this, SoldActivity.class);
            startActivity(listSoldIntent);
        }

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
        } else if (item.getItemId() == R.id.menu_Help) {
            Intent userGuide = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/11kMRshvPOeUrqo0h9pvM0SDFSmgQVI59FW49WyUxKoM/edit"));
            startActivity(userGuide);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}



