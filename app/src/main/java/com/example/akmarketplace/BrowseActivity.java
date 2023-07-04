package com.example.akmarketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class BrowseActivity extends AppCompatActivity {

    private TextView tv_welcometitle;
    static FirebaseFirestore db;

    private Button btn_Browse1;
    private Button btn_Sell1;
    private Button btn_Profile1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        tv_welcometitle= findViewById(R.id.tv_welcometitle);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address etc
            String name = user.getDisplayName();
            tv_welcometitle.setText("Welcome, " + name);
        }

        btn_Browse1 = findViewById(R.id.btn_Browse1);
        btn_Sell1 = findViewById(R.id.btn_Sell1);
        btn_Profile1 = findViewById(R.id.btn_Profile1);

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.showOverflowMenu();

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