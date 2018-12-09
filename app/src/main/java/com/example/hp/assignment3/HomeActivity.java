package com.example.hp.assignment3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList<String> emailList = new ArrayList<String>();
    ArrayList<String> ageList = new ArrayList<String>();
    ArrayList<String> urlList = new ArrayList<String>();
    //int[] images = {};
    //int[] images = {R.drawable.ambulance};
    //String[] version = {};
    ListView lView;
    listAdapter lAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent=getIntent();
        nameList=intent.getStringArrayListExtra("nameList");
        emailList=intent.getStringArrayListExtra("emailList");
        ageList=intent.getStringArrayListExtra("ageList");
        urlList=intent.getStringArrayListExtra("urlList");
        final String[] version = nameList.toArray(new String[nameList.size()]);
        final String[] images = urlList.toArray(new String[nameList.size()]);
        lView = (ListView) findViewById(R.id.name_list);

        lAdapter = new listAdapter(HomeActivity.this, version, images);

        lView.setAdapter(lAdapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(HomeActivity.this, version[i]+" ", Toast.LENGTH_SHORT).show();

            }
        });
        lView.setAdapter(lAdapter);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeActivity.this,UserInfoActivity.class);
                System.out.println("***************************************** \n"+nameList.get(i));
                System.out.println("Array List: "+nameList.get(i));
                System.out.println("email List: "+emailList.get(i));
                System.out.println("age List: "+ageList.get(i));
                intent.putExtra("name",nameList.get(i));
                intent.putExtra("email",emailList.get(i));
                intent.putExtra("age",ageList.get(i));
                intent.putExtra("url",urlList.get(i));
                startActivity(intent);
            }
        });
    }
}
