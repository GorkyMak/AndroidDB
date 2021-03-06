package com.example.androiddb.Entities.Users;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.androiddb.Entities.BaseDao;

import java.util.List;
@Dao
public abstract class UsersDao implements BaseDao<Users> {
    @Query("Select * from Users")
    public abstract List<Users> GetAll();

    @Query("Select * from Users Where Login = :Login")
    public abstract Users GetByLogin(String Login);

    @Query("Select Count(*) from Users")
    public abstract int GetCount();

    @Query("Select Count(*) from (Select * from Users limit 1) as count")
    public abstract int GetEmptyInfo();
}
