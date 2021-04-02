package com.example.epoka_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddMission extends AppCompatActivity {
    JSONObject user;
    String base_url = "http://10.0.2.2:8080/";

    EditText etd_start;
    EditText etd_end;
    Spinner lb_cities;

    public class City {
        public int id;
        public String name;
        public String zip_code;

        public City(int id, String name, String zip_code) {
            this.id = id;
            this.name = name;
            this.zip_code = zip_code;
        }

        @Override
        public String toString() {
            return this.name + " (" + this.zip_code + ")";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mission);
        getUser();

        etd_start = findViewById(R.id.etd_start);
        etd_end = findViewById(R.id.etd_end);
        lb_cities = findViewById(R.id.lb_cities);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, base_url + "api/cities", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Things to handle dropdown spinner
                            List<City> list = new ArrayList<>();

                            JSONArray cities = response.getJSONArray("cities");

                            for(int i = 0; i < cities.length(); i++) {
                                JSONObject city = cities.getJSONObject(i);
                                Integer id = city.getInt("id");
                                String name = city.getString("name");
                                String zip_code = city.getString("zip_code");
                                list.add(new City(id, name, zip_code));
                            }

                            //Add the cities to the dropdown spinner
                            ArrayAdapter<City> adapter = new ArrayAdapter<City>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            lb_cities.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void getUser() {
        try {
            user = new JSONObject(getIntent().getStringExtra("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postMission(View view) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("user_id", user.getInt("id"));
            postData.put("user_password", user.getString("password"));
            postData.put("date_start", etd_start.getText());
            postData.put("date_end", etd_end.getText());
            City city = (City) lb_cities.getSelectedItem();
            Integer city_id = city.id;
            postData.put("city_id", city_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, base_url + "api/city", postData, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("success")) {
                            Toast toast;
                            try {
                                if (response.getBoolean("success")) {
                                    toast = Toast.makeText(AddMission.this, "Mission créée avec succès.", Toast.LENGTH_LONG);
                                } else {
                                    toast = Toast.makeText(AddMission.this, "Impossible de créer la mission.", Toast.LENGTH_LONG);
                                }
                                toast.show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
