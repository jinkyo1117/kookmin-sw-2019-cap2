package com.example.managenetproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);





        final EditText idText = (EditText) findViewById(R.id.IDView);
        final EditText pwText = (EditText) findViewById(R.id.PWView);
        final Button LoginButton = (Button) findViewById(R.id.LoginButton);
        final Button RegisterButton = (Button) findViewById(R.id.RegisterButton);



//        ((Global_id)this.getApplication()).setData(idText.getText().toString());
//        Global_id g_id = (Global_id) getApplication();
//        g_id.setData(idText.getText().toString());
        //로그인클릭

        LoginButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               final String id = idText.getText().toString();
                                               final String password = pwText.getText().toString();

                                               //id 공용으로 쓸수잇게 하는 함수
                                               SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
                                               SharedPreferences.Editor editor = pref.edit();
                                               editor.putString("Globalid",id);
                                               editor.commit();




                                               Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                   @Override
                                                   public void onResponse(String response) {
                                                       try{
                                                           JSONObject jsonResponse = new JSONObject(response);
                                                           boolean success = jsonResponse.getBoolean("success");
                                                           if(success)
                                                           {
                                                               String id = jsonResponse.getString("id");
                                                               String password = jsonResponse.getString("password");

                                                               Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                               Toast.makeText(getApplicationContext(),"로그인 성공!", Toast.LENGTH_SHORT).show();
                                                               intent.putExtra("putid",id);
                                                               startActivity(intent);
                                                               finish();//
                                                           }
                                                           else
                                                           {
                                                               AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                                               builder.setMessage("회원정보가 맞지 않습니다.")
                                                                       .setNegativeButton("다시 시도해 주세요.",null)
                                                                       .create()
                                                                       .show();
                                                           }
                                                       }catch (Exception e)
                                                       {
                                                           e.printStackTrace();
                                                       }
                                                   }
                                               };
                                               LoginRequest loginRequest = new LoginRequest(id, password, responseListener);
                                               RequestQueue queue = Volley.newRequestQueue(Login.this);
                                               queue.add(loginRequest);
                                           }
                                       }
        );

        //회원가입 클릭

        RegisterButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Toast.makeText(getApplicationContext(),"회원가입 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                                                  Intent intent2 = new Intent(getApplicationContext(),Register.class);
                                                  startActivity(intent2);
                                              }
                                          }
        );


    }


}
