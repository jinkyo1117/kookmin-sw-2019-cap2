package com.example.managenetproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Clubpage extends AppCompatActivity {

    String page_club_name;

    private TextView viewTextClubName;
    private TextView viewTextDepatment;
    private TextView viewTextChairman;
    private TextView viewTextIntroduce;
    private TextView viewTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubpage);

        Bundle extras = getIntent().getExtras();
        String get_club_name = extras.getString("put_club_name");
        String get_department = extras.getString("put_department");
        String get_introduce = extras.getString("put_introduce");
        String get_chairman = extras.getString("put_chairman");
        String get_email = extras.getString("put_email");

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String g_id = pref.getString("Globalid","");

        viewTextClubName = (TextView) findViewById(R.id.Vclub_name);
        viewTextDepatment = (TextView) findViewById(R.id.Vdepartment);
        viewTextChairman = (TextView) findViewById(R.id.Vchairman);
        viewTextIntroduce = (TextView) findViewById(R.id.Vintroduce);
        viewTextEmail = (TextView) findViewById(R.id.Vemail);


        viewTextClubName.setText(get_club_name);
        viewTextDepatment.setText(get_department);
        viewTextIntroduce.setText(get_introduce);
        viewTextChairman.setText(get_chairman);
        viewTextEmail.setText(get_email);

    }



}
