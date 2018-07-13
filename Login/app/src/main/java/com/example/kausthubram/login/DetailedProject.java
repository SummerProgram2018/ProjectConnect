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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

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

        Log.d("WaterCooler", "setProject:" + project.getWholeDescription());
        Log.d("WaterCooler", "setProject:" + project.getAllSkills());
        Log.d("WaterCooler", "setProject:" + project.getLength());

        ((TextView)findViewById( R.id.titleDetailed)).setText(project.getName().toUpperCase());
        ((TextView)findViewById( R.id.typeDetailed)).setText(project.getType());
        ((TextView)findViewById( R.id.descriptionDetailed)).setText(project.getWholeDescription());
        ((TextView)findViewById( R.id.skillsDetailed)).setText(project.getAllSkills());
        ((TextView)findViewById( R.id.peopleDetailed)).setText(String.valueOf(project.getPositions()));
        ((TextView)findViewById( R.id.lengthDetailed)).setText(String.valueOf(project.getLength()));
    }


    private void appliedProjectEmail(){
        Log.i("SendMailActivity", "Send Button Clicked.");

				/*String fromEmail = ((TextView) findViewById(R.id.editText1))
						.getText().toString();
				String fromPassword = ((TextView) findViewById(R.id.editText2))
						.getText().toString();*/
        String fromEmail = "papillion072018@gmail.com";
        String fromPassword = "dnui2018";

        //Destination email list
        String toEmails = project.getAuthor();
        List<String> toEmailList = Arrays.asList(toEmails
                .split("\\s*,\\s*"));
        Log.i("SendMailActivity", "To List: " + toEmailList);
        //Email content (edit as necessary)
        String emailSubject = "Applicant for Project"+project.getName();
        String emailBody = user.getFullname() + " has applied to your project " + project.getName() +
                ". Please get in further contact with them on the email " + user.getEmail()+ ".";
        //MailTask, to send the email
        new SendMailTask(DetailedProject.this).execute(fromEmail,
                fromPassword, toEmailList, emailSubject, emailBody);
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
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");

        String emailID = user.getEmail().replace("\\.","").replace("@","");

        if(user.getEmail().equals(project.getAuthor())){
            toastPopup("You cannot apply to your own project");
            return;
        }

        final DatabaseReference profReference = database.getReference(("profiles/"+emailID+"/applied/").replaceAll("\\.",""));


        profReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild(String.valueOf(project.getid()))) {
                    profReference.child(String.valueOf(project.getid())).setValue(project);
                    DatabaseReference projReference = database.getReference("projects/"+project.getid()+"/applied/"+user.getId());
                    projReference.setValue(user);
                    appliedProjectEmail();
                    toastPopup("Applied.");
                } else{
                    toastPopup("You have already applied to this project");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        goToView();
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
