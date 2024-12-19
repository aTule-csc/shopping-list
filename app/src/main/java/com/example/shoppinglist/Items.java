package com.example.shoppinglist;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Items extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private ListView listView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
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
        textView =findViewById(R.id.textView2);

        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("position", 0) + 1;
        textView.setText(mIntent.getStringExtra("list_type"));


        ArrayList<HashMap<String,String>> items =new ArrayList<>();
        HashMap <String,String> item;
        Cursor cursor = database.rawQuery("SELECT item_id, item_string, item_amount FROM item_in_list WHERE list_id = ?", new String[] { String.valueOf(intValue) } );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            item=new HashMap<>();
            item.put("name", cursor.getString(0));
            item.put("info", cursor.getString(1) );
            items.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                items, android.R.layout.simple_list_item_2,
                new String[]{"name","info"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        listView.setAdapter(adapter);

    }
    public void toMain(View v)
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}