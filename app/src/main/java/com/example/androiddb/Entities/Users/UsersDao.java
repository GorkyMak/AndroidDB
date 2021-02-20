package com.example.androiddb.Entities.Users;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface UsersDao {
    @Query("Select * from Users")
    List<Users> GetAll();

    @Query("Select * from Users Where Login = :Login")
    Users GetByLogin(String Login);

    @Insert
    void Insert(Users users);

    @Update
    void Update(Users users);

    @Delete
    void Delete(Users users);
}
