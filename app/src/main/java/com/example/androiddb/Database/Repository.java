package com.example.androiddb.Database;

import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class Repository extends Application {
    public static Repository instance;
    AppDatabase database;

    public static Repository getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        InitDB();
    }

    private void InitDB() {
        if(database != null)
            return;

        instance = this;
        Completable.fromAction
                (
                    () -> database = Room.databaseBuilder(this, AppDatabase.class,"database")
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@androidx.annotation.NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            FillDB(db);
                        }
                    })
                    .build()
                )
                .subscribeOn(Schedulers.newThread())
                .subscribe();
    }

    void FillDB(SupportSQLiteDatabase db)
    {
        ContentValues Admin = new ContentValues();
        Admin.put("Login", "Admin");
        Admin.put("Password", "123");
        Admin.put("Phone", "7(764)059-44-98");
        Admin.put("Email", "fodatipu-5632@yopmail.com");
        Admin.put("LastName", "Admin");
        Admin.put("FirstName", "Admin");
        Admin.put("MiddleName", "Admin");
        Admin.put("Role", "Admin");

        db.insert("Users", SQLiteDatabase.CONFLICT_NONE, Admin);

        ContentValues wer = new ContentValues();
        wer.put("Login", "wer");
        wer.put("Password", "124");
        wer.put("Phone", "1(3685)549-50-09");
        wer.put("Email", "elinnyllabe-3318@yopmail.com");
        wer.put("LastName", "wer");
        wer.put("FirstName", "wer");
        wer.put("MiddleName", "wer");
        wer.put("Role", "User");

        db.insert("Users", SQLiteDatabase.CONFLICT_NONE, wer);
    }
}
