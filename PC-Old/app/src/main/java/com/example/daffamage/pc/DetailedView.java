package com.example.daffamage.pc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DetailedView extends AppCompatActivity {

    //Make Project block here

    public void refreshView (View view)
    {
        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
    }

    public void goApply (View view)
    {
        Intent intent = new Intent(this, SuccessApply.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
    }
}
