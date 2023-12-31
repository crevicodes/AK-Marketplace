package com.example.akmarketplace;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class SoldActivity extends AppCompatActivity { //history of sold items

    private ListView lv_items;
    private ArrayList<Item> items;
    private ArrayList<Item> filteredItems;
    private Toolbar toolbar1;
    private TextView tv_welcometitle;

    private String targetEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view_list);
        toolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);

        tv_welcometitle = findViewById(R.id.tv_welcometitle);
        tv_welcometitle.setText("AK Marketplace - Sell History");

        lv_items = findViewById(R.id.lv_items);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        targetEmail = user.getEmail();
    }

    @Override
    protected void onResume() {
        super.onResume();
        items = new ArrayList<>();
        filteredItems = new ArrayList<>();
        updateAndDisplay(targetEmail);
    }

    public void updateAndDisplay(String key) {
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

    public void getAndUpdate(String key) {
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
                        if (i.getSellerEmail().equals(key) && i.getSold().equals("true")) {
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

                            Picasso.get().load(imageURL).into(img_itemImage);

                            return view;
                        }
                    };
                    lv_items.setAdapter(adapter);

                }
            }
        });

    }
}