package com.example.kausthubram.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DetailedProject extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item){
            switch (item.getItemId()) {
                case R.id.navigation_view:
                    goToView();
                    return true;
                case R.id.navigation_make:
                    goToMake();
                    return true;
                case R.id.navigation_profile:
                    goToProfile();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_project);

        Intent i = getIntent();
        Project project = (Project)i.getParcelableExtra("project");

        setProject(project);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_menu_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(true);

    }


    private void setProject(Project project){

        ((TextView)findViewById( R.id.titleDetailed)).setText(project.getName());
        ((TextView)findViewById( R.id.descriptionDetailed)).setText(project.getWholeDescription());
        ((TextView)findViewById( R.id.skillsDetailed)).setText(project.getAllSkills());
        ((TextView)findViewById( R.id.peopleDetailed)).setText(String.valueOf(project.getPositions()));
    }

    private void makeProject(View view){
        Intent i = new Intent(this,DisplayMessageActivity.class);

        startActivity(i);
    }

    private void goToView(){
        Intent intent = new Intent(this, ViewProjects.class);
        startActivity(intent);
    }

    private void goToMake(){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);
    }

    private void goToProfile(){}

}
