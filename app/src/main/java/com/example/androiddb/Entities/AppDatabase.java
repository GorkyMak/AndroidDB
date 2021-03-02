package com.example.androiddb.Entities;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.androiddb.Entities.Users.Users;
import com.example.androiddb.Entities.Users.UsersDao;

@Database(entities = {Users.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsersDao usersDao();
}
