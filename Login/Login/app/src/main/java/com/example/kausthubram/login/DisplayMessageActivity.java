package com.example.kausthubram.login;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DisplayMessageActivity extends AppCompatActivity {

    Profile user;

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
        setContentView(R.layout.activity_display_message);
        Intent intent = getIntent();
        Profile profile = (Profile)intent.getSerializableExtra("profile");
        user = profile;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_menu_make);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.getMenu().getItem(1).setChecked(true);

    }

    public void createProject(View view){
        String descField = ((EditText) findViewById(R.id.descField)).getText().toString();
        String nameField = ((EditText) findViewById(R.id.nameField)).getText().toString();
        String shortDescField = ((EditText) findViewById(R.id.shortDescField)).getText().toString();
        String typeField = ((EditText) findViewById(R.id.typeField)).getText().toString();
        String skillsField = ((EditText) findViewById(R.id.skillsField)).getText().toString();
        String positionField = ((EditText) findViewById(R.id.positionsField)).getText().toString();
        String allSkillsField = ((EditText) findViewById(R.id.allSkillsField)).getText().toString();


        FirebaseDatabase database = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");

        DatabaseReference myRef = database.getReference("projects/" + nameField);

        Project project = new Project(nameField,typeField,Integer.valueOf(positionField),skillsField,allSkillsField,shortDescField,descField,0,user.getEmail(),"1 week",0);
        myRef.setValue(project);

    }


    private void getID(){}

    public void goToView(){
        Intent intent = new Intent(this, ViewProjects.class);
        intent.putExtra("profile",user);
        startActivity(intent);
    }

    public void goToMake(){}

    public void goToProfile(){}

}
