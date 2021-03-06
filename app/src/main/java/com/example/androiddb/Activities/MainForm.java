package com.example.androiddb.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
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

    @Override
    public void onResume() {
        super.onResume();

        RestoreClick();
    }

    @Override
    protected void onPause() {
        super.onPause();

        BlockClick();
    }

    private void RestoreClick() {
        OpenTableUser.setClickable(true);
        OpenGenData.setClickable(true);
    }

    private void BlockClick() {
        OpenTableUser.setClickable(false);
        OpenGenData.setClickable(false);
    }

    @Override
    public void onBackPressed() {
        Exit();
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

    public void OpenAuth(View view) {
        Exit();
    }

    private void Exit() {
        new AlertDialog.Builder(this)
                .setMessage("Вы действительно хотите выйти ?")
                .setNegativeButton("Нет", null)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainForm.this, Authorization.class);
                        startActivity(intent);
                    }
                })
                .create().show();
    }
}