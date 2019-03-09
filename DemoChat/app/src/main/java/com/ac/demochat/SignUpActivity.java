package com.ac.demochat;

import android.content.Context;;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private Button btnSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Init Views
        txtEmail = findViewById(R.id.etEmail);
        txtPassword = findViewById(R.id.etPass);
        txtConfirmPassword = findViewById(R.id.etConfirmPass);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Not show keyboard
        txtEmail.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txtEmail, InputMethodManager.SHOW_IMPLICIT);

        //return arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //actions
        signUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i("ActionBar", "Back!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signUp() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String confirmPass = txtConfirmPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.complete_your_data), Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPass)) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.pass_not_match), Toast.LENGTH_SHORT).show();
                } else {
                    createUserWithEmailAndPassword(email, password);
                }
            }
        });
    }

    private void createUserWithEmailAndPassword(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("createUser", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("createUser", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void startPrincipal() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}

