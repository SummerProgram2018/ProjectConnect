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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

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

    private void profileAdder(String name, String email, String password) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");
        email = email.replaceAll("\\.","").replaceAll("@","");
        DatabaseReference myRef = database.getReference("profiles/"+email);

        myRef.setValue(new Profile(email,password,name));

        /*myRef.child("username").setValue(name);
        myRef.child("password").setValue(password);*/
        viewProjects(name);
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

    public void viewProjects(String name) {
        Intent intent = new Intent(this, ViewProjects.class);
//Add profile
        startActivity(intent);
    }
}
