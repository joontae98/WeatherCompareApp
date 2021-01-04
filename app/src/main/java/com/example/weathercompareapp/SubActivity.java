package com.example.weathercompareapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SubActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        ListAdapter adapter = new ListAdapter(this, R.layout.row_weather,  DBOpenHelper.SelectAllKids());

        ListView listView = findViewById(R.id.listView_sub);

        listView.setAdapter(adapter);
    }
}
