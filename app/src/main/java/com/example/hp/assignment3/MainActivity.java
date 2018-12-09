package com.example.hp.assignment3;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText email_;
    EditText passsword;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    ArrayList<String> arrayList;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        email_=(EditText) findViewById(R.id.emailAddress);
        passsword=(EditText) findViewById(R.id.pswd);
        arrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);

    }
    public void signUp(View view){
        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intent);
    }
    public void signIn(View view){
        String email = email_.getText().toString().trim();
        String password = passsword.getText().toString().trim();

        if (email.equals("") || password.equals("")) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("ALERT!");
            alertDialog.setMessage("Fill All Fields");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
        }
        else{
            progressDialog.setMessage("Signing up.....");
            progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("tag", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            showUserList();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("tag", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    }
    void showUserList()
    {
        final ArrayList<String> nameList = new ArrayList<String>();
        final ArrayList<String> emailList = new ArrayList<String>();
        final ArrayList<String> ageList = new ArrayList<String>();
        final ArrayList<String> urlList = new ArrayList<String>();
        database= FirebaseDatabase.getInstance();
        mDatabase = database.getReference("users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Users user = snapshot.getValue(Users.class);
                    nameList.add(user.name);
                    emailList.add(user.email);
                    ageList.add(user.dob);
                    urlList.add(user.imgUrl);

                }
                //arrayAdapter.notifyDataSetChanged();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                progressDialog.dismiss();
                intent.putStringArrayListExtra("nameList",nameList);
                intent.putStringArrayListExtra("emailList",emailList);
                intent.putStringArrayListExtra("ageList",ageList);
                intent.putStringArrayListExtra("urlList",urlList);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
