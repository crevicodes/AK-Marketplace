package com.example.akmarketplace;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {



    private Button btn_createacc, btn_login, btn_forgotPass;
    private EditText et_email, et_password;

    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_createacc = findViewById(R.id.btn_createacc);
        btn_login = findViewById(R.id.btn_login);
        btn_forgotPass = findViewById(R.id.btn_forgotPass);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);



        btn_login.setOnClickListener(this);
        btn_createacc.setOnClickListener(this);
        btn_forgotPass.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_createacc)
        {
            Intent intent = new Intent(this, CreateAccountActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.btn_login)
        {
            Task<AuthResult> username = mAuth.signInWithEmailAndPassword(et_email.getText().toString(),
                            et_password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent i = new Intent(getApplicationContext(), BrowseActivity.class);
                                i.putExtra("username", user.getEmail());
                                startActivity(i);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        else if(v.getId() == R.id.btn_forgotPass)
        {
            AlertDialog.Builder forgot = new AlertDialog.Builder(this);
            forgot.setMessage("Enter Email Address for Password Reset");
            forgot.setTitle("Forgot Password");
            final EditText edittext = new EditText(this);

            forgot.setView(edittext);

            forgot.setNegativeButton("Cancel", null);
            forgot.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String email = edittext.getText().toString();
                    if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(getApplicationContext(), "Please Enter Registered Email",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(), "Check Email to Reset Password",
                                            Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Unable to Send Email",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
            forgot.show();
        }
    }


}