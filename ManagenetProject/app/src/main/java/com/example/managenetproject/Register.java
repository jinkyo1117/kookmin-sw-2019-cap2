package com.example.managenetproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
public class Register extends AppCompatActivity {

    private EditText editTextId;
    private EditText editTextPw;
    private EditText editTextPw_check;
    //
    private EditText editTextPhone;
    private EditText editTextBirth;
    private EditText editTextName;
    private EditText editTextClubName;

    private AlertDialog dialog;
    private boolean validate = false; //ID체크
    private ImageView imageView;
    private static final int REQUEST_CODE = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextId = (EditText) findViewById(R.id.new_id);
        editTextPw = (EditText) findViewById(R.id.new_pw);
        editTextPw_check = (EditText) findViewById(R.id.new_pw_check);

        editTextPhone = (EditText) findViewById(R.id.new_phone);
        editTextBirth = (EditText) findViewById(R.id.new_birth);
        editTextName = (EditText) findViewById(R.id.new_name);
        editTextClubName = (EditText) findViewById(R.id.new_club_name);


        //이미지 추가
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


        //중복확인 버튼
        Button validateButton = (Button)findViewById(R.id.validateButton);


        //버튼 클릭
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String check_id = editTextId.getText().toString();

//                if(validate){
//                    return;//검증 완료
//                }
                //ID 값을 입력하지 않았다면

                if(check_id.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    dialog = builder.setMessage("아이디를 입력해 주세요.")
                            .setPositiveButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }


                //검증시작
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try{

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){//사용할 수 있는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();

                                validate = true;//검증완료


                            }else{//사용할 수 없는 아이디라면
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                dialog = builder.setMessage("이미 사용중인 아이디입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };//Response.Listener 완료

                //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                ValidateRequest validateRequest = new ValidateRequest(check_id, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(validateRequest);
            }
        });


    }
    public void insert(View view) {
        String id = editTextId.getText().toString();
        String password = editTextPw.getText().toString();
        String password_check = editTextPw_check.getText().toString();

        String phone_number = editTextPhone.getText().toString();
        String birth_date = editTextBirth.getText().toString();
        String name = editTextName.getText().toString();
        String club_name = editTextName.getText().toString();

        if(id.isEmpty() || password.isEmpty() || password_check.isEmpty() || name.isEmpty() || phone_number.isEmpty() || birth_date.isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
            dialog = builder.setMessage("모든 항목을 입력해 주세요.")
                    .setNegativeButton("확인", null)
                    .create();
            dialog.show();
        }
        else
        {
            if(validate)
            {
                if(password.equals(password_check))
                {
                    insertToDatabase(id, password,phone_number, birth_date, name, club_name);
//                    finish();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    dialog = builder.setMessage("입력한 비밀번호와 비밀번호확인이 서로 다릅니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                }
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                dialog = builder.setMessage("아이디 중복확인을 해주세요.")
                        .setNegativeButton("확인", null)
                        .create();
                dialog.show();
            }
        }

    }
    private void insertToDatabase(String id, String password, String phone_number, String birth_date, String name,String club_name) {
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "기다려 주세요.", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                if(s.equals("회원가입 성공"))
                    finish();

            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String id = (String) params[0];
                    String password = (String) params[1];
                    //이미지
                    String phone_number = (String) params[2];
                    String birth_date = (String) params[3];
                    String name = (String) params[4];
                    String club_name = (String) params[5];


                    String link = "http://10.223.119.38/Register_cap.php";
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    //
                    data += "&" + URLEncoder.encode("phone_number", "UTF-8") + "=" + URLEncoder.encode(phone_number, "UTF-8");
                    data += "&" + URLEncoder.encode("birth_date", "UTF-8") + "=" + URLEncoder.encode(birth_date, "UTF-8");
                    data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("club_name", "UTF-8") + "=" + URLEncoder.encode(club_name, "UTF-8");


                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(id, password, phone_number,birth_date, name, club_name);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    imageView.setImageBitmap(img);
                } catch (Exception e) {

                }
            }
        }
    }
}