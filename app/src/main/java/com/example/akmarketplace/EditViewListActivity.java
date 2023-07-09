package com.example.akmarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class EditViewListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    static FirebaseFirestore db;
    static FirebaseStorage storage;
    private ListView lv_items;
    private ArrayList<Item> items;
    private ArrayList<Item> filteredItems;
    private Toolbar toolbar1;

    private String targetEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view_list);

        items = new ArrayList<>();
        filteredItems = new ArrayList<>();


        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        toolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);

        lv_items = findViewById(R.id.lv_items);
        lv_items.setOnItemClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        targetEmail = user.getEmail();

        updateDisplay(targetEmail);
    }

    public void updateDisplay(String email) {
        items = new ArrayList<>();
        filteredItems = new ArrayList<>();
        EditViewListActivity.db.collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        items.add(document.toObject(Item.class));
                    }

                    //items.sort((o1, o2) -> o1.getTime_added_millis().compareTo(o2.getTime_added_millis()));

                    ArrayList<HashMap<String, Object>> data = new ArrayList<>();
                    for (Item i : items) {
                        if (i.getSellerEmail().equals(targetEmail)) {
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = filteredItems.get(position);
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        //updateDisplay(targetEmail);
        Intent editItem = new Intent(this, EditItemActivity.class);
        editItem.putExtra("title", item.getTitle());
        editItem.putExtra("description", item.getDescription());
        editItem.putExtra("price", item.getPrice());
        editItem.putExtra("locationLat", item.getLocationLat());
        editItem.putExtra("locationLng", item.getLocationLng());
        startActivity(editItem);
    }
}