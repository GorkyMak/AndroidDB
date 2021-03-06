package com.example.androiddb.Entities;

import android.app.Application;

import androidx.room.Room;

import com.example.androiddb.Entities.Users.Users;
import com.example.androiddb.Entities.Users.UsersDao;

public class InstanceDB extends Application {
    public static InstanceDB instance;
    AppDatabase database;
    UsersDao usersDao;
    Thread DBThread;
    int ExistRecords;

    public static InstanceDB getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        InitDB();

        GetDB();

        if (!CheckEmptyUsers())
            return;

        FillDB();
    }

    private void InitDB() {
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class,"database")
                .build();
    }

    private void GetDB() {
        DBThread = new Thread(() ->
        {
            usersDao = database.usersDao();

            ExistRecords = usersDao.GetEmptyInfo();
        });
        DBThread.start();

        try {
            DBThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean CheckEmptyUsers() {
        return ExistRecords == 0;
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

        DBThread = new Thread(() ->
            usersDao.InsertAll(Admin, wer));
        DBThread.start();

        try {
            DBThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
