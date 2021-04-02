package com.example.epoka_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class HomePage extends AppCompatActivity {
    JSONObject user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        getUser();
        setHelloText();
    }

    public void getUser() {
        try {
            user = new JSONObject(getIntent().getStringExtra("user"));
            System.out.println("mdddddddddddr " + user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setHelloText() {
        TextView hello = (TextView) findViewById(R.id.tv_hello);
        try {
            hello.setText("Bonjour " + user.getString("name") + " " + user.getString("surname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
