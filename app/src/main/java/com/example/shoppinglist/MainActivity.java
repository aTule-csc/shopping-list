package com.example.shoppinglist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{

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

        ArrayList<HashMap<String,String>>types =new ArrayList<>();
        HashMap <String,String> type;
        Cursor cursor = database.rawQuery("SELECT list.id, list.name FROM list", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            type=new HashMap<>();
            type.put("list", cursor.getString(1));
            types.add(type);
            cursor.moveToNext();
        }
        cursor.close();

        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                types, android.R.layout.simple_list_item_2,
                new String[]{"list"},
                new int[]{android.R.id.text1}
        );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), Items.class);
                intent.putExtra("position",position);
                HashMap itemData = (HashMap) listView.getItemAtPosition(position);
                String armor_name = String.valueOf(itemData.get("list"));
                intent.putExtra("list_type",armor_name);
                startActivity(intent);
            }
        });
    }
}