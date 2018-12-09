package com.example.hp.assignment3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SignupActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    EditText userName;
    String username;
    EditText passsword_;
    String password;
    EditText email_;
    String email;
    String age;
    String id;
    String imageUrl="";
    String storedPassword;
    ArrayList<String> arrayList;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private static final int CODE_CAMERA =1;
    Bitmap image;
    Bitmap uncropImage;
    TextView txtDate;
    ImageButton profile_image;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    private StorageReference mStorageRef;
    FirebaseStorage storage ;
    Uri downloadUrl;
    StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userName=(EditText) findViewById(R.id.name);
        passsword_=(EditText) findViewById(R.id.pswd);
        email_=(EditText) findViewById(R.id.emailAddress);
        txtDate=(TextView)findViewById(R.id.DOB);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        arrayList = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        profile_image = (ImageButton) findViewById(R.id.user_profile_photo);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent,CODE_CAMERA);
            }
        });

    }

    public void signup(View view){

        username = userName.getText().toString().trim();
        password = passsword_.getText().toString().trim();
        email = email_.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter email",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter password",Toast.LENGTH_LONG).show();
        }
        if (username.equals("") || password.equals("") || email.equals("") || age.equals("")) {
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
            progressDialog.setMessage("Registering.....");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();

                                Add_User();
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignupActivity.this, "Couldn't register", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }




    public void Add_User()
    {
        username = userName.getText().toString().trim();
        email = email_.getText().toString().trim();
        database=FirebaseDatabase.getInstance();
        mDatabase = database.getReference("users");
        id = mDatabase.push().getKey();
        Toast.makeText(SignupActivity.this,"ID: "+id,Toast.LENGTH_LONG).show();
        //imageUrl= String.valueOf(downloadUrl);
        Log.d("downloadUrl-->", "" + downloadUrl);
        System.out.println("image === "+imageUrl);

        Users user = new Users(id, username, email, age,imageUrl);
        mDatabase.child(id).setValue(user);
    }


    private void uploadFile(Bitmap bitmap) {
        Calendar c = new GregorianCalendar();  // This creates a Calendar instance with the current time
        mHour = c.get(Calendar.HOUR);
        mMinute = c.get(Calendar.MINUTE);
        String date = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
        StorageReference mountainImagesRef = mStorageRef.child("userimages/" + date + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadUrl = taskSnapshot.getDownloadUrl();
                imageUrl= String.valueOf(downloadUrl);
            }
        });

    }







    public void setAge(View view){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        txtDate.setText(getAge(year,monthOfYear+1,dayOfMonth));
                        age=getAge(year,monthOfYear+1,dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            image = (Bitmap) data.getExtras().get("data");

            image = Bitmap.createBitmap(image,0,0 ,120,120);
            uncropImage=image;
            uploadFile(image);
            image=roundCornerImage(image,50);

            profile_image.setImageBitmap(image);

        }
    }






    public Bitmap roundCornerImage(Bitmap raw, float round) {
        int width = raw.getWidth();
        int height = raw.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawARGB(0, 0, 0, 0);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#000000"));

        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        canvas.drawRoundRect(rectF, round, round, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(raw, rect, rect, paint);

        return result;
    }


}
