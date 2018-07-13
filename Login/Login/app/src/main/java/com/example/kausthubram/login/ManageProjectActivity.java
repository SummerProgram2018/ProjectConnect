package com.example.kausthubram.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ManageProjectActivity extends AppCompatActivity {
    Profile user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_project);
        Intent i = getIntent();

        this.user = (Profile)i.getSerializableExtra("profile");

    }


    private void goToView(){
        Intent intent = new Intent(this, ViewProjects.class);
        intent.putExtra("profile",user);
        startActivity(intent);
    }

    private void goToMake(){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra("profile",user);
        startActivity(intent);
    }

    private void goToProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("profile",user);
        startActivity(intent);
    }
}
