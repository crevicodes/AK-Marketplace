package com.example.akmarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ItemViewActivity extends AppCompatActivity {

    private ArrayList<Item> items;
    private ArrayList<Item> filteredItems;
    private String search_key;
    private int pos;
    private Item selectedItem;

    ImageView img_itemView_display;
    TextView tv_itemView_title, tv_itemView_desc, tv_itemView_price, tv_itemView_sellerName, tv_itemView_sellerPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        img_itemView_display = findViewById(R.id.img_itemView_display);
        tv_itemView_title = findViewById(R.id.tv_itemView_title);
        tv_itemView_desc = findViewById(R.id.tv_itemView_desc);
        tv_itemView_price = findViewById(R.id.tv_itemView_price);
        tv_itemView_sellerName = findViewById(R.id.tv_itemView_sellerName);
        tv_itemView_sellerPhone = findViewById(R.id.tv_itemView_sellerPhone);


        Intent intent = getIntent();
        search_key = intent.getStringExtra("search");
        pos = intent.getIntExtra("position", 0);

        items = new ArrayList<>();
        filteredItems = new ArrayList<>();

        updateDisplay(search_key);
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
                        if (i.getTitle().replaceAll(" ", "").toLowerCase().contains(key)) {

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

                }
            }
        });

}
}