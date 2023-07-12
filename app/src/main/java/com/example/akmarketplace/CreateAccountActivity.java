package com.example.akmarketplace;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_firstname, et_lastname, et_email2, et_password2, et_password3, et_phone2;
    private Button btn_createacc2;
    private String fullname, phone, email;
    private FirebaseAuth mAuth;
    static FirebaseFirestore db;
    CollectionReference users;

    public CreateAccountActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        db = FirebaseFirestore.getInstance();
        users = db.collection("users");

        et_firstname = findViewById(R.id.et_firstname);
        et_lastname = findViewById(R.id.et_lastname);
        et_phone2 = findViewById(R.id.et_phone2);
        et_email2 = findViewById(R.id.et_email2);
        et_password2 = findViewById(R.id.et_password2);
        et_password3 = findViewById(R.id.et_password3);
        btn_createacc2 = findViewById(R.id.btn_createacc2);

        btn_createacc2.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_createacc2)
        {
            if(et_firstname.getText().toString().isEmpty() || et_lastname.getText().toString().isEmpty() || et_phone2.getText().toString().isEmpty()||et_email2.getText().toString().isEmpty() ||et_password2.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please Fill in all Fields", Toast.LENGTH_SHORT).show();
            }
            else if(!(et_password2.getText().toString().equals(et_password3.getText().toString())))
            {
                Toast.makeText(this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
            }
            else {
                mAuth.createUserWithEmailAndPassword(et_email2.getText().toString(),
                                et_password2.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Create User With Email : " +
                                            "success\nPlease login",Toast.LENGTH_SHORT).show();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            user.updateEmail(et_email2.getText().toString());

                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            Map<String, Object> userData = new HashMap<>();
                                            userData.put("fullname", et_firstname.getText().toString() + " " + et_lastname.getText().toString());
                                            userData.put("phone", et_phone2.getText().toString());
                                            userData.put("email", et_email2.getText().toString());
                                            users.document(et_email2.getText().toString()).set(userData);
                                            startActivity(intent);
                                } else {
                                    Log.d(TAG, "createUserWithEmail:failure: " +
                                                    task.getException().toString());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}