package com.ac.demochat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnSignUp;
    private Button btnSignIn;
    private TextView txtForgotPass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Init Views
        txtEmail = findViewById(R.id.etEmail);
        txtPassword = findViewById(R.id.etPass);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);
        txtForgotPass = findViewById(R.id.txtforgotPass);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Not show keyboard
        txtEmail.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txtEmail, InputMethodManager.SHOW_IMPLICIT);

        //TextUnderline
        txtForgotPass.setPaintFlags(txtForgotPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //actions
        signIn();
        signUp();
        forgotPass();
    }

    private void signUp() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signIn() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.complete_your_data), Toast.LENGTH_SHORT).show();
                } else {
                    signInWithEmailAndPassword(email, password);
                }

            }
        });
    }

    private void forgotPass() {
        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPass();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startPrincipal();
        }
    }

    private void clear() {
        txtEmail.setText("");
        txtPassword.setText("");
    }

    private void startPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void signInWithEmailAndPassword(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignIn", "signInWithEmail:onComplete:" + task.isSuccessful());
                            startPrincipal();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignIn", "signInWithEmail:failed", task.getException());
                            clear();
                        }
                    }
                });
    }

    private void resetPass() {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        final View viewDialog = inflater.inflate(R.layout.dialog_forgot_pass,

                (ViewGroup) findViewById(R.id.layout_forgot_pass));

        popDialog.setTitle(getString(R.string.forgot_password));

        final EditText txtEmail = viewDialog.findViewById(R.id.etEmail);

        popDialog.setView(viewDialog);

        popDialog.setPositiveButton(getString(R.string.restore_password), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                String email = txtEmail.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.complete_your_data), Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword(email);
                }
                dialog.dismiss();

            }

        }).setNegativeButton(getString(R.string.cancel),

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }

                });


        popDialog.create();

        popDialog.show();
    }

    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("resetPass", "Correo enviado.");
                            Toast.makeText(LoginActivity.this, getString(R.string.email_sent), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("resetPass", task.getException().getMessage());
                            Toast.makeText(LoginActivity.this, getString(R.string.error_ressetting_pass), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
