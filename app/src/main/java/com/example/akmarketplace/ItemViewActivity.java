package com.example.akmarketplace;

import static java.lang.System.currentTimeMillis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ItemViewActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences savedBuying;
    private ArrayList<Item> items;
    private ArrayList<Item> filteredItems;
    private String search_key;
    private int pos;
    private Item selectedItem;

    boolean isBuying;

    private String userEmail;
    private String userName;
    private ArrayList<String> buyers = new ArrayList<>();

    private ImageView img_itemView_display;
    private TextView tv_itemView_title, tv_itemView_desc, tv_itemView_price, tv_itemView_sellerName, tv_itemView_sellerPhone, tv_itemView_location;
    private Button btn_itemView_buy, btn_itemView_back;
    private boolean dne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        savedBuying = getSharedPreferences("savedBuying", MODE_PRIVATE);

        //userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        img_itemView_display = findViewById(R.id.img_itemView_display);
        tv_itemView_title = findViewById(R.id.tv_itemView_title);
        tv_itemView_desc = findViewById(R.id.tv_itemView_desc);
        tv_itemView_price = findViewById(R.id.tv_itemView_price);
        tv_itemView_sellerName = findViewById(R.id.tv_itemView_sellerName);
        tv_itemView_sellerPhone = findViewById(R.id.tv_itemView_sellerPhone);
        btn_itemView_buy = findViewById(R.id.btn_itemView_buy);
        btn_itemView_back = findViewById(R.id.btn_itemView_back);
        tv_itemView_location = findViewById(R.id.tv_itemView_location);

        btn_itemView_back.setOnClickListener(this);
        btn_itemView_buy.setOnClickListener(this);
        tv_itemView_location.setOnClickListener(this);


        Intent intent = getIntent();
        search_key = intent.getStringExtra("search");
        pos = intent.getIntExtra("position", 0);
        userEmail = intent.getStringExtra("userEmail");
        userName = intent.getStringExtra("userName");


        /*items = new ArrayList<>();
        filteredItems = new ArrayList<>();


        updateDisplay(search_key);*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Test5", "in onResume");

        items = new ArrayList<>();
        filteredItems = new ArrayList<>();
        updateDisplay(search_key);

        Log.d("Test5", "update display called");


    }

    public void updateDisplay(String key) {
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
                        if (i.getTitle().replaceAll(" ", "").toLowerCase().contains(key) && i.getSold().equals("false")) {

                            filteredItems.add(i);
                        }
                    }

                    selectedItem = filteredItems.get(pos);
                    Picasso.get().load(selectedItem.getImage()).into(img_itemView_display);
                    tv_itemView_title.setText(selectedItem.getTitle());
                    tv_itemView_price.setText(Double.toString(selectedItem.getPrice()));
                    tv_itemView_sellerName.setText(selectedItem.getSellerName());
                    tv_itemView_sellerPhone.setText(selectedItem.getSellerPhone());
                    tv_itemView_desc.setText(selectedItem.getDescription());


                    Log.d("Test5", "update display finished");

                    isBuying = savedBuying.getBoolean(Long.toString(selectedItem.getTime_added_millis()), true);
                    Log.d("Test5", "got shared references: " + isBuying);
                    btn_itemView_buy.setEnabled(isBuying);
                    Log.d("Test5", "button set");

                    if (selectedItem.getSellerEmail().equals(userEmail))
                        btn_itemView_buy.setEnabled(false);

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_itemView_back) {
            finish();
        }
        else if (v.getId() == R.id.btn_itemView_buy) {
            AlertDialog alertDialog = new AlertDialog.Builder(ItemViewActivity.this).create();
            alertDialog.setTitle("Notify Seller");
            alertDialog.setMessage("This will notify the seller that you are interested in buying this item.");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Notify",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dne = true;
                            BrowseActivity.db.collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (Long.parseLong(document.getId()) == selectedItem.getTime_added_millis()) {
                                            dne = false;
                                        }
                                    }
                                    if (dne) {
                                        Toast.makeText(ItemViewActivity.this, "Item no longer exists...", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        CollectionReference notifications = BrowseActivity.db.collection("notifications");
                                        Map<String, Object> notification = new HashMap<>();
                                        notification.put("buyerEmail", userEmail); //data type = long
                                        notification.put("sellerEmail", selectedItem.getSellerEmail());
                                        notification.put("buyerName", userName);
                                        notification.put("itemName", selectedItem.getTitle());
                                        notification.put("itemId", Long.toString(selectedItem.getTime_added_millis()));
                                        notifications.document(Long.toString(selectedItem.getTime_added_millis())).set(notification);

                                        BrowseActivity.db.collection("items").document(Long.toString(selectedItem.getTime_added_millis())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    buyers = (ArrayList<String>) document.get("buyerEmails");
                                                    buyers.add(userEmail);

                                                    BrowseActivity.db.collection("items").document(Long.toString(selectedItem.getTime_added_millis())).update("buyerEmails", buyers);

                                                    v.setEnabled(false);

                                                    SharedPreferences.Editor editor = savedBuying.edit();
                                                    editor.putBoolean(Long.toString(selectedItem.getTime_added_millis()), false);
                                                    editor.commit();
                                                }
                                            }
                                        });
                                    }


                                    //buyers = selectedItem.getBuyerEmails();


                                }
                            });
                        }});

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else if (v.getId() == R.id.tv_itemView_location) {
            Intent gpsIntent = new Intent(this, MapsActivity.class);
            gpsIntent.putExtra("locLat", selectedItem.getLocationLat());
            gpsIntent.putExtra("locLng", selectedItem.getLocationLng());
            startActivityForResult(gpsIntent, 205);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
        }
        else if (requestCode == 205) {

        }
    }
}
