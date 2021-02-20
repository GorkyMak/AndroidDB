package com.example.androiddb.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddb.R;

public class MainForm extends AppCompatActivity {
    Button OpenTableUser, OpenGenData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);

        Intent intent = getIntent();

        OpenTableUser = findViewById(R.id.btnOpenTableUser);
        OpenGenData = findViewById(R.id.btnOpenGenData);

        if(intent.getStringExtra("Role") != null)
            OpenTableUser.setVisibility(View.VISIBLE);
    }

    public void OpenTableUser(View view)
    {
        Intent intent = new Intent(MainForm.this, TableUsers.class);
        startActivity(intent);
    }

    public void OpenGenData(View view)
    {
        Intent intent = new Intent(MainForm.this, GeneralUserData.class);
        startActivity(intent);
    }
}