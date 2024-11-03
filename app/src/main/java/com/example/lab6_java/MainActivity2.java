package com.example.lab6_java;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity2 extends AppCompatActivity {
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper=new DBHelper(getApplicationContext());
        try {
            database=dbHelper.getWritableDatabase();
        } catch (Exception e){
            e.printStackTrace();
        }

        listView=findViewById(R.id.ListView);
        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("position", 0) + 1;
        ArrayList<HashMap<String,String>> categories =new ArrayList<>();
        HashMap <String,String> category;
        Cursor cursor = database.rawQuery("SELECT name, info,price FROM products WHERE products.category_id =" + intValue, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            category=new HashMap<>();
            category.put("name", cursor.getString(0));
            category.put("info", "Описание: " + cursor.getString(1) + "\nЦена: " + cursor.getString(2));
            categories.add(category);
            cursor.moveToNext();
        }
        cursor.close();

        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                categories, android.R.layout.simple_list_item_2,
                new String[]{"name","info"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        listView.setAdapter(adapter);
}}