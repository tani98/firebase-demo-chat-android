package com.ac.demochat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.ac.demochat.adapter.ChatAdapter;
import com.ac.demochat.entity.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvChat;
    private FirebaseUser currentUser;
    protected ChatAdapter chatAdapter;
    private List<Chat> mChats;
    private Button btnSend;
    private EditText txtMessage;
    private String mId;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Views
        rvChat = findViewById(R.id.rvChat);
        btnSend = findViewById(R.id.btSent);
        txtMessage = findViewById(R.id.etMessage);

        //Initialize Firebase DB
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        mChats = new ArrayList<>();
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        mId = currentUser.getUid();
        chatAdapter = new ChatAdapter(mChats, mId);
        rvChat.setAdapter(chatAdapter);

        receivesMessage();
        sendMessage();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void sendMessage() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = txtMessage.getText().toString();

                if (!message.isEmpty()) {
                    mDatabase.child("chat").push().setValue(new Chat(message, mId, currentUser.getEmail()));
                    txtMessage.setText("");
                }

            }
        });
    }

    private void receivesMessage() {
        mDatabase.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue() != null) {
                    try {
                        Chat model = dataSnapshot.getValue(Chat.class);

                        mChats.add(model);
                        rvChat.scrollToPosition(mChats.size() - 1);
                        chatAdapter.notifyItemInserted(mChats.size() - 1);
                    } catch (Exception ex) {
                        Log.e("onChildAdded", ex.getMessage());
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void signOut(){

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        mAuth.signOut();
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("¿Esta seguro que cerrar sesión?").setPositiveButton("Si", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


}
