package com.example.kausthubram.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

import javax.xml.validation.Validator;

public class SignUpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void validater(View view) {

        String name = ((EditText) (findViewById(R.id.signup_name))).getText().toString();
        String email = ((EditText) (findViewById(R.id.signup_email))).getText().toString();
        String password = ((EditText) (findViewById(R.id.signup_password))).getText().toString();
        String password2 = ((EditText) (findViewById(R.id.signup_2password))).getText().toString();

        TextView passErrflag = (TextView) findViewById(R.id.passNotSameErr);
        TextView generalErrFlag = (TextView) findViewById(R.id.nameNotValidErr);

        passErrflag.setVisibility(View.INVISIBLE);
        generalErrFlag.setVisibility(View.INVISIBLE);

        if (!password.equals(password2)) {
            raisePasswordFlag();
            return;
        } else if (name.matches("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                || password.matches("") || password2.matches("")){
            raiseGeneralFlag();
            return;
        }
        profileAdder(name, email, password);
    }

    private void profileAdder(final String name, final String email, final String password) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");
        String email2 = email.replaceAll("\\.","").replaceAll("@","");
        final DatabaseReference myRef = database.getReference("profiles/"+email2);


        final DatabaseReference id = database.getReference("userNo");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer integer = (Integer) dataSnapshot.getValue(Integer.class);
                Profile newUser = new Profile(email,password,name,integer);
                myRef.setValue(newUser);

                id.setValue(integer + 1);

                viewProjects(newUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        id.addListenerForSingleValueEvent(valueEventListener);


    }



    private void raisePasswordFlag() {
        TextView passErrflag = (TextView) findViewById(R.id.passNotSameErr);
        TextView generalErrFlag = (TextView) findViewById(R.id.nameNotValidErr);
        passErrflag.setVisibility(View.VISIBLE);
        generalErrFlag.setVisibility(View.INVISIBLE);
    }

    private void raiseGeneralFlag() {
        TextView passErrflag = (TextView) findViewById(R.id.passNotSameErr);
        TextView generalErrFlag = (TextView) findViewById(R.id.nameNotValidErr);
        passErrflag.setVisibility(View.INVISIBLE);
        generalErrFlag.setVisibility(View.VISIBLE);
    }

    public void viewProjects(Profile profile) {
        Intent intent = new Intent(this, ViewProjects.class);
        intent.putExtra("profile",profile);
        startActivity(intent);
    }
}
