package com.example.akmarketplace;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

public class BrowseActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    //private TextView tv_welcometitle;

    static FirebaseFirestore db;
    static FirebaseStorage storage;
    private Button btn_Browse1, btn_Sell1, btn_Profile1;
    private EditText et_Search;
    private Toolbar toolbar1;
    private ArrayList<Item> items;
    private ArrayList<Item> filteredItems;
    ListView lv_items;
    private String search_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        //tv_welcometitle= findViewById(R.id.tv_welcometitle);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address etc
            String name = user.getDisplayName();
            //tv_welcometitle.setText("Welcome, " + name);
        }

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


        et_Search.setOnEditorActionListener(this);
        search_key = "";

        updateDisplay(search_key);

    }

    @Override
    public void onClick(View v) {
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
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

                    //items.sort((o1, o2) -> o1.getTime_added_millis().compareTo(o2.getTime_added_millis()));

                    ArrayList<HashMap<String, Object>> data = new ArrayList<>();
                    for (Item i : items) {
                        if (i.getTitle().replaceAll(" ", "").toLowerCase().contains(key)) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("title", i.getTitle());

                            //StorageReference storeRef = BrowseActivity.storage.getReference().child(i.getTitle()+(i.getDescription().length()>7 ? i.getDescription().substring(0,7) : i.getDescription()));

                            //map.put("image", i.getImage());

                            map.put("seller", i.getSellerName());
                            map.put("price", i.getPrice());
                            data.add(map);
                            filteredItems.add(i);
                        }
                    }

                    int resource = R.layout.listview_item;
                    String[] from = {/*"image",*/"title","seller","price"};
                    int[] to = {/*R.id.img_itemImage,*/ R.id.tv_itemTitle, R.id.tv_itemSeller, R.id.tv_itemPrice};

                    SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, resource, from, to);
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
        updateDisplay(search_key);
        return false;
    }

    private void uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    /*void setMenuBackground()
    {
        getLayoutInflater().setFactory(new LayoutInflater.Factory() {
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
                    try {
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView(name, null, attrs);

                        new Handler().post(new Runnable() {
                            public void run() {
                                // sets the background color
                                view.setBackgroundResource(R.color.primarycolor);
                                // sets the text color
                                ((TextView)view).setTextColor(Color.WHITE);
                                // sets the text size
                                ((TextView) view).setTextSize(18);
                            }
                        });
                        return view;
                    } catch (InflateException e) {
                    } catch (ClassNotFoundException e) {
                    }
                }
                return null;
            }});
    }*/
}