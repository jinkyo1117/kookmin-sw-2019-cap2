package com.example.managenetproject.ui.notifications;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.managenetproject.Clubpage;
import com.example.managenetproject.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static android.app.Activity.RESULT_OK;





public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    Button Submit;
    private static final int REQUEST_CODE = 0;
    private ImageView imageView;
    private Object MainActivity;
    private AlertDialog dialog;
    private EditText editTextClubName;
    private EditText editTextDepartment;
    private EditText editTextIntroduce;
    //
    private EditText editTextChairman;
    private EditText editTextEmail;

    private Activity activity;
    static String putclub;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);


        editTextClubName = (EditText) root.findViewById(R.id.club_name);
        editTextDepartment = (EditText) root.findViewById(R.id.department);
        editTextIntroduce = (EditText) root.findViewById(R.id.introduce);
        editTextChairman= (EditText) root.findViewById(R.id.chairman);
        editTextEmail = (EditText) root.findViewById(R.id.email);

        SharedPreferences pref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        final String g_id = pref.getString("Globalid","");


        //putclub = editTextClubName.getText().toString();

        //TextView Chairman2 = (TextView) root.findViewById(R.id.chairman2);

//        Global_id global_id = (Global_id) activity.getApplication();
//        String id_ = g_id.getGlobal_id();
//
//        Chairman2.setText(id_);
////        Bundle extra = this.getArguments();
////        if(extra != null)
////        {
////            String id_ = extra.getString("f_id");
////            Toast.makeText(getActivity(),id_,Toast.LENGTH_SHORT).show();
////            Chairman2.setText(id_);
////        }
////        else
////        {
////            Chairman2.setText("gggggggggggggg");
////        }


        //사진가져오기
        imageView = root.findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Intent intent = new Intent();
                                             intent.setType("image/*");
                                             intent.setAction(Intent.ACTION_GET_CONTENT);
                                             startActivityForResult(intent, REQUEST_CODE);
                                         }
                                     });

        //subimt 버튼 클릭
        Submit = (Button)root.findViewById(R.id.btn_submit);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String club_name = editTextClubName.getText().toString();
                final String department = editTextDepartment.getText().toString();
                final String introduce = editTextIntroduce.getText().toString();
                final String chairman = editTextChairman.getText().toString();
                final String email = editTextEmail.getText().toString();
                final String id = g_id;



                if(club_name.isEmpty() || department.isEmpty() || introduce.isEmpty() || chairman.isEmpty() || email.isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("모든 항목을 입력해 주세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                }
                else
                {
                    insertToClubDatabase(club_name, department, introduce, chairman, email,id);

                }



            }
        });

        return root;





    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                try{
                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    imageView.setImageBitmap(img);
                }catch(Exception e)
                {

                }
            }
        }
    }

    private void insertToClubDatabase(final String club_name, final String department,final String introduce, final String chairman, final String email,final String id) {
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "기다려 주세요.", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                if(s.equals("동아리 등록 성공"))
                {
                    Intent intent = new Intent(getActivity(), Clubpage.class);
                    intent.putExtra("put_club_name",club_name);
                    intent.putExtra("put_department",department);
                    intent.putExtra("put_introduce",introduce);
                    intent.putExtra("put_chairman",chairman);
                    intent.putExtra("put_email",email);


                    startActivity(intent);
                }


            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String club_name = (String) params[0];
                    String department = (String) params[1];
                    String introduce = (String) params[2];
                    String chairman = (String) params[3];
                    String email = (String) params[4];
                    String id = (String) params[5];


                    String link = "http://10.223.119.38/Club_Register_cap.php";
                    String data = URLEncoder.encode("club_name", "UTF-8") + "=" + URLEncoder.encode(club_name, "UTF-8");
                    data += "&" + URLEncoder.encode("department", "UTF-8") + "=" + URLEncoder.encode(department, "UTF-8");
                    //
                    data += "&" + URLEncoder.encode("introduce", "UTF-8") + "=" + URLEncoder.encode(introduce, "UTF-8");
                    data += "&" + URLEncoder.encode("chairman", "UTF-8") + "=" + URLEncoder.encode(chairman, "UTF-8");
                    data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                    data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;

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
        InsertData InsertTask = new InsertData();
        InsertTask.execute(club_name, department, introduce, chairman, email,id);

    }


}



