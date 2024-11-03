package com.example.lab6_java;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
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

        ArrayList<HashMap<String,String>>categories =new ArrayList<>();
        HashMap <String,String> category;
        Cursor cursor = database.rawQuery("SELECT categories.id, categories.name AS \"Категория\" FROM categories", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            category=new HashMap<>();
            category.put("name", cursor.getString(1));
            categories.add(category);
            cursor.moveToNext();
        }
        cursor.close();

        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                categories, android.R.layout.simple_list_item_2,
                new String[]{"name"},
                new int[]{android.R.id.text1}
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), MainActivity2.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }
}