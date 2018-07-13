package com.example.kausthubram.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailedProject extends AppCompatActivity {
    Project project;
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
        setContentView(R.layout.activity_detailed_project);

        Intent i = getIntent();
        Project project = (Project)i.getParcelableExtra("project");
        Profile user = (Profile) i.getSerializableExtra("profile");

        Log.d("ID",String.valueOf(project.getid()));

        this.project = project;
        this.user = user;


        setProject();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_menu_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(true);

    }


    private void setProject(){

        ((TextView)findViewById( R.id.titleDetailed)).setText(project.getName().toUpperCase());
        ((TextView)findViewById( R.id.typeDetailed)).setText(project.getType());
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

    public void application(View view){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");

        String emailID = user.getEmail().replace("\\.","").replace("@","");

        if(user.getEmail().equals(project.getAuthor())){
            Log.d("proj", "application: you cannot apply to your own project");
            return;
        }

        DatabaseReference profReference = database.getReference(("profiles/"+emailID+"/applied/"+project.getid()+"/").replaceAll("\\.",""));
        profReference.setValue(project);

        DatabaseReference projReference = database.getReference("projects/"+project.getid()+"/applied/"+user.getId());
        projReference.setValue(user);

        goToView();
    }
}
