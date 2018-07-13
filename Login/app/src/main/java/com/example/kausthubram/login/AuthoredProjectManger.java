package com.example.kausthubram.login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class AuthoredProjectManger extends AppCompatActivity {

    Profile user;
    Project currentProj;

    Map<Integer,Profile> applicants = new HashMap<>();

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

    private  class ProjectManagerOnClickListener implements View.OnClickListener{

        String s;

        public ProjectManagerOnClickListener(String s){
            this.s = s;
        }

        @Override
        public void onClick(View view) {



            String applicantEmail = ((TextView) ((LinearLayout) view.getParent()).getChildAt(0)).getText().toString();
            final String emailID = applicantEmail.replaceAll("\\.","").replaceAll("@","");
            DatabaseReference db = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/").getReference("profiles/"+emailID);

            db.addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FirebaseDatabase fd = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");
                    Profile prof = (Profile) dataSnapshot.getValue(Profile.class);
                    DatabaseReference dr1 = fd.getReference("projects/"+currentProj.getid()+"/accepted/"+String.valueOf(prof.getId()));
                    DatabaseReference dr2 = fd.getReference("projects/"+currentProj.getid()+"/applied/"+String.valueOf(prof.getId()));
                    DatabaseReference dr3 = fd.getReference("profiles/"+emailID+"/accepted/"+String.valueOf(currentProj.getid()));
                    DatabaseReference dr4 = fd.getReference("profiles/"+emailID+"/applied/"+currentProj.getid());


                    //PROFILES
                    if(s.equals("ACCEPTED")){
                        dr1.setValue(prof);
                        dr3.setValue(currentProj);
                        toastPopup("Accepted");
                    } else {
                        toastPopup("Rejected");
                    }


                    dr2.removeValue();
                    dr4.removeValue();
                    goToProfile();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });






        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authored_project_manger);

        Intent i = getIntent();
        user = (Profile) i.getSerializableExtra("profile");
        currentProj = (Project) i.getParcelableExtra("project");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_menu_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(2).setChecked(true);
        setProjectDetails();
        getData();
    }

    public void getData(){
        Log.d("WaterCooler", "getData: run");
        FirebaseDatabase fd = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");
        DatabaseReference dr = fd.getReference("projects/"+currentProj.getid()+"/applied");

        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot iter: dataSnapshot.getChildren()){
                    Profile prof = (Profile) iter.getValue(Profile.class);
                    applicants.put(prof.getId(),prof);
                   // Log.d("WaterCooler", "onDataChange: "+prof.getFullname());
                }
                loadApplicants();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadApplicants(){
        LinearLayout applicantsLayout = (LinearLayout) findViewById(R.id.applicants);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for(Map.Entry<Integer,Profile> current:applicants.entrySet()){
            Integer id = current.getKey();
            Profile prof = current.getValue();

            LinearLayout specificApplicants = new LinearLayout(this);
            specificApplicants.setOrientation(LinearLayout.HORIZONTAL);

            TextView applicant_email = new TextView(this);
            Button accept = new Button(this);
            Button reject = new Button(this);


            applicant_email.setText(prof.getEmail());

            accept.setText("ACCEPT");
            reject.setText("REJECT");

            specificApplicants.addView(applicant_email,lp);
            specificApplicants.addView(accept,lp);
            specificApplicants.addView(reject,lp);


            accept.setOnClickListener(new ProjectManagerOnClickListener("ACCEPTED"));
            reject.setOnClickListener(new ProjectManagerOnClickListener("REJECTED"));
            applicantsLayout.addView(specificApplicants,lp);

        }




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
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("profile",user);
        startActivity(intent);
    }

    public void deleteProject(View view){

        FirebaseDatabase fd = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");
        String emailID = user.getEmail().replaceAll("\\.","").replaceAll("@","");
        DatabaseReference dr_auth =  fd.getReference("profiles/"+emailID+"/projects/"+String.valueOf(currentProj.getid()));

        Log.d("WaterCooler", "deleteProject:"+emailID);

        dr_auth.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                deleteApplicants();
            }
        });


    }

    public void deleteApplicants(){
        final FirebaseDatabase fd = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");
        DatabaseReference dr_app = fd.getReference("projects/"+currentProj.getid()+"/applied");
        dr_app.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot iter: dataSnapshot.getChildren()){
                    Profile iterProf = iter.getValue(Profile.class);
                    String iterProfEmailID = iterProf.getEmail().replaceAll("\\.","").replaceAll("@","");
                    Log.d("WaterCooler", "onDataChange:"+ iterProfEmailID);
                    fd.getReference("profiles/"+iterProfEmailID+"/applied/"+String.valueOf(currentProj.getid())).removeValue();

                }

                deleteAccepted();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void deleteAccepted(){
        final FirebaseDatabase fd = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");
        DatabaseReference dr_app = fd.getReference("projects/"+currentProj.getid()+"/accepted");
        dr_app.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot iter: dataSnapshot.getChildren()){
                    Profile iterProf = iter.getValue(Profile.class);
                    String iterProfEmailID = iterProf.getEmail().replaceAll("\\.","").replaceAll("@","");
                    Log.d("WaterCooler", "onDataChange:"+ iterProfEmailID);
                    fd.getReference("profiles/"+iterProfEmailID+"/accepted/"+String.valueOf(currentProj.getid())).removeValue();

                }

                fd.getReference("projects/"+currentProj.getid()).removeValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        toastPopup("Deleted Projected");
        goToProfile();
    }

    public void setProjectDetails(){
        ((EditText) findViewById(R.id.descField)).setText(currentProj.getWholeDescription());
        ((TextView) findViewById(R.id.nameField)).setText(currentProj.getName());
        ((EditText) findViewById(R.id.shortDescField)).setText(currentProj.getBriefDescription());
        ((EditText) findViewById(R.id.typeFieldat)).setText(currentProj.getType());
        ((EditText) findViewById(R.id.skillsField)).setText(currentProj.getKeySkills());
        ((EditText) findViewById(R.id.positionsField)).setText(String.valueOf(currentProj.getPositions()));
        ((EditText) findViewById(R.id.allSkillsField)).setText(currentProj.getAllSkills());
        ((EditText) findViewById(R.id.lengthField)).setText(currentProj.getLength());
    }

     public void updateProject(View view){
        FirebaseDatabase fd = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");


         final String descField = ((EditText) findViewById(R.id.descField)).getText().toString();
         final String shortDescField = ((EditText) findViewById(R.id.shortDescField)).getText().toString();
         final String typeField = ((EditText) findViewById(R.id.typeFieldat)).getText().toString();
         final String skillsField = ((EditText) findViewById(R.id.skillsField)).getText().toString();
         final String positionField = ((EditText) findViewById(R.id.positionsField)).getText().toString();
         final String allSkillsField = ((EditText) findViewById(R.id.allSkillsField)).getText().toString();
         final String lengthField = ((EditText) findViewById(R.id.lengthField)).getText().toString();


         final String userEmail = user.getEmail().replaceAll("\\.","").replaceAll("@","");

         final FirebaseDatabase database = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");

         DatabaseReference dr = database.getReference("projects/"+currentProj.getid());
         DatabaseReference dr2 = database.getReference("profiles/"+userEmail+"/projects/"+currentProj.getid());
         Project project = new Project(currentProj.getName(),typeField,Integer.valueOf(positionField),skillsField,allSkillsField,shortDescField,descField,currentProj.getid(),user.getEmail(),lengthField,0);

         Log.d("WaterCooler", "updateProject: " + "profiles/"+userEmail+"/projects/"+currentProj.getid());

         dr.setValue(project);
         dr2.setValue(project);

     }


    public void toastPopup(String msg){
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

}
