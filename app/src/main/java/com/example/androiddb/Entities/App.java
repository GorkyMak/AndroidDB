package com.example.androiddb.Entities;

import android.app.Application;

import androidx.room.Room;

import com.example.androiddb.Entities.Users.Users;
import com.example.androiddb.Entities.Users.UsersDao;

public class App extends Application {
    public static App instance;
    AppDatabase database;
    UsersDao usersDao;
    int CountRecords;
    Thread SecondThread;

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
        database = Room.databaseBuilder(this, AppDatabase.class,
                "database").build();

        Runnable InitDB = () ->
        {
            usersDao = database.usersDao();

            CountRecords = usersDao.GetCount();
        };
        SecondThread = new Thread(InitDB);
        SecondThread.start();

        try {
            SecondThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CheckDB();
    }

    void CheckDB()
    {
        if(CountRecords > 0)
            return;

        FillDB();
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

        Runnable AddBaseUsers = () ->
        {
            usersDao.Insert(Admin);
            usersDao.Insert(wer);
        };
        SecondThread = new Thread(AddBaseUsers);
        SecondThread.start();

        try {
            SecondThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
