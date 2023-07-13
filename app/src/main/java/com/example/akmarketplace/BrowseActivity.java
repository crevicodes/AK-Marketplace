package com.example.akmarketplace;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class BrowseActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, AdapterView.OnItemClickListener {

    static FirebaseFirestore db; //all activities can access this
    static FirebaseStorage storage;
    private Button btn_Browse1, btn_Sell1, btn_Profile1;
    private EditText et_Search;
    private Toolbar toolbar1;
    private ArrayList<Item> items; //get all items here
    private ArrayList<Item> filteredItems; //filtered to display and open the corresponding displayed item
    ListView lv_items;
    private String search_key;
    ImageView img_itemImage;
    String targetEmail;
    String targetName;
    boolean dne; //for solving the user concurrency delete issue


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        btn_Browse1 = findViewById(R.id.btn_Browse1);
        btn_Sell1 = findViewById(R.id.btn_Sell1);
        btn_Profile1 = findViewById(R.id.btn_Profile1);
        et_Search = findViewById(R.id.et_Search);


        btn_Sell1.setOnClickListener(this);
        btn_Profile1.setOnClickListener(this);
        btn_Browse1.setOnClickListener(this);


        toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);

        items = new ArrayList<>();
        filteredItems = new ArrayList<>();
        lv_items = findViewById(R.id.lv_items);

        lv_items.setOnItemClickListener(this);

        dne = true;


        et_Search.setOnEditorActionListener(this);
        et_Search.addTextChangedListener(new TextWatcher() { //searches as soon as you type something
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_Search.getText().toString().replaceAll(" ","").isEmpty()) search_key = "";
                else {
                    search_key = et_Search.getText().toString().replaceAll(" ", "").toLowerCase();
                }
                updateAndDisplay(search_key);
            }
        });

        search_key = "";
        if (user != null) {
            String name = user.getDisplayName();
            targetEmail = user.getEmail();
            Task<DocumentSnapshot> tk = db.collection("users").document(targetEmail).get();
            tk.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot res = task.getResult();
                    targetName = res.getString("fullname");
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAndDisplay(search_key);
    }

    @Override
    public void onClick(View v) { //navigating between the 3 main interfaces
        if (v.getId() == R.id.btn_Sell1) {
            Intent sellIntent = new Intent(getApplicationContext(), SellActivity.class);
            startActivity(sellIntent);
            finish();
        }
        else if (v.getId() == R.id.btn_Profile1) {
            Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(profileIntent);
            finish();
        }
        else if (v.getId() == R.id.btn_Browse1) {
            et_Search.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(
                R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //menu items
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
            Intent userGuide = new Intent(Intent.ACTION_VIEW, Uri.parse("https://publuu.com/flip-book/187365/457764"));
            startActivity(userGuide);
            return true;
        }
            return super.onOptionsItemSelected(item);

    }

    public void updateAndDisplay(String key) { //try catch just in case
        try {
            getAndUpdate(key);
        } catch (Exception e) {
            try {
                sleep(500);
                getAndUpdate(key);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
    public void getAndUpdate(String key) { //actual updateAndDisplay()
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

                    ArrayList<HashMap<String, String>> data = new ArrayList<>();
                    for (Item i : items) {
                        if (i.getTitle().replaceAll(" ", "").toLowerCase().contains(key) && i.getSold().equals("false")) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("title", i.getTitle());

                            map.put("image", i.getImage());

                            map.put("seller", i.getSellerName());
                            map.put("price", Double.toString(i.getPrice()));
                            data.add(map);
                            filteredItems.add(i);
                        }
                    }

                    int resource = R.layout.listview_item;
                    String[] from = {"image","title","seller","price"};
                    int[] to = {R.id.img_itemImage, R.id.tv_itemTitle, R.id.tv_itemSeller, R.id.tv_itemPrice};

                    SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, resource, from, to) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            ImageView img_itemImage = view.findViewById(R.id.img_itemImage);
                            String imageURL = data.get(position).get("image");

                            Picasso.get().load(imageURL).into(img_itemImage); //for image loading to image view

                            return view;
                        }
                    };
                    lv_items.setAdapter(adapter);


                }
            }
        });


    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(et_Search.getText().toString().replaceAll(" ","").isEmpty()) search_key = "";
        else {
            search_key = et_Search.getText().toString().replaceAll(" ", "").toLowerCase();
        }
        updateAndDisplay(search_key);
        return false;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        dne = true;
        db.collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (Long.parseLong(document.getId())==filteredItems.get(position).getTime_added_millis()) {
                        dne = false;
                    }
                }
                if(dne) {
                    Toast.makeText(BrowseActivity.this, "Item no longer exists...", Toast.LENGTH_SHORT).show();
                    updateAndDisplay(search_key);
                }
                else {
                    Intent itemIntent = new Intent(getApplicationContext(), ItemViewActivity.class);
                    itemIntent.putExtra("position", position);
                    itemIntent.putExtra("search", search_key);
                    itemIntent.putExtra("userEmail", targetEmail);
                    itemIntent.putExtra("userName", targetName);
                    startActivity(itemIntent);
                }

            }
        });

    }
}