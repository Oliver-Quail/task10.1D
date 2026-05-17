package com.example.task6d.Data.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.task6d.Data.Entity.Interest;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface InterestDAO {
    @Insert
    void createInterest(Interest interest);

    @Query("SELECT * FROM interest WHERE id = :id")
    Interest getInterest(int id);

    @Query("SELECT * FROM interest")
    List<Interest> getInterests();


}
