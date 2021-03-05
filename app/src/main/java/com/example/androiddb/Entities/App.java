package com.example.androiddb.Entities;

import android.app.Application;

import androidx.room.Room;

import com.example.androiddb.Entities.Users.Users;
import com.example.androiddb.Entities.Users.UsersDao;

public class App extends Application {
    public static App instance;
    AppDatabase database;
    UsersDao usersDao;
    Thread SecondThread;
    int ExistRecords;

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class,"database")
                .build();

        SecondThread = new Thread(() ->
        {
            usersDao = database.usersDao();

            ExistRecords = usersDao.GetEmptyInfo();
        });
        SecondThread.start();

        try {
            SecondThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (CheckEmptyUsers())
            return;

        FillDB();
    }

    private boolean CheckEmptyUsers() {
        return ExistRecords == 1;
    }

    void FillDB()
    {
        Users Admin = new Users
                (
                        "Admin",
                        "123",
                        "7(764)059-44-98",
                        "fodatipu-5632@yopmail.com",
                        "Admin",
                        "Admin",
                        "Admin",
                        "Admin"
                );

        Users wer = new Users
                (
                        "wer",
                        "124",
                        "1(3685)549-50-09",
                        "elinnyllabe-3318@yopmail.com",
                        "wer",
                        "wer",
                        "wer",
                        "User"
                );

        SecondThread = new Thread(() ->
            usersDao.InsertAll(Admin, wer));
        SecondThread.start();

        try {
            SecondThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
