package com.example.akmarketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.zip.Inflater;

public class BrowseActivity extends AppCompatActivity implements View.OnClickListener {

    //private TextView tv_welcometitle;
    static FirebaseFirestore db;

    private Button btn_Browse1, btn_Sell1, btn_Profile1;
    private EditText et_Search;
    Toolbar toolbar1;


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

        btn_Browse1 = findViewById(R.id.btn_Browse1);
        btn_Sell1 = findViewById(R.id.btn_Sell1);
        btn_Profile1 = findViewById(R.id.btn_Profile1);
        et_Search = findViewById(R.id.et_Search);

        btn_Sell1.setOnClickListener(this);
        btn_Profile1.setOnClickListener(this);
        btn_Browse1.setOnClickListener(this);


        toolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);

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