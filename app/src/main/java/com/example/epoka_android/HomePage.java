package com.example.epoka_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    public void addMission(View view) {
        Intent intent = new Intent(this, AddMission.class);
        intent.putExtra("user", user.toString());
        startActivity(intent);
        finish();
    }

    public void quit(View view) {
        finish();
    }
}
