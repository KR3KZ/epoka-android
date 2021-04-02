package com.example.epoka_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String base_url = "http://10.0.2.2:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onLogin(View view) {
        String url = this.base_url + "api/login";
        TextView tv_error = (TextView) findViewById(R.id.tv_response);
        TextView id = (TextView) findViewById(R.id.edt_id);
        TextView password = (TextView) findViewById(R.id.edt_password) ;


        JSONObject postData = new JSONObject();
        try {
            postData.put("id", id.getText());
            postData.put("password", password.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("error")) {
                            try {
                                tv_error.setText(response.getString("error"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (response.has("id")) {
                                Intent intent = new Intent(MainActivity.this, HomePage.class);
                                intent.putExtra("user", response.toString());
                                startActivity(intent);
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tv_error.setText(error.toString());
                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}