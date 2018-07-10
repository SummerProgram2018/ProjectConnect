package com.example.kausthubram.login;

import android.content.Intent;
import android.os.DeadObjectException;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class ViewProjects extends AppCompatActivity {


    private List<Project> bookList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProjectAdapter mAdapter;

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
        setContentView(R.layout.activity_view_projects);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ProjectAdapter(bookList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.setAdapter(mAdapter);

        initBookData();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_menu_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(true);

    }


    private void initBookData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://projectconnect123-dbbc6.firebaseio.com/");
        DatabaseReference myRef = database.getReference("projects");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Project post = postSnapshot.getValue(Project.class);
                    Log.e("Get Data", post.getName());
                    bookList.add(new Project(post.getName(),
                            post.getType(),
                            post.getPositions(),
                            post.getKeySkills(),
                            post.getAllSkills(),
                            post.getBriefDescription(),
                            post.getWholeDescription(),
                            post.getid(),
                            post.getAuthor(),
                            post.getLength(),
                            post.getCurrentPeople()));
                }
                Log.e("SIZE",String.valueOf(bookList.size()));
                mAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });


    }

    public void changePage(Project project){
        Intent intent = new Intent(this,DetailedProject.class);
        intent.putExtra("project",project);
        startActivity(intent);
    }

    public void goToView(){}
    public void goToMake(){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);
    }
    public void goToProfile(){}

}
