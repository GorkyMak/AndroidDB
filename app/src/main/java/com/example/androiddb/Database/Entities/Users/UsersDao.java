package com.example.androiddb.Database.Entities.Users;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.androiddb.Database.BaseDao;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class UsersDao implements BaseDao<Users> {
    @Query("Select * from Users")
    public abstract Flowable<List<Users>> GetAll();

    @Query("Select * from Users Where Login = :Login")
    public abstract Maybe<Users> GetByLogin(String Login);

    @Query("Select Count(*) from Users")
    public abstract Single<Integer> GetCount();

    @Query("Select Count(*) from (Select * from Users limit 1) as count")
    public abstract Single<Integer> GetEmptyInfo();
}
