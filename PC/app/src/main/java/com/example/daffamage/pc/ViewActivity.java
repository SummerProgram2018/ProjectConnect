package com.example.daffamage.pc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewActivity extends AppCompatActivity {

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
    }
}
