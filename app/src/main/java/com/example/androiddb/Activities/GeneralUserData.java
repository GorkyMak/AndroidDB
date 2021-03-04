package com.example.androiddb.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddb.Entities.App;
import com.example.androiddb.Entities.AppDatabase;
import com.example.androiddb.Entities.Users.Users;
import com.example.androiddb.Entities.Users.UsersDao;
import com.example.androiddb.R;

import java.text.MessageFormat;
import java.util.List;

public class GeneralUserData extends AppCompatActivity {
    TextView CountUsers;
    AppDatabase database;
    UsersDao usersDao;
    List<Users> users;
    Thread SecondThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_user_data);

        CountUsers = findViewById(R.id.txtCountUsers);

        InitDB();

        CountUsers.setText(MessageFormat.format("{0}{1}", CountUsers.getText(), String.valueOf(users.size()) ));
    }

    private void InitDB() {
        SecondThread = new Thread(() ->
        {
            database = App.getInstance().getDatabase();
            usersDao = database.usersDao();

            users = usersDao.GetAll();
        });
        SecondThread.start();

        try {
            SecondThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}