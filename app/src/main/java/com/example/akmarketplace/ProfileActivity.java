package com.example.akmarketplace;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    //private TextView tv_welcometitle;
    private Button btn_Browse3;
    private Button btn_Sell3;
    private Button btn_Profile3;
    private TextView tv_Fullname, tv_Email, tv_Phone;
    private String targetEmail;

    private Button btn_changePassword, btn_changePhone;

    private Toolbar toolbar3;


    FirebaseFirestore fStore;
    CollectionReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fStore = FirebaseFirestore.getInstance();

        tv_Fullname = findViewById(R.id.tv_Fullname);
        tv_Email = findViewById(R.id.tv_Email);
        tv_Phone = findViewById(R.id.tv_Phone);
        btn_Browse3 = findViewById(R.id.btn_Browse3);
        btn_Sell3 = findViewById(R.id.btn_Sell3);
        btn_Profile3 = findViewById(R.id.btn_Profile3);
        btn_changePhone = findViewById(R.id.btn_changePhone);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        targetEmail = user.getEmail();

        DocumentReference docRef = fStore.collection("users").document(targetEmail);

        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tv_Fullname.setText(value.getString("fullname"));
                tv_Email.setText(value.getString("email"));
                tv_Phone.setText(value.getString("phone"));
            }
        });


        btn_changePassword.setOnClickListener(this);
        btn_changePhone.setOnClickListener(this);
        btn_Browse3.setOnClickListener(this);
        btn_Sell3.setOnClickListener(this);


        toolbar3 = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar3);


    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Browse3) {
            Intent browseIntent = new Intent(getApplicationContext(), BrowseActivity.class);
            startActivity(browseIntent);
            finish();
        } else if (v.getId() == R.id.btn_Sell3) {
            Intent sellIntent = new Intent(getApplicationContext(), SellActivity.class);
            startActivity(sellIntent);
            finish();
        }
        else if(v.getId()==R.id.btn_changePhone)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Enter New Phone Number");
            alert.setTitle("Change Phone Number");
            final EditText edittext = new EditText(this);

            alert.setView(edittext);

            alert.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if(edittext.getText().toString().equals("")){
                        Toast.makeText(ProfileActivity.this, "Phone Number was not Entered", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DocumentReference docRef = fStore.collection("users").document(targetEmail);

                    docRef.update("phone", edittext.getText().toString());
                }
            });

            alert.setNegativeButton("Cancel", null);

            alert.show();
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
}



