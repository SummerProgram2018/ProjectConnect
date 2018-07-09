package com.example.daffamage.pc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SuccessApply extends AppCompatActivity {

    public void refreshView (View view)
    {
        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_apply);
    }
}
