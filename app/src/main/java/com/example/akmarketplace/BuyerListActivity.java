package com.example.akmarketplace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class BuyerListActivity extends ListActivity {

        private String targetEmail;
        private int itemPosition;
        private SimpleAdapter buyerAdapter;
        private Item currentItem;
        private ArrayList <Item> items;
        private ArrayList <Item> filteredItems;
        private ArrayList<String> buyers;

        @Override
        public void onCreate(Bundle savedInstanceState) { //loads the list of buyers for that item
            super.onCreate(savedInstanceState);
            Log.d("CMP", "Entered Buyer List");
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
                            if (i.getSellerEmail().equals(targetEmail) && i.getSold().equals("false")) {

                                filteredItems.add(i);
                            }
                        }
                        currentItem = filteredItems.get(itemPosition);
                        Log.d("CMP", "Got currentItem");

                        BrowseActivity.db.collection("items").document(Long.toString(currentItem.getTime_added_millis())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    buyers = (ArrayList<String>) document.get("buyerEmails");
                                    ArrayList<AccountUser> allUsers = new ArrayList<>();
                                    ArrayList<AccountUser> filteredUsers = new ArrayList<>();

                                    BrowseActivity.db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()) {
                                                for(QueryDocumentSnapshot document : task.getResult()) {
                                                    allUsers.add(document.toObject(AccountUser.class));
                                                }

                                                ArrayList<HashMap<String, String>> data = new ArrayList<>();
                                                for ( AccountUser u : allUsers) {
                                                    if (buyers.contains(u.getEmail())) {
                                                        HashMap<String, String> map = new HashMap<>();
                                                        map.put("fullname", u.getFullname());
                                                        map.put("phone", u.getPhone());

                                                        data.add(map);
                                                        filteredUsers.add(u);
                                                    }
                                                }

                                                int resource = R.layout.activity_buyer_list_item;
                                                String[] from = {"fullname","phone"};
                                                int[] to = {R.id.tv_BuyerNameItem, R.id.tv_BuyerPhoneItem};

                                                buyerAdapter = new SimpleAdapter(getApplicationContext(), data, resource, from, to);
                                                setListAdapter(buyerAdapter);
                                                Log.d("CMP", "Set Adapter");
                                            }
                                        }
                                    });
                            }
                        }});
                    }
                }
            });
        }
}
