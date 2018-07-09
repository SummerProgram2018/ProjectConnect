package com.example.daffamage.pc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewActivity extends AppCompatActivity {

    ListView simpleList;
    String countryList[] = {"PROJECT 1", "PROJECT 2", "PROJECT 3", "PROJECT 4", "PROJECT 5", "PROJECT 6", "PROJECT 7", "PROJECT 8", "PROJECT 9", "PROJECT 10", "PROJECT 11"};

    //Make Project block here

    public void refreshView (View view)
    {
        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
    }

    public void openProject (View view)
    {
        Intent intent = new Intent(this, DetailedView.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        simpleList = (ListView)findViewById(R.id.project_dynamic);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.listViewText, countryList);
        simpleList.setAdapter(arrayAdapter);
    }
}
