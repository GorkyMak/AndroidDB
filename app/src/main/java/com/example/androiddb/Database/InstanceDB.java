package com.example.androiddb.Database;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.example.androiddb.Database.Entities.Users.Users;
import com.example.androiddb.Database.Entities.Users.UsersDao;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

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

        GetEmptyInfo();
    }

    private void InitDB() {
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class,"database")
                .build();
    }

    private void GetDB() {
        DBThread = new Thread(() ->
            usersDao = database.usersDao());
        DBThread.start();

        try {
            DBThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void GetEmptyInfo() {
        usersDao.GetEmptyInfo()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        ExistRecords = integer;

                        if (!CheckEmptyUsers())
                            return;

                        FillDB();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("ERROR-GetEmptyInfo", e.getMessage());
                    }
                });
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
