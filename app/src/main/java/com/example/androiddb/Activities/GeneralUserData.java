package com.example.androiddb.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddb.Entities.InstanceDB;
import com.example.androiddb.Entities.AppDatabase;
import com.example.androiddb.Entities.Users.UsersDao;
import com.example.androiddb.R;

import java.text.MessageFormat;

public class GeneralUserData extends AppCompatActivity {
    TextView CountUsers;
    AppDatabase database;
    UsersDao usersDao;
    int CountRecords;
    Thread DBThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_user_data);

        CountUsers = findViewById(R.id.txtCountUsers);

        GetDB();

        CountUsers.setText(MessageFormat.format("{0}{1}", CountUsers.getText(), String.valueOf(CountRecords)));
    }

    private void GetDB() {
        DBThread = new Thread(() ->
        {
            database = InstanceDB.getInstance().getDatabase();
            usersDao = database.usersDao();

            CountRecords = usersDao.GetCount();
        });
        DBThread.start();

        try {
            DBThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}