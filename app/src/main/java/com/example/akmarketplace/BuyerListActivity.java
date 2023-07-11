package com.example.akmarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class BuyerListActivity extends ListActivity {

        private String targetEmail;
        private int itemPosition;
        private ListView buyerListView; // the ListActivity's ListView
        private CursorAdapter buyerAdapter; // adapter for ListView
        //TS: Adapter that exposes data from a Cursor to a ListView widget.
        //The Cursor must include a column named "_id" or this class will not work
        private Item currentItem;
        private ArrayList <Item> items;
        private ArrayList <Item> filteredItems;

    // called when the activity is first created
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState); // call super's onCreate

            //TS: 1. create list view and set it event handler
            items = new ArrayList<>();
            filteredItems = new ArrayList<>();
            Intent intent = getIntent();
            itemPosition = intent.getIntExtra("itemposition", 0);
            targetEmail = intent.getStringExtra("targetemail");
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
                            if (i.getSellerEmail().equals(targetEmail)) {

                                filteredItems.add(i);
                            }
                        }
                        currentItem = filteredItems.get(itemPosition);
                    }
                }
            });

                       /* int resource = R.layout.listview_item;
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

        }*/
    }

}