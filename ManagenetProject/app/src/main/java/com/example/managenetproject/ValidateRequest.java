package com.example.managenetproject;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ValidateRequest extends StringRequest {

    final static private String URL = "http://10.223.119.38/UserValidate_cap.php";
    private Map<String, String> parameters;

    public ValidateRequest(String id, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("id", id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}