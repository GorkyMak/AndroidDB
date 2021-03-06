package com.example.androiddb.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.androiddb.Database.Entities.Users.Users;
import com.example.androiddb.Database.Entities.Users.UsersDao;

@Database(entities = {Users.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsersDao usersDao();
}
