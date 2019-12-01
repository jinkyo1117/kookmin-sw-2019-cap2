package com.example.managenetproject.ui.mypage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.managenetproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MypageFragment extends Fragment {

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_Password = "password";
    private static final String TAG_Phone = "phone_number";
    private static final String TAG_Birth = "birth_date";
    private static final String TAG_Name = "name";
    private static final String TAG_Club = "club_name";


    TextView vID;
    TextView vPhone;
    TextView vBirth;
    TextView vName;
    TextView vClub;


    ArrayList<HashMap<String,String>> personList;


    String url = "http://10.223.119.38/Mypage_cap.php";

    JSONArray peoples = null;

    private MypageViewModel mypageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mypageViewModel =
                ViewModelProviders.of(this).get(MypageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mypage, container, false);


        vPhone = (TextView) root.findViewById(R.id.my_phone);
        vBirth = (TextView) root.findViewById(R.id.my_birth);
        vName = (TextView) root.findViewById(R.id.my_name);
        vClub = (TextView) root.findViewById(R.id.my_club);

        SharedPreferences pref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        final String g_id = pref.getString("Globalid","");


        personList = new ArrayList<HashMap<String, String>>();

        class GetDataJSON extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params){
                String uri = params[0];

                BufferedReader bufferedReader = null;
                try{
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json=bufferedReader.readLine())!=null)
                    {
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();
                }catch(Exception e){
                    return null;
                }
            }


            protected void showList()
            {

                try
                {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    peoples = jsonObj.getJSONArray(TAG_RESULTS);
//                    vClub.setText(peoples.length());
                    for(int i=0;i<peoples.length();i++)
                    {

                        JSONObject c = peoples.getJSONObject(i);
                        String id = c.getString(TAG_ID);
                        String password = c.getString(TAG_Password);
                        String phone_number = c.getString(TAG_Phone);
                        String birth_date = c.getString(TAG_Birth);
                        String name = c.getString(TAG_Name);
                        String club_name = c.getString(TAG_Club);

                        HashMap<String,String> persons = new HashMap<String,String>();

                        if(id.equals(g_id))
                        {
                            vClub.setText(club_name);
                            vName.setText(name);
                            vBirth.setText(birth_date);
                            vPhone.setText(phone_number);
                            break;
                        }

                    }
//        ListAdapter adapter = new SimpleAdapter(
//                MainActivity.this,personList,R.layout.list_item, new String[]{TAG_ID,TAG_Name}, new int[]{R.id.id, R.id.name}
//        );
//            list.setAdapter(adapter);
                } catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            protected void onPostExecute(String result)
            {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);

        return root;
    }

}