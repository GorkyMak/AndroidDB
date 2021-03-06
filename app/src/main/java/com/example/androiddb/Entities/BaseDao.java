package com.example.androiddb.Entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

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