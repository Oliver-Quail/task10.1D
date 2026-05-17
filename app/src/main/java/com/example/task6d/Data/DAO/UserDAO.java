package com.example.task6d.Data.DAO;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.task6d.Data.Entity.User;

@Dao
public interface UserDAO {

    @Insert
    void createUser(User user);

    @Query("SELECT * FROM user WHERE isActive = 1")
    User getActiveUser();

    @Query("UPDATE user SET isActive = 0 WHERE 1=1")
    void deactivateAllUsers();

    @Query("SELECT * FROM user WHERE userName = :userName")
    User getUserByName(String userName);

    @Update
    void updateUser(User user);

}
