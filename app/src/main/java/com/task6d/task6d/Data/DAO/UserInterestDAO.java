package com.task6d.task6d.Data.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.task6d.task6d.Data.Entity.UserInterest;

import java.util.List;

@Dao
public interface UserInterestDAO {

    @Query("SELECT * FROM userinterest")
    List<UserInterest> getUserInterests();

    @Insert
    void createUserInterest(UserInterest userInterest);

    @Query("SELECT * FROM userinterest WHERE userId = :userId AND interestId = :interestId")
    UserInterest getUserInterest(int userId, int interestId);

    @Query("SELECT * FROM userinterest WHERE userId = :userId")
    List<UserInterest> getUserInterest(int userId);

    @Delete
    void deleteUserInterest(UserInterest userInterest);
}
