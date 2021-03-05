package com.example.androiddb.Entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androiddb.Entities.Users.Users;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface BaseDao<T> {
    @Insert
    void Insert(T obj);

    @Insert
    void InsertAll(T... obj);

    @Update
    void Update(T obj);

    @Delete
    void Delete(T obj);
}