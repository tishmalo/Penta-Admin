package com.example.pentaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class TermDates extends AppCompatActivity {

    Toolbar tb;
    TextView date;
    Button btn;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_dates);

        tb=findViewById(R.id.toolBar);
        date=findViewById(R.id.date);
        btn=findViewById(R.id.submit);

        setSupportActionBar(tb);
        getSupportActionBar().setTitle("CHOOSE END TERM DATE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);



        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(TermDates.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                datePickerDialog.show();



            }
        });

        setListener= new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month= month+1;
                String fullDate= day+"/"+month+"/"+year;

                date.setText(fullDate);

                /**
                 *Implmenent firebase and set the event to database...Let's gooo
                 * First let's set Onclick event on button
                 *
                 */
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatabaseReference ref;

                        ref= FirebaseDatabase.getInstance().getReference("Terms");
                        String CurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


                        final String finalDate= fullDate.trim();

                        HashMap map=new HashMap();
                        map.put("Date",finalDate);

                        ref.push().setValue(map);





                        Intent intent= new Intent(TermDates.this, Home.class);
                        startActivity(intent);
                        finish();


                    }
                });

            }
        };


    }
}