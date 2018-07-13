package com.example.kausthubram.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        final String descField = ((EditText) findViewById(R.id.descField)).getText().toString();
        final String nameField = ((EditText) findViewById(R.id.nameField)).getText().toString();
        final String shortDescField = ((EditText) findViewById(R.id.shortDescField)).getText().toString();
        final String typeField = ((EditText) findViewById(R.id.typeField)).getText().toString();
        final String skillsField = ((EditText) findViewById(R.id.skillsField)).getText().toString();
        final String positionField = ((EditText) findViewById(R.id.positionsField)).getText().toString();
        final String allSkillsField = ((EditText) findViewById(R.id.allSkillsField)).getText().toString();
        final String lengthField = ((EditText) findViewById(R.id.lengthField)).getText().toString();


        final String userEmail = user.getEmail().replaceAll("\\.","").replaceAll("@","");

        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");

        final DatabaseReference getID = database.getReference("projectNo");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer integer = (Integer) dataSnapshot.getValue(Integer.class);
                //Log.d("ID",String.valueOf(integer));


                DatabaseReference myRef = database.getReference("projects/" +String.valueOf(integer));
                Project project = new Project(nameField,typeField,Integer.valueOf(positionField),skillsField,allSkillsField,shortDescField,descField,integer,user.getEmail(),lengthField,0);


                DatabaseReference addToUser = database.getReference("profiles/"+userEmail+"/projects/"+String.valueOf(integer));

                addToUser.setValue(project);
                myRef.setValue(project);
                getID.setValue((integer + 1));


                goToView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        getID.addListenerForSingleValueEvent(valueEventListener);

    }


    private void getID(){}

    public void goToView(){
        Intent intent = new Intent(this, ViewProjects.class);
        intent.putExtra("profile",user);
        startActivity(intent);
    }

    public void goToMake(){}

    public void goToProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("profile",user);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        return;
    }

}
