package com.example.kausthubram.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    Profile user;


    Map<Integer,Project> authored = new HashMap<Integer, Project>();
    Map<Integer,Project> applied = new HashMap<Integer, Project>();
    Map<Integer,Project> accepted = new HashMap<Integer, Project>();

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

    private class AuthorProfileProjectClicked implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            goToProjectManager(Integer.valueOf(((TextView) view).getHint().toString()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        Intent intent = getIntent();
        Profile profile = (Profile)intent.getSerializableExtra("profile");
        user = profile;


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_menu_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(2).setChecked(true);

        loadProfiles();
    }


    public void loadAuthorProj(){
        ((TextView) findViewById(R.id.view_profile_name_field)).setText(user.getFullname().toUpperCase());

        LinearLayout ll = (LinearLayout) findViewById(R.id.profile_project_manager);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout temp1 = new LinearLayout(this);
        temp1.setOrientation(LinearLayout.HORIZONTAL);

        SpannableString name = new SpannableString("PROJECT NAME");
        SpannableString statusunder = new SpannableString("STATUS");

        name.setSpan(new UnderlineSpan(), 0, name.length(), 0);
        statusunder.setSpan(new UnderlineSpan(), 0, statusunder.length(), 0);


        TextView heading = new TextView(this);
        TextView statushead = new TextView(this);

        heading.setText(name);
        heading.setTextColor(getResources().getColor(R.color.BLACK));
        heading.setTextSize(18);
        heading.setGravity(View.TEXT_ALIGNMENT_CENTER);
        lp2.weight = 1;

        statushead.setText(statusunder);
        statushead.setTextColor(getResources().getColor(R.color.BLACK));
        statushead.setTextSize(18);
        statushead.setGravity(View.TEXT_ALIGNMENT_VIEW_END);

        temp1.addView(heading,lp2);
        temp1.addView(statushead,lp2);

        ll.addView(temp1,lp);

        for (Map.Entry<Integer,Project> entry : authored.entrySet()) {
            Integer key = entry.getKey();
            Project project = entry.getValue();

            LinearLayout temp = new LinearLayout(this);
            temp.setOrientation(LinearLayout.HORIZONTAL);

            TextView projectName = new TextView(this);
            TextView status = new TextView(this);

            projectName.setText(project.getName().toUpperCase());
            projectName.setHint(String.valueOf(project.getid()));
            projectName.setTextColor(getResources().getColor(R.color.BLACK));
            projectName.setTextSize(16);
            projectName.setGravity(View.TEXT_ALIGNMENT_CENTER);
            lp2.weight = 1;


            status.setText("AUTHOR");
            status.setHint(String.valueOf(project.getid()));
            status.setTextColor(getResources().getColor(R.color.ownerblue));
            status.setTextSize(16);
            status.setGravity(View.TEXT_ALIGNMENT_VIEW_END);

            temp.addView(projectName,lp2);
            temp.addView(status,lp2);

            AuthorProfileProjectClicked a = new AuthorProfileProjectClicked();

            projectName.setOnClickListener(a);

            ll.addView(temp,lp);
        }

    }
    public void loadAppliedProj(){
        ((TextView) findViewById(R.id.view_profile_name_field)).setText(user.getFullname().toUpperCase());

        LinearLayout ll = (LinearLayout) findViewById(R.id.profile_project_manager);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (Map.Entry<Integer,Project> entry : applied.entrySet()) {
            Integer key = entry.getKey();
            Project project = entry.getValue();

            LinearLayout temp = new LinearLayout(this);
            temp.setOrientation(LinearLayout.HORIZONTAL);


            TextView projectName = new TextView(this);
            TextView status = new TextView(this);
            final Button remove = new Button(this);

            projectName.setText(project.getName().toUpperCase());
            projectName.setHint(String.valueOf(project.getid()));
            projectName.setTextColor(getResources().getColor(R.color.BLACK));
            projectName.setTextSize(16);
            projectName.setGravity(View.TEXT_ALIGNMENT_CENTER);
            lp2.weight = 1;

            status.setText("PENDING");
            status.setHint(String.valueOf(project.getid()));
            status.setTextColor(getResources().getColor(R.color.appliedyellow));
            status.setTextSize(18);
            status.setGravity(View.TEXT_ALIGNMENT_VIEW_END);

            remove.setText("Withdraw");
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  int id = Integer.valueOf(((TextView) ((LinearLayout) view.getParent()).getChildAt(1)).getHint().toString());
                  Project projToRemove = applied.get(id);
                  String email = user.getEmail().replaceAll("\\.","").replaceAll("@","");

                  FirebaseDatabase databaseReference =  FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");
                  DatabaseReference toRemove1 = databaseReference.getReference("profiles/"+email+"/applied/"+id);
                  DatabaseReference toRemove2 = databaseReference.getReference("projects/"+id+"/applied/"+user.getId());
                  toRemove1.removeValue();
                  toRemove2.removeValue();
                  refresh();
                }
            });



            temp.addView(projectName,lp2);
            lp2.weight = 4;
            temp.addView(status,lp2);
            temp.addView(remove,lp2);
            ll.addView(temp,lp);
        }

    }
    public void loadAcceptedProj(){
        ((TextView) findViewById(R.id.view_profile_name_field)).setText(user.getFullname().toUpperCase());

        LinearLayout ll = (LinearLayout) findViewById(R.id.profile_project_manager);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (Map.Entry<Integer,Project> entry : accepted.entrySet()) {
            Integer key = entry.getKey();
            Project project = entry.getValue();

            LinearLayout temp = new LinearLayout(this);
            temp.setOrientation(LinearLayout.HORIZONTAL);


            TextView projectName = new TextView(this);
            TextView status = new TextView(this);

            projectName.setText(project.getName().toUpperCase());
            projectName.setHint(String.valueOf(project.getid()));
            projectName.setTextColor(getResources().getColor(R.color.BLACK));
            projectName.setTextSize(16);
            projectName.setGravity(View.TEXT_ALIGNMENT_CENTER);
            lp2.weight = 1;

            status.setText("ACCEPTED");
            status.setHint(String.valueOf(project.getid()));
            status.setTextColor(getResources().getColor(R.color.success));
            status.setTextSize(16);
            status.setGravity(View.TEXT_ALIGNMENT_VIEW_END);

            temp.addView(projectName,lp2);
            lp2.weight = 4;
            temp.addView(status,lp2);

            ll.addView(temp,lp);
        }

    }
    private void loadProfiles(){

        final String emailID = user.getEmail().replaceAll("\\.","").replaceAll("@","");

        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");
        DatabaseReference getAuthored = database.getReference("profiles/"+emailID+"/projects");
        DatabaseReference getAppliedFor = database.getReference("profiles/"+emailID+"/applied");
        DatabaseReference getAccepted = database.getReference("profiles/"+emailID+"/accepted");

        getAuthored.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot iter: dataSnapshot.getChildren()){
                    Project proj = (Project) iter.getValue(Project.class);
                    authored.put(proj.getid(),proj);
                }
                loadAuthorProj();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getAppliedFor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot iter: dataSnapshot.getChildren()){
                    Project proj = (Project) iter.getValue(Project.class);
                    Log.d("DEBUGGGGGG",String.valueOf(proj.getid()));
                    applied.put(proj.getid(),proj);
                }
                loadAppliedProj();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getAccepted.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot iter: dataSnapshot.getChildren()){
                    Project proj = (Project) iter.getValue(Project.class);
                    Log.d("DEBUGGGGGG",String.valueOf(proj.getid()));
                    accepted.put(proj.getid(),proj);

                }
                loadAcceptedProj();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }

    public void goToView(){
        Intent intent = new Intent(this, ViewProjects.class);
        intent.putExtra("profile",user);
        startActivity(intent);}
    public void goToMake(){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra("profile",user);
        startActivity(intent);
    }
    public void goToProfile(){

    }

    public void refresh(){
        finish();
        startActivity(getIntent());
    }

    public void goToProjectManager(Integer projectID){
        Project currentProj = authored.get(projectID);
        Intent i = new Intent(this,AuthoredProjectManger.class);
        i.putExtra("project",currentProj);
        i.putExtra("profile",user);
        startActivity(i);
    }


    @Override
    public void onBackPressed() {
        return;
    }

    public void logOut(View view){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

}
