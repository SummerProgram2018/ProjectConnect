package com.example.kausthubram.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent intent = new Intent(this,DisplayMessageActivity.class);
        //startActivity(intent);

    }


    public void viewProjects(View view,Profile profile) {
        Intent intent = new Intent(this, ViewProjects.class);
        intent.putExtra("profile",profile);
        startActivity(intent);
    }

    public void signUpScreen(View view){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);

    }

    public void logInChecker(final View view){
        String login = ((TextView) findViewById(R.id.edit_text_login)).getText().toString();


        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");
        DatabaseReference myRef = database.getReference("profiles/" + stringReplacer(login));

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String password = ((TextView) findViewById(R.id.edit_text_password)).getText().toString();

                Profile post = dataSnapshot.getValue(Profile.class);

                if(post != null){
                    Log.w("Password",post.getPassword());

                    if(password.equals(post.getPassword())){
                        viewProjects(view,post);
                    } else {
                        toastPopup("Username or Password incorrect");

                    }
                } else {
                    toastPopup("Username or Password incorrect");
                }

            }

            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Did not work", "loadPost:onCancelled", databaseError.toException());
            }
        };


        myRef.addValueEventListener(postListener);





    }

    private String stringReplacer(String s){
        return s.replaceAll("@","").replaceAll("\\.","");
    }


    @Override
    public void onBackPressed(){
        
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
