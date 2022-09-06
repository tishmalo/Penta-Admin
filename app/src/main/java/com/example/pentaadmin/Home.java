package com.example.pentaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout layout;
    NavigationView navigationView;
    Toolbar toolbar;

    Intent intent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        layout=findViewById(R.id.drawerLayout1);
        navigationView=findViewById(R.id.navigationBar1);
        toolbar=findViewById(R.id.toolBar2);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ADMIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(Home.this, layout,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        layout.addDrawerListener(toggle);
        toggle.syncState();

    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.home:
                intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;

            case R.id.posts:
                intent= new Intent(getApplicationContext(), UpdatePosts.class);
                startActivity(intent);
                break;

            case R.id.term:
                intent= new Intent(getApplicationContext(), TermDates.class);
                startActivity(intent);
                break;


            case R.id.logout:

                FirebaseAuth.getInstance().signOut();

                intent= new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                break;


        }



        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        Date dd=new Date();
        SimpleDateFormat formatter=new SimpleDateFormat("dd/mm/yyyy");
        final String DATES= formatter.format(dd);

        DatabaseReference ref;
        ref=FirebaseDatabase.getInstance().getReference("Terms");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    final String TISH=snapshot.child("Date").getValue().toString();

                    if(DATES.equals(TISH)){

                        DatabaseReference reference,databaseReference;
                        reference=FirebaseDatabase.getInstance().getReference("Subjects");
                        databaseReference=FirebaseDatabase.getInstance().getReference("Results");

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot sn: snapshot.getChildren()){

                                    sn.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds:snapshot.getChildren()){

                                    ds.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}